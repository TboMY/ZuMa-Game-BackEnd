package com.tbomy.canvas.service.hit.impl;

import com.tbomy.canvas.param.request.Hit;
import com.tbomy.canvas.param.response.Circle;
import com.tbomy.canvas.param.response.CircleTrack;
import com.tbomy.canvas.service.hit.HitService;
import com.tbomy.canvas.service.init.InitService;
import com.tbomy.canvas.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*@author cj
 *@date 2023/12/31 15:16:08
 */

@Service
public class HitServiceImpl implements HitService {
    private final InitService initService;
    
    @Autowired
    public HitServiceImpl(InitService initService) {
        this.initService = initService;
    }
    
    @Override
    public Circle[] hit(Hit hit) {
        // 被碰撞的圆
        Circle trackCircle = hit.getTrackCircle();
        // 射击的圆
        Circle shotCircle = hit.getShotCircle();
        // 被碰撞的圆在所有球数组中的索引
        int index = hit.getIndexInCircleArr();
        
        // 被撞球是否在直线上
        boolean isOnLine = trackCircle.getIndex() == 0;
        
        // 先插入,然后再进行是否消除判断
        boolean isInsertOnBefore = insert(isOnLine, trackCircle, shotCircle, index, initService.getCircleTrackArr(), hit);
        // 新插入的球在新数组中的索引
        index = isInsertOnBefore ? index : index + 1;
        // 尝试消除满足消除条件的球
        return Util.tryRemoveSameColorCircle(index, hit.getCircleArr());
    }
    
    // 插入
    @Override
    public boolean insert(boolean isOnLine, Circle trackCircle, Circle shotCircle, int index, CircleTrack[] circleTrackArr, Hit hit) {
        if (isOnLine) {
            return insertOnLine(trackCircle, shotCircle, index, circleTrackArr, hit);
        } else {
            boolean isInsertBefore = Util.isInsertBefore(circleTrackArr, trackCircle, shotCircle);
            return insertOnCircleTrack(trackCircle, shotCircle, trackCircle.getIndex(), index, isInsertBefore, circleTrackArr, hit);
        }
    }
    
    // 在直线上插入
    @Override
    public boolean insertOnLine(Circle trackCircle, Circle shotCircle, int indexInCircleArr, CircleTrack[] circleTrackArr, Hit hit) {
        Circle[] arr = hit.getCircleArr();
        // 前插
        if (shotCircle.getY() >= trackCircle.getY()) {
            // 默认不会跨轨道
            setShotCircleXAndY(shotCircle, trackCircle.getX(), trackCircle.getY() + 80);
            // 如果跨轨道
            if (isStepTrack(trackCircle)) {
                // 设置跨轨道后射击球的角度
                setAngleWhenStepToCircleTrack(trackCircle, shotCircle, circleTrackArr);
            }
            // 给插入的预留空间,将前面的都向前移动(不包括被碰撞的圆)
            if (indexInCircleArr != 0) {
                foreachMoveCircleOnLineTrackForward(indexInCircleArr - 1, circleTrackArr, arr);
            }
            // 真正改变数组,前插
            hit.setCircleArr(Util.insertAtIndexBefore(arr, indexInCircleArr, shotCircle));
            return true;
        } else {
            // 默认打中的不是最后一个球
            setShotCircleXAndY(shotCircle, trackCircle.getX(), trackCircle.getY());
            // 如果后插打中最后一个球,则不会把前面的球向前移动
            if (indexInCircleArr == arr.length - 1) {
                shotCircle.setY(trackCircle.getY() - 80);
            }
            // 给插入的预留空间,将前面的都向前移动(包括本身)
            if (indexInCircleArr != arr.length - 1) {
                foreachMoveCircleOnLineTrackForward(indexInCircleArr, circleTrackArr, arr);
            }
            // 真正改变数组,后插
            hit.setCircleArr(Util.insertAtIndexAfter(arr, indexInCircleArr, shotCircle));
            return false;
        }
    }
    
