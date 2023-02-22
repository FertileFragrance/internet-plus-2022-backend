package com.example.springbootinit.VO;

import lombok.Data;

@Data
public class TextMatchVO {
    private Integer penaltyId;
    private Integer innerPolicyId;
    private Integer ranking;
    private Double cosine;
}
