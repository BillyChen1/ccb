package com.example.ccb.service.impl;

import com.example.ccb.entity.User;
import com.example.ccb.mapper.UserMapper;
import com.example.ccb.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 存放用户名密码 服务实现类
 * </p>
 *
 * @author chen
 * @since 2021-07-19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
