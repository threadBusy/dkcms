package cc.dkcms.cms.template.directive.front.thymeleaf;

import cc.dkcms.cms.common.vo.SinglePageVo;
import cc.dkcms.cms.service.ServiceSinglePage;
import com.jfinal.aop.Aop;
import org.apache.commons.lang.StringUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templatemode.TemplateMode;

public class SinglePageUrl extends AbstractPageTagProcessor {
    private static final String ATTR_NAME = "pageUrl";

    public SinglePageUrl(final String dialectPrefix) {

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


        String strPageId = tag.getAttributeValue("cms:pageUrl");
        if (StringUtils.isEmpty(strPageId)) {
            error(handler, "pageIdMissing");
            return;
        }
        try {
            Integer           pageId            = Integer.valueOf(strPageId);
            ServiceSinglePage singlePageService = Aop.get(ServiceSinglePage.class);
            if (singlePageService == null) {
                error(handler, "spring getBean return null");
                return;
            }
            SinglePageVo vo = singlePageService.getPage(pageId);
            if (vo == null) {
                error(handler, "pageId return null:" + pageId);
                return;
            }
            handler.setAttribute("href", vo.getPageUrl());


        } catch (Exception e) {
            error(handler, "pageIdParseError:" + strPageId);

        }

    }

    private void error(IElementTagStructureHandler handler, String msg) {
        handler.setAttribute("data-error", msg);

    }
}
