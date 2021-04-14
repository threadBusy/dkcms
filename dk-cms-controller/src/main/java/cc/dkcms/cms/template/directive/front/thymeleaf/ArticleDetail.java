package cc.dkcms.cms.template.directive.front.thymeleaf;

import cc.dkcms.cms.common.vo.ArticleVo;
import cc.dkcms.cms.service.ServiceContent;
import com.jfinal.aop.Aop;
import org.thymeleaf.context.EngineContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templatemode.TemplateMode;

public class ArticleDetail extends AbstractPageTagProcessor {
    private static final String ATTR_NAME     = "article";
    private static final String ATTR_ID       = "articleId";
    private static final String ATTR_VAR_NAME = "varName";

    public ArticleDetail(final String dialectPrefix) {

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


        ServiceContent articleService = Aop.get(ServiceContent.class);
        if (articleService == null) {
            structureHandler.replaceWith("<div>get spring bean error</div>", false);
            return;
        }


        Integer articleId = getIntAttribute(tag, ATTR_ID, 0);
        String  varName   = getAttribute(tag, ATTR_VAR_NAME, "item");

        ArticleVo articleVo = articleService.getArticleVo(articleId);


        structureHandler.removeAttribute(ATTR_VAR_NAME);
        structureHandler.removeAttribute(ATTR_ID);

        ((EngineContext) context).setVariable(varName, articleVo);

    }
}
