package com.example.springbootinit.Repository;

import com.example.springbootinit.Entity.TextMatch;
import com.example.springbootinit.Entity.TextMatchId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TextMatchRepository extends JpaRepository<TextMatch, TextMatchId> {

    /**
     * 通过处罚ID查询内规匹配
     *
     * @param penaltyId 处罚ID
     * @return 查询到的匹配列表
     */
    List<TextMatch> findByPenaltyId(Integer penaltyId);

}
