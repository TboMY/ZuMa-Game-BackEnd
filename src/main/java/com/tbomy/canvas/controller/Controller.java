package com.tbomy.canvas.controller;

import com.tbomy.canvas.param.request.Hit;
import com.tbomy.canvas.param.request.Rollback;
import com.tbomy.canvas.param.response.Circle;
import com.tbomy.canvas.param.response.CircleTrack;
import com.tbomy.canvas.param.response.InitLine;
import com.tbomy.canvas.util.Util;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class Controller {
    // 直线轨道
    InitLine initLine = new InitLine(400, 0, 400, 400);
    // 半圆轨道
    CircleTrack[] circleTrackArr = {
            new CircleTrack(900, 400, 500, true),
            new CircleTrack(1000, 400, 400, false),
            new CircleTrack(900, 400, 300, true)
    };
    // 在不同的半圆轨道上,每两个圆之间有同的夹角
    Double[] diffAngleOnSameTrack = new Double[circleTrackArr.length];
    // 轨道上的所有球
    private Circle[] arr;
    // 所有球的颜色
    private final String[] colorArr = {"#00c0ff", "#fff900", "#00ff00", "#ff0000"};
    
    // 所有轨道初始数据
    @GetMapping("/initTracks")
    public Object[] initTracks() {
        computeDiffAngleOnSameTrack();
        return new Object[]{initLine, circleTrackArr};
    }
    
    // 轨道上的小球初始数据
    @GetMapping("/initCircles")
    public Circle[] initCircles() {
        // 生成轨道上的圆
        arr = new Circle[60];
        for (int i = 0; i < arr.length; i++) {
            Circle circle = new Circle(400, i * -80, colorArr[(int) (Math.random() * 4)]);
            arr[i] = circle;
        }
        return arr;
    }
    
    // 生成Circle的实例
    @GetMapping("/newShotCircle")
    public Circle newShotCircle() {
        return new Circle(70, 0, colorArr[(int) (Math.random() * 4)]);
//        return new Circle(70, 0, "#095c1a");
    }
    
    //  小球撞击后
    @PostMapping("/hit")
    public Circle[] hit(@RequestBody Hit hit) {
        // 被碰撞的圆
        Circle trackCircle = hit.getTrackCircle();
        // 射击的圆
        Circle shotCircle = hit.getShotCircle();
        // 被碰撞的圆在所有球数组中的索引
        int index = hit.getIndexInCircleArr();
        // 因为前端动画改变了数组内元素
        arr = hit.getCircleArr();
        // 被撞球是否在直线上
        boolean isOnLine = trackCircle.getIndex() == 0;
        
        // 先插入,然后再进行是否消除判断
        boolean isInsertOnBefore = insert(isOnLine, trackCircle, shotCircle, index);
        // 新插入的球在新数组中的索引
        index = isInsertOnBefore ? index : index + 1;
        // 尝试消除满足消除条件的球
        tryRemoveSameColorCircle(index);
        return arr;
    }
    
    // 插入
    public boolean insert(boolean isOnLine, Circle trackCircle, Circle shotCircle, int index) {
        if (isOnLine) {
            return insertOnLine(trackCircle, shotCircle, index);
        } else {
            boolean isInsertBefore = Util.isInsertBefore(circleTrackArr, trackCircle, shotCircle);
            return insertOnCircleTrack(trackCircle, shotCircle, trackCircle.getIndex(), index, isInsertBefore);
        }
    }
    
    // 在直线上插入
    public boolean insertOnLine(Circle trackCircle, Circle shotCircle, int indexInCircleArr) {
        // 前插
        if (shotCircle.getY() >= trackCircle.getY()) {
            // 默认不会跨轨道
            shotCircle.setY(trackCircle.getY() + 80);
            shotCircle.setX(trackCircle.getX());
            
            // 如果跨轨道
            if (whetherFromLineToCircleTrack(trackCircle)) {
                // 设置跨轨道后射击球的角度
                setAngleWhenFromLineToCircleTrack(trackCircle, shotCircle);
            }
            
            // 给插入的预留空间,将前面的都向前移动(不包括被碰撞的圆)
            if (indexInCircleArr != 0) {
                for (int i = indexInCircleArr - 1; i >= 0; i--) {
                    // 如果是直线上的球
                    if (arr[i].getIndex() == 0) {
                        // 如果跨轨道
                        if (whetherFromLineToCircleTrack(arr[i])) {
                            setAngleWhenFromLineToCircleTrack(arr[i], arr[i]);
                            continue;
                        }
                        arr[i].setY(arr[i].getY() + 80);
                    } else {
                        // 如果是半圆轨道上的球
                        // 计算角度差(弧长=角度*半径),80为弧长
                        double diffAngle = 2 * 40.0 / (circleTrackArr[arr[i].getIndex() - 1].getRadius());
                        moveCircleTrackForward(arr[i], diffAngle);
//                        double diffAngle = 2 * 40.0 / (circleTrackArr[arr[i].getIndex() - 1].getRadius());
//                        if (arr[i].getAngle() + diffAngle >= Math.PI * 2) {
//                            // 校验角度
//                            double checkAngle = calculateDiffAngle(arr[i], circleTrackArr[arr[i].getIndex() == 3 ? 2 : arr[i].getIndex()]);
//                            arr[i].setAngle(checkAngle);
//                            arr[i].setIndex(arr[i].getIndex() + 1);
//                        } else {
//                            arr[i].setAngle(arr[i].getAngle() + diffAngleOnSameTrack[arr[i].getIndex() - 1]);
//                        }
                    }
                }
            }
            arr = Util.insertAtIndexBefore(arr, indexInCircleArr, shotCircle);
            return true;
        } else {
            // 默认打中的不是最后一个球
            shotCircle.setY(trackCircle.getY());
            shotCircle.setX(trackCircle.getX());
            
            // 如果打中的是最后一个球,则不会把前面的球向前移动
            if (indexInCircleArr == arr.length - 1) {
                shotCircle.setY(trackCircle.getY() - 80);
                shotCircle.setIndex(trackCircle.getIndex());
            }
            
            // 给插入的预留空间,将前面的都向前移动(包括本身)
            if (indexInCircleArr != arr.length - 1) {
                for (int i = indexInCircleArr; i >= 0; i--) {
                    // 在直线上
                    if (arr[i].getIndex() == 0) {
                        // 从直线跨轨道到半圆
                        if (whetherFromLineToCircleTrack(arr[i])) {
                            setAngleWhenFromLineToCircleTrack(arr[i], arr[i]);
                            continue;
                        }
                        arr[i].setY(arr[i].getY() + 80);
                    } else {
                        // 计算角度差
                        double diffAngle = 2 * 40.0 / (circleTrackArr[arr[i].getIndex() - 1].getRadius());
                        moveCircleTrackForward(arr[i], diffAngle);
//                        if (arr[i].getAngle() + diffAngle >= Math.PI * 2) {
//                            // 校验角度
//                            double checkAngle = calculateDiffAngle(arr[i], circleTrackArr[arr[i].getIndex() == 3 ? 2 : arr[i].getIndex()]);
//                            arr[i].setAngle(checkAngle);
//                            arr[i].setIndex(arr[i].getIndex() + 1);
//                        } else {
//                            arr[i].setAngle(arr[i].getAngle() + diffAngleOnSameTrack[arr[i].getIndex() - 1]);
//                        }
                    }
                }
            }
            // 后插入
            arr = Util.insertAtIndexAfter(arr, indexInCircleArr, shotCircle);
            return false;
        }
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
    public boolean insertOnCircleTrack(Circle trackCircle, Circle shotCircle, int indexInTrackArr, int indexInCircleArr, boolean isInsertBefore) {
        // 计算被碰撞的球和其下一个球之间夹角
        double diffAngle = diffAngleOnSameTrack[indexInTrackArr - 1];
        
        if (isInsertBefore) {
            // 如果会进入下一个轨道
            if (trackCircle.getAngle() + diffAngle >= Math.PI * 2) {
                // 如果跨越了轨道,则加的角度不再是简单的diffAngle,而是另外计算的角(数学问题)
                double checkAngle = calculateDiffAngle(trackCircle, circleTrackArr[trackCircle.getIndex() == 3 ? 2 : trackCircle.getIndex()]);
                shotCircle.setIndex(trackCircle.getIndex() + 1);
                shotCircle.setAngle(checkAngle);
            } else {
                shotCircle.setAngle(trackCircle.getAngle() + diffAngle);
                shotCircle.setIndex(trackCircle.getIndex());
            }
            
            // 给插入的预留空间,将前面的都向前移动(不包括被碰撞的圆)
            if (indexInCircleArr != 0) {
                for (int i = indexInCircleArr - 1; i >= 0; i--) {
                    if (arr[i].getAngle() + diffAngle >= Math.PI * 2) {
                        // 校验角度
                        double checkAngle = calculateDiffAngle(arr[i], circleTrackArr[arr[i].getIndex() == 3 ? 2 : arr[i].getIndex()]);
                        arr[i].setAngle(checkAngle);
                        arr[i].setIndex(arr[i].getIndex() + 1);
                    } else {
                        arr[i].setAngle(arr[i].getAngle() + diffAngleOnSameTrack[arr[i].getIndex() - 1]);
                    }
                }
            }
            // 前插入
            arr = Util.insertAtIndexBefore(arr, indexInCircleArr, shotCircle);
            return true;
        } else {
            // 默认打中的不是最后一个球
            shotCircle.setAngle(trackCircle.getAngle());
            shotCircle.setIndex(trackCircle.getIndex());
            // 如果打中的是最后一个球,则不会把前面的球向前移动(可能有bug,先不管
            if (indexInCircleArr == arr.length - 1) {
                shotCircle.setAngle(trackCircle.getAngle() - diffAngle);
                shotCircle.setIndex(trackCircle.getIndex());
            }
            
            // 给插入的预留空间,将前面的都向前移动(包括本身)
            if (indexInCircleArr != arr.length - 1) {
                for (int i = indexInCircleArr; i >= 0; i--) {
                    if (arr[i].getAngle() + diffAngle >= Math.PI * 2) {
                        // 校验角度
                        double checkAngle = calculateDiffAngle(arr[i], circleTrackArr[arr[i].getIndex() == 3 ? 2 : arr[i].getIndex()]);
                        arr[i].setAngle(checkAngle);
                        arr[i].setIndex(arr[i].getIndex() + 1);
                    } else {
                        arr[i].setAngle(arr[i].getAngle() + diffAngleOnSameTrack[arr[i].getIndex() - 1]);
                    }
                }
            }
            // 后插入
            arr = Util.insertAtIndexAfter(arr, indexInCircleArr, shotCircle);
            return false;
        }
    }
    
    // 如果满足连续三个颜色相同,则删除
    public void tryRemoveSameColorCircle(int index) {
        int left = 1;
        int right = 1;
        for (int i = 0; i < arr.length; i++) {
            if (index - left >= 0 && arr[index - left].getColor().equals(arr[index].getColor())) {
                left++;
            }
            if (index + right < arr.length && arr[index + right].getColor().equals(arr[index].getColor())) {
                right++;
            }
        }
        arr = right + left - 1 >= 3 ? Util.splice(arr, index - left + 1, right + left - 1) : arr;
    }
    
    // 判断是否会从直线跨界到圆弧轨道
    public boolean whetherFromLineToCircleTrack(Circle trackCircle) {
        return trackCircle.getY() + 80 > initLine.getY2();
    }
    
    // 设置从直线跨界到圆弧轨道时的小球角度
    public void setAngleWhenFromLineToCircleTrack(Circle trackCircle, Circle circle) {
        double diffS = trackCircle.getY() + 80 - initLine.getY2();
        double diffAngle = diffS / circleTrackArr[0].getRadius();
        circle.setAngle(Math.PI + diffAngle);
        circle.setIndex(1);
    }
    
    // 将圆弧轨道上的小球向前挪动
    public void moveCircleTrackForward(Circle trackCircle, double diffAngle) {
//         如果是最后一个球
//        if (indexInTrackArr == circleTrackArr.length - 1) {
//            trackCircle.setY(trackCircle.getY() - 80);
//            trackCircle.setIndex(trackCircle.getIndex() - 1);
//        } else {
//            // 计算角度差
//            double diffAngle = 2 * 40.0 / (circleTrackArr[indexInTrackArr].getRadius());
//            if (trackCircle.getAngle() - diffAngle <= 0) {
//                // 校验角度
//                double checkAngle = calculateDiffAngle(trackCircle, circleTrackArr[indexInTrackArr]);
//                trackCircle.setAngle(checkAngle);
//                trackCircle.setIndex(trackCircle.getIndex() - 1);
//            } else {
//                trackCircle.setAngle(trackCircle.getAngle() - diffAngleOnSameTrack[indexInTrackArr]);
//            }
//        }
        
        if (trackCircle.getAngle() + diffAngle >= Math.PI * 2) {
            // 校验角度
            double checkAngle = calculateDiffAngle(trackCircle, circleTrackArr[trackCircle.getIndex() == 3 ? 2 : trackCircle.getIndex()]);
            trackCircle.setAngle(checkAngle);
            trackCircle.setIndex(trackCircle.getIndex() + 1);
        } else {
            trackCircle.setAngle(trackCircle.getAngle() + diffAngle);
        }
    }
    
    // 在相同的圆弧轨道上,每两个圆之间有同的夹角
    public void computeDiffAngleOnSameTrack() {
        for (int i = 0; i < circleTrackArr.length; i++) {
            double R = circleTrackArr[i].getRadius();
            double diffAngle = 2 * Math.asin(40 / R);
            diffAngleOnSameTrack[i] = diffAngle;
        }
    }
    
    // 在圆弧轨道上,当球跨界时,计算圆与水平线的夹角
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
    
    // 是否应该停止回滚
    @PostMapping("/isStopRollback")
    public boolean isStopRollback(@RequestBody Rollback rollback) {
        Circle rollbackCircle = rollback.getRollbackCircle();
        Circle preCircle = rollback.getPreCircle();
        if (rollbackCircle == null || preCircle == null) {
            return false;
        }
        System.out.println(rollbackCircle);
        System.out.println(preCircle);
        // 如果此时两个球处于同一轨道
        if (rollbackCircle.getIndex() == preCircle.getIndex()) {
            if (rollbackCircle.getIndex() == 0) {
                return rollbackCircle.getY() - preCircle.getIndex() <= 80;
            } else {
                double standardValue = diffAngleOnSameTrack[rollbackCircle.getIndex() - 1];
                return rollbackCircle.getAngle() - preCircle.getAngle() <= standardValue;
            }
        } else {
            // 如果此时两个球处于不同的轨道
            // 从轨道退到直线
            if (preCircle.getIndex() == 0) {
                // rollbackCircle在圆弧轨道上的弧长
                double rollbackCircleS = (rollbackCircle.getAngle() - Math.PI) * diffAngleOnSameTrack[rollbackCircle.getIndex() - 1];
                return initLine.getY2() - preCircle.getY() <= 80 - rollbackCircleS;
            } else {
                // 从圆弧轨道退到圆弧轨道
                // rollbackCircle的轨道的圆心到preCircle的圆心的距离
                double distance = Math.sqrt(Math.pow(preCircle.getX() - circleTrackArr[rollbackCircle.getIndex() - 1].getX(), 2) +
                        Math.pow(preCircle.getY() - circleTrackArr[rollbackCircle.getIndex() - 1].getY(), 2));
                double standardValue = Util.calculateAngleByThreeSide(circleTrackArr[rollbackCircle.getIndex() - 1].getRadius(),
                        distance, 80);
                return (rollbackCircle.getAngle() - Math.PI) + (2 * Math.PI - preCircle.getAngle()) <= standardValue;
            }
        }
    }
    
    
    @GetMapping("/test")
    public String test() {
//        int count = 0;
//        for (int i = 0; i < 9999999; i++) {
//            System.out.println(count);
//        }
//        System.out.println('?');
        return "success";
    }
}