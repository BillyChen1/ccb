package com.example.ccb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 存放用户名密码
 * </p>
 *
 * @author chen
 * @since 2021-07-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="User对象", description="存放用户名密码")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户名，学生用户为身份证号")
    private String accountName;

    @ApiModelProperty(value = "密码")
    private String accountPassword;

    @ApiModelProperty(value = "0管理员1企业2高校3学生")
    private Integer role;

    @ApiModelProperty(value = "身份证号码")
    private String identityNum;


}
