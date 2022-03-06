package zhuke.manong.community.provide.dto;

import lombok.Data;
/********************************************************************************
 * Purpose: Get 请求传参获取 Gitee 用户信息 的 字段
 * Author: Caijinbang
 * Created Date: 2022-2-28
 * Modify Description:
 * 1. 添加 Get 请求传参获取 Gitee 用户信息 的 字段 (Caijinbang 2022-2-28)
 ** *****************************************************************************/
@Data
public class GiteeUser {
    //用户名
    private String name;
    //用户id
    private Long id;
    //用户描述
    private String bio;
    //用户头像地址
    private String avatarUrl;
}
