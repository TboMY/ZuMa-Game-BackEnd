package com.tbomy.canvas.service.achievement.impl;

import com.tbomy.canvas.mapper.UserMapper;
import com.tbomy.canvas.service.achievement.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*@author cj
 *@date 2024/1/1 22:44:07
 */
@Service
public class AchievementServiceImpl implements AchievementService {
    private final UserMapper userMapper;
    
    @Autowired
    public AchievementServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    
    @Override
    public Integer getLevelId(String name) {
        return userMapper.selectUserByName(name).getLevelId();
    }
}
