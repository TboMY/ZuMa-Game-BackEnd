package com.tbomy.canvas.controller;

import com.tbomy.canvas.param.request.Clear;
import com.tbomy.canvas.param.request.Hit;
import com.tbomy.canvas.param.request.Rollback;
import com.tbomy.canvas.param.response.Circle;
import com.tbomy.canvas.service.clear.ClearService;
import com.tbomy.canvas.service.hit.HitService;
import com.tbomy.canvas.service.init.InitService;
import com.tbomy.canvas.service.stop.StopRollbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class Controller {
    private final InitService initService;
    private final HitService hitService;
    private final ClearService clearService;
    private final StopRollbackService stopRollbackService;
    
    // 所有轨道初始数据
    @GetMapping("/initTracks")
    public Object[] initTracks() {
        return initService.initTracks();
    }
    
    // 轨道上的小球初始数据
    @GetMapping("/initCircleArr")
    public Circle[] initCircleArr() {
        return initService.initCircleArr(30);
    }
    
    // 生成Circle的实例
    @GetMapping("/newShotCircle")
    
    public Circle getNewShotCircle() {
        return new Circle(70, 0, initService.getColorList().get((int) (Math.random() * initService.getColorList().size())));
    }
    
    @PostMapping("/hit")
    public Circle[] hit(@RequestBody Hit hit) {
        return hitService.hit(hit);
    }
    
    // 是否应该停止回滚
    @PostMapping("/isStopRollback")
    public boolean isStopRollback(@RequestBody Rollback rollback) {
        return stopRollbackService.isStopRollback(rollback);
    }
    
    @PostMapping("/isReClear")
    public Circle[] reHit(@RequestBody Clear clear) {
        return clearService.clear(clear);
    }
    
    @Autowired
    public Controller(InitService initService, HitService hitService, ClearService clearService, StopRollbackService stopService) {
        this.initService = initService;
        this.hitService = hitService;
        this.clearService = clearService;
        this.stopRollbackService = stopService;
    }
}