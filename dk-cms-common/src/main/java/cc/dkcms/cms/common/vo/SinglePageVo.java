package cc.dkcms.cms.common.vo;

import cc.dkcms.cms.common.helper.UrlHelper;
import cc.dkcms.cms.common.util.CmsUtils;
import lombok.Data;

import java.util.Date;

@Data
public class SinglePageVo {

    private Integer id;
    private String  permalink;
    private String  name;
    private String  template;
    private String  content;
    private Date    createdAt;

    private String seoDescription;

    private String seoKeywords;

    private Byte isDelete;

    private String pageUrl;

    public String getPageUrl() {
        return UrlHelper.getSinglePageUrl(this, CmsUtils.isPreviewMode());
    }


}
