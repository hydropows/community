package zhuke.manong.community.provide.dto;
/********************************************************************************
 * Purpose: Get 请求传参获取 Github 用户信息 的 字段
 * Author: Caijinbang
 * Created Date:2021-10-11
 * Modify Description:
 * 1. 添加 Get 请求传参获取 Github 用户信息 的 字段 (Caijinbang 2021-10-11)
 ** *****************************************************************************/
public class GithubUser {
    //用户名
    private String name;
    //用户Id
    private Long id;
    //描述
    private String bio;
    //用户头像地址
    private String avatarUrl;

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public String toString() {
        return "GithubUser{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", bio='" + bio + '\'' +
                '}';
    }
}
