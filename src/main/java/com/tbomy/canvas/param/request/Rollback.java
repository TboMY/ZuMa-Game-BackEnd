package com.tbomy.canvas.param.request;

import com.tbomy.canvas.param.response.Circle;

/*@author cj
 *@date 2023/12/28 21:34:32
 */
public class Rollback {
    private Circle rollbackCircle;
    private Circle preCircle;
    
    public Rollback(Circle rollbackCircle, Circle preCircle) {
        this.rollbackCircle = rollbackCircle;
        this.preCircle = preCircle;
    }
    
    public Circle getRollbackCircle() {
        return rollbackCircle;
    }
    
    public void setRollbackCircle(Circle rollbackCircle) {
        this.rollbackCircle = rollbackCircle;
    }
    
    public Circle getPreCircle() {
        return preCircle;
    }
    
    public void setPreCircle(Circle preCircle) {
        this.preCircle = preCircle;
    }
}
