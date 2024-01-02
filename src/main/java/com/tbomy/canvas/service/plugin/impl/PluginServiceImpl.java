package com.tbomy.canvas.service.plugin.impl;

import com.tbomy.canvas.mapper.PluginMapper;
import com.tbomy.canvas.pojo.Plugin;
import com.tbomy.canvas.service.plugin.PluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/*@author cj
 *@date 2024/1/2 11:27:01
 */
@Service
public class PluginServiceImpl implements PluginService {
    private final PluginMapper pluginMapper;
    
    @Override
    public Plugin getPluginCount(String name) {
        System.out.println(pluginMapper.selectPluginCountByName(name));
        return pluginMapper.selectPluginCountByName(name);
    }
    
    @Override
    public void updatePluginCount(String name, Integer moneyPlugin, Integer commonPlugin) {
        pluginMapper.updatePluginCountByName(name, moneyPlugin, commonPlugin);
    }
    
    @Autowired
    public PluginServiceImpl(PluginMapper pluginMapper) {
        this.pluginMapper = pluginMapper;
    }
}
