package com.tbomy.canvas.param.response;

/*@author cj
 *@date 2023/12/25 17:01:41
 */
public class CircleTrack {
    private Integer x;
    private Integer y;
    private Integer radius;
    private boolean loop;
    
    public CircleTrack(Integer x, Integer y, Integer radius, boolean loop) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.loop = loop;
    }
    
    public Integer getX() {
        return x;
    }
    
    public void setX(Integer x) {
        this.x = x;
    }
    
    public Integer getY() {
        return y;
    }
    
    public void setY(Integer y) {
        this.y = y;
    }
    
    public Integer getRadius() {
        return radius;
    }
    
    public void setRadius(Integer radius) {
        this.radius = radius;
    }
    
    public boolean isLoop() {
        return loop;
    }
    
    public void setLoop(boolean loop) {
        this.loop = loop;
    }
    
    @Override
    public String toString() {
        return "CircleTrack{" +
                "x=" + x +
                ", y=" + y +
                ", radius=" + radius +
                ", loop=" + loop +
                '}';
    }
}
