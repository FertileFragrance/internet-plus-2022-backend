package com.example.springbootinit.VO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataListVO<E> {

    private String listRelatedOperation;

    private Long listRelatedData;

    @Valid
    @NotEmpty(message = "数据列表不能为空")
    private List<E> dataList;

}
