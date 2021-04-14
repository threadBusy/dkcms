package cc.dkcms.cms.common.vo;

import cc.dkcms.cms.common.helper.UrlHelper;
import cc.dkcms.cms.common.util.CmsUtils;
import lombok.Data;

import java.util.List;

@Data
public class TagVo {
    private Integer          id;
    private String           tagName;
    private Byte             tagType;
    private String           tagUrl;
    private List<CategoryVo> categoryVoList;
    private List<ArticleVo>  articleVoList;

    public String getTagUrl() {
        return UrlHelper.getTagListPageUrl(this, CmsUtils.isPreviewMode());
    }
}
