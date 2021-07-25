package com.example.ccb.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.ccb.common.BaseResult;
import com.example.ccb.entity.StudentGrade;
import com.example.ccb.service.IStudentGradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author chen
 * @since 2021-06-01
 */
@CrossOrigin
@RestController
@RequestMapping("/studentGrade")
@Api(tags = "成绩")
@Slf4j
public class StudentGradeController {
    @Autowired
    private IStudentGradeService studentGradeService;

    @PostMapping("")
    @ApiOperation("成绩录入, 表单里的id不用填")
    public BaseResult addGrade(@RequestBody StudentGrade studentGrade) {
        studentGradeService.save(studentGrade);
        log.info("成绩录入成功");
        return BaseResult.success();
    }

    @GetMapping("")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "term", value = "学年"),
            @ApiImplicitParam(name = "courseNum", value = "课程号"),
            @ApiImplicitParam(name = "studentNum", value = "学号"),
            @ApiImplicitParam(name = "education", value = "学历层次")
    })
    @ApiOperation("根据一定条件成绩信息列表")
    public BaseResult listStudentGrade(@RequestParam(value = "term", required = false) Integer term,
                                       @RequestParam(value = "courseNum", required = false) Integer courseNum,
                                       @RequestParam(value = "studentNum", required = false) String studentNum,
                                       @RequestParam(value = "education", required = false) String education) {

        QueryWrapper<StudentGrade> queryWrapper = new QueryWrapper<>();
        if (term != null) {
            queryWrapper.eq("term", term);
        }
        if (courseNum != null) {
            queryWrapper.eq("course_num", courseNum);
        }
        if (studentNum != null) {
            queryWrapper.eq("student_num", studentNum);
        }
        if (education != null) {
            queryWrapper.eq("education", education);
        }
        List<StudentGrade> list = studentGradeService.list(queryWrapper);
        return BaseResult.successWithData(list);
    }
}

