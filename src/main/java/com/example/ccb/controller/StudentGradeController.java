package com.example.ccb.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.ccb.common.BaseResult;
import com.example.ccb.entity.StudentGrade;
import com.example.ccb.entity.StudentGraduate;
import com.example.ccb.entity.StudentIn;
import com.example.ccb.service.IStudentGradeService;
import com.example.ccb.service.IStudentInService;
import io.swagger.annotations.Api;
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
    @ApiOperation("根据学年和课程号查询成绩信息列表,如果两个参数为空，则返回全部信息")
    public BaseResult listStudentGrade(@RequestParam(value = "term", required = false) Integer term,
                                    @RequestParam(value = "courseNum", required = false) Integer courseNum) {
        if (term == null && courseNum == null) {
            return BaseResult.successWithData(studentGradeService.list());
        } else if (courseNum == null) {
            List<StudentGrade> list = studentGradeService.list(
                    new QueryWrapper<StudentGrade>().eq("term", term)
            );
            return BaseResult.successWithData(list);
        }
        QueryWrapper<StudentGrade> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("term", term)
                .eq("course_num", courseNum);
        List<StudentGrade> list = studentGradeService.list(queryWrapper);
        return BaseResult.successWithData(list);
    }
}

