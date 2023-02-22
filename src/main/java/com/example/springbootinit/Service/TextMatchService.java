package com.example.springbootinit.Service;

import com.example.springbootinit.Entity.TextMatchId;
import com.example.springbootinit.VO.TextMatchVO;

import java.util.List;

public interface TextMatchService {

    /**
     * 通过ID查询内规匹配
     *
     * @param textMatchId 内规匹配ID
     * @return 查询到的匹配
     */
    TextMatchVO queryById(TextMatchId textMatchId);

    /**
     * 通过处罚ID查询内规匹配
     *
     * @param penaltyId 处罚ID
     * @return 查询到的匹配列表
     */
    List<TextMatchVO> queryByPenaltyId(Integer penaltyId);

}
