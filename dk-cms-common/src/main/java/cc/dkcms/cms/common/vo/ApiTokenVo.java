package cc.dkcms.cms.common.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ApiTokenVo {
    private Integer id;
    private String  name;
    private String  token;
    private Date    createdAt;

    private Date expiredAt;

    private Boolean isExpired;

    private Byte isValid;

    private Integer managerId;

    public Boolean getIsExpired() {
        if (expiredAt == null) {
            return true;
        }
        return (expiredAt.compareTo(new Date()) < 0);
    }


}
