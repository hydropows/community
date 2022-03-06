package zhuke.manong.community.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import zhuke.manong.community.mapper.UserMapper;
import zhuke.manong.community.model.User;
import zhuke.manong.community.model.UserExample;
import zhuke.manong.community.service.NotificationService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/********************************************************************************
 * Purpose: 码农社区拦截器的业务逻辑
 * Author: Caijinbang
 * Created Date:2021-10-26
 * Modify Description:
 * 1. 新增码农社区 拦截器校验用户登录 (Caijinbang 2021-10-26)
 * 2. 修改、更新码农社区 拦截器校验用户登录 (Caijinbang 2021-10-29)
 * 3. 新增用户未读通知数 (Caijinbang 202-2-23)
 ** *****************************************************************************/
@Service
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationService notificationService;

    @Value("${github.redirect.uri}")
    private String githubRedirectUri;

    @Value("${gitee.redirect.uri}")
    private String giteeRedirectUri;

    /*************************************************************************************************
     * Purpose: 对码农社区 访问地址进行拦截，获取用户信息，存储用户未读信息数
     *************************************************************************************************/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //设置 context 级别的属性
        request.getServletContext().setAttribute("giteeRedirectUri", giteeRedirectUri);
        request.getServletContext().setAttribute("githubRedirectUri", githubRedirectUri);

        Cookie[] cookies = request.getCookies();
        if (cookies!=null && cookies.length!=0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token") && StringUtils.isNotBlank(cookie.getValue())) {
                    //根据获取到的token，在数据库中查找用户信息
                    String token = cookie.getValue();
                    UserExample userExample = new UserExample();
                    userExample.createCriteria()
                            .andTokenEqualTo(token);
                    List<User> users = userMapper.selectByExample(userExample);

                    //存储用户信息至 session 中
                    if (users.size() != 0) {
                        HttpSession session = request.getSession();
                        session.setAttribute("user", users.get(0));
                        Long unreadCount = notificationService.unreadCount(users.get(0).getId());
                        session.setAttribute("unreadCount", unreadCount);
                    }
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
