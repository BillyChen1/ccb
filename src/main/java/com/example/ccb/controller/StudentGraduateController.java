package com.example.ccb.controller;


import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.ccb.common.BaseResult;
import com.example.ccb.common.NoobChain;
import com.example.ccb.common.SignStatus;
import com.example.ccb.dto.StudentSignDTO;
import com.example.ccb.entity.StudentGrade;
import com.example.ccb.entity.StudentGraduate;
import com.example.ccb.exception.ErrorCode;
import com.example.ccb.service.IStudentGraduateService;
import com.example.ccb.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.security.KeyPair;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

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
@RequestMapping("/studentGraduate")
@Api(tags = "毕业")
@Slf4j
public class StudentGraduateController {

    @Autowired
    private IStudentGraduateService studentGraduateService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private NoobChain noobChain;

    @Value("${redis.publicKeyName}")
    private String pubKeyName;

    @Value("${redis.privateKeyName}")
    private String priKeyName;

    @PostMapping("")
    @ApiOperation("毕业录入, 表单里的id不用填, 后台学校会自动签名")
    public BaseResult addStudentGraduate(@RequestBody StudentGraduate studentGraduate) {
        studentGraduate.setCertificateNum(UUID.randomUUID().toString());
        //studentGraduate.setEducation("本科");
        studentGraduate.setSignStatus(SignStatus.SCHOOL_SIGN);
        studentGraduateService.save(studentGraduate);

        //学校对毕业录入信息进行签名
        //学校公钥和私钥存在redis中
        //公钥的Key为schoolPub 私钥key为schoolPri
        if (redisUtil.get(priKeyName) == null || redisUtil.get(pubKeyName) == null) {
            //第一次如果为空，说明尚未生成公钥私钥，需要生成
            KeyPair pair = SecureUtil.generateKeyPair("RSA");
            String priKey = Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded());
            String pubKey = Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());
            //将密钥进行Base64编码后存入redis
            redisUtil.set(pubKeyName, pubKey, -1);
            redisUtil.set(priKeyName, priKey, -1);
        }
        //对毕业信息用私钥进行签名
        byte[] priKey = Base64.getDecoder().decode((String)redisUtil.get(priKeyName));
        RSA rsa = new RSA(priKey, null);
        //毕业信息转化成json串，作为数据
        String graduateJSONString = JSON.toJSONString(studentGraduate);
        byte[] encrypt = rsa.encrypt(graduateJSONString, KeyType.PrivateKey);
        //密文先暂时存入redis, key为毕业信息编号，将来学生签名的时候从里面取
        redisUtil.set(studentGraduate.getId()+"", Base64.getEncoder().encodeToString(encrypt), -1);
