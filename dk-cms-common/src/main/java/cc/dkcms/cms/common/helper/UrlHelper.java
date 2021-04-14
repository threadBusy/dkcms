package cc.dkcms.cms.common.helper;


import cc.dkcms.cms.common.vo.ArticleVo;
import cc.dkcms.cms.common.vo.CategoryVo;
import cc.dkcms.cms.common.vo.SinglePageVo;
import cc.dkcms.cms.common.vo.TagVo;

import static cc.dkcms.cms.common.Constant.PREVIEW_URL_PATH;

public class UrlHelper {

    public static String getTagListPageUrl(TagVo vo, Boolean isPreview) {
        if (isPreview) {
            return PREVIEW_URL_PATH + "/tag?tagId=" + vo.getId();
        }
        return "/" + FileNameHelper.getArticleListByTagFileName(vo);
    }

    public static String getSiteHomePage(Boolean isPreview) {
        if (isPreview) {
            return PREVIEW_URL_PATH + "/homepage";

        }
        return "/" + FileNameHelper.getHomePageFileName();
    }


    public static String getCategoryHomeUrl(CategoryVo vo, Boolean isPreview) {

        if (isPreview) {
            return PREVIEW_URL_PATH + "/category?categoryId=" + vo.getId();
        }
        return "/" + FileNameHelper.getCategoryFileName(vo);
    }

    public static String getCategoryListUrl(CategoryVo vo, Integer page, Boolean isPreview) {
        if (isPreview) {
            return PREVIEW_URL_PATH + "/list?categoryId=" + vo.getId() + "&page=" + page;
        }
        return "/" + FileNameHelper.getCategoryListFileName(vo, page);
    }

    public static String getCategoryArticleUrl(ArticleVo vo, CategoryVo categoryVo, Boolean isPreview) {
        if (isPreview) {
            return PREVIEW_URL_PATH + "/content?articleId=" + vo.getId();
        }
        return "/" + FileNameHelper.getCategoryArticleFileName(vo, categoryVo);

    }

    public static String getSinglePageUrl(SinglePageVo vo, Boolean isPreview) {
        if (isPreview) {
            return PREVIEW_URL_PATH + "/page?pageId=" + vo.getId();
        }
        return "/" + FileNameHelper.getSinglePageFileName(vo);

    }

}
