package cc.dkcms.cms.common.vo;

import lombok.Data;

@Data
public class CategoryVoForList {
    private Integer id;
    private String  categoryName;
    private Integer parentId;
    private String  parentName;
    private String  permalink;
    private String  categoryUrl;
    private Integer totalCount;
    private Integer childCount;
    private Integer sort;
    private Integer level;
    private Integer categoryType;
    private String  categoryTypeString;

}
