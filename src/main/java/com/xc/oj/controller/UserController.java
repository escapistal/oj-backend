package com.xc.oj.controller;

import com.xc.oj.entity.User;
import com.xc.oj.entity.UserInfo;
import com.xc.oj.response.responseBase;
import com.xc.oj.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
    public responseBase<Page<User>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return userService.list(keyword,page,size);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public responseBase<User> get(@PathVariable Long id){return userService.get(id);}

    @ApiOperation("用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public responseBase<UserInfo> login(@RequestParam String username, @RequestParam String password) {
        return userService.login(username,password);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public responseBase<String> register(@RequestParam String username,@RequestParam String password,@RequestParam String email) {
        return userService.register(username,password,email, Collections.singletonList("user"));
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public responseBase<String> add(@RequestBody User user) {
        return userService.add(user);
    }

    @PreAuthorize("#id==principal.id or hasAuthority('admin')")  //常规user只支持修改自己的信息
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public responseBase<User> update(@PathVariable long id, @RequestBody User user) {
        return userService.update(id,user);
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public responseBase<String> delete(@PathVariable long id) {
        return userService.delete(id);
    }
}
