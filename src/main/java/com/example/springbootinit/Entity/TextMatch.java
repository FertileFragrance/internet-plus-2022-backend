package com.example.springbootinit.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@IdClass(TextMatchId.class)
@Table(name = "text_match")
public class TextMatch {
    @Id
    @Column(name = "penalty_id")
    private Integer penaltyId;

    @Id
    @Column(name = "inner_policy_id")
    private Integer innerPolicyId;

    @Column(name = "ranking")
    private Integer ranking;

    @Column(name = "cosine")
    private Double cosine;
}
