package com.example.springbootinit.VO;


import lombok.Data;

@Data
public class ProvinceDetailVO {

    private String province; //省份名称

    private String count; //罚单笔数

    private String countRatio; //罚单笔数占比

    private String amount; //罚没金额

    private String amountRatio; //罚没金额占比

    private String amountOrganization; //机构罚没金额

    private String amountPersonal; //个人罚没金额

    private String countOrganization; //处罚机构数

    private String countPersonal; //处罚个人数
}
