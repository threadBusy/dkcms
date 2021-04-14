package cc.dkcms.cms.template.directive.front.enjoy;


import cc.dkcms.cms.common.helper.UrlHelper;
import cc.dkcms.cms.common.render.DkCmsRenderPageType;
import cc.dkcms.cms.common.util.CmsUtils;
import cc.dkcms.cms.common.vo.ArticleVo;
import cc.dkcms.cms.common.vo.CategoryVo;
import cc.dkcms.cms.service.ServiceCategory;
import cc.dkcms.cms.template.directive.front.enjoy.base.BaseEnjoyTemplateDirective;
import cc.dkcms.cms.template.render.DkCmsRenderContext;
import com.jfinal.aop.Aop;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Breadcrumb extends BaseEnjoyTemplateDirective {


    @Override
    public void exec(Env env, Scope scope, Writer writer) {


        List<String> navList = new LinkedList<>();

        DkCmsRenderContext  dkCmsRenderContext = getDkCmsRenderContext(scope);
        DkCmsRenderPageType pageType           = dkCmsRenderContext.getPageType();

        Integer categoryId = null;
        // 如果是文章，先把标题加入，cid 是文章的cid
        if (pageType.equals(DkCmsRenderPageType.CATEGORY_ARTICLE)) {
            ArticleVo articleVo = dkCmsRenderContext.getArticleVo();
            navList.add("<a href='javascript:;'>" + articleVo.getTitle() + "</a>");
            categoryId = articleVo.getCategoryId();
        }
        // 如果 栏目，cid就是栏目
        if (pageType.equals(DkCmsRenderPageType.CATEGORY_LIST)
                || pageType.equals(DkCmsRenderPageType.CATEGORY_HOME)) {
            categoryId = tryGuessCategoryId(scope);
        }

        // 无论是文章or栏目，追溯栏目到 root
        if (categoryId != null) {
            ServiceCategory categoryService = Aop.get(ServiceCategory.class);

            while (categoryId > 0) {
                CategoryVo vo = categoryService.getCategoryById(categoryId);
                navList.add("<a href='" + vo.getCategoryUrl() + "'>" + vo.getCategoryName() + "</a>");
                categoryId = vo.getParentId();
            }
        }

        if (pageType.equals(DkCmsRenderPageType.SINGLE_PAGE)) {
            navList.add("<a href='javascript:;'>" + dkCmsRenderContext.getSinglePageVo().getName() + "</a>");
        }


        // 加上首页
        navList.add("<a href='" + UrlHelper.getSiteHomePage(CmsUtils.isPreviewMode()) + "'>首页</a>");

        Collections.reverse(navList);

        write(writer, StringUtils.join(navList, "\n"));


    }


}
