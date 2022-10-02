package com.example.springbootinit.VO;

import lombok.Data;

@Data
public class TrendVO {
    private String time; //时间，格式为YYYY-MM,表示年-月

    private String amount; //罚没金额，单位万元

    private String count; //罚单总数
}

