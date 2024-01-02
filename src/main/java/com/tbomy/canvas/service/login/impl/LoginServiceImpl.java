package com.tbomy.canvas.service.login.impl;

import com.tbomy.canvas.mapper.PluginMapper;
import com.tbomy.canvas.mapper.UserMapper;
import com.tbomy.canvas.param.request.LoginInfo;
import com.tbomy.canvas.pojo.User;
import com.tbomy.canvas.service.login.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*@author cj
 *@date 2024/1/1 0:27:52
 */
@Service
public class LoginServiceImpl implements LoginService {
    private final UserMapper userMapper;
    private final PluginMapper pluginMapper;
    
    @Override
    public void login(LoginInfo loginInfo) {
        String name = loginInfo.getName();
        Boolean isNewGame = loginInfo.getNewGame();
        User user = userMapper.selectUserByName(name);
        
        // 如果没有这个用户，无论是否新游戏
        if (user == null) {
            User newUser = new User(null, name, 1);
            userMapper.insertUser(newUser);
            pluginMapper.insertPluginCountByName(name);
        } else {
            // 如果有这个用户，如果是新游戏，就把levelId设为1
            if (isNewGame) {
                userMapper.updateLevelIdByName(name, 1);
                pluginMapper.updatePluginCountByName(name, 0, 0);
            }
        }
    }
    
    @Autowired
    public LoginServiceImpl(UserMapper userMapper, PluginMapper pluginMapper) {
        this.userMapper = userMapper;
        this.pluginMapper = pluginMapper;
    }
}
