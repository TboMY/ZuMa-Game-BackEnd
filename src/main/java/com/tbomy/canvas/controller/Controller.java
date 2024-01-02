package com.tbomy.canvas.controller;

import com.tbomy.canvas.param.request.Clear;
import com.tbomy.canvas.param.request.Hit;
import com.tbomy.canvas.param.request.Rollback;
import com.tbomy.canvas.param.request.UpdatePluginCount;
import com.tbomy.canvas.param.response.Circle;
import com.tbomy.canvas.pojo.Plugin;
import com.tbomy.canvas.pojo.User;
import com.tbomy.canvas.service.achievement.AchievementService;
import com.tbomy.canvas.service.clear.ClearService;
import com.tbomy.canvas.service.hit.HitService;
import com.tbomy.canvas.service.init.InitService;
import com.tbomy.canvas.service.next.NextLevelService;
import com.tbomy.canvas.service.plugin.PluginService;
import com.tbomy.canvas.service.ranking.RankingService;
import com.tbomy.canvas.service.stop.StopRollbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin
public class Controller {
    private final InitService initService;
    private final HitService hitService;
    private final ClearService clearService;
    private final StopRollbackService stopRollbackService;
    private final NextLevelService nextLevelService;
    private final AchievementService achievementService;
    private final RankingService rankingService;
    private final PluginService pluginService;
    
    // 所有轨道初始数据
    @GetMapping("/initTracks")
    public Object[] initTracks() {
        return initService.initTracks();
    }
    
    // 轨道上的小球初始数据
    @GetMapping("/initCircleArr")
    public Object[] initCircleArr(@RequestParam String name) {
        return initService.initCircleArr(name);
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
    
    // 回滚结束时,是否应该再消除
    @PostMapping("/isReClear")
    public Circle[] reHit(@RequestBody Clear clear) {
        return clearService.clear(clear);
    }
    
    @GetMapping("/nextLevel")
    public void nextLevel(@RequestParam String name) {
       nextLevelService.nextLevel(name);
    }
    
    @GetMapping("/getLevel")
    public Integer getLevel(@RequestParam String name) {
        return achievementService.getLevelId(name);
    }
    
    @GetMapping("/getRanking")
    public List<User> getRanking(@RequestParam Integer page) {
        return rankingService.getRanking(page);
    }
    
    @GetMapping("/getTotalPage")
    public Integer getTotalPage() {
        return rankingService.getTotalPage();
    }
    
    @GetMapping("/getPluginCount")
    public Plugin getPluginCount(@RequestParam String name) {
        System.out.println(name);
        return pluginService.getPluginCount(name);
    }
    
    @PostMapping("/updatePluginCount")
    public void updatePluginCount(@RequestBody UpdatePluginCount updatePluginCount) {
        pluginService.updatePluginCount(updatePluginCount.getName(), updatePluginCount.getMoneyPlugin(), updatePluginCount.getCommonPlugin());
    }
    
    @Autowired
    public Controller(InitService initService, HitService hitService, ClearService clearService, StopRollbackService stopService, NextLevelService nextLevelService, AchievementService achievementService, RankingService rankingService, PluginService pluginService) {
        this.initService = initService;
        this.hitService = hitService;
        this.clearService = clearService;
        this.stopRollbackService = stopService;
        this.nextLevelService = nextLevelService;
        this.achievementService = achievementService;
        this.rankingService = rankingService;
        this.pluginService = pluginService;
    }
}