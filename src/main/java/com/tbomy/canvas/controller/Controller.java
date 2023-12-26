package com.tbomy.canvas.controller;

import com.tbomy.canvas.param.request.Hit;
import com.tbomy.canvas.param.response.Circle;
import com.tbomy.canvas.param.response.CircleTrack;
import com.tbomy.canvas.param.response.InitLine;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
        System.out.println(Arrays.toString(diffAngleOnSameTrack));
//        computeDiffAngleOnDifferentTrack();
//        System.out.println(Arrays.toString(diffAngleOnDifferentTrack));
        return new Object[]{initLine, circleTrackArr};
    }
    
    // 轨道上的小球初始数据
    @GetMapping("/initCircles")
    public Circle[] initCircles() {
        // 生成轨道上的圆
        arr = new Circle[10];
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
        // 前端动画改变了数组内元素
        arr = hit.getCircleArr();
        
        // 如果射击的球和轨道上的球颜色不同,直接插入
        if (!trackCircle.getColor().equals(shotCircle.getColor())) {
//            System.out.println("不同颜色");
            insert(trackCircle, shotCircle, trackCircle.getIndex(), index);
            return arr;
        }
        
        // 如果射击的球和轨道上的球颜色相同,判断是消除还是插入
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
        // 消除
        if (left + right - 1 >= 2) {
            arr = splice(arr, index - left + 1, left + right - 1);
        } else {
            // 插入
            insert(trackCircle, shotCircle, trackCircle.getIndex(), index);
        }
        return arr;
    }
    
    /**
     * @param trackCircle      被碰撞的圆
     * @param shotCircle       射击的圆
     * @param indexInTrackArr  被碰撞的圆在第几个轨道上
     * @param indexInCircleArr 被碰撞的圆在所有球数组中的索引
     */
    public void insert(Circle trackCircle, Circle shotCircle, int indexInTrackArr, int indexInCircleArr) {
        double circleCenterX = circleTrackArr[indexInTrackArr - 1].getX();
        double circleCenterY = circleTrackArr[indexInTrackArr - 1].getY();
        // 计算被碰撞的球和其下一个球之间夹角
        double diffAngle = diffAngleOnSameTrack[indexInTrackArr - 1];
        double alreadyAngle = trackCircle.getAngle() - Math.PI;
        
        // 根据三角函数算出x,y, 默认假设下一次不会进入下一个轨道
        double cosAngle = alreadyAngle + diffAngle;
        
        // 如果会进入下一个轨道
        if (trackCircle.getAngle() + diffAngle >= Math.PI * 2) {
            System.out.println("会进入下一个轨道");
            double checkAngle = calculateDiffAngle(trackCircle, circleTrackArr[trackCircle.getIndex() == 3 ? 2 : trackCircle.getIndex()]);
            shotCircle.setIndex(trackCircle.getIndex() + 1);
            shotCircle.setAngle(diffAngleOnSameTrack[trackCircle.getIndex() == 3 ? 2 : trackCircle.getIndex()] - checkAngle + Math.PI);
            // 用锐角,三角函数算坐标
            cosAngle = shotCircle.getAngle() - Math.PI;
        } else {
            // 不会进入下一个轨道
            shotCircle.setAngle(trackCircle.getAngle() + diffAngle);
            shotCircle.setIndex(trackCircle.getIndex());
            // 如果加起来是钝角
            if (cosAngle > Math.PI / 2.0) {
                cosAngle = Math.PI - cosAngle + Math.PI;
            }
        }
        
        // 用三角函数算出x,y
        double diffX = circleCenterX * Math.cos(cosAngle);
        shotCircle.setX(circleCenterX - diffX);
        double diffY = circleCenterY * Math.sin(cosAngle);
        shotCircle.setY(circleCenterY + diffY);
        
        // 给插入的预留空间,将前面的都向前移动
        if (indexInCircleArr != 0) {
            for (int i = indexInCircleArr - 1; i >= 0; i--) {
                if (arr[i].getAngle() + diffAngle >= Math.PI * 2) {
                    // 校验角度
                    double cos = calculateDiffAngle(arr[i], circleTrackArr[arr[i].getIndex() == 3 ? 2 : arr[i].getIndex()]);
                    System.out.println(cos);
                    arr[i].setAngle(diffAngleOnSameTrack[arr[i].getIndex() == 3 ? 2 : arr[i].getIndex()] - cos + Math.PI);
                    arr[i].setIndex(arr[i].getIndex() + 1);
                } else {
                    arr[i].setAngle(arr[i].getAngle() + diffAngleOnSameTrack[arr[i].getIndex() - 1]);
                }
            }
        }
        
        // 插入
        arr = insertAtIndexBefore(arr, indexInCircleArr, shotCircle);
    }
    
    public Circle[] splice(Circle[] arr, int index, int deleteCount) {
        if (index < 0 || index > arr.length - 1 || deleteCount < 0) {
            return null;
        }
        if (deleteCount >= arr.length - index) {
            return Arrays.copyOfRange(arr, 0, index);
        }
        Circle[] newArr = new Circle[arr.length - deleteCount];
        System.arraycopy(arr, 0, newArr, 0, index);
        System.arraycopy(arr, index + deleteCount, newArr, index, arr.length - index - deleteCount);
        return newArr;
    }
    
    // 在索引处插入元素
    public Circle[] insertAtIndexBefore(Circle[] arr, int index, Circle circle) {
        if (index < 0 || index > arr.length - 1) {
            return null;
        }
        Circle[] newArr = new Circle[arr.length + 1];
        System.arraycopy(arr, 0, newArr, 0, index);
        newArr[index] = circle;
        System.arraycopy(arr, index, newArr, index + 1, arr.length - index);
        return newArr;
    }
    
    // 在索引后一位插入元素
    public Circle[] insertAtIndexAfter(Circle[] arr, int index, Circle circle) {
        if (index < 0 || index > arr.length - 1) {
            return null;
        }
        Circle[] newArr = new Circle[arr.length + 1];
        System.arraycopy(arr, 0, newArr, 0, index + 1);
        newArr[index + 1] = circle;
        if (index + 2 < newArr.length) {
            System.arraycopy(arr, index + 1, newArr, index + 2, arr.length - index - 1);
        }
        return newArr;
    }
    
    // 在相同的轨道上,每两个圆之间有同的夹角
    public void computeDiffAngleOnSameTrack() {
        for (int i = 0; i < circleTrackArr.length; i++) {
            double R = circleTrackArr[i].getRadius();
            double diffAngle = 2 * Math.asin(40 / R);
            diffAngleOnSameTrack[i] = diffAngle;
        }
    }
    
    // 通过三角形三边,求c边对应角的角度
    public double calculateAngleByThreeSide(double a, double b, double c) {
        double cos = (a * a + b * b - c * c) / (2 * a * b);
        return Math.acos(cos);
    }
    
    // 在球跨界时,计算圆与水平线的夹角
    public double calculateDiffAngle(Circle circle, CircleTrack track) {
        double a = track.getRadius();
        double b = Math.sqrt(Math.pow(circle.getX() - track.getX(), 2) + Math.pow(circle.getY() - track.getY(), 2));
        double c = Math.sqrt(Math.pow(circle.getX() - (track.getX() + track.getRadius()), 2) + Math.pow(circle.getY() - track.getY(), 2));
        return calculateAngleByThreeSide(a, b, c);
    }
}