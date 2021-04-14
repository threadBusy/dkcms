package cc.dkcms.cms.template.directive.front.thymeleaf;


import cc.dkcms.cms.common.render.DkCmsRenderPageType;
import cc.dkcms.cms.template.render.DkCmsRenderContext;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class AbstractPageTagProcessor<T> extends AbstractAttributeTagProcessor {
    private String _elementName;

    public AbstractPageTagProcessor(TemplateMode templateMode, String dialectPrefix,
                                    String elementName, boolean prefixElementName,
                                    String attributeName, boolean prefixAttributeName,
                                    int precedence, boolean removeAttribute) {
        super(templateMode,
                dialectPrefix,
                elementName,
                prefixElementName,
                attributeName,
                prefixAttributeName,
                precedence,
                removeAttribute);
        _elementName = dialectPrefix + ":" + attributeName;
    }

    protected DkCmsRenderContext getDkCmsRenderContext(ITemplateContext context) {
        return (DkCmsRenderContext) context.getVariable("dkRenderContext");
    }

    protected void showMsg(IElementTagStructureHandler structureHandler, String msg) {

        structureHandler.replaceWith(
                "<!--dkCmsMsg:{tag:" + _elementName + "}{msg:" + msg + "}-->",
                false);

    }

    protected Integer tryGetCategoryId(DkCmsRenderContext dkCmsRenderContext) {
        if (dkCmsRenderContext == null) {
            return null;
        }

        DkCmsRenderPageType contextType = dkCmsRenderContext.getPageType();
        if (contextType.equals(DkCmsRenderPageType.CATEGORY_HOME) ||
                contextType.equals(DkCmsRenderPageType.CATEGORY_LIST)) {
            return dkCmsRenderContext.getCategoryId();
        }
        if (contextType.equals(DkCmsRenderPageType.CATEGORY_ARTICLE)) {
            return dkCmsRenderContext.getArticleVo().getCategoryId();
        }

        return null;

    }


    public String getAttribute(IProcessableElementTag tag, String key, String def) {
        return UtilTagProcessor.getAttribute(tag, key, def);
    }

    public Boolean getBoolAttribute(IProcessableElementTag tag, String key, Boolean def) {
        return UtilTagProcessor.getBoolAttribute(tag, key, def);
    }

    public Integer getIntAttribute(IProcessableElementTag tag, String key, Integer def) {
        return UtilTagProcessor.getIntAttribute(tag, key, def);
    }

    protected String getAllAttrbutesString(IProcessableElementTag tag) {
        return UtilTagProcessor.getAllAttrbutesString(tag);

    }


    protected Object getExpressionValue(ITemplateContext context, String exp) {
        return UtilTagProcessor.getExpressionValue(context, exp);
    }

    private final static String queryStringPrefix = "queryString-";

    protected Map<String, String> getQueryStringMap(ITemplateContext context, IProcessableElementTag tag) {
        Map<String, String> result = new HashMap<>();
        if (tag == null) {
            return result;
        }
        tag.getAttributeMap().forEach((key, expValue) -> {
            if (key != null && key.startsWith(queryStringPrefix) && !key.equals(queryStringPrefix)) {
                String value = String.valueOf(getExpressionValue(context, expValue));
                result.put(key.substring(queryStringPrefix.length()), value);
            }
        });
        return result;

    }

}
