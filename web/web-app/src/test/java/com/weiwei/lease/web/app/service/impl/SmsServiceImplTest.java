package com.weiwei.lease.web.app.service.impl;

import com.weiwei.lease.web.app.service.SmsService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SmsServiceImplTest {
    @Resource
    private SmsService smsService;

    @Test
    public void sendCode() {
        smsService.sendCode("18090184580", "1234");
    }
}