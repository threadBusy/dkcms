package cc.dkcms.cms.common.vo;

import lombok.Data;

import java.util.Date;

@Data
public class CommentVo {

    private Integer id;
    private Integer articleId;
    private Integer accountId;
    private Date    publishAt;
    private Boolean isShow;
    private String  content;


    private String nickname;
    private String logo;
    private String publishAtStr;

}



