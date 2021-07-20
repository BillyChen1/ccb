package com.example.ccb.mapper;

import com.example.ccb.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 存放用户名密码 Mapper 接口
 * </p>
 *
 * @author chen
 * @since 2021-07-19
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
