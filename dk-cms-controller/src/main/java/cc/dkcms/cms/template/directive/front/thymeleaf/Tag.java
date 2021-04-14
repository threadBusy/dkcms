package cc.dkcms.cms.template.directive.front.thymeleaf;

import cc.dkcms.cms.common.vo.TagVo;
import cc.dkcms.cms.service.ServiceTag;
import com.jfinal.aop.Aop;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templatemode.TemplateMode;

public class Tag extends AbstractPageTagProcessor {
    private static final String ATTR_NAME = "tag";

    private static final String ATTR_TAG_ID     = "tagId";
    private static final String ATTR_VAR_NAME   = "varName";
    private static final String ATTR_ORDER_BY   = "orderBy";
    private static final String ATTR_ORDER_SORT = "orderSort";

    public Tag(final String dialectPrefix) {

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




        Integer tagId = getIntAttribute(tag, ATTR_TAG_ID, -1);
        if (tagId == null || tagId < 0) {
            structureHandler.replaceWith("<div data-generator-by-cms>tagId missing</div>", false);
            return;
        }


        // 读取文章列表
        ServiceTag tagService = Aop.get(ServiceTag.class);
        TagVo      tagVo      = tagService.getTag(tagId);
        if (tagVo == null) {
            structureHandler.replaceWith("<div data-generator-by-cms>tagVo get null:" + tagId + "</div>", false);
            return;
        }

        String varName = getAttribute(tag, ATTR_VAR_NAME, "item");


        structureHandler.removeAttribute(ATTR_VAR_NAME);
        structureHandler.removeAttribute(ATTR_TAG_ID);
        // TODO: 2020-08-18 tagType 被硬编码了
        if (tagVo.getTagType() == 1) {
            structureHandler.iterateElement(varName, "status", tagVo.getArticleVoList());

        } else if (tagVo.getTagType() == 2) {
            structureHandler.iterateElement(varName, "status", tagVo.getCategoryVoList());
        } else {
            structureHandler.replaceWith("<div data-generator-by-cms>tagType error:" + tagVo.getTagType() + "</div>", false);

        }


    }
}
