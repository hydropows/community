package zhuke.manong.community.strategy;

import lombok.Data;
/********************************************************************************
 * Purpose: 封装用户登录的字段信息
 * Author: Caijinbang
 * Created Date:2022-2-28
 * Modify Description:
 * 1. 添加用户登录的字段信息 (Caijinbang 2022-2-28)
 ** *****************************************************************************/
@Data
public class LoginUserInfo {
    //用户名
    private String name;
    //用户id
    private Long id;
    //用户描述
    private String bio;
    //用户头像地址
    private String avatarUrl;
}
