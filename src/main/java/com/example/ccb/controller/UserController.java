package com.example.ccb.controller;


import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.ccb.common.BaseResult;
import com.example.ccb.dto.LoginSuccessDTO;
import com.example.ccb.dto.UserLoginDTO;
import com.example.ccb.entity.User;
import com.example.ccb.exception.ErrorCode;
import com.example.ccb.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 存放用户名密码 前端控制器
 * </p>
 *
 * @author chen
 * @since 2021-07-19
 */
@CrossOrigin
@RestController
@RequestMapping("/user")
@Api(tags = "用户注册登录")
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/register")
    @ApiOperation("注册 id不用传")
    public BaseResult register(@RequestBody User user) {
        //判断是否已经注册
        User dbUser = userService.getOne(
                new QueryWrapper<User>().eq("account_name", user.getAccountName())
                .or().eq("identity_num", user.getIdentityNum()));
        if (dbUser != null) {
            //return BaseResult.failWithCodeAndMsg(1, "用户已注册");
            return BaseResult.failWithErrorCode(ErrorCode.REGISTER_FAILED);
        }

        //对密码进行md5摘要后存入数据库
        String digestPass = SecureUtil.md5(user.getAccountPassword());
        user.setAccountPassword(digestPass);
        if (userService.save(user)) {
            return BaseResult.success();
        } else {
//            return BaseResult.failWithCodeAndMsg(1, "注册失败");
            return BaseResult.failWithErrorCode(ErrorCode.REGISTER_FAILED);
        }
    }

    @PostMapping("/login")
    @ApiOperation("登录 id不用传")
    public BaseResult login(@RequestBody UserLoginDTO user) {
        User dbUser = userService.getOne(
                new QueryWrapper<User>().eq("account_name", user.getAccountName()));
        if (dbUser == null) {
            return BaseResult.failWithErrorCode(ErrorCode.LOGIN_FAILED);
        }
        String password = user.getAccountPassword();
        if (dbUser.getAccountPassword().equals(SecureUtil.md5(password))) {
            //登陆成功返回用户的信息
            LoginSuccessDTO loginSuccessDTO = new LoginSuccessDTO();
            loginSuccessDTO.setAccountName(dbUser.getAccountName());
            loginSuccessDTO.setRole(dbUser.getRole());
            loginSuccessDTO.setIdentityNum(dbUser.getIdentityNum());
            return BaseResult.successWithData(loginSuccessDTO);
        }
//        return BaseResult.failWithCodeAndMsg(1, "登陆失败");
        return BaseResult.failWithErrorCode(ErrorCode.LOGIN_FAILED);
    }
}

