package com.tbomy.canvas.mapper;

import com.tbomy.canvas.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    User selectUserByName(String name);
 
    void insertUser(@Param("user") User user);
    
    void updateLevelIdByName(@Param("name") String name, @Param("levelId") Integer levelId);
    
    List<User> getListByPage(Integer startLine);
    
    Integer getTotalCount();
}
