package cc.dkcms.cms.template.directive.front.thymeleaf;

import cc.dkcms.cms.common.define.ContentListQueryParam;
import cc.dkcms.cms.common.render.DkCmsRenderPageType;
import cc.dkcms.cms.common.vo.ArticleVo;
import cc.dkcms.cms.service.ServiceContent;
import cc.dkcms.cms.template.render.DkCmsRenderContext;
import com.jfinal.aop.Aop;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang.StringUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templatemode.TemplateMode;

public class ArticleList extends AbstractPageTagProcessor {

    private static final String ATTR_NAME = "articleList";

    private static final String ATTR_CATEGORY_ID     = "categoryId";
    private static final String ATTR_VAR_NAME        = "varName";
    private static final String ATTR_ORDER_BY        = "orderBy";
    private static final String ATTR_ORDER_SORT      = "orderSort";
    private static final String ATTR_PER_PAGE        = "perPage";
    private static final String ATTR_INCLUDE_SUB     = "includeSubCategory";
    private static final String ATTR_IS_RECOMMEND    = "isRecommend";
    private static final String ATTR_HAS_COVER_IMAGE = "hasCoverImage";

    private static final String ATTR_KEYWORD       = "keyword";
    private static final String ATTR_KEYWORD_FIELD = "keywordField";
    private static final String ATTR_KEYWORD_LIKE  = "keywordLike";


    public ArticleList(final String dialectPrefix) {

        super(TemplateMode.HTML,
                dialectPrefix,
                null,
                false,
                ATTR_NAME,
                true,
                StandardDialect.PROCESSOR_PRECEDENCE,
                true);
    }

    @Override
    protected void doProcess(ITemplateContext context,
                             IProcessableElementTag tag,
                             AttributeName attributeName,
                             String attributeValue,
                             IElementTagStructureHandler structureHandler) {




        DkCmsRenderContext  dkCmsRenderContext = (DkCmsRenderContext) context.getVariable("dkRenderContext");
        DkCmsRenderPageType ctxType            = dkCmsRenderContext.getPageType();


        //默认栏目id，
        // 如果后续没有指定栏目id，就是这个
        Integer defaultCategoryId = null;
        Integer page              = 1;

        if (ctxType.equals(DkCmsRenderPageType.CATEGORY_LIST)) {
            defaultCategoryId = dkCmsRenderContext.getCategoryId();
            page = dkCmsRenderContext.getPage();
        } else if (ctxType.equals(DkCmsRenderPageType.CATEGORY_HOME)) {
            defaultCategoryId = dkCmsRenderContext.getCategoryId();
            page = dkCmsRenderContext.getPage();
        } else if (ctxType.equals(DkCmsRenderPageType.CATEGORY_ARTICLE)) {
            defaultCategoryId = dkCmsRenderContext.getArticleVo().getCategoryId();
            page = dkCmsRenderContext.getPage();
        } else if (ctxType.equals(DkCmsRenderPageType.SITE_HOMEPAGE)) {
            defaultCategoryId = 0;
            page = dkCmsRenderContext.getPage();
        }
        if (page == null) {
            page = 1;
        }

        // 如果html中读取到cid，则用此cid
        // 否则获取上边获取的默认cid
        // 否则 模板报错, cid missing
        String categoryIdInAttr = getAttribute(tag, ATTR_CATEGORY_ID, String.valueOf(defaultCategoryId));
        if (StringUtils.isEmpty(categoryIdInAttr)) {
            structureHandler.replaceWith("<div data-generator-by-cms>categoryId missing</div>", false);
            return;
        }
        Integer categoryId = null;
        try {
            categoryId = Integer.valueOf(String.valueOf(getExpressionValue(context, categoryIdInAttr)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (categoryId == null) {
            structureHandler.replaceWith("<div data-generator-by-cms>categoryId null</div>", false);
            return;
        }

        ContentListQueryParam queryParam = ContentListQueryParam.builder().build();

        queryParam.getIdList().add(categoryId);
        queryParam.setPage(page);


        //orderBy
        queryParam.setOrderBy(getAttribute(tag, ATTR_ORDER_BY, "publish_date"));
        queryParam.setOrderSort(getAttribute(tag, ATTR_ORDER_SORT, "desc"));
        queryParam.setPageSize(getIntAttribute(tag, ATTR_PER_PAGE, 10));

        // 是否包含子栏目一起，默认是 false
        String includeSubAttr = getAttribute(tag, ATTR_INCLUDE_SUB, "false");
        if (!includeSubAttr.equals("true")) {
            includeSubAttr = "false";
        }
        queryParam.setIncludeSubCate(Boolean.valueOf(includeSubAttr));

        // 是否推荐
        String isRecommendAttr = getAttribute(tag, ATTR_IS_RECOMMEND, "false");
        if (!isRecommendAttr.equals("true")) {
            isRecommendAttr = "false";
        }
        queryParam.setIsRecommend(Boolean.valueOf(isRecommendAttr));

        // 是否有图片
        String hasCoverAttr = getAttribute(tag, ATTR_HAS_COVER_IMAGE, "false");
        if (!hasCoverAttr.equals("true")) {
            hasCoverAttr = "false";
        }
        queryParam.setHasCoverImage(Boolean.valueOf(hasCoverAttr));

        // keyword 相关
        String keyword      = getAttribute(tag, ATTR_KEYWORD, null);
        String keywordField = getAttribute(tag, ATTR_KEYWORD_FIELD, null);
        if (!StringUtils.isEmpty(keyword) && !StringUtils.isEmpty(keywordField)) {
            String keywordValue      = String.valueOf(getExpressionValue(context, keyword));
            String keywordFieldValue = String.valueOf(getExpressionValue(context, keywordField));

            Boolean isLikeSearch = getBoolAttribute(tag, ATTR_KEYWORD_LIKE, false);
            queryParam.setKeyword(keywordValue);
            queryParam.setKeywordField(keywordFieldValue);
            queryParam.setIsKeywordLikeSearch(isLikeSearch);
            queryParam.setIsNeedDealKeyword(true);
        }

        // 读取文章列表
        ServiceContent  articleService = Aop.get(ServiceContent.class);
        Page<ArticleVo> pageInfo       = articleService.getPage(queryParam);


        String varName = getAttribute(tag, ATTR_VAR_NAME, "item");

        structureHandler.removeAttribute(ATTR_INCLUDE_SUB);

        // 迭代
        structureHandler.iterateElement(varName, "status", pageInfo.getList());

        //set PageInfo for PageNav

        dkCmsRenderContext.setArticleListPageInfo(pageInfo);


    }
}
