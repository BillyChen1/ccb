package com.example.ccb.controller;

import com.example.ccb.common.BaseResult;
import com.example.ccb.common.NoobChain;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/chain")
@Api(tags = "打印区块链相关信息")
public class ChainController {

    @Autowired
    private NoobChain chain;

    @GetMapping()
    @ApiOperation("打印区块链信息")
    public BaseResult getBlockChain() {
        return BaseResult.successWithData(chain.getBlockchain());
    }

    @GetMapping("/attack")
    @ApiOperation("模仿攻击者篡改区块链 暂时先别用")
    public BaseResult attack() {
        if (!chain.getBlockchain().isEmpty()) {
            chain.getBlockchain().get(0).setHash(UUID.randomUUID().toString());
            return BaseResult.success();
        } else {
            return BaseResult.failWithCodeAndMsg(1,  "区块链为空");
        }
    }
}
