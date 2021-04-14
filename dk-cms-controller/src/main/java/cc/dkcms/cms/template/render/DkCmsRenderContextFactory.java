package cc.dkcms.cms.template.render;

import cc.dkcms.cms.common.Constant;
import cc.dkcms.cms.common.define.ContentListQueryParam;
import cc.dkcms.cms.common.render.DkCmsRenderPageType;
import cc.dkcms.cms.common.vo.ArticleVo;
import cc.dkcms.cms.common.vo.CategoryVo;
import cc.dkcms.cms.common.vo.SinglePageVo;
import cc.dkcms.cms.service.*;
import com.jfinal.aop.Aop;
import com.jfinal.core.Controller;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static cc.dkcms.cms.common.Constant.ROOT_CATEGORY_ID;

public class DkCmsRenderContextFactory {

    private ServiceSetting    serviceSetting    = Aop.get(ServiceSetting.class);
    private ServiceCategory   serviceCategory   = Aop.get(ServiceCategory.class);
    private ServiceSinglePage serviceSinglePage = Aop.get(ServiceSinglePage.class);
    private ServiceContent    serviceContent    = Aop.get(ServiceContent.class);
    private ServiceTag        serviceTag        = Aop.get(ServiceTag.class);
    private Controller        controller;

    /**
     * 传入了controller，就可以在内部获取到page参数，仅此而已
     * 如果这里丢掉这个参数，page需要在controller中获取，构造context的时候传入
     *
     * @param controller
     */
    public DkCmsRenderContextFactory(Controller controller) {
        this.controller = controller;
    }

    public DkCmsRenderContextFactory() {
    }


    private Map<String, Object> getGlobalVars() {

        Map<String, Object> map = new HashMap<>();
        map.put("setting", serviceSetting.getForContext());
        map.put("category", serviceCategory.getChildVoList(0));
        map.put("cmsName", Constant.SOFT_NAME);
        map.put("cmsVersion", Constant.SOFT_VERSION);
        map.put("singlePage", serviceSinglePage.getList());
        map.put("queryString", getQueryString());
        return map;
    }


    private Integer getQueryStringPage() {
        if (controller == null) {
            return 1;
        }
        HttpServletRequest request = controller.getRequest();
        String             pageStr = request.getParameter("page");
        if (StringUtils.isEmpty(pageStr)) {
            return 1;
        }
        try {
            return Integer.parseInt(pageStr);
        } catch (Exception e) {
            return 1;
        }
    }

    private Map<String, String> getQueryString() {
        Map<String, String> result = new HashMap<>();
        if (controller == null) {
            return result;
        }
        HttpServletRequest request = controller.getRequest();
        request.getParameterMap().forEach((k, v) -> {
            if (v != null && v.length > 0) {
                result.put(k, v[0]);
            }
        });
        return result;


    }

    private DkCmsRenderContext __getBaseContext(DkCmsRenderPageType contextType) {
        DkCmsRenderContext cmsContext = new DkCmsRenderContext(contextType);
        cmsContext.setGlobal(getGlobalVars());
        return cmsContext;
    }

    /**
     * 首页
     */
    public DkCmsRenderContext getHomePageContext() {
        DkCmsRenderContext context = __getBaseContext(DkCmsRenderPageType.SITE_HOMEPAGE);
        context.setPage(getQueryStringPage());
        context.setCategoryVo(serviceCategory.getRootVo());
        context.setCategoryId(ROOT_CATEGORY_ID);
        return context;
    }


    public DkCmsRenderContext getCategoryArticleContext(Integer articleId) {
        ArticleVo articleVo = serviceContent.getArticleVo(articleId);
        return getCategoryArticleContext(articleVo);
    }

    public DkCmsRenderContext getCategoryArticleContext(ArticleVo articleVo) {
        DkCmsRenderContext cmsContext = __getBaseContext(DkCmsRenderPageType.CATEGORY_ARTICLE);
        Integer            categoryId = 0;
        CategoryVo         categoryVo = CategoryVo.NO_CATEGORY;

        if (articleVo.getCategoryVo() != null) {
            categoryId = articleVo.getCategoryId();
            categoryVo = articleVo.getCategoryVo();
        }
        serviceContent.setNeighborContent(articleVo);

        cmsContext.setArticleVo(articleVo);
        cmsContext.setArticleId(articleVo.getId());
        cmsContext.setCategoryId(categoryId);
        cmsContext.setCategoryVo(categoryVo);

        /*
        cmsContext.setVariable("currentCategoryId", categoryId);
        cmsContext.setVariable("currentCategoryVo", categoryVo);
        cmsContext.setVariable("categoryId", categoryId);
        cmsContext.setVariable("category", categoryVo);
        cmsContext.setVariable("articleId", articleVo.getId());
        cmsContext.setVariable("article", articleVo);
        */
        return cmsContext;
    }


    public DkCmsRenderContext getCategoryListContext(Integer categoryId, Integer page, Integer pageSize) {
        // 在cateHome的基础上，增加page变量
        DkCmsRenderContext cmsContext = getCategoryHomeContext(categoryId);
        cmsContext.setPageType(DkCmsRenderPageType.CATEGORY_LIST);
        if (page == null) {
            page = 1;
        }
        cmsContext.setPage(page);

        ContentListQueryParam contentListQueryParam = ContentListQueryParam.builder()
                .page(page)
                .pageSize(pageSize)
                .includeSubCate(true)
                .build();
        contentListQueryParam.getIdList().add(categoryId);


        cmsContext.setArticleListPageInfo(serviceContent.getPage(contentListQueryParam));


        return cmsContext;
    }

    public DkCmsRenderContext getCategoryHomeContext(Integer categoryId) {
        DkCmsRenderContext cmsContext = __getBaseContext(DkCmsRenderPageType.CATEGORY_HOME);
        CategoryVo         vo         = serviceCategory.getCategoryById(categoryId);
        cmsContext.setCategoryId(categoryId);
        cmsContext.setCategoryVo(vo);
        cmsContext.setPage(getQueryStringPage());


        /*
        cmsContext.setVariable("currentCategoryId", categoryId);
        cmsContext.setVariable("currentCategoryVo", vo);

        cmsContext.setVariable("categoryId", categoryId);
        cmsContext.setVariable("category", vo);
        */

        return cmsContext;
    }


    public DkCmsRenderContext getSinglePageContext(SinglePageVo vo) {
        DkCmsRenderContext cmsContext = __getBaseContext(DkCmsRenderPageType.SINGLE_PAGE);
        cmsContext.setSinglePageId(vo.getId());
        cmsContext.setSinglePageVo(vo);
        /*
        cmsContext.setVariable("singlePageId", vo.getId());
        cmsContext.setVariable("singlePageVo", vo);
        cmsContext.setVariable("singlePage", vo);
        cmsContext.setVariable("page", vo);
        */

        return cmsContext;
    }

    public DkCmsRenderContext getSinglePageContext(Integer pageId) {
        SinglePageVo vo = serviceSinglePage.getPage(pageId);
        return getSinglePageContext(vo);
    }

    public DkCmsRenderContext getSiteMapContext() {
        return __getBaseContext(DkCmsRenderPageType.SITE_MAP);
    }

    public DkCmsRenderContext getTagArticleListPageContext(Integer tagId) {
        DkCmsRenderContext context = __getBaseContext(DkCmsRenderPageType.ARTICLE_LIST_BY_TAG);
        context.setTagId(tagId);
        context.setTagVo(serviceTag.getTag(tagId));

        ContentListQueryParam contentListQueryParam = ContentListQueryParam.builder()
                .tagId(tagId)
                .page(1)
                .pageSize(10)
                .build();


        context.setArticleListPageInfo(serviceContent.getPage(contentListQueryParam));
        return context;
    }

    public DkCmsRenderContext getSearchPageContext(String keyword) {
        DkCmsRenderContext context = __getBaseContext(DkCmsRenderPageType.SEARCH_RESULT);
        context.setKeyword(keyword);

        ContentListQueryParam contentListQueryParam = ContentListQueryParam.builder()
                .page(1)
                .pageSize(10)
                .isNeedDealKeyword(true)
                .keyword(keyword)
                .keywordField("title")
                .isKeywordLikeSearch(true)
                .build();


        context.setArticleListPageInfo(serviceContent.getPage(contentListQueryParam));
        return context;
    }
}
