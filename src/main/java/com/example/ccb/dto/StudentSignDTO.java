package com.example.ccb.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "学生进行签名时需要传入的参数")
public class StudentSignDTO {
    @ApiModelProperty(value = "学生的身份证号")
    private String identityNum;

    @ApiModelProperty(value = "学生要进行签名的毕业信息id")
    private Integer gradateInfoId;
}
