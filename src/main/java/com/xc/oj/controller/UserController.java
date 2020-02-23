package com.xc.oj.controller;

import com.xc.oj.entity.User;
import com.xc.oj.response.responseBase;
import com.xc.oj.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    public responseBase<List<User>> listAll(){
        return userService.listAll();
    }

    @ApiOperation("用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public responseBase<String> login(@RequestBody User user) {
        return userService.login(user.getUsername(),user.getPassword());
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public responseBase<String> register(@RequestBody User user) {
        return userService.register(user);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public responseBase<String> update(@PathVariable long id, @RequestBody User user) {
        return userService.update(id,user);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public responseBase<String> delete(@PathVariable long id) {
        return userService.delete(id);
    }
}
