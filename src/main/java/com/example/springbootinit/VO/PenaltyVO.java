package com.example.springbootinit.VO;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@ExcelTarget(value = "penalty")
public class PenaltyVO {

    private String id; //案例的主键

    @Size(max = 64, message = "处罚名称长度不能超过64")
    @Excel(name = "行政处罚名称", isImportField = "true")
    private String name; //行政处罚名称

    @Size(max = 128, message = "文号长度不能超过128")
    @Excel(name = "行政处罚决定文号", isImportField = "true")
    private String number; //行政处罚决定文号

    @Pattern(regexp = "^(0|1)$", message = "不存在的类型")
    @Excel(name = "处罚类型", replace = {"个人_1", "企业_0"}, isImportField = "true")
    private String type; //处罚类型('0':个人|'1':企业)

    @Size(max = 64, message = "当事人名称长度不能超过64")
    @Excel(name = "被罚当事人名称", isImportField = "true")
    private String partyName; //被罚当事人名称

    @Size(max = 64, message = "负责人名称长度不能超过64")
    @Excel(name = "主要负责人姓名")
    private String responsiblePersonName; //主要负责人姓名

    @Size(max = 255, message = "违法事实长度不能超过255")
    @Excel(name = "主要违法违规事实")
    private String facts; //主要违法违规事实

    @Excel(name = "行政处罚依据")
    @Size(max = 256, message = "处罚依据长度不能超过256")
    private String basis; //行政处罚依据

    @Excel(name = "行政处罚决定")
    @Size(max = 512, message = "处罚决定长度不能超过512")
    private String decision; //行政处罚决定

    @Excel(name = "行政处罚类型")
    @Size(max = 64, message = "处罚决定长度不能超过64")
    private String punishmentType; //行政处罚类型

    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "不合法的金额格式")
    @Excel(name = "罚金")
    private String fine;

    @Excel(name = "行政处罚机关名称")
    @Size(max = 64, message = "机关名称长度不能超过64")
    private String organName; //行政处罚机关名称

    @Excel(name = "省份")
    @Size(max = 64, message = "省份长度不能超过64")
    private String province;

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Excel(name = "行政处罚日期")
    private String date;  //行政处罚日期

    @Pattern(regexp = "^(0|1)$", message = "不存在的状态")
    @Excel(name = "发布状态")
    private String status; //发布状态('0':未发布|'1':已发布)
}
