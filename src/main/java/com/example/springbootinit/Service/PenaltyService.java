package com.example.springbootinit.Service;

import com.example.springbootinit.VO.*;


import java.util.List;

public interface PenaltyService {

    /**
     * 新增行政处罚记录
     * @param penalty 行政处罚记录对象
     */
    PenaltyVO insertPenalty(PenaltyVO penalty);


    /**
     * 批量新增行政处罚记录
     * @param penaltyList 行政处罚记录对象列表
     */
    DataListVO<PenaltyVO> insertPenalties(List<PenaltyVO> penaltyList);


    /**
     * 删除行政处罚记录
     * @param id 删除id
     */
    void deletePenalty(int id);

    /**
     * 删除行政处罚记录
     * @param ids
     */
    void deletePenalties(List<String> ids);

    /**
     * 修改行政处罚记录
     * @param penaltyVO 行政处罚记录对象
     */
    PenaltyVO updatePenalty(PenaltyVO penaltyVO);


    /**
     * @param status
     * @param ids
     */
    DataListVO<PenaltyVO> changePenaltyStatus(String status, List<String> ids);


    /**
     * 查询所有行政处罚记录
     */
    DataListVO<PenaltyVO> findAllPenalty(PenaltyVO penaltyVO, int pageNumber, int pageSize, boolean isVague);


    /**
     * 处罚决定分析
     */
    DataListVO<FrequencyStatisticsVO> getAnalysis(String type, String year, String month);


    /**
     * 违规点分析
     */
    DataListVO<FrequencyStatisticsVO> getBasisStatistics(String year, String month);


    /**
     * 获取该年月的总体处罚情况入口
     */
    SummaryVO getSummary(String year, String month);


    /**
     * 获取该年月的大额罚单详情
     */
    DataListVO<PenaltyVO> getPenaltyOrderByFine(String year, String month);


    /**
     * 获取机构罚单笔数排行
     */
    DataListVO<OrganDetailVO> getOrganListOrderByCount(String year, String month);


    /**
     * 获取机构罚没金额排行
     */
    DataListVO<OrganDetailVO> getOrganListOrderByFine(String year, String month);

    /**
     * 获取罚单地域分布
     */
    DataListVO<ProvinceDetailVO> getPenaltyDistribution(String year, String month);

    /**
     * 趋势分析
     */
    DataListVO<TrendVO> getAnalysisForTrend(String start, String end);
}
