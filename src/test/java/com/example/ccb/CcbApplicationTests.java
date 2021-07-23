package com.example.ccb;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.example.ccb.common.BaseResult;
import com.example.ccb.dto.GraduateInfoReturnDTO;
import com.example.ccb.dto.StudentSignDTO;
import com.example.ccb.entity.StudentGraduate;
import com.example.ccb.service.IStudentGraduateService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import sun.net.www.http.HttpClient;

import java.util.LinkedHashMap;


@SpringBootTest
@RunWith(SpringRunner.class)
class CcbApplicationTests {

    @Autowired
    private IStudentGraduateService studentGraduateService;

    private RestTemplate restTemplate = new RestTemplate();

    private String url1 = "http://localhost:8432/studentGraduate";

    private String url2 = "http://localhost:8432/studentGraduate/student";


    @Test
    void contextLoads() {
        RSA rsa = new RSA();
//        RSA rsa1 = new RSA();

        //获得私钥
//        rsa.getPrivateKey();
//        rsa.getPrivateKeyBase64();
        //获得公钥
//        rsa.getPublicKey();
//        rsa.getPublicKeyBase64();

        //公钥加密，私钥解密
        byte[] encrypt = rsa.encrypt(StrUtil.bytes("我是一段测试aaaa", CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
        byte[] decrypt = rsa.decrypt(encrypt, KeyType.PrivateKey);
//        byte[] decrypt = rsa1.decrypt(encrypt, KeyType.PrivateKey);

        //Junit单元测试
        Assert.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8));

        //私钥加密，公钥解密
//        byte[] encrypt2 = rsa.encrypt(StrUtil.bytes("我是一段测试aaaa", CharsetUtil.CHARSET_UTF_8), KeyType.PrivateKey);
//        byte[] decrypt2 = rsa.decrypt(encrypt2, KeyType.PublicKey);

        //Junit单元测试
        //Assert.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt2, CharsetUtil.CHARSET_UTF_8));
    }

    @Test
    void addRecord() {
        int n = 10;
        for (int i = 0; i < n; i++) {
            StudentGraduate graduateInfo = new StudentGraduate();
            graduateInfo.setEducation("本科");
            graduateInfo.setAdmission("2015");
            graduateInfo.setGraduate("2019");
            graduateInfo.setGender("女");
            graduateInfo.setIdentityNum("420111111111111111");
            graduateInfo.setMajor("软件工程");
            graduateInfo.setPoliticalStatus("共青团员");
            graduateInfo.setStudentName("张三三");
            graduateInfo.setStudentNum("2015000000000");
            graduateInfo.setStudyForm("全日制");
            graduateInfo.setUniversity("家里蹲大学");
            graduateInfo.setSchool("家里蹲学院");
            BaseResult returnDTO = restTemplate.postForObject(url1, graduateInfo, BaseResult.class);
            StudentSignDTO studentSignDTO = new StudentSignDTO();
            int id = (int)((LinkedHashMap)returnDTO.getData()).get("id");
            studentSignDTO.setGradateInfoId(id);
            studentSignDTO.setIdentityNum(graduateInfo.getIdentityNum());
            restTemplate.postForObject(url2, studentSignDTO, BaseResult.class);
        }
    }



}
