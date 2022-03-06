package zhuke.manong.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zhuke.manong.community.mapper.UserMapper;
import zhuke.manong.community.model.User;
import zhuke.manong.community.model.UserExample;

import java.util.List;

/********************************************************************************
 * Purpose: 码农社区的登入判断的业务逻辑
 * Author: Caijinbang
 * Created Date:2021-10-12
 * Modify Description:
 * 1. 码农社区 Github 登录的登入判断的业务逻辑 (Caijinbang 2021-10-14)
 * 2. 修改Github 登录的登入判断的业务逻辑中的 userMapper 语句逻辑 (Caijinbang 2021-10-30)
 ** *****************************************************************************/
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /*************************************************************************************************
     * Purpose: 码农社区 Github 登录的登入判断的业务逻辑，判断该用户是否已登录过
     * Param: user 用户信息
     *************************************************************************************************/
    public void createOrUpdate(User user) {
        //根据用户id查找数据库中对应的数据
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(user.getAccountId())
                .andTypeEqualTo(user.getType());
        List<User> users = userMapper.selectByExample(userExample);
        //用户初次登录
        if(users.size() == 0){
            //插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            //插入操作
            userMapper.insert(user);
        }
        //用户已登录过
        else{
            //更新
            User dbUser = users.get(0);

            User updateUser = new User();
            //更新用户修改日期
            updateUser.setGmtModified(System.currentTimeMillis());
            //更新用户头像地址
            updateUser.setAvatarUrl(user.getAvatarUrl());
            //更新用户名称
            updateUser.setName(user.getName());
            //更新用户token值
            updateUser.setToken(user.getToken());
            //更新操作
            UserExample example = new UserExample();
            example.createCriteria()
                    .andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, example);
        }
    }
}
