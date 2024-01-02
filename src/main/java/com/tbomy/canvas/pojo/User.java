package com.tbomy.canvas.pojo;

/*@author cj
 *@date 2024/1/1 0:13:41
 */
public class User {
    private Integer id;
    private String name;
    private Integer levelId;
    
    public User() {
    }
    
    public User(Integer id, String name, Integer levelId) {
        this.id = id;
        this.name = name;
        this.levelId = levelId;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getLevelId() {
        return levelId;
    }
    
    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }
}
