package com.example.springbootinit.Service;

import com.example.springbootinit.VO.InnerPolicyVO;

public interface InnerPolicyService {

    /**
     * 通过ID查询内规
     *
     * @param id 内规ID
     * @return 查询到的内规
     */
    InnerPolicyVO queryById(Integer id);

}