    // 设置射击球的x,y
    @Override
    public void setShotCircleXAndY(Circle shotCircle, double x, double y) {
        shotCircle.setX(x);
        shotCircle.setY(y);
    }
    
    // for循环将球向前移动
    @Override
    public void foreachMoveCircleOnLineTrackForward(int indexInCircleArr, CircleTrack[] circleTrackArr, Circle[] arr) {
        for (int i = indexInCircleArr; i >= 0; i--) {
            // 如果是直线上的球
            if (arr[i].getIndex() == 0) {
                // 如果跨轨道
                if (isStepTrack(arr[i])) {
                    setAngleWhenStepToCircleTrack(arr[i], arr[i], circleTrackArr);
                    continue;
                }
                arr[i].setY(arr[i].getY() + 80);
            } else {
                // 如果是半圆轨道上的球
                // 计算角度差(弧长=角度*半径),80为弧长
                double diffAngle = 2 * 40.0 / (circleTrackArr[arr[i].getIndex() - 1].getRadius());
                moveCircleOnTrackForward(arr[i], diffAngle, circleTrackArr);
            }
        }
    }
    
    // 判断是否会从直线跨界到圆弧轨道
    @Override
    public boolean isStepTrack(Circle trackCircle) {
        return trackCircle.getY() + 80 > initService.getInitLine().getY2();
    }
    
    // 设置从直线跨界到圆弧轨道时的小球角度
    @Override
    public void setAngleWhenStepToCircleTrack(Circle trackCircle, Circle circle, CircleTrack[] circleTrackArr) {
        double diffS = trackCircle.getY() + 80 - initService.getInitLine().getY2();
        double diffAngle = diffS / circleTrackArr[0].getRadius();
        circle.setAngle(Math.PI + diffAngle);
        circle.setIndex(1);
    }
    
    
    /**
     * 在圆轨道上相撞
     *
     * @param trackCircle      被碰撞的圆
     * @param shotCircle       射击的圆
     * @param indexInTrackArr  被碰撞的圆在第几个轨道上
     * @param indexInCircleArr 被碰撞的圆在所有球数组中的索引
     * @param isInsertBefore   是往前插入还是往后
     */
    @Override
    public boolean insertOnCircleTrack(Circle trackCircle, Circle shotCircle, int indexInTrackArr, int indexInCircleArr, boolean isInsertBefore, CircleTrack[] circleTrackArr, Hit hit) {
        Circle[] arr = hit.getCircleArr();
        // 被碰撞的球和其下一个球之间夹角
        double diffAngle = initService.getDiffAngleOnSameTrack()[indexInTrackArr - 1];
        // 前插?
        if (isInsertBefore) {
            // 如果会进入下一个轨道
            if (trackCircle.getAngle() + diffAngle >= Math.PI * 2) {
                // 如果跨越了轨道,则加的角度不再是简单的diffAngle,而是另外计算的角(数学问题)
                double checkAngle = calculateDiffAngle(trackCircle, circleTrackArr[trackCircle.getIndex() == 3 ? 2 : trackCircle.getIndex()]);
                setShotCircleAngleAndIndex(shotCircle, checkAngle, trackCircle.getIndex() + 1);
            } else {
                setShotCircleAngleAndIndex(shotCircle, trackCircle.getAngle() + diffAngle, trackCircle.getIndex());
            }
            
            // 给插入的预留空间,将前面的都向前移动(不包括被碰撞的圆)
            if (indexInCircleArr != 0) {
                foreachMoveCircleOnCircleTrackForward(indexInCircleArr - 1, diffAngle, circleTrackArr, arr);
            }
            
            // 前插入
            hit.setCircleArr(Util.insertAtIndexBefore(arr, indexInCircleArr, shotCircle));
            return true;
        } else {
            // 默认打中的不是最后一个球
            setShotCircleAngleAndIndex(shotCircle, trackCircle.getAngle(), trackCircle.getIndex());
            
            // 如果打中的是最后一个球,则不会把前面的球向前移动(可能有bug,先不管
            if (indexInCircleArr == arr.length - 1) {
                setShotCircleAngleAndIndex(shotCircle, trackCircle.getAngle() - diffAngle, trackCircle.getIndex());
            }
            
            // 给插入的预留空间,将前面的都向前移动(包括本身)
            if (indexInCircleArr != arr.length - 1) {
                foreachMoveCircleOnCircleTrackForward(indexInCircleArr, diffAngle, circleTrackArr, arr);
            }
            // 后插入
            hit.setCircleArr(Util.insertAtIndexAfter(arr, indexInCircleArr, shotCircle));
            return false;
        }
    }
    
    @Override
    public void setShotCircleAngleAndIndex(Circle shotCircle, double angle, int index) {
        shotCircle.setAngle(angle);
        shotCircle.setIndex(index);
    }
    
    @Override
    public void foreachMoveCircleOnCircleTrackForward(int indexInCircleArr, double diffAngle, CircleTrack[] circleTrackArr, Circle[] arr) {
        for (int i = indexInCircleArr; i >= 0; i--) {
            if (arr[i].getAngle() + diffAngle >= Math.PI * 2) {
                // 校验角度
                double checkAngle = calculateDiffAngle(arr[i], circleTrackArr[arr[i].getIndex() == 3 ? 2 : arr[i].getIndex()]);
                arr[i].setAngle(checkAngle);
                arr[i].setIndex(arr[i].getIndex() + 1);
            } else {
                arr[i].setAngle(arr[i].getAngle() + initService.getDiffAngleOnSameTrack()[arr[i].getIndex() - 1]);
            }
        }
    }
    
    // 在圆弧轨道上,当球跨轨道时,计算圆与水平线的夹角
    @Override
    public double calculateDiffAngle(Circle circle, CircleTrack track) {
        // 下一个轨道的半径
        double r = track.getRadius();
        // 下一个轨道圆心到被撞击圆心的距离
        double distanceFromCircleCenterToOnTrackCircle = Math.sqrt(Math.pow(circle.getX() - track.getX(), 2) + Math.pow(circle.getY() - track.getY(), 2));
        // 两个小圆之间的距离
        double distanceFromOnTrackCircleToNextCircle = 40 * 2;
        // 上两个小圆之间的夹角
        double totalAngle = Util.calculateAngleByThreeSide(r, distanceFromCircleCenterToOnTrackCircle, distanceFromOnTrackCircleToNextCircle);
        // 两个轨道相切点
        double endpointX = circle.getIndex() % 2 == 1 ? track.getX() + track.getRadius() : track.getX() - track.getRadius();
        double endpointY = track.getY();
        // 被撞击圆心到相切点的距离
        double distanceFromEndpointToOnTrackCircle = Math.sqrt(Math.pow(circle.getX() - endpointX, 2) + Math.pow(circle.getY() - endpointY, 2));
        // 被撞击圆心到相切点那个水平线的夹角
        double alreadyAngle = Util.calculateAngleByThreeSide(r, distanceFromCircleCenterToOnTrackCircle, distanceFromEndpointToOnTrackCircle);
        return totalAngle - alreadyAngle + Math.PI;
    }
    
    
    // 将圆弧轨道上的小球向前挪动
    @Override
    public void moveCircleOnTrackForward(Circle trackCircle, double diffAngle, CircleTrack[] circleTrackArr) {
        if (trackCircle.getAngle() + diffAngle >= Math.PI * 2) {
            // 校验角度
            double checkAngle = calculateDiffAngle(trackCircle, circleTrackArr[trackCircle.getIndex() == 3 ? 2 : trackCircle.getIndex()]);
            trackCircle.setAngle(checkAngle);
            trackCircle.setIndex(trackCircle.getIndex() + 1);
        } else {
            trackCircle.setAngle(trackCircle.getAngle() + diffAngle);
        }
    }
}
