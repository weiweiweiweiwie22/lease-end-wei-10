package com.weiwei.lease.web.app.controller.login;


import com.weiwei.lease.common.login.LoginUserHolder;
import com.weiwei.lease.common.result.Result;
import com.weiwei.lease.web.app.service.LoginService;
import com.weiwei.lease.web.app.vo.user.LoginVo;
import com.weiwei.lease.web.app.vo.user.UserInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@Tag(name = "登录管理")
@RestController
@RequestMapping("/app/")
public class LoginController {

    @Resource
    private LoginService loginService;
    @GetMapping("login/getCode")
    @Operation(summary = "获取短信验证码")
    public Result getCode(@RequestParam String phone) {
        loginService.getCode(phone);
        return Result.ok();
    }

    @PostMapping("login")
    @Operation(summary = "登录")
    public Result<String> login(@RequestBody LoginVo loginVo) {
        String token = loginService.login(loginVo);
        return Result.ok(token);
    }

    @GetMapping("info")
    @Operation(summary = "获取登录用户信息")
    public Result<UserInfoVo> info() {
        Long userId = LoginUserHolder.getLoginUser().getUserId();
        UserInfoVo result = loginService.selectOneByUserId(userId);
        return Result.ok(result);
    }
}

