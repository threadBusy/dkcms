package cc.dkcms.cms.common.vo;

import lombok.Data;

@Data
public class AccountVo {
    private Integer id;
    private String username;
    private String password;
    private Boolean enable;
    private java.util.Date regTime;
    private Long loginTimes;
    private java.util.Date lastLoginAt;
    private String lastIp;
    private Integer role;
    private String realname;
    private String telephone;
    private String email;
    private String gender;
    private String age;
    private String country;
    private String province;
    private String city;
    private String county;
    private String logo;
    private Integer storeId;
}
