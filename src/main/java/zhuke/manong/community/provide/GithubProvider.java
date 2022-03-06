package zhuke.manong.community.provide;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import zhuke.manong.community.dto.AccessTokenDTO;
import zhuke.manong.community.provide.dto.GithubUser;

/********************************************************************************
 * Purpose:Github 的 工具类
 * Author: Caijinbang
 * Created Date:2021-10-11
 * Modify Description:
 * 1. 添加获取 access_token 的方法 (Caijinbang 2021-10-11)
 * 2. 添加获取 用户信息 的方法 (Caijinbang 2021-10-11)
 ** *****************************************************************************/
@Component
@Slf4j
public class GithubProvider {

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    /*******************************************************************************
     * Purpose: 通过 Post 请求传参到 Github 中获取 access_token
     * Param: accessTokenDTO Post 请求传参对象
     * Return: string 返回 access_token
     ********************************************************************************/
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setRedirect_uri(redirectUri);
        //决定将以什么形式、什么编码对资源进行解析
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        //请求
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        //发送 Post 请求
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            //获取返回的内容
            String string = response.body().string();
            //获取access_token
            String token = string.split("&")[0].split("=")[1];
            return token;
        } catch (Exception e) {
            log.error("getAccessToken error,{}", accessTokenDTO, e);
        }
        return null;
    }

    /*******************************************************************************
     * Purpose: 通过 Get 请求传参到 Github 中获取 用户信息
     * Param: accessToken Get 请求传参
     * Return: GithubUser 返回 用户信息
     ********************************************************************************/
    public GithubUser getUser(String accessToken){
        //请求
        OkHttpClient client = new OkHttpClient();
        //发送 Get 请求
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .header("Authorization", "token " + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            //获取返回的内容
            String string = response.body().string();
            //获取 用户信息
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (Exception e) {
            log.error("getUser error,{}", accessToken, e);
        }
        return null;
    }
}