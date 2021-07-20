package com.example.ccb.service.impl;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.ccb.common.Block;
import com.example.ccb.common.NoobChain;
import com.example.ccb.common.SignStatus;
import com.example.ccb.entity.StudentGrade;
import com.example.ccb.entity.StudentGraduate;
import com.example.ccb.exception.CustomizeException;
import com.example.ccb.exception.ErrorCode;
import com.example.ccb.mapper.StudentGradeMapper;
import com.example.ccb.mapper.StudentGraduateMapper;
import com.example.ccb.service.IStudentGraduateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ccb.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chen
 * @since 2021-06-01
 */
@Service
@Slf4j
public class StudentGraduateServiceImpl extends ServiceImpl<StudentGraduateMapper, StudentGraduate> implements IStudentGraduateService {

    @Autowired
    private StudentGraduateMapper studentGraduateMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private NoobChain chain;

    @Value("${redis.publicKeyName}")
    private String pubKeyName;

    @Value("${redis.privateKeyName}")
    private String priKeyName;

    @Override
    public StudentGraduate getValidGraduateInfo(String certificateNum, String university, String identityNum) {
        //1. 根据学历证书编号在区块链查找对应区块
        Block block = chain.findBlockbByCertificateNum(certificateNum);
        if (block == null) {
            return null;
        }
        String jsonString = null;
        //2. 使用公钥对内容进行解密，解密失败则说明被篡改，直接返回错误
        String encryptedGraduateData = block.getData();
        //先取出学生的公钥进行解密
        try {
            String studentPubKey = (String)redisUtil.get(identityNum+pubKeyName);
            if (studentPubKey == null) {
                throw new CustomizeException(ErrorCode.KEY_NOT_FOUND);
            }
            RSA rsa = new RSA(null, Base64.getDecoder().decode(studentPubKey));
            //使用学生公钥进行解密
            byte[] ret = rsa.decrypt(Base64.getDecoder().decode(encryptedGraduateData), KeyType.PublicKey);
            ret = Base64.getDecoder().decode(new String(ret));

            //再取出企业的公钥进行解密
            String enterprisePubKey = (String)redisUtil.get(pubKeyName);
            if (enterprisePubKey == null) {
                throw new CustomizeException(ErrorCode.KEY_NOT_FOUND);
            }
            RSA rsa1 = new RSA(null, Base64.getDecoder().decode(enterprisePubKey));
            ret = rsa1.decrypt(ret, KeyType.PublicKey);
            jsonString = new String(ret);
            log.info(jsonString);

        } catch (Exception e) {
            throw new CustomizeException(ErrorCode.DATA_UNBELIEVABLE);
        }


        //3. 把解密得到的json串转化为对象
        StudentGraduate graduateInfo = JSON.parseObject(jsonString, StudentGraduate.class);
        //记得删除redis中的中间加密结果
        redisUtil.expire(graduateInfo.getId()+"", 1);


        //4. 验证对象的university和identityNum是否正确，正确则返回该条信息
        if (university.equals(graduateInfo.getUniversity())
            && identityNum.equals(graduateInfo.getIdentityNum())) {
            return graduateInfo;
        } else {
            return null;
        }
    }
}
