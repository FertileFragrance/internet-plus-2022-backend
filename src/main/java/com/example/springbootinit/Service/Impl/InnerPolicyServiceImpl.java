package com.example.springbootinit.Service.Impl;

import com.example.springbootinit.Entity.InnerPolicy;
import com.example.springbootinit.Repository.InnerPolicyRepository;
import com.example.springbootinit.Service.InnerPolicyService;
import com.example.springbootinit.VO.InnerPolicyVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class InnerPolicyServiceImpl implements InnerPolicyService {
    @Resource
    private InnerPolicyRepository innerPolicyRepository;

    @Override
    public InnerPolicyVO queryById(Integer id) {
        Optional<InnerPolicy> optionalInnerPolicy = innerPolicyRepository.findById(id);
        if (optionalInnerPolicy.isPresent()) {
            InnerPolicy innerPolicy = optionalInnerPolicy.get();
            InnerPolicyVO innerPolicyVO = new InnerPolicyVO();
            BeanUtils.copyProperties(innerPolicy, innerPolicyVO);
            return innerPolicyVO;
        }
        return null;
    }
}
