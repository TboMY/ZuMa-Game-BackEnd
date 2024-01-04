package com.tbomy.canvas.service.ranking.impl;

import com.tbomy.canvas.mapper.UserMapper;
import com.tbomy.canvas.pojo.User;
import com.tbomy.canvas.service.ranking.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*@author cj
 *@date 2024/1/2 1:06:35
 */
@Service
public class RankingServiceImpl implements RankingService {
    private final UserMapper userMapper;
    
    @Override
    public List<User> getRanking(Integer page) {
        return userMapper.getListByPage((page - 1) * 10);
    }
    
    @Override
    public Integer getTotalCount() {
        return userMapper.getTotalCount();
    }
    
    @Autowired
    public RankingServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
}
