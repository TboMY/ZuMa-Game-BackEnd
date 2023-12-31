package com.tbomy.canvas.service.stop.impl;

import com.tbomy.canvas.param.request.Rollback;
import com.tbomy.canvas.param.response.Circle;
import com.tbomy.canvas.param.response.CircleTrack;
import com.tbomy.canvas.service.init.InitService;
import com.tbomy.canvas.service.stop.StopRollbackService;
import com.tbomy.canvas.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*@author cj
 *@date 2023/12/31 16:57:36
 */
@Service
public class StopRollbackServiceImpl implements StopRollbackService {
    private final InitService initService;
    
    @Autowired
    public StopRollbackServiceImpl(InitService initService) {
        this.initService = initService;
    }
    
    @Override
    public boolean isStopRollback(Rollback rollback) {
        Circle rollbackCircle = rollback.getRollbackCircle();
        Circle preCircle = rollback.getPreCircle();
        if (rollbackCircle == null || preCircle == null) {
            return false;
        }
        
        CircleTrack[] circleTrackArr = initService.getCircleTrackArr();
        Double[] diffAngleOnSameTrack = initService.getDiffAngleOnSameTrack();
        // 如果此时两个球处于同一轨道,且一定处于圆弧轨道,因为都在直线上不会发请求
        if (rollbackCircle.getIndex() == preCircle.getIndex()) {
            double standardValue = diffAngleOnSameTrack[rollbackCircle.getIndex() - 1];
            return rollbackCircle.getAngle() - preCircle.getAngle() <= standardValue;
        } else {
            // 如果此时两个球处于不同的轨道
            // 从轨道退到直线
            if (preCircle.getIndex() == 0) {
                // rollbackCircle在圆弧轨道上的弧长
                double rollbackCircleS = (rollbackCircle.getAngle() - Math.PI) * diffAngleOnSameTrack[rollbackCircle.getIndex() - 1];
                return initService.getInitLine().getY2() - preCircle.getY() + rollbackCircleS <= 81;
            } else {
                // 从圆弧轨道退到圆弧轨道
                // rollbackCircle的轨道的圆心到preCircle的圆心的距离
                double distance = Math.sqrt(Math.pow(preCircle.getX() - circleTrackArr[rollbackCircle.getIndex() - 1].getX(), 2)
                        + Math.pow(preCircle.getY() - circleTrackArr[rollbackCircle.getIndex() - 1].getY(), 2));
                double standardValue = Util.calculateAngleByThreeSide(circleTrackArr[rollbackCircle.getIndex() - 1].getRadius(), distance, 80);
                return (rollbackCircle.getAngle() - Math.PI) + (2 * Math.PI - preCircle.getAngle()) <= standardValue;
            }
        }
    }
}
