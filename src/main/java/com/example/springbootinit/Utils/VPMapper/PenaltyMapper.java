package com.example.springbootinit.Utils.VPMapper;

import com.example.springbootinit.Entity.Penalty;
import com.example.springbootinit.VO.PenaltyVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PenaltyMapper {
    PenaltyMapper INSTANCE = Mappers.getMapper(PenaltyMapper.class);

    @Mappings({})
    PenaltyVO p2v(Penalty penalty);
    List<PenaltyVO> pList2vList(List<Penalty> penaltyList);

    @Mappings({})
    Penalty v2p(PenaltyVO penaltyVO);
    List<Penalty> vList2pList(List<PenaltyVO> penaltyVOList);
}
