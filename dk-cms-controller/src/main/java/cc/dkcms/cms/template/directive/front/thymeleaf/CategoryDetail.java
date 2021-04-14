package cc.dkcms.cms.template.directive.front.thymeleaf;


import cc.dkcms.cms.common.vo.CategoryVo;
import cc.dkcms.cms.service.ServiceCategory;
import com.jfinal.aop.Aop;
import org.thymeleaf.context.EngineContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templatemode.TemplateMode;

public class CategoryDetail extends AbstractPageTagProcessor {



    private static final String ATTR_NAME = "category";

    private static final String ATTR_ID       = "categoryId";
    private static final String ATTR_VAR_NAME = "varName";

    public CategoryDetail(final String dialectPrefix) {

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


        ServiceCategory categoryService = Aop.get(ServiceCategory.class);
        if (categoryService == null) {
            structureHandler.replaceWith("<div>get spring bean error</div>", false);
            return;
        }


        Integer articleId = getIntAttribute(tag, ATTR_ID, 0);
        String  varName   = getAttribute(tag, ATTR_VAR_NAME, "item");

        CategoryVo categoryVo = categoryService.getCategoryById(articleId);

        structureHandler.removeAttribute(ATTR_VAR_NAME);
        structureHandler.removeAttribute(ATTR_ID);


        ((EngineContext) context).setVariable(varName, categoryVo);

    }
}
