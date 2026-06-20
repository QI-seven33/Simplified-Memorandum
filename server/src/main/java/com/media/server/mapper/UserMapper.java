package com.media.server.mapper;

import com.media.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {


    @Select("select * from user where username = #{username}")
    User getByUsername(String username);


    void register(User user);

    User getInformation(Long id);

    int updateUserInfo(User user);

}