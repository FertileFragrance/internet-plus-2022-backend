package com.example.springbootinit.Service.Impl;

import com.example.springbootinit.Entity.TextMatch;
import com.example.springbootinit.Entity.TextMatchId;
import com.example.springbootinit.Repository.TextMatchRepository;
import com.example.springbootinit.Service.TextMatchService;
import com.example.springbootinit.VO.TextMatchVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TextMatchServiceImpl implements TextMatchService {
    @Resource
    private TextMatchRepository textMatchRepository;

    @Override
    public TextMatchVO queryById(TextMatchId textMatchId) {
        Optional<TextMatch> optionalTextMatch = textMatchRepository.findById(textMatchId);
        if (optionalTextMatch.isPresent()) {
            TextMatch textMatch = optionalTextMatch.get();
            TextMatchVO textMatchVO = new TextMatchVO();
            BeanUtils.copyProperties(textMatch, textMatchVO);
            return textMatchVO;
        }
        return null;
    }

    @Override
    public List<TextMatchVO> queryByPenaltyId(Integer penaltyId) {
        List<TextMatch> textMatches = textMatchRepository.findByPenaltyId(penaltyId);
        List<TextMatchVO> textMatchVOList = new ArrayList<>();
        for (TextMatch textMatch : textMatches) {
            TextMatchVO textMatchVO = new TextMatchVO();
            BeanUtils.copyProperties(textMatch, textMatchVO);
            textMatchVOList.add(textMatchVO);
        }
        return textMatchVOList;
    }
}
