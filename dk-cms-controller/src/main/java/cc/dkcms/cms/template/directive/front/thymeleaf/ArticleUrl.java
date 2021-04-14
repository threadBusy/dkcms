package cc.dkcms.cms.template.directive.front.thymeleaf;


import cc.dkcms.cms.common.vo.ArticleVo;
import cc.dkcms.cms.service.ServiceContent;
import com.jfinal.aop.Aop;
import org.apache.commons.lang.StringUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templatemode.TemplateMode;

public class ArticleUrl extends AbstractPageTagProcessor {
    private static final String ATTR_NAME = "articleUrl";

    public ArticleUrl(final String dialectPrefix) {

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
                             IElementTagStructureHandler handler) {


        String strArticleId = tag.getAttributeValue("cms:articleUrl");
        if (StringUtils.isEmpty(strArticleId)) {
            error(handler, "strArticleIdMissing");
            return;
        }
        try {
            Integer        articleId      = Integer.valueOf(strArticleId);
            ServiceContent articleService = Aop.get(ServiceContent.class);
            if (articleService == null) {
                error(handler, "spring getBean return null");
                return;
            }
            ArticleVo vo = articleService.getArticleVo(articleId);
            if (vo == null) {
                error(handler, "getArticleVoById return null:" + articleId);
                return;
            }
            handler.setAttribute("href", vo.getArticleUrl());


        } catch (Exception e) {
            error(handler, "pageIdParseError:" + strArticleId);

        }

    }

    private void error(IElementTagStructureHandler handler, String msg) {
        handler.setAttribute("data-error", msg);

    }

}