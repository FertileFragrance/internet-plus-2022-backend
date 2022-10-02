package com.example.springbootinit.VO;

import lombok.Data;

@Data
public class FrequencyStatisticsVO {
    private String type; //处罚类型

    private String frequency; //频次

    private String ratio; //频次占比

    private String amount; //罚没金额
}
