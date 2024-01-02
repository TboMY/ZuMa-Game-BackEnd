package com.tbomy.canvas.param.request;

/*@author cj
 *@date 2024/1/1 12:52:24
 */
public class LoginInfo {
    private String name;
    private Boolean newGame;
    
    public LoginInfo() {
    }
    
    public LoginInfo(String name, Boolean newGame) {
        this.name = name;
        this.newGame = newGame;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Boolean getNewGame() {
        return newGame;
    }
    
    public void setNewGame(Boolean newGame) {
        this.newGame = newGame;
    }
}
