package com.tbomy.canvas.service.init.impl;

import com.tbomy.canvas.mapper.UserMapper;
import com.tbomy.canvas.param.response.Circle;
import com.tbomy.canvas.param.response.CircleTrack;
import com.tbomy.canvas.param.response.InitLine;
import com.tbomy.canvas.pojo.User;
import com.tbomy.canvas.service.init.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
    private ArrayList<String> colorList;
    // 每通关一层,颜色加一种
    private final String[] colorArr;
    private final UserMapper userMapper;
    
    @Autowired
    public InitServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
        initLine = new InitLine(400, 0, 400, 400);
        circleTrackArr = new CircleTrack[]{
                new CircleTrack(900, 400, 500, true),
                new CircleTrack(1000, 400, 400, false),
                new CircleTrack(900, 400, 300, true)
        };
        diffAngleOnSameTrack = new Double[circleTrackArr.length];
        colorArr = new String[]{"#F100D2", "#1000F1", "#090808", "#E39050"};
        computeDiffAngleOnSameTrack();
    }
    
    @Override
    public Object[] initTracks() {
        return new Object[]{initLine, circleTrackArr};
    }
    
    @Override
    public Object[] initCircleArr(String name) {
        Integer levelId = userMapper.selectUserByName(name).getLevelId();
        // 颜色
        colorList = new ArrayList<>(List.of("#00c0ff", "#fff900", "#00ff00", "#ff0000"));
        colorList.addAll(Arrays.asList(colorArr).subList(0, levelId - 1));
        // 球数组
        Circle[] arr = new Circle[20 + levelId * 10];
        for (int i = 0; i < arr.length; i++) {
            Circle circle = new Circle(400, i * -80, colorList.get((int) (Math.random() * colorList.size())));
            arr[i] = circle;
        }
        // 加球速
        double dt = 0.04 + (levelId - 1) * 0.003;
        return new Object[]{arr, dt};
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

