package com.xc.oj.controller;

import com.xc.oj.entity.User;
import com.xc.oj.response.responseBase;
import com.xc.oj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public responseBase<List<User>> listAll(){
        return userService.listAll();
    }

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