package cc.dkcms.cms.template.directive.front.thymeleaf;

import cc.dkcms.cms.common.vo.CategoryVo;
import cc.dkcms.cms.service.ServiceCategory;
import cc.dkcms.cms.template.render.DkCmsRenderContext;
import com.jfinal.aop.Aop;
import org.apache.commons.lang.StringUtils;
import org.thymeleaf.context.EngineContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CategoryList extends AbstractPageTagProcessor {

    private static final String ATTR_NAME = "categoryList";

    private static final String ATTR_PARENT_ID      = "parentId";
    private static final String ATTR_VAR_NAME       = "varName";
    private static final String ATTR_ORDER_BY       = "orderBy";
    private static final String ATTR_ORDER_SORT     = "orderSort";
    private static final String ATTR_INCLUDE_HIDDEN = "includeHidden";

    public CategoryList(final String dialectPrefix) {

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


        DkCmsRenderContext dkCmsRenderContext = (DkCmsRenderContext) context.getVariable("dkRenderContext");

        ServiceCategory categoryService = Aop.get(ServiceCategory.class);
        if (categoryService == null) {
            structureHandler.replaceWith("<div>get spring bean error</div>", false);
            return;
        }

        // 当前环境，如果没有指定parentId,以此id为categoryId
        Integer defaultCategoryId = tryGetCategoryId(dkCmsRenderContext);
        if (defaultCategoryId == null) {
            defaultCategoryId = 0;
        }


        String varName = getAttribute(tag, ATTR_VAR_NAME, "item");
        //
        // 如果用户指定了parentId,以此用户指定为准，如果用户没指定，按上边获取的为准
        // parentId 可能是一个表达式，需要计算表达式的值
        //
        String parentIdExpFromAttribute = getAttribute(tag, ATTR_PARENT_ID, String.valueOf(defaultCategoryId));


        //orderBy
        String orderBy   = getAttribute(tag, ATTR_ORDER_BY, null);
        String orderSort = getAttribute(tag, ATTR_ORDER_SORT, null);

        if (StringUtils.isEmpty(orderBy)) {
            orderBy = "id";
        }
        if (StringUtils.isEmpty(orderSort) || orderBy.toLowerCase().equals("asc")) {
            orderSort = "desc";
        }


        Integer parentId = Integer.valueOf(String.valueOf(getExpressionValue(context, parentIdExpFromAttribute)));

        ((EngineContext) context).setVariable("currentCategoryId", parentId);

        String includeHidden = getAttribute(tag, ATTR_INCLUDE_HIDDEN, "false");

        List<CategoryVo> list = new ArrayList<>();
        list.addAll(categoryService.getChildVoList(parentId));
        if (!"true".equals(includeHidden)) {
            list.removeIf(vo -> vo.getSort().equals(-1));
        }


        final String orderByFinal   = orderBy;
        final String orderSortFinal = orderSort;
        list.sort(new CategoryCompare(orderByFinal, orderSortFinal));

        structureHandler.removeAttribute(ATTR_VAR_NAME);
        structureHandler.removeAttribute(ATTR_PARENT_ID);
        structureHandler.iterateElement(varName, "status", list);


    }

    static class CategoryCompare implements Comparator<CategoryVo> {

        private String orderBy;
        private String orderSort;

        public CategoryCompare(String orderBy, String orderSort) {
            this.orderBy = orderBy;
            this.orderSort = orderSort;
        }

        @Override
        public int compare(CategoryVo o1, CategoryVo o2) {
            if ("sort".equals(orderBy)) {
                if ("desc".equals(orderSort)) {
                    return o1.getSort() - o2.getSort();
                } else {
                    return o2.getSort() - o1.getSort();
                }

            }
            if ("desc".equals(orderSort)) {
                return o1.getId() - o2.getId();
            } else {
                return o2.getId() - o1.getId();
            }
        }
    }
}
