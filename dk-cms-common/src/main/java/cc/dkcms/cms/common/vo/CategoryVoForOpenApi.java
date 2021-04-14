package cc.dkcms.cms.common.vo;

import lombok.Data;

@Data
public class CategoryVoForOpenApi {
    private Integer id;
    private String  categoryName;
    private Integer parentId;
    private String  parentName;
    private String  permalink;
    private String  categoryUrl;
    private Integer totalCount;
    private String  templateCate;
    private String  templateList;
    private String  templateContent;
    private String  coverImage;
}