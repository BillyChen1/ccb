package com.example.ccb.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "登陆时要上传的信息")
public class UserLoginDTO {
    @ApiModelProperty(value = "用户名")
    private String accountName;

    @ApiModelProperty(value = "密码")
    private String accountPassword;
}
