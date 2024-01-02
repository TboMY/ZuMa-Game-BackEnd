package com.tbomy.canvas.pojo;

/*@author cj
 *@date 2024/1/2 11:33:10
 */
public class Plugin {
    private String name;
    private Integer moneyPlugin;
    private Integer commonPlugin;
    
    public Plugin() {
    }
    
    public Plugin(String name, Integer moneyPlugin, Integer commonPlugin) {
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
    
    @Override
    public String toString() {
        return "Plugin{" +
                "name='" + name + '\'' +
                ", moneyPlugin=" + moneyPlugin +
                ", commonPlugin=" + commonPlugin +
                '}';
    }
}
