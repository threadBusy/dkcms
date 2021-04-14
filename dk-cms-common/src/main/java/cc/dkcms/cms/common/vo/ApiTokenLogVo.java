package cc.dkcms.cms.common.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ApiTokenLogVo {

    private Integer id;
    private String  token;

    private String apiName;

    private Date createdAt;

    private String parameters;
    private String result;
}
