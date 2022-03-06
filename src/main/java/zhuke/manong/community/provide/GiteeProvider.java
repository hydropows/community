package zhuke.manong.community.provide;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import zhuke.manong.community.dto.AccessTokenDTO;
import zhuke.manong.community.provide.dto.GiteeUser;

/********************************************************************************
 * Purpose: Gitee 的 工具类
 * Author: Caijinbang
 * Created Date: 2022-2-28
 * Modify Description:
 * 1. 添加获取 access_token 的方法 (Caijinbang 2022-2-28)
 * 2. 添加获取 用户信息 的方法 (Caijinbang 2022-2-28)
 ** *****************************************************************************/
@Component
@Slf4j
public class GiteeProvider {
    @Value("${gitee.client.id}")
    private String clientId;

    @Value("${gitee.client.secret}")
    private String clientSecret;

    @Value("${gitee.redirect.uri}")
    private String redirectUri;

    /*******************************************************************************
     * Purpose: 通过 Post 请求传参到 Gitee 中获取 access_token
     * Param: accessTokenDTO Post 请求传参对象
     * Return: string 返回 access_token
     ********************************************************************************/
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        //决定将以什么形式、什么编码对资源进行解析
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        //请求
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        //发送 Post 请求
        String url = "https://gitee.com/oauth/token?grant_type=authorization_code&code=%s&client_id=%s&redirect_uri=%s&client_secret=%s";
        url = String.format(url, accessTokenDTO.getCode(), clientId, redirectUri, clientSecret);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            //获取返回的内容
            String string = response.body().string();
            JSONObject jsonObject = JSON.parseObject(string);
            return jsonObject.getString("access_token");
        } catch (Exception e) {
            log.error("getAccessToken error,{}", accessTokenDTO, e);
        }
        return null;
    }

    /*******************************************************************************
     * Purpose: 通过 Get 请求传参到 Gitee 中获取 用户信息
     * Param: accessToken Get 请求传参
     * Return: GiteeUser 返回 用户信息
     ********************************************************************************/
    public GiteeUser getUser(String accessToken) {
        //请求
        OkHttpClient client = new OkHttpClient();
        //发送 Get 请求
        Request request = new Request.Builder()
                .url("https://gitee.com/api/v5/user?access_token=" + accessToken)
                .build();
        try {
            //获取返回的内容
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GiteeUser giteeUser = JSON.parseObject(string, GiteeUser.class);
            return giteeUser;
        } catch (Exception e) {
            log.error("getUser error,{}", accessToken, e);
        }
        return null;
    }
}
