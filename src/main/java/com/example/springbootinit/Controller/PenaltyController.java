package com.example.springbootinit.Controller;

import com.example.springbootinit.Service.PenaltyService;
import com.example.springbootinit.Utils.DataHandle;
import com.example.springbootinit.Utils.MyResponse;
import com.example.springbootinit.VO.DataListVO;
import com.example.springbootinit.VO.PenaltyVO;
import com.example.springbootinit.VO.SummaryVO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/penalty")
public class PenaltyController {
    private static final String EMPTY_FILE = "文件不能为空";

    @Resource
    PenaltyService penaltyService;

    /**
     * 导入excel
     */
    @PostMapping("/importXls")
    public MyResponse importXls(@RequestParam(value = "uploadXls") MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) return MyResponse.buildFailure(EMPTY_FILE);
        List<PenaltyVO> penaltyList = (List<PenaltyVO>) DataHandle.parseExcel(multipartFile.getInputStream(), PenaltyVO.class);
        return MyResponse.buildSuccess(penaltyService.insertPenalties(penaltyList));
    }

    /**
     * 新增处罚记录
     */
    @PostMapping("/createPunishment")
    public MyResponse addPenalty(@Valid @RequestBody DataListVO<PenaltyVO> dataList) {
        return MyResponse.buildSuccess(penaltyService.insertPenalties(dataList.getDataList()));
    }


    /**
     * 删除处罚记录
     */
    @PostMapping("/deletePunishment")
    public MyResponse deletePenalty(@Valid @RequestBody DataListVO<String> dataList) {
        penaltyService.deletePenalties(dataList.getDataList());
        return MyResponse.buildSuccess();
    }

    /**
     * 修改处罚记录
     */
    @PostMapping("/editPunishment")
    public MyResponse updatePenalty(@Valid @RequestBody PenaltyVO penaltyVO) {
        return MyResponse.buildSuccess(penaltyService.updatePenalty(penaltyVO));
    }

    /**
     * 批量发布处罚记录
     */
    @PostMapping("/releasePunishment")
    public MyResponse changePenaltyStatus(@Valid @RequestBody DataListVO<String> dataList) {
        return MyResponse.buildSuccess(penaltyService.changePenaltyStatus(dataList.getListRelatedOperation(), dataList.getDataList()));
    }

    /**
     * 查询处罚记录
     * 默认 pageNumber = 1; pageSize = 20;
     */
    @GetMapping("/getPunishmentList/{pageNumber}")
    public MyResponse findAll(
            @PathVariable(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
            @RequestParam(value = "isVague", required = false, defaultValue = "true") Boolean isVague,
            @Valid @ModelAttribute PenaltyVO penaltyVO) {

        if (pageNumber == null) pageNumber = 1;
        DataListVO dataList = penaltyService.findAllPenalty(penaltyVO, pageNumber, pageSize, isVague);
        return MyResponse.buildSuccess(dataList);

    }


    /**
     * 获取总体情况
     */
    @GetMapping("/getSummary")
    public MyResponse getSummary(@RequestParam(value = "year") String year,
                                 @RequestParam(value = "month") String month) {

        return MyResponse.buildSuccess(penaltyService.getSummary(year, month));
    }

    /**
     * 获取机构罚单笔数排行
     */
    @GetMapping("/getOrganListOrderByCount")
    public MyResponse getOrganListOrderByCount(@RequestParam(value = "year") String year,
                                               @RequestParam(value = "month") String month) {

        return MyResponse.buildSuccess(penaltyService.getOrganListOrderByCount(year, month));
    }

    /**
     * 获取机构罚没金额排行
     */
    @GetMapping("/getOrganListOrderByFine")
    public MyResponse getOrganListOrderByFine(@RequestParam(value = "year") String year,
                                              @RequestParam(value = "month") String month) {

        return MyResponse.buildSuccess(penaltyService.getOrganListOrderByFine(year, month));
    }

    /**
     * 获取罚单地域分布
     */
    @GetMapping("/getPenaltyDistribution")
    public MyResponse getPenaltyDistribution(@RequestParam(value = "year") String year,
                                             @RequestParam(value = "month") String month) {

        return MyResponse.buildSuccess(penaltyService.getPenaltyDistribution(year, month));
    }

    /**
     * 获取大额罚单详情
     */
    @GetMapping("/getPenaltyOrderByFine")
    public MyResponse getPenaltyOrderByFine(@RequestParam(value = "year") String year,
                                            @RequestParam(value = "month") String month) {

        return MyResponse.buildSuccess(penaltyService.getPenaltyOrderByFine(year, month));
    }

    /**
     * 获取机构处罚决定分析
     */
    @GetMapping("/getAnalysis")
    public MyResponse getAnalysisForOrgan(@RequestParam(value = "type") String type,
                                          @RequestParam(value = "year") String year,
                                          @RequestParam(value = "month") String month) {
        return MyResponse.buildSuccess(penaltyService.getAnalysis(type, year, month));
    }

    /**
     * 获取违规点分析
     */
    @GetMapping("/getBasisStatistics")
    public MyResponse getBasisStatistics(@RequestParam(value = "year") String year,
                                         @RequestParam(value = "month") String month) {
        return MyResponse.buildSuccess(penaltyService.getBasisStatistics(year, month));
    }

    /**
     * 趋势分析
     */
    @GetMapping("/getAnalysisForTrend")
    public MyResponse getAnalysisForTrend(@RequestParam(value = "start") String start,
                                          @RequestParam(value = "end") String end) {
        return MyResponse.buildSuccess(penaltyService.getAnalysisForTrend(start, end));
    }
}
