package com.tbomy.canvas.service.hit;

import com.tbomy.canvas.param.request.Hit;
import com.tbomy.canvas.param.response.Circle;
import com.tbomy.canvas.param.response.CircleTrack;

public interface HitService {
    Circle[] hit(Hit hit);
    
    boolean insert(boolean isOnLine, Circle trackCircle, Circle shotCircle, int index, CircleTrack[] circleTrackArr, Hit hit);
    
    
    boolean insertOnLine(Circle trackCircle, Circle shotCircle, int indexInCircleArr, CircleTrack[] circleTrackArr, Hit hit);
    
    void setShotCircleXAndY(Circle shotCircle, double x, double y);
    
    void foreachMoveCircleOnLineTrackForward(int indexInCircleArr, CircleTrack[] circleTrackArr, Circle[] arr);
    
    boolean isStepTrack(Circle trackCircle);
    
    void setAngleWhenStepToCircleTrack(Circle trackCircle, Circle circle, CircleTrack[] circleTrackArr);
    
    
    
    boolean insertOnCircleTrack(Circle trackCircle, Circle shotCircle, int indexInTrackArr, int indexInCircleArr, boolean isInsertBefore, CircleTrack[] circleTrackArr, Hit hit);
    
    void setShotCircleAngleAndIndex(Circle shotCircle, double angle, int index);
    
    void foreachMoveCircleOnCircleTrackForward(int indexInCircleArr, double diffAngle, CircleTrack[] circleTrackArr, Circle[] arr);
    
    double calculateDiffAngle(Circle circle, CircleTrack track);
    
    
    void moveCircleOnTrackForward(Circle trackCircle, double diffAngle, CircleTrack[] circleTrackArr);
    
}
