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
    private Integer courseNum;

    @ApiModelProperty(value = "课程名")
    private String courseName;

    @ApiModelProperty(value = "成绩")
    private Integer score;


}
