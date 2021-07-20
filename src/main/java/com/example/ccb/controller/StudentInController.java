package com.example.ccb.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.ccb.common.BaseResult;
import com.example.ccb.entity.StudentGrade;
import com.example.ccb.entity.StudentIn;
import com.example.ccb.exception.ErrorCode;
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
@RequestMapping("/studentIn")
@Api(tags = "入学")
@Slf4j
public class StudentInController {

    @Autowired
    private IStudentInService studentInService;

    @PostMapping("")
    @ApiOperation("入学录入, 表单里的id不用填")
    public BaseResult addNewStudent(@RequestBody StudentIn studentIn) {
        studentInService.save(studentIn);
        log.info("入学录入成功");
        return BaseResult.success();
    }

    @GetMapping("")
    @ApiOperation("根据年级和学号查询入学信息列表,如果两个参数为空，则返回全部信息")
    public BaseResult listStudentIn(@RequestParam(value = "grade", required = false) Integer grade,
                                    @RequestParam(value = "studentNum", required = false) String studentNum) {
        if (grade == null && studentNum == null) {
            return BaseResult.successWithData(studentInService.list());
        } else if (studentNum == null) {
            List<StudentIn> list = studentInService.list(
                    new QueryWrapper<StudentIn>().eq("grade", grade)
            );
            return BaseResult.successWithData(list);
        }
        QueryWrapper<StudentIn> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("grade", grade)
                .eq("student_num", studentNum);
        List<StudentIn> list = studentInService.list(queryWrapper);
        return BaseResult.successWithData(list);
    }

    @DeleteMapping("{id}")
    @ApiOperation("根据id删除入学记录")
    public BaseResult removeStudentIn(@PathVariable Integer id) {
        if (studentInService.removeById(id)) {
            return BaseResult.success();
        } else {
            return BaseResult.failWithErrorCode(ErrorCode.DELETE_FAILED);
        }
    }


}

