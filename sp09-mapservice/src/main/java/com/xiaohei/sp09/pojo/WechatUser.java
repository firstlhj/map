package com.xiaohei.sp09.pojo;

import lombok.Getter;
import lombok.Setter;




public class WechatUser extends BaseEntity {

    @Getter
    @Setter
    private String openId;

    @Getter
    @Setter
    private String nickname;

    @Getter
    @Setter
    private String avatar;

    @Getter
    @Setter
    private String country;

    @Getter
    @Setter
    private String province;

    @Getter
    @Setter
    private String city;
}
