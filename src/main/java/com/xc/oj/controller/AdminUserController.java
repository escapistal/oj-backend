package com.xc.oj.controller;

import com.xc.oj.entity.User;
import com.xc.oj.response.responseBase;
import com.xc.oj.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/user")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public responseBase<String> register(@RequestParam String username,@RequestParam String password,@RequestParam String email,@RequestParam List<String> role) {
        return userService.register(username,password,email,role);
    }


    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public responseBase<User> update(@PathVariable long id, @RequestBody User user) {
        return userService.update(id,user);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public responseBase<String> delete(@PathVariable long id) {
        return userService.delete(id);
    }
}
