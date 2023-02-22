package com.example.springbootinit.Entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class TextMatchId implements Serializable {
    private Integer penaltyId;
    private Integer innerPolicyId;
}
