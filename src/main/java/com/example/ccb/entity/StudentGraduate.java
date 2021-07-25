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
 * 
 * </p>
 *
 * @author chen
 * @since 2021-06-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="学生毕业信息", description="")
public class StudentGraduate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "姓名")
    private String studentName;

    @ApiModelProperty(value = "学号")
    private String studentNum;

    @ApiModelProperty(value = "性别")
    private String gender;

    @ApiModelProperty(value = "身份证号")
    private String identityNum;

    @ApiModelProperty(value = "入学时间")
    private String admission;

    @ApiModelProperty(value = "毕业时间")
    private String graduate;

    @ApiModelProperty(value = "政治面貌")
    private String politicalStatus;

    @ApiModelProperty(value = "学校")
    private String university;

    @ApiModelProperty(value = "学院")
    private String school;

    @ApiModelProperty(value = "专业")
    private String major;

    @ApiModelProperty(value = "学习形式")
    private String studyForm;

    @ApiModelProperty(value = "证书编号")
    private String certificateNum;

    @ApiModelProperty(value = "学历层次")
    private String education;

    @ApiModelProperty(value = "0未签名1学校已签名2学生已签名3待定")
    private Integer signStatus;

    @ApiModelProperty(value = "民族")
    private String folk;

    @ApiModelProperty(value = "学制")
    private String schoolSystem;

    @ApiModelProperty(value = "班级")
    private String studentClass;
}
