package com.tbomy.canvas.service.ranking;

import com.tbomy.canvas.pojo.User;

import java.util.List;

public interface RankingService {
    List<User> getRanking(Integer page);
    
    Integer getTotalCount();
}
