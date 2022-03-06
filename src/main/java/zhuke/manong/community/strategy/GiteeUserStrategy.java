package zhuke.manong.community.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zhuke.manong.community.dto.AccessTokenDTO;
import zhuke.manong.community.provide.GiteeProvider;
import zhuke.manong.community.provide.dto.GiteeUser;
/********************************************************************************
 * Purpose: 码农社区 Gitee 的登入
 * Author: Caijinbang
 * Created Date: 2022-2-28
 * Modify Description:
 * 1. 新增码农社区 Gitee 登录的业务逻辑 (Caijinbang 2022-2-28)
 ** *****************************************************************************/
@Service
public class GiteeUserStrategy implements UserStrategy {
    @Autowired
    private GiteeProvider giteeProvider;

    @Override
    public LoginUserInfo getUser(String code, String state) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        String accessToken = giteeProvider.getAccessToken(accessTokenDTO);
        GiteeUser giteeUser = giteeProvider.getUser(accessToken);
        LoginUserInfo loginUserInfo = new LoginUserInfo();
        //用户头像
        loginUserInfo.setAvatarUrl(giteeUser.getAvatarUrl());
        //用户描述
        loginUserInfo.setBio(giteeUser.getBio());
        //用户id
        loginUserInfo.setId(giteeUser.getId());
        //用户名
        loginUserInfo.setName(giteeUser.getName());
        return loginUserInfo;
    }

    @Override
    public String getSupportedType() {
        return "gitee";
    }
}
