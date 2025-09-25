package com.weiwei.lease.web.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.weiwei.lease.common.constant.RedisConstant;
import com.weiwei.lease.common.exception.BusinessException;
import com.weiwei.lease.common.result.ResultCodeEnum;
import com.weiwei.lease.common.utils.JwtUtil;
import com.weiwei.lease.model.entity.SystemUser;
import com.weiwei.lease.model.enums.BaseStatus;
import com.weiwei.lease.web.admin.mapper.SystemUserMapper;
import com.weiwei.lease.web.admin.service.LoginService;
import com.weiwei.lease.web.admin.vo.login.CaptchaVo;
import com.weiwei.lease.web.admin.vo.login.LoginVo;
import com.weiwei.lease.web.admin.vo.system.user.SystemUserInfoVo;
import com.wf.captcha.SpecCaptcha;
import jakarta.annotation.Resource;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Resource
    private SystemUserMapper systemUserMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public CaptchaVo getCaptcha() {
        try {
            SpecCaptcha specCaptcha = new SpecCaptcha(100, 40, 4);
            String code = specCaptcha.text().toLowerCase();
            String key = RedisConstant.ADMIN_LOGIN_PREFIX + UUID.randomUUID();

            stringRedisTemplate.opsForValue().set(key, code, RedisConstant.ADMIN_LOGIN_CAPTCHA_TTL_SEC, TimeUnit.SECONDS);

            return new CaptchaVo(specCaptcha.toBase64(), key);
        } catch (Exception e) {
            logger.error("Failed to generate captcha", e);
            throw new RuntimeException("Failed to generate captcha", e);
        }
    }

    @Override
    public String login(LoginVo loginVo) {
        if (loginVo.getCaptchaCode() == null) {
            throw new BusinessException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_NOT_FOUND);
        }
        String code = stringRedisTemplate.opsForValue().get(loginVo.getCaptchaKey());
        if (code == null){
            throw new BusinessException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_EXPIRED);
        }
        if (!code.equals(loginVo.getCaptchaCode().toLowerCase())){
            throw new BusinessException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_ERROR);
        }

        SystemUser systemUser = systemUserMapper.selectOneByUsername(loginVo.getUsername());

        if (systemUser == null){
            throw new BusinessException(ResultCodeEnum.ADMIN_ACCOUNT_NOT_EXIST_ERROR);
        }
        if (systemUser.getStatus()==(BaseStatus.DISABLE)){
            throw new BusinessException(ResultCodeEnum.ADMIN_ACCOUNT_DISABLED_ERROR);
        }
        if (!systemUser.getPassword().equals(DigestUtils.md5Hex(loginVo.getPassword()))){
            throw new BusinessException(ResultCodeEnum.ADMIN_ACCOUNT_ERROR);
        }

        return JwtUtil.createToken(systemUser.getId(), systemUser.getUsername());
    }

    @Override
    public SystemUserInfoVo selectOneByUserId(Long userId) {
        SystemUser systemUser = systemUserMapper.selectById(userId);
        SystemUserInfoVo systemUserInfoVo = new SystemUserInfoVo();
        systemUserInfoVo.setName(systemUser.getName());
        systemUserInfoVo.setAvatarUrl(systemUser.getAvatarUrl());
        return systemUserInfoVo;
    }
}