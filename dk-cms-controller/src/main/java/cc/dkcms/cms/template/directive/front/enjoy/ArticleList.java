package cc.dkcms.cms.template.directive.front.enjoy;

import cc.dkcms.cms.common.Constant;
import cc.dkcms.cms.common.define.ContentListQueryParam;
import cc.dkcms.cms.common.vo.ArticleVo;
import cc.dkcms.cms.service.ServiceContent;
import cc.dkcms.cms.template.directive.front.enjoy.base.BaseEnjoyTemplateDirective;
import cc.dkcms.cms.template.render.DkCmsRenderContext;
import com.jfinal.aop.Aop;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * #contentList(
 * categoryId,
 * withChild,
 * orderBy,
 * orderSort,
 * page,
 * pageSize,
 * isRecommend,
 * hasCoverImage,
 * keyword,
 * keywordField,
 * isKeywordLike
 * )
 */

public class ArticleList extends BaseEnjoyTemplateDirective {

    private static final String ATTR_NAME = "contentList";

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


    @Override
    public boolean hasEnd() {
        return true;
    }

    @Override
    public void exec(Env env, Scope scope, Writer writer) {


        DkCmsRenderContext dkCmsRenderContext = (DkCmsRenderContext) scope.get("dkRenderContext");

        Map<String, Object> map = getMap(scope);

        Integer categoryId    = getIntFromMap(map, "categoryId", -1);
        Boolean withChild     = getBoolFromMap(map, "withChild", true);
        String  orderBy       = getStrFromMap(map, "orderBy", "id");
        String  orderSort     = getStrFromMap(map, "orderSort", "desc");
        Integer page          = getIntFromMap(map, "page", 1);
        Integer pageSize      = getIntFromMap(map, "pageSize", Constant.PAGE_SIZE);
        Integer limit         = getIntFromMap(map, "limit", -1);
        Boolean isRecommend   = getBoolFromMap(map, "isRecommend", false);
        Boolean hasCoverImage = getBoolFromMap(map, "hasCoverImage", false);
        String  keyword       = getStrFromMap(map, "keyword", "");
        String  keywordField  = getStrFromMap(map, "keywordField", "");
        Boolean isKeywordLike = getBoolFromMap(map, "isKeywordLike", false);
        Boolean needNavPage   = getBoolFromMap(map, "needNavPage", false);


        if (categoryId < 0) {
            categoryId = tryGuessCategoryId(scope);
            if (categoryId == null || categoryId < 0) {
                failMsg(writer, "articleList not found categoryId");
                return;
            }
        }


        ContentListQueryParam queryParam = ContentListQueryParam.builder().build();

        queryParam.getIdList().add(categoryId);
        queryParam.setPage(dkCmsRenderContext.getPage());
        if (queryParam.getPage() == null) {
            queryParam.setPage(1);
        }

        //orderBy
        queryParam.setOrderBy(orderBy);
        queryParam.setOrderSort(orderSort);
        queryParam.setPageSize(pageSize);

        // 是否包含子栏目一起，默认是 false
        queryParam.setIncludeSubCate(withChild);

        // 是否推荐
        queryParam.setIsRecommend(isRecommend);

        // 是否有图片
        queryParam.setHasCoverImage(hasCoverImage);


        if (!StringUtils.isEmpty(keyword) && !StringUtils.isEmpty(keywordField)) {

            queryParam.setKeyword(keyword);
            queryParam.setKeywordField(keywordField);
            queryParam.setIsKeywordLikeSearch(isKeywordLike);
            queryParam.setIsNeedDealKeyword(true);
        }

        // 读取文章列表
        ServiceContent  articleService = Aop.get(ServiceContent.class);
        Page<ArticleVo> pageInfo       = articleService.getPage(queryParam);


        int index = 0;
        for (ArticleVo vo : pageInfo.getList()) {
            scope.set("item", vo);
            scope.set("index", index);
            stat.exec(env, scope, writer);
            if (limit > 0 && index >= (limit-1)) {
                break;
            }
            index++;
        }


        // 默认情况下，主要数据都在context里设置好了，一般情况下各种标签，不要改变
        // 除非有这个配置，articleList 标签，可以修改pageNav

        //if (needNavPage) {
        dkCmsRenderContext.setArticleListPageInfo(pageInfo);
        //}
    }
}
