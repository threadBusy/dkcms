package cc.dkcms.cms.common.vo;

import lombok.Data;

@Data
public class ArticleSimpleVo {
    private Integer id;
    private String  title;
    private String  author;
    private String  coverImage;
    private String  articleUrl;
}
