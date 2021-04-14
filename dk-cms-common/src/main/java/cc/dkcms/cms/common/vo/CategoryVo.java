package cc.dkcms.cms.common.vo;

import cc.dkcms.cms.common.define.CategoryType;
import cc.dkcms.cms.common.helper.UrlHelper;
import cc.dkcms.cms.common.util.BeanCopyUtils;
import cc.dkcms.cms.common.util.CmsUtils;
import cc.dkcms.cms.common.util.JsonUtil;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cc.dkcms.cms.common.Constant.ROOT_CATEGORY_ID;


@Data
public class CategoryVo {

    private Integer id;

    private String categoryName;

    private String categoryUrl;

    private Integer sort;

    private String keywords;

    private String description;

    private String content;

    private Integer parentId;

    private String parentName;

    private CategoryVo parentVo;
    private CategoryVo topCategoryVo;

    private Integer categoryType;
    private String  categoryTypeString;

    private List<CategoryVo> child;

    private Integer totalCount;

    private String templateCate;

    private String templateList;

    private String templateContent;

    private Byte enterPage;

    private String tags;


    private String coverImage;


    private String seoDescription;

    private String permalink;

    private Integer level;

    private String              extJson;
    private Map<String, String> ext;


    public Map<String, String> getExt() {
        if (StringUtils.isEmpty(extJson)) {
            return new HashMap<>();
        }

        return JsonUtil.mySonDecode(extJson);

    }

    // 默认是文章内容
    public Integer getCategoryType() {
        if (categoryType == null) {


            /// TODO  TODO
            /// TODO  TODO
            /// TODO  TODO
            /// TODO  TODO
            /// TODO  TODO
            /// TODO  TODO
            /// TODO  TODO
            /// TODO  TODO

            return null;
            //return CategoryType.ARTICLE.getType();
        }
        return categoryType;
    }

    public CategoryVo getTopCategoryVo() {
        CategoryVo top = parentVo;
        while (true) {
            if (top == null) {
                return null;
            }
            if (top.parentVo != null && top.parentVo.getId().equals(ROOT_CATEGORY_ID)) {
                return top;
            }
            top = top.parentVo;
        }

    }


    public String toString() {
        return String.format("{name:%s|id:%s|pid:%s|pName:%s|childSize:%s}",
                categoryName,
                id, parentId, parentName, (child == null) ? "null" : child.size());
    }


    public String getCategoryUrl() {
        return UrlHelper.getCategoryHomeUrl(this, CmsUtils.isPreviewMode());
    }

    public CategoryVoForOpenApi getSimpleVo() {
        CategoryVoForOpenApi vo = new CategoryVoForOpenApi();
        BeanCopyUtils.copy(this, vo);
        return vo;
    }

    public CategoryVoForList getVoForList() {
        CategoryVoForList vo = new CategoryVoForList();
        BeanCopyUtils.copy(this, vo);
        vo.setChildCount(this.getChild().size());
        return vo;
    }

    public static CategoryVo NO_CATEGORY = new CategoryVo();

    static {
        NO_CATEGORY.setParentId(0);
        NO_CATEGORY.setCategoryType(CategoryType.ARTICLE.getType());
        NO_CATEGORY.setTotalCount(0);
        NO_CATEGORY.setCategoryName("默认类目");
        NO_CATEGORY.setTemplateContent("content.html");
    }
}
