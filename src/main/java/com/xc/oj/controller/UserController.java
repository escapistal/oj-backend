package com.xc.oj.controller;

import com.xc.oj.entity.User;
import com.xc.oj.response.responseBase;
import com.xc.oj.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Api("用户相关接口")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public responseBase<List<User>> listAll() {
        return userService.listAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public responseBase<User> get(@PathVariable Long id){return userService.get(id);}

    @ApiOperation("用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public responseBase<String> login(@RequestParam String username,@RequestParam String password) {
        return userService.login(username,password);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public responseBase<String> register(@RequestParam String username,@RequestParam String password,@RequestParam String email) {
        return userService.register(username,password,email);
    }

    @PreAuthorize("#id==principal.id")  //常规user只支持修改自己的信息
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public responseBase<User> update(@PathVariable long id, @RequestBody User user) {
        return userService.update(id,user);
    }

}
