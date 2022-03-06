package zhuke.manong.community.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import zhuke.manong.community.dto.AccessTokenDTO;
import zhuke.manong.community.provide.dto.GithubUser;
import zhuke.manong.community.model.User;
import zhuke.manong.community.provide.GithubProvider;
import zhuke.manong.community.service.UserService;
import zhuke.manong.community.strategy.LoginUserInfo;
import zhuke.manong.community.strategy.UserStrategy;
import zhuke.manong.community.strategy.UserStrategyFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
/********************************************************************************
 * Purpose: 码农社区的登入和登出
 * Author: Caijinbang
 * Created Date:2021-10-12
 * Modify Description:
 * 1. 码农社区 Github 登录 (Caijinbang 2021-10-13)
 * 2. 码农社区的登出 (Caijinbang 2021-10-15)
 * 3. 新增码农社区 的 多种登录方式功能 (Caijinbang 2022-2-28)
 ** *****************************************************************************/
@Controller
@Slf4j
public class AuthorizeController {

    @Autowired
    private UserStrategyFactory userStrategyFactory;

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserService userService;

    /*************************************************************************************************
     * Purpose: 码农社区 Github 或 Gitee 登录后返回 论坛主页面
     * Param: type 为 用户登录方式
     *        code 为 收到的作为对上一步的响应的代码
     *        state 为 一个不可猜测的随机字符串。它用于防止跨站点请求伪造攻击。
     * Return: string 返回 index 论坛主页
     *************************************************************************************************/
    @GetMapping("/callback/{type}")
    public String newCallback(@PathVariable(name = "type") String type,
                              @RequestParam(name = "code") String code,
                              @RequestParam(name = "state", required = false) String state,
                              HttpServletRequest request,
                              HttpServletResponse response) {
        UserStrategy userStrategy = userStrategyFactory.getStrategy(type);
        LoginUserInfo loginUserInfo = userStrategy.getUser(code, state);
        if (loginUserInfo != null && loginUserInfo.getId() != null) {
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(loginUserInfo.getName());
            user.setAccountId(String.valueOf(loginUserInfo.getId()));
            user.setType(type);
            user.setAvatarUrl(loginUserInfo.getAvatarUrl());

            userService.createOrUpdate(user);

            Cookie cookie = new Cookie("token", token);
            cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
            //设置cookie存放路径
            cookie.setPath("/");
            response.addCookie(cookie);

            return "redirect:/";
        } else {
            log.error("callback get github error,{}", loginUserInfo);
            // 登录失败，重新登录
            return "redirect:/";
        }
    }

    /*************************************************************************************************
     * Purpose: 码农社区 Github 登录后返回 论坛主页面 （ 该方法已弃用 ）
     * Param: code 为 收到的作为对上一步的响应的代码，state 为 一个不可猜测的随机字符串。它用于防止跨站点请求伪造攻击。
     * Return: string 返回 index 论坛主页
     *************************************************************************************************/
    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletResponse response){
        //Post 请求传参对象到 Github
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        //从 GitHub 收到的 OAuth 应用程序的客户端 ID
        accessTokenDTO.setClient_id(clientId);
        //从 GitHub 收到的 OAuth 应用程序的客户端密码
        accessTokenDTO.setClient_secret(clientSecret);
        //收到的作为对上一步的响应的代码
        accessTokenDTO.setCode(code);
        //授权后发送用户的应用程序中的 URL
        accessTokenDTO.setRedirect_uri(redirectUri);
        //一个不可猜测的随机字符串。它用于防止跨站点请求伪造攻击。
        accessTokenDTO.setState(state);
        //通过 Post 请求相关参数到 Github 中获取返回的 access_token
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        //通过 Get 请求获取返回的 用户信息
        GithubUser githubUser = githubProvider.getUser(accessToken);


        //判断通过 Github 登录的 User 不为空，且其 Id 也不为空
        if(githubUser!=null && githubUser.getId() != null){
            //创建一个 User 对象
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setAvatarUrl(githubUser.getAvatarUrl());
            //传入 user 对象判断该 user 是否已在数据库中
            userService.createOrUpdate(user);
            //登录成功，写cookie和session
            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        }else{
            log.error("callback get github error,{}", githubUser);
            //登录失败，重新登录
            return "redirect:/";
        }
    }

    /*************************************************************************************************
     * Purpose: 码农社区 Github 登出后返回 论坛主页面
     * Return: string 返回 index 论坛主页
     *************************************************************************************************/
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        request.getSession().invalidate();
        //删除名称为token的Cookie
        Cookie cookie = new Cookie("token", null);
        //立即删除型
        cookie.setMaxAge(0);
        //项目所有目录均有效
        cookie.setPath("/");
        //重新写入，将覆盖之前的
        response.addCookie(cookie);
        return "redirect:/";
    }
}
