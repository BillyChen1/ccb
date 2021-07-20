package com.example.ccb.service;

import com.example.ccb.entity.StudentGrade;
import com.example.ccb.entity.StudentGraduate;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chen
 * @since 2021-06-01
 */
public interface IStudentGraduateService extends IService<StudentGraduate> {

    StudentGraduate getValidGraduateInfo(String certificateNum, String university, String identityNum);
}
