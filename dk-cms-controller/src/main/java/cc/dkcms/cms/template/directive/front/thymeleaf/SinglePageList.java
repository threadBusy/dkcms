package cc.dkcms.cms.template.directive.front.thymeleaf;

import cc.dkcms.cms.common.vo.SinglePageVo;
import cc.dkcms.cms.service.ServiceSinglePage;
import com.jfinal.aop.Aop;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.List;

public class SinglePageList extends AbstractPageTagProcessor {
    private static final String ATTR_NAME = "pageList";

    public SinglePageList(final String dialectPrefix) {

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


        ServiceSinglePage serviceSinglePage = Aop.get(ServiceSinglePage.class);

        List<SinglePageVo> list = serviceSinglePage.getList();

        handler.iterateElement("item","status", list);

    }
}