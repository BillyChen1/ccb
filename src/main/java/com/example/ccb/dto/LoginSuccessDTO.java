package com.example.ccb.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "登陆成功返回的信息")
public class LoginSuccessDTO {
    @ApiModelProperty(value = "用户名，学生用户为身份证号")
    private String accountName;

    @ApiModelProperty(value = "0管理员1企业2高校3学生")
    private Integer role;
}
