package com.tbomy.canvas.mapper;

import com.tbomy.canvas.pojo.Plugin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PluginMapper {
    Plugin selectPluginCountByName(String name);
    
    void updatePluginCountByName(@Param("name") String name,@Param("moneyPlugin") Integer moneyPlugin,@Param("commonPlugin") Integer commonPlugin);
    
    void insertPluginCountByName(@Param("name") String name);
}
