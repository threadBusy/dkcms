package cc.dkcms.cms.template.directive.front.thymeleaf;

import cc.dkcms.cms.common.util.CmsUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templatemode.TemplateMode;

public class HomePageUrl extends AbstractPageTagProcessor {
    private static final String ATTR_NAME = "homePageUrl";

    public HomePageUrl(final String dialectPrefix) {

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
        String url = CmsUtils.isPreviewMode() ? "/site" : "/index.html";
        handler.setAttribute("href", url);
    }

}