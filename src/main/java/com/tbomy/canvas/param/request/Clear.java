package com.tbomy.canvas.param.request;

import com.tbomy.canvas.param.response.Circle;

/*@author cj
 *@date 2023/12/30 10:42:19
 */
public class Clear {
    private Circle[] circleArr;
    private int index;
    
    public Clear(Circle[] circleArr, int index) {
        this.circleArr = circleArr;
        this.index = index;
    }
    
    public Circle[] getCircleArr() {
        return circleArr;
    }
    
    public void setCircleArr(Circle[] circleArr) {
        this.circleArr = circleArr;
    }
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
}
