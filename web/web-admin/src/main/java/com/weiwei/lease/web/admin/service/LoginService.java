package com.weiwei.lease.web.admin.service;

import com.weiwei.lease.web.admin.vo.login.CaptchaVo;
import com.weiwei.lease.web.admin.vo.login.LoginVo;
import com.weiwei.lease.web.admin.vo.system.user.SystemUserInfoVo;

public interface LoginService {

    CaptchaVo getCaptcha();

    String login(LoginVo loginVo);

    SystemUserInfoVo selectOneByUserId(Long userId);
}
