package com.example.springbootinit.Controller;

import com.example.springbootinit.Entity.TextMatchId;
import com.example.springbootinit.Service.TextMatchService;
import com.example.springbootinit.VO.TextMatchVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/match")
public class TextMatchController {

    @Resource
    private TextMatchService textMatchService;

    @GetMapping("/queryById")
    public TextMatchVO queryById(@RequestParam Integer penaltyId, @RequestParam Integer innerPolicyId) {
        TextMatchId textMatchId = new TextMatchId();
        textMatchId.setPenaltyId(penaltyId);
        textMatchId.setInnerPolicyId(innerPolicyId);
        return textMatchService.queryById(textMatchId);
    }

    @GetMapping("/queryByPenaltyId")
    public List<TextMatchVO> queryByPenaltyId(@RequestParam Integer penaltyId) {
        return textMatchService.queryByPenaltyId(penaltyId);
    }

}
