package com.tbomy.canvas.service.next.impl;

import com.tbomy.canvas.mapper.PluginMapper;
import com.tbomy.canvas.mapper.UserMapper;
import com.tbomy.canvas.pojo.Plugin;
import com.tbomy.canvas.service.next.NextLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*@author cj
 *@date 2024/1/1 19:45:25
 */
@Service
public class NextLevelServiceImpl implements NextLevelService {
    private final UserMapper userMapper;
    private final PluginMapper pluginMapper;
    
    @Autowired
    public NextLevelServiceImpl(UserMapper userMapper, PluginMapper pluginMapper) {
        this.userMapper = userMapper;
        this.pluginMapper = pluginMapper;
    }
    
    @Override
    public void nextLevel(String name) {
        Integer levelId = userMapper.selectUserByName(name).getLevelId();
        Plugin plugin = pluginMapper.selectPluginCountByName(name);
        
        pluginMapper.updatePluginCountByName(name, plugin.getMoneyPlugin(), plugin.getCommonPlugin()+1);
        userMapper.updateLevelIdByName(name, levelId + 1);
    }
}
