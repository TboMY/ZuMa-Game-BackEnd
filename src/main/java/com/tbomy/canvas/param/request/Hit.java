package com.tbomy.canvas.param.request;

import com.tbomy.canvas.param.response.Circle;

import java.util.Arrays;

/*@author cj
 *@date 2023/12/24 18:11:49
 */
public class Hit {
    private Circle trackCircle;
    private Circle shotCircle;
    private int indexInCircleArr;
    private Circle[] circleArr;
    
    public Hit(Circle trackCircle, Circle shotCircle, int indexInCircleArr, Circle[] circleArr) {
        this.trackCircle = trackCircle;
        this.shotCircle = shotCircle;
        this.indexInCircleArr = indexInCircleArr;
        this.circleArr = circleArr;
    }
    
    public Circle getTrackCircle() {
        return trackCircle;
    }
    
    public void setTrackCircle(Circle trackCircle) {
        this.trackCircle = trackCircle;
    }
    
    public Circle getShotCircle() {
        return shotCircle;
    }
    
    public void setShotCircle(Circle shotCircle) {
        this.shotCircle = shotCircle;
    }
    
    public int getIndexInCircleArr() {
        return indexInCircleArr;
    }
    
    public void setIndexInCircleArr(int indexInCircleArr) {
        this.indexInCircleArr = indexInCircleArr;
    }
    
    public Circle[] getCircleArr() {
        return circleArr;
    }
    
    public void setCircleArr(Circle[] circleArr) {
        this.circleArr = circleArr;
    }
    
    @Override
    public String toString() {
        return "Hit{" +
                "trackCircle=" + trackCircle +
                ", shotCircle=" + shotCircle +
                ", indexInCircleArr=" + indexInCircleArr +
                ", circleArr=" + Arrays.toString(circleArr) +
                '}';
    }
}
