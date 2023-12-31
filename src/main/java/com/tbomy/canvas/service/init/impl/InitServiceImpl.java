package com.tbomy.canvas.service.init.impl;

import com.tbomy.canvas.param.response.Circle;
import com.tbomy.canvas.param.response.CircleTrack;
import com.tbomy.canvas.param.response.InitLine;
import com.tbomy.canvas.service.init.InitService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/*@author cj
 *@date 2023/12/30 23:43:42
 */
@Service
public class InitServiceImpl implements InitService {
    // 直线轨道
    private final InitLine initLine;
    // 半圆轨道
    private final CircleTrack[] circleTrackArr;
    // 在不同的半圆轨道上,每两个圆之间有同的夹角
    private final Double[] diffAngleOnSameTrack;
    // 所有球的颜色
    private final ArrayList<String> colorList;
    
    // 无参构造
    public InitServiceImpl() {
        initLine = new InitLine(400, 0, 400, 400);
        circleTrackArr = new CircleTrack[]{
                new CircleTrack(900, 400, 500, true),
                new CircleTrack(1000, 400, 400, false),
                new CircleTrack(900, 400, 300, true)
        };
        diffAngleOnSameTrack = new Double[circleTrackArr.length];
        colorList = new ArrayList<>(List.of("#00c0ff", "#fff900", "#00ff00", "#ff0000"));
        computeDiffAngleOnSameTrack();
    }
    
    @Override
    public Object[] initTracks() {
        return new Object[]{initLine, circleTrackArr};
    }
    
    @Override
    public Circle[] initCircleArr(int initArrLength) {
        // 生成轨道上的圆
        Circle[] arr = new Circle[initArrLength];
        for (int i = 0; i < arr.length; i++) {
            Circle circle = new Circle(400, i * -80, colorList.get((int) (Math.random() * colorList.size())));
            arr[i] = circle;
        }
        return arr;
    }
    
    // 在相同的圆弧轨道上,每两个圆之间有同的夹角
    public void computeDiffAngleOnSameTrack() {
        for (int i = 0; i < circleTrackArr.length; i++) {
            double R = circleTrackArr[i].getRadius();
            double diffAngle = 2 * Math.asin(40 / R);
            diffAngleOnSameTrack[i] = diffAngle;
        }
    }
    
    public InitLine getInitLine() {
        return initLine;
    }
    
    public CircleTrack[] getCircleTrackArr() {
        return circleTrackArr;
    }
    
    public Double[] getDiffAngleOnSameTrack() {
        return diffAngleOnSameTrack;
    }
    
    public ArrayList<String> getColorList() {
        return colorList;
    }
    
}

