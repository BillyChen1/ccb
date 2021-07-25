package com.example.ccb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ccb.entity.StudentGraduate;

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
