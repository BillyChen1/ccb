package com.example.ccb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javafx.scene.chart.ValueAxis;
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
@ApiModel(value="学生成绩信息", description="")
public class StudentGrade implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "姓名")
    private String studentName;

    @ApiModelProperty(value = "学号")
    private String studentNum;

    @ApiModelProperty(value = "学年")
    private Integer term;

    @ApiModelProperty(value = "任课老师")
    private String teacher;

    @ApiModelProperty(value = "课程号")
    private String courseNum;

    @ApiModelProperty(value = "课程名")
    private String courseName;

    @ApiModelProperty(value = "成绩")
    private Integer score;

    @ApiModelProperty(value = "学历层次")
    private String education;

    @ApiModelProperty(value = "身份证号")
    private String identityNum;

    @ApiModelProperty(value = "学校")
    private String university;

    @ApiModelProperty(value = "学分")
    private Double credit;

    @ApiModelProperty(value = "课程类型必修选修")
    private String courseType;

}
