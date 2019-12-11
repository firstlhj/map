package com.xiaohei.sp09.pojo;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
public class Order extends BaseEntity {

    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private String wechatOpenId;

    @Getter
    @Setter
    @Size(max = 32)
    private String productId;

    @Getter
    @Setter
    private String productName;

    @Getter
    @Setter
    private BigDecimal amount;

    @Getter
    @Setter
    private Integer status;

    @Getter
    @Setter
    private LocalDateTime payTime;

    @Getter
    @Setter
    private String remark;
}
