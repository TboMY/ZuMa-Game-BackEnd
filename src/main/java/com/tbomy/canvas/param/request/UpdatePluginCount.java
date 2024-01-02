package com.tbomy.canvas.param.request;

/*@author cj
 *@date 2024/1/2 12:18:13
 */
public class UpdatePluginCount {
    private String name;
    private Integer moneyPlugin;
    private Integer commonPlugin;
    
    public UpdatePluginCount() {
    }
    
    public UpdatePluginCount(String name, Integer moneyPlugin, Integer commonPlugin) {
        this.name = name;
        this.moneyPlugin = moneyPlugin;
        this.commonPlugin = commonPlugin;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getMoneyPlugin() {
        return moneyPlugin;
    }
    
    public void setMoneyPlugin(Integer moneyPlugin) {
        this.moneyPlugin = moneyPlugin;
    }
    
    public Integer getCommonPlugin() {
        return commonPlugin;
    }
    
    public void setCommonPlugin(Integer commonPlugin) {
        this.commonPlugin = commonPlugin;
    }
}
