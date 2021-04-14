package cc.dkcms.cms.template.render;

import cc.dkcms.cms.common.Constant;
import cc.dkcms.cms.common.define.CategoryType;
import cc.dkcms.cms.common.helper.FileNameHelper;
import cc.dkcms.cms.common.helper.UrlHelper;
import cc.dkcms.cms.common.render.DkCmsRenderPageType;
import cc.dkcms.cms.common.render.TemplateType;
import cc.dkcms.cms.common.util.CmsUtils;
import cc.dkcms.cms.common.vo.*;
import cc.dkcms.cms.service.ServiceTemplateFile;
import com.jfinal.aop.Aop;
import com.jfinal.plugin.activerecord.Page;
import lombok.Data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


@Data
public class DkCmsRenderContext {


    //private Map<String, Object> data = new ConcurrentHashMap<>();

    public DkCmsRenderContext(DkCmsRenderPageType pageType) {
        this.pageType = pageType;

    }

    /*
    public void setVariable(String key, Object val) {
        this.data.put(key, val);
    }*/


    //private DkCmsRenderPageType contextType;

    private Boolean flagNeedMakeList = false;

    private DkCmsRenderPageType pageType;
    private Map<String, Object> global;

    // 不同的context情况下， 用到了不同的变量
    private Integer categoryId;
    private Integer articleId;
    private Integer page;
    private Integer pageSize; // list页也用到

    private ArticleVo  articleVo;
    private CategoryVo categoryVo;

    private Page<ArticleVo> articleListPageInfo;

    private Integer      singlePageId;
    private SinglePageVo singlePageVo;

    private LinkedList<String> siteMapUrls;


    private String keyword;

    // tag list page
    private Integer tagId;
    private TagVo   tagVo;


    @Override
    public String toString() {
        return String.format("typ:%s|cid:%s|aid:%s|page:%s|spid:%s",
                pageType, categoryId, articleId, page, singlePageId);
    }


    /**
     * 计算此context 最终确定的url
     * 每个context 代表了一个具体的页面渲染任务，肯定能唯一确定一个url
     * //TODO ,应该进入 ContextHandler
     *
     * @return
     */
    public String getFinalUrl() {
        boolean isPreview = CmsUtils.isPreviewMode();
        switch (pageType) {
            case SITE_HOMEPAGE:
                return UrlHelper.getSiteHomePage(isPreview);
            case SINGLE_PAGE:
                return UrlHelper.getSinglePageUrl(getSinglePageVo(), isPreview);
            case CATEGORY_LIST:
                return UrlHelper.getCategoryListUrl(getCategoryVo(), getPage(), isPreview);
            case CATEGORY_HOME:
                return UrlHelper.getCategoryHomeUrl(getCategoryVo(), isPreview);
            case CATEGORY_ARTICLE:
                return UrlHelper.getCategoryArticleUrl(getArticleVo(), getCategoryVo(), isPreview);
            case ARTICLE_LIST_BY_TAG:
                return UrlHelper.getTagListPageUrl(tagVo, isPreview);
            default:
                return UrlHelper.getSiteHomePage(isPreview);
        }
    }


    /**
     * 计算当前context 静态文件的路径
     *
     * @return
     */
    public String getStaticPageFilePath() {


        String file = null;
        switch (pageType) {
            case SITE_HOMEPAGE:
                file = FileNameHelper.getHomePageFileName();
                break;
            case SINGLE_PAGE:
                file = FileNameHelper.getSinglePageFileName(getSinglePageVo());
                break;
            case CATEGORY_LIST:
                file = FileNameHelper.getCategoryListFileName(getCategoryVo(), getPage());
                break;
            case CATEGORY_HOME:
                file = FileNameHelper.getCategoryFileName(getCategoryVo());
                break;
            case CATEGORY_ARTICLE:
                file = FileNameHelper.getCategoryArticleFileName(getArticleVo(), getCategoryVo());
                break;
            case SEARCH_RESULT:
                file = "search-" + getKeyword() + ".html";
                break;
            case ARTICLE_LIST_BY_TAG:
                file = FileNameHelper.getArticleListByTagFileName(tagVo);
        }
        return Constant.PATH_WEBROOT + "/" + file;
    }


    /**
     * 计算当前context应该渲染的模板
     *
     * @return
     */
    public DkCmsRenderTemplate getTemplateInfo() {

        DkCmsRenderTemplate template = new DkCmsRenderTemplate();


        TemplateVo   templateVo   = Aop.get(ServiceTemplateFile.class).getCurrentTemplateVo();
        TemplateType templateType = TemplateType.getTemplateType(templateVo.getType());


        template.setTemplateType(templateType);
        // vo中的name 是中文名
        // dktemplate 中的name是path
        template.setTemplateName(templateVo.getTemplatePath());
        template.setTemplateBase(Constant.PATH_TEMPLATE + templateVo.getTemplatePath());

        switch (pageType) {
            case SITE_HOMEPAGE:
                template.setTemplateFile("index.html");
                return template;
            case SINGLE_PAGE:
                template.setTemplateFile(singlePageVo.getTemplate());
                return template;
            case CATEGORY_LIST:
                template.setTemplateFile(categoryVo.getTemplateList());
                return template;
            case CATEGORY_HOME:
                template.setTemplateFile(categoryVo.getTemplateCate());
                return template;
            case CATEGORY_ARTICLE:
                template.setTemplateFile(categoryVo.getTemplateContent());
                return template;
            case ARTICLE_LIST_BY_TAG:
                template.setTemplateFile("tag.html");
                return template;
            case SEARCH_RESULT:
                template.setTemplateFile("search.html");
                return template;
            default:
                template.setTemplateFile("index.html");
                return template;
        }

    }


    /**
     * 模板默认值
     *
     * @return
     */
    // 如果栏目设定的模板不存在或者未指定模板的时候，使用这个模板，这个模板应该是 模板文件包里带的
    public String getDefaultTemplateFileName() {

        switch (pageType) {
            case SITE_HOMEPAGE:
                return "index.html";
            case SINGLE_PAGE:
                return "page.html";

            case CATEGORY_LIST:
                return "list.html";

            case CATEGORY_HOME:
                if (categoryVo != null && categoryVo.getCategoryType().equals(CategoryType.ALBUM.getType())) {
                    return "album.html";
                }
                return "category.html";

            // return serviceSetting.getValue("templateCategoryPage");
            case CATEGORY_ARTICLE:

                if (categoryVo != null && categoryVo.getCategoryType().equals(CategoryType.ALBUM.getType())) {
                    return "album-content.html";
                }

                return "content.html";

            case ARTICLE_LIST_BY_TAG:
                return "tag.html";

            case SEARCH_RESULT:
                return "search.html";

            //return serviceSetting.getValue("templateContentPage");
            default:
                return "index.html";
            //return serviceSetting.getValue("templateSiteHome");
        }
    }


    // 这些变量都会在模中默认出现
    public Map<String, Object> getDataForRender() {
        Map<String, Object> data = new HashMap<>();
        data.put("dkRenderContext", this);
        data.put("global", global);

        //data.put("contextType", pageType);
        data.put("pageType", pageType.name());


        data.put("categoryId", categoryId);
        data.put("articleId", articleId);
        data.put("page", page);

        data.put("articleVo", articleVo);
        data.put("article", articleVo);

        data.put("categoryVo", categoryVo);
        data.put("category", categoryVo);

        data.put("articleListPageInfo", articleListPageInfo);


        data.put("singlePageId", singlePageId);

        if (singlePageVo != null) {
            data.put("singlePageVo", singlePageVo);
            data.put("singlePage", singlePageVo);
            data.put("page", singlePageVo);
        }
        data.put("siteMapUrls", siteMapUrls);

        data.put("tagId", tagId);
        data.put("tagVo", tagVo);
        data.put("searchKeyword", keyword);
        if (articleListPageInfo != null) {
            data.put("articleList", articleListPageInfo.getList());
        }

        return data;
    }


}
