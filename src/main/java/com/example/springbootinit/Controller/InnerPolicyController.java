package com.example.springbootinit.Controller;

import com.example.springbootinit.Service.InnerPolicyService;
import com.example.springbootinit.VO.InnerPolicyVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/policy")
public class InnerPolicyController {

    @Resource
    private InnerPolicyService innerPolicyService;

    @GetMapping("/queryById")
    public InnerPolicyVO queryById(@RequestParam Integer id) {
        return innerPolicyService.queryById(id);
    }

}
