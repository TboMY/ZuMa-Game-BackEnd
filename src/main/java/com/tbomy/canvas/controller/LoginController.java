package com.tbomy.canvas.controller;

import com.tbomy.canvas.param.request.LoginInfo;
import com.tbomy.canvas.service.login.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cj
 * @date 2024/1/1 0:11:58
 */
@RestController
@CrossOrigin
public class LoginController {
    private final LoginService loginService;
    
    @PostMapping("/login")
    public void login(@RequestBody LoginInfo loginInfo) {
      loginService.login(loginInfo);
    }
    
    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }
}
