package zhuke.manong.community.dto;
/********************************************************************************
 * Purpose:用户被 GitHub 重定向回站点的参数
 * Author: Caijinbang
 * Created Date:2021-10-11
 * Modify Description:
 * 1. 添加用户被 GitHub 重定向回站点的参数 (Caijinbang 2021-10-11)
 ** *****************************************************************************/
public class AccessTokenDTO {
    //从 GitHub 收到的 OAuth 应用程序的客户端 ID
    private String client_id;
    //从 GitHub 收到的 OAuth 应用程序的客户端密码
    private String client_secret;
    //收到的作为对上一步的响应的代码
    private String code;
    //授权后发送用户的应用程序中的 URL
    private String redirect_uri;
    //一个不可猜测的随机字符串。它用于防止跨站点请求伪造攻击。
    private String state;

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRedirect_uri() {
        return redirect_uri;
    }

    public void setRedirect_uri(String redirect_uri) {
        this.redirect_uri = redirect_uri;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
