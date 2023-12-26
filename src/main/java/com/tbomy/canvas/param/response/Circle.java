package com.tbomy.canvas.param.response;

/*@author cj
 *@date 2023/12/24 13:06:10
 */
public class Circle {
    private double x;
    private double y;
    private String color;
    
    private int index;
    private double angle;
    
    public Circle(Integer x, Integer y, String color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.index = 0;
        this.angle = Math.PI;
    }
    
    public double getX() {
        return x;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
    public double getAngle() {
        return angle;
    }
    
    public void setAngle(double angle) {
        this.angle = angle;
    }
    
    @Override
    public String toString() {
        return "Circle{" +
                "x=" + x +
                ", y=" + y +
                ", color='" + color + '\'' +
                ", index=" + index +
                ", angle=" + angle +
                '}';
    }
}
