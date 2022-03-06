package zhuke.manong.community.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zhuke.manong.community.dto.AccessTokenDTO;
import zhuke.manong.community.provide.dto.GithubUser;
import zhuke.manong.community.provide.GithubProvider;

/********************************************************************************
 * Purpose: 码农社区 Github 的登入
 * Author: Caijinbang
 * Created Date: 2022-2-28
 * Modify Description:
 * 1. 新增码农社区 Github 登录的业务逻辑 (Caijinbang 2022-2-28)
 ** *****************************************************************************/
@Service
public class GithubUserStrategy implements UserStrategy{
    @Autowired
    private GithubProvider githubProvider;

    @Override
    public LoginUserInfo getUser(String code, String state) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        LoginUserInfo loginUserInfo = new LoginUserInfo();
        //用户头像
        loginUserInfo.setAvatarUrl(githubUser.getAvatarUrl());
        //用户描述
        loginUserInfo.setBio(githubUser.getBio());
        //用户id
        loginUserInfo.setId(githubUser.getId());
        //用户名
        loginUserInfo.setName(githubUser.getName());
        return loginUserInfo;
    }

    @Override
    public String getSupportedType() {
        return "github";
    }
}
