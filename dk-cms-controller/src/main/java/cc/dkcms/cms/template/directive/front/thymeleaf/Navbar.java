package cc.dkcms.cms.template.directive.front.thymeleaf;


import cc.dkcms.cms.common.render.DkCmsRenderPageType;
import cc.dkcms.cms.common.vo.ArticleVo;
import cc.dkcms.cms.common.vo.CategoryVo;
import cc.dkcms.cms.service.ServiceCategory;
import cc.dkcms.cms.template.render.DkCmsRenderContext;
import com.jfinal.aop.Aop;
import org.apache.commons.lang.StringUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Navbar extends AbstractPageTagProcessor {

    private static final String ATTR_NAME = "navbar";

    public Navbar(final String dialectPrefix) {

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


        // 获取所有class style 之类，原封不动输出
        String attributeString = getAllAttrbutesString(tag);


        DkCmsRenderContext dkCmsRenderContext = (DkCmsRenderContext) context.getVariable("dkRenderContext");

        // 确定 categoryId;
        Integer categoryId = tryGetCategoryId(dkCmsRenderContext);

        if (categoryId == null) {
            structureHandler.replaceWith("<div data-generator-by-cms>categoryId missing</div>", false);
            return;
        }


        DkCmsRenderPageType contextType = dkCmsRenderContext.getPageType();


        ServiceCategory categoryService = Aop.get(ServiceCategory.class);

        if (categoryService == null) {
            structureHandler.replaceWith("<div data-generator-by-cms>get bean is null</div>", false);
            return;
        }

        List<String> navList = new LinkedList<>();

        // 如果是文章页，先把文章标题放在最后
        if (contextType.equals(DkCmsRenderPageType.CATEGORY_ARTICLE)) {
            ArticleVo articleVo = dkCmsRenderContext.getArticleVo();
            navList.add("<a href='javascript:;'>" + articleVo.getTitle() + "</a>");
        }

        while (categoryId > 0) {
            CategoryVo    categoryVo = categoryService.getCategoryById(categoryId);
            StringBuilder sb         = new StringBuilder("\n");

            sb.append("<a href='");
            sb.append(categoryVo.getCategoryUrl());
            sb.append("'>");
            sb.append(categoryVo.getCategoryName());
            sb.append("</a>");
            navList.add(sb.toString());

            categoryId = categoryVo.getParentId();
        }
        navList.add("<a href='index.html'>首页</a>");

        Collections.reverse(navList);


        structureHandler.replaceWith(StringUtils.join(navList, '\n'), false);


    }
}