//        noobChain.add(Base64.getEncoder().encodeToString(encrypt), studentGraduate.getCertificateNum());

        log.info("毕业录入成功");
        return BaseResult.success();
    }

    @GetMapping("")
    @ApiOperation("根据毕业年份和证书号查询毕业信息列表,如果两个参数为空，则返回全部信息")
    public BaseResult listStudentGraduate(@RequestParam(value = "graduateYear", required = false) String graduateYear,
                                    @RequestParam(value = "certificateNum", required = false) String certificateNum) {
        if (certificateNum == null) {
            return BaseResult.successWithData(studentGraduateService.list());
        }
        QueryWrapper<StudentGraduate> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("graduate", graduateYear)
                .eq("certificate_num", certificateNum);
        List<StudentGraduate> list = studentGraduateService.list(queryWrapper);
        return BaseResult.successWithData(list);
    }

    @GetMapping("certificate")
    @ApiOperation("根据毕业年份、证书号、身份证号查询毕业信息列表")
    public BaseResult listCertificate(@RequestParam(value = "graduateYear", required = true) String graduateYear,
                                      @RequestParam(value = "certificateNum", required = false) String certificateNum,
                                      @RequestParam(value = "identityNum", required = false) String identityNum) {
        if (certificateNum == null && identityNum == null) {
            return BaseResult.successWithData(studentGraduateService.list());
        } else if (certificateNum == null) {
            List<StudentGraduate> list = studentGraduateService.list(
                    new QueryWrapper<StudentGraduate>().eq("identity_num", identityNum)
            );
            return BaseResult.successWithData(list);
        } else if (identityNum == null) {
            List<StudentGraduate> list = studentGraduateService.list(
                    new QueryWrapper<StudentGraduate>().eq("certificate_num", certificateNum)
            );
            return BaseResult.successWithData(list);
        }
        QueryWrapper<StudentGraduate> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("graduate", graduateYear)
                .eq("certificate_num", certificateNum);
        List<StudentGraduate> list = studentGraduateService.list(queryWrapper);
        return BaseResult.successWithData(list);
    }

    @GetMapping("/graduateInfo/{identityNum}")
    @ApiOperation("根据学生的身份证号，返回毕业信息列表")
    public BaseResult getGraduateListByIdentityNum(@PathVariable("identityNum") String identityNum) {
        List<StudentGraduate> list = studentGraduateService.list(
                new QueryWrapper<StudentGraduate>().eq("identity_num", identityNum));
        return BaseResult.successWithData(list);
    }

    @PostMapping("/student")
    @ApiOperation("学生对毕业信息进行签名")
    public BaseResult studentSign(@RequestBody StudentSignDTO studentSignDTO) {
        StudentGraduate graduateInfo = studentGraduateService.getById(studentSignDTO.getGradateInfoId());
        if (graduateInfo == null) {
//            return BaseResult.failWithCodeAndMsg(1, "无法找到毕业信息");
            return BaseResult.failWithErrorCode(ErrorCode.GRADUATE_INFO_NOT_FOUNT);
        }
        if (!graduateInfo.getIdentityNum().equals(studentSignDTO.getIdentityNum())) {
//            return BaseResult.failWithCodeAndMsg(1, "无法对他人的学历信息签名！");
            return BaseResult.failWithErrorCode(ErrorCode.SIGN_OTHER_FAILED);

        }
        if (graduateInfo.getSignStatus() != SignStatus.SCHOOL_SIGN) {
            //只有在状态为1（学校已经签名）的情况下，才能轮到学生签名
//            return BaseResult.failWithCodeAndMsg(1, "当前状态无法签名");
            return BaseResult.failWithErrorCode(ErrorCode.SIGN_FAILED);
        }

        //尝试从redis中寻找该学生的私钥，如果找不到则新建
        String studentIdentityNum = studentSignDTO.getIdentityNum();
        if (redisUtil.get(studentIdentityNum + priKeyName) == null || redisUtil.get(studentIdentityNum + pubKeyName) == null) {
            //第一次如果为空，说明尚未生成公钥私钥，需要生成
            KeyPair pair = SecureUtil.generateKeyPair("RSA");
            String priKey = Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded());
            String pubKey = Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());
            //将密钥进行Base64编码后存入redis
            redisUtil.set(studentIdentityNum + priKeyName, priKey, -1);
            redisUtil.set(studentIdentityNum + pubKeyName, pubKey, -1);
        }
        //对毕业信息用学生的私钥进行签名，存入区块链
        byte[] priKey = Base64.getDecoder().decode((String)redisUtil.get(studentIdentityNum + priKeyName));
        RSA rsa = new RSA(priKey, null);
        //取学校签名后的结果，再次签名
        String encyptedGraduateInfo = (String)redisUtil.get(graduateInfo.getId()+"");
        byte[] encrypt = rsa.encrypt(encyptedGraduateInfo, KeyType.PrivateKey);
        //签名后上链
        noobChain.add(Base64.getEncoder().encodeToString(encrypt), graduateInfo.getCertificateNum());
        //修改数据库中该条毕业信息的状态
        graduateInfo.setSignStatus(SignStatus.STUDENT_SIGN);
        studentGraduateService.updateById(graduateInfo);

        return BaseResult.success();
    }

    @GetMapping("/enterprise")
    @ApiOperation("企业查询学历信息，需要传入学历证书编号，学校，身份证号，公钥验证成功才能查到")
    public BaseResult enterpriseCheck(@RequestParam("certificateNum") String certificateNum,
                                      @RequestParam("university") String university,
                                      @RequestParam("identityNum") String identityNum) {
        //验证成功后记得删除redis中第一次签名得到的中间结果
        StudentGraduate graduateInfo = studentGraduateService.getValidGraduateInfo(certificateNum, university, identityNum);
        if (graduateInfo == null) {
            return BaseResult.failWithErrorCode(ErrorCode.DATA_NOT_FOUND);
        } else {
            return BaseResult.successWithData(graduateInfo);
        }
    }
}

