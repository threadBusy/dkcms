package cc.dkcms.cms.common.vo;


import cc.dkcms.cms.common.helper.UrlHelper;
import cc.dkcms.cms.common.util.CmsUtils;
import cc.dkcms.cms.common.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class ArticleVo {

    private Integer id;

    private String permalink;

    private Integer    categoryId;
    private String     categoryName;
    private CategoryVo categoryVo;

    private String title;

    /**
     * 发布时间
     */
    private Date publishDate;

    /**
     * 摘要
     */
    private String summary;

    private String titleColor;

    /**
     * 点击量
     */
    private Integer click;

    /**
     * 推荐
     */
    private Byte isRecommend;

    /**
     * 是否前台显示
     */
    private Byte isShow;

    /**
     * 是否进入回收站
     */
    private Byte isDelete;

    private String keywords;

    /**
     * 封面配图
     */
    private String coverImage;
    private String coverImageUrl;
    private String coverImageSize;

    /**
     * 作者
     */
    private String author;

    /**
     * 来源
     */
    private String source;

    /**
     * 发布管理员id
     */
    private Integer managerId;

    private String content;

    private String articleUrl;


    private String              extJson;
    private Map<String, String> ext;

    private Integer sort;

    private String tags;

    // 上一篇
    private ArticleVo previousArticle;
    // 下一篇
    private ArticleVo nextArticle;


    public String getSummary() {

        if (!StringUtils.isEmpty(summary)) {
            return summary;
        }
        return ArticleVo.getSummary(content);

    }

    public Map<String, String> getExt() {

        if (StringUtils.isEmpty(extJson)) {
            return new HashMap<>();
        }
        return JsonUtil.readValue(getExtJson(), new TypeReference<Map<String, String>>() {
        });
    }

    public String getArticleUrl() {
        return UrlHelper.getCategoryArticleUrl(this, this.categoryVo, CmsUtils.isPreviewMode());
    }

    private static String getSummary(String content) {

        if (StringUtils.isEmpty(content)) {
            return "";
        }

        int length = 200;

        content = content.replaceAll("<.*?>", "");
        content = content.replaceAll("&\\w+;", "");
        //content = StringUtils.deleteWhitespace(content);
        if (StringUtils.isEmpty(content)) {
            return "";
        }
        if (content.length() <= length) {
            return content;
        }
        return content.substring(0, length);


    }
}
