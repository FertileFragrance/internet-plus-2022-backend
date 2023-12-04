package com.example.springbootinit.Service;

import com.example.springbootinit.VO.PenaltyVO;

import java.util.List;

public interface SimilarityService {
    List<PenaltyVO> calByBasis(Integer penaltyId);

    List<PenaltyVO> calByFine(Integer penaltyId);

    List<PenaltyVO> calByPartyName(Integer penaltyId);

    List<PenaltyVO> calByOrganName(Integer penaltyId);
}
