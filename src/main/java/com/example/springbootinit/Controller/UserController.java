package com.example.springbootinit.Controller;

import com.example.springbootinit.Entity.User;
import com.example.springbootinit.Service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 新增用户
     */
    @PostMapping("")
    public User addUser(@RequestBody User user) {
        return userService.insertUser(user);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
    }

    /**
     * 修改用户
     */
    @PutMapping("")
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    /**
     * id查用户
     */
    @GetMapping("/{id}")
    public User findUesrbyId(@PathVariable("id") int id) {
        return userService.findUserById(id);
    }

    /**
     * 全查用户
     */
    @GetMapping("/{pageNumber}/{pageSize}")
    public List<User> findAll(@PathVariable(value = "pageNumber", required = false) Integer pageNumber, @PathVariable(value = "pageSize", required = false) Integer pageSize) {
//        List<User> users = new ArrayList<>();
//        users = userService.findAllUser();
//        return users.subList(1,4);
        if (pageNumber == null) pageNumber = 1;
        if (pageSize == null) pageSize = 2;
        return userService.findAllUser(pageNumber, pageSize);
    }
}