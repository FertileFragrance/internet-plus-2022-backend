package com.example.springbootinit.VO;
import lombok.Data;

@Data
public class OrganDetailVO {

    private String name; //机构名称

    private String count; //机构罚单笔数

    private String amount; //机构罚没金额
}
