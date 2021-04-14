package cc.dkcms.cms.template.directive.front.thymeleaf;

import cc.dkcms.cms.common.define.TagType;
import cc.dkcms.cms.common.vo.TagVo;
import cc.dkcms.cms.service.ServiceTag;
import com.jfinal.aop.Aop;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.List;

public class TagList extends AbstractPageTagProcessor {
    private static final String ATTR_NAME       = "tagList";
    private static final String ATTR_ORDER_BY   = "orderBy";
    private static final String ATTR_ORDER_SORT = "orderSort";

    public TagList(final String dialectPrefix) {

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


        String type = getAttribute(tag, "type", "content");

        ServiceTag serviceTag = Aop.get(ServiceTag.class);

        List<TagVo> list = serviceTag.getListByType(TagType.getType(type));

        structureHandler.iterateElement("item", "status", list);


    }
}
