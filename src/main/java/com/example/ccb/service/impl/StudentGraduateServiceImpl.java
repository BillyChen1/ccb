package com.example.ccb.service.impl;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.ccb.common.Block;
import com.example.ccb.common.BloomList;
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
import net.bytebuddy.asm.Advice;
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

    @Autowired
    private BloomList bloomList;

    @Value("${redis.publicKeyName}")
    private String pubKeyName;

    @Value("${redis.privateKeyName}")
    private String priKeyName;

    @Override
    public StudentGraduate getValidGraduateInfo(String certificateNum, String university, String identityNum) {
        //1. 根据学历证书编号在区块链查找对应区块
        //区块链不可信直接返回异常
        if (!chain.isChainValid()) {
            throw new CustomizeException(ErrorCode.DATA_UNBELIEVABLE);
        }
        //含有target的区块号
//        int blockId = -1;

        Block block = chain.findBlockByCertificateNum(certificateNum);
//        Block block = null;
//        blockId = bloomList.search(certificateNum);
//        //0号1号特殊处理
//        if (certificateNum.equals(chain.getBlockchain().get(0).getCertificateNum())) {
//            blockId = 0;
//        }
//        if (certificateNum.equals(chain.getBlockchain().get(1).getCertificateNum())) {
//            blockId = 1;
//        }
//        if (blockId == -1) {
//            return null;
//        } else {
//            block = chain.getBlockchain().get(blockId);
//        }
//        if (block == null) {
//            return null;
//        }
        String jsonString = null;
        //2. 使用公钥对内容进行解密，解密失败则说明不是学生或者学校本人操作，数据不安全，直接返回错误
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


        //4. 验证对象的university和identityNum是否正确，正确则返回该条信息
        if (university.equals(graduateInfo.getUniversity())
            && identityNum.equals(graduateInfo.getIdentityNum())) {
            //还需要和数据库的信息进行对比，如果不一致说明存在篡改风险
            graduateInfo.setSignStatus(SignStatus.STUDENT_SIGN);
            StudentGraduate dbInfo = studentGraduateMapper.selectOne(
                    new QueryWrapper<StudentGraduate>()
                            .eq("certificate_num", certificateNum));
            if (dbInfo.equals(graduateInfo)) {
                return graduateInfo;
            } else {
                throw new CustomizeException(ErrorCode.DATA_UNBELIEVABLE);
            }
        } else {
            return null;
        }
    }
}
