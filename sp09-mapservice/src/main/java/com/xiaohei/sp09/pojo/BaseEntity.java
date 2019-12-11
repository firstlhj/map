package com.xiaohei.sp09.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;


public class BaseEntity implements Serializable {


    @Size(max = 32)
    @Getter
    @Setter
    private String id;

   
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @Getter
    @Setter
    private LocalDateTime creatTime;
}
