package com.tbomy.canvas.util;

import com.tbomy.canvas.param.response.Circle;
import com.tbomy.canvas.param.response.CircleTrack;

import java.util.Arrays;

/*@author cj
 *@date 2023/12/27 17:54:26
 */
public class Util {
    
    // 通过三角形三边,求第三边对应角的弧度
    public static double calculateAngleByThreeSide(double a, double b, double resultAngleMappingSide) {
        double cos = (a * a + b * b - resultAngleMappingSide * resultAngleMappingSide) / (2 * a * b);
        return Math.acos(cos);
    }
    
    /**
     * 从数组指定索引中删除指定个数元素, js中splice的部分功能
     * @param arr         源数组
     * @param index       开始删除的索引
     * @param deleteCount 删除的个数
     * @return 新数组
     */
    public static Circle[] splice(Circle[] arr, int index, int deleteCount) {
        if (arr == null || index < 0 || index > arr.length - 1 || deleteCount < 0) {
            throw new RuntimeException("参数错误");
        }
        if (deleteCount >= arr.length - index) {
            return Arrays.copyOfRange(arr, 0, index);
        }
        Circle[] newArr = new Circle[arr.length - deleteCount];
        System.arraycopy(arr, 0, newArr, 0, index);
        System.arraycopy(arr, index + deleteCount, newArr, index, arr.length - index - deleteCount);
        return newArr;
    }
    
    /**
     * 在轨道上,判断是往前插还是后插;
     * @param circleTrackArr 轨道数组
     * @param trackCircle   轨道上的球
     * @param shotCircle   射击球
     * @return true:前插, false:后插
     */
    public static boolean isInsertBefore(CircleTrack[] circleTrackArr, Circle trackCircle, Circle shotCircle) {
        // 数学结算,计算从轨道端点开始,到射击圆心的夹角
        double y = shotCircle.getY() - circleTrackArr[trackCircle.getIndex() - 1].getY();
        double absX = Math.abs(shotCircle.getX() - circleTrackArr[trackCircle.getIndex() - 1].getX());
        if (trackCircle.getIndex() % 2 == 0) {
            y *= -1;
        }
        double atan = Math.atan(y / absX);
        // 在上半圆,y轴向下,都小于圆心y坐标
        if (trackCircle.getAngle() - Math.PI > Math.PI / 2) {
            atan = Math.PI - atan;
        }
        return atan > trackCircle.getAngle() - Math.PI;
    }
    
    // 在索引处插入元素
    public static Circle[] insertAtIndexBefore(Circle[] arr, int index, Circle circle) {
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
    public static Circle[] insertAtIndexAfter(Circle[] arr, int index, Circle circle) {
        if (index < 0 || index > arr.length - 1) {
            return null;
        }
        Circle[] newArr = new Circle[arr.length + 1];
        System.arraycopy(arr, 0, newArr, 0, index + 1);
        newArr[index + 1] = circle;
        if (index < arr.length - 1) {
            System.arraycopy(arr, index + 1, newArr, index + 2, arr.length - index - 1);
        }
        return newArr;
    }
    
    // 如果满足连续三个颜色相同,则删除
    public static Circle[] tryRemoveSameColorCircle(int index, Circle[] arr) {
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
        return right + left - 1 >= 3 ? Util.splice(arr, index - left + 1, right + left - 1) : arr;
    }
}
