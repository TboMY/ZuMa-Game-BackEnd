package com.tbomy.canvas.service.plugin;

import com.tbomy.canvas.pojo.Plugin;


/*@author cj
 *@date 2024/1/2 11:26:04
 */

public interface PluginService {
    Plugin getPluginCount(String name);
    
    void updatePluginCount(String name, Integer moneyPlugin, Integer commonPlugin);
}


