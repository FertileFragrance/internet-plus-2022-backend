package com.example.springbootinit.VO;

import lombok.Data;

@Data
public class SummaryVO {

    private String total; //罚单总数

    private String amount; //累计罚没金额

    private String countOrganization; //处罚机构数量

    private String countPersonal; //处罚人员数量

    private String lastTotal; //去年本月罚单总数

    private String lastAmount; //去年累计罚没金额

    private String lastCountOrganization; //去年处罚机构数量

    private String lastCountPersonal; //去年处罚人员数量

    private String branchTotal; //分行罚单合计

    private String branchTotalRatio; //分行罚单百分比

    private String branchAmount; //分行罚没金额

    private String branchAmountRatio; //分行罚没金额百分比

    private String centerBranchTotal; //中心支行罚单合计

    private String centerBranchTotalRatio; //中心支行罚单百分比

    private String centerBranchAmount; //中心支行罚没金额

    private String centerBranchAmountRatio; //中心支行罚没金额百分比

}
