package com.example.springbootinit.Service.Impl;

import com.example.springbootinit.Entity.User;
import com.example.springbootinit.Repository.UserRepository;
import com.example.springbootinit.Service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserRepository userRepository;

    @Override
    public User insertUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> findAllUser(int pageNumber, int pageSize) {
        List<User> users = userRepository.findAll();
        int count = users.size();
//        int pageNumber = 1;
//        int pageSize = 2;

        int startCurrentPage = (pageNumber - 1) * pageSize; //开启的数据
        int endCurrentPage = pageNumber * pageSize; //结束的数据

        return users.subList(startCurrentPage, endCurrentPage);
    }
}