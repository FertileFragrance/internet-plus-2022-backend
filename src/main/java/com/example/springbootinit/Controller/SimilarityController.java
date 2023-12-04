package com.example.springbootinit.Controller;

import com.example.springbootinit.Service.SimilarityService;
import com.example.springbootinit.VO.PenaltyVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/similarity")
public class SimilarityController {

    @Resource
    private SimilarityService similarityService;

    @GetMapping("/calByBasis")
    public List<PenaltyVO> calByBasis(@RequestParam Integer penaltyId) {
        return similarityService.calByBasis(penaltyId);
    }

    @GetMapping("/calByFine")
    public List<PenaltyVO> calByFine(@RequestParam Integer penaltyId) {
        return similarityService.calByFine(penaltyId);
    }

    @GetMapping("/calByPartyName")
    public List<PenaltyVO> calByPartyName(@RequestParam Integer penaltyId) {
        return similarityService.calByPartyName(penaltyId);
    }

    @GetMapping("/calByOrganName")
    public List<PenaltyVO> calByOrganName(@RequestParam Integer penaltyId) {
        return similarityService.calByOrganName(penaltyId);
    }

}
