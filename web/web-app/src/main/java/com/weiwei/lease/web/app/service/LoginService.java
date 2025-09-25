package com.weiwei.lease.web.app.service;

import com.weiwei.lease.web.app.vo.user.LoginVo;
import com.weiwei.lease.web.app.vo.user.UserInfoVo;

public interface LoginService {
    void getCode(String phone);

    String login(LoginVo loginVo);

    UserInfoVo selectOneByUserId(Long userId);
}
