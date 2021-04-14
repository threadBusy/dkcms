package cc.dkcms.cms.template.directive.front.thymeleaf.dialect;

import org.apache.commons.lang.StringUtils;
import org.thymeleaf.context.IExpressionContext;

public class CmsExpressObject {
    private final IExpressionContext context;

    public String cleanHtml(String input) {
        if (StringUtils.isEmpty(input)) {
            return input;
        }
        input = input.replaceAll("&nbsp; ", "");
        if (StringUtils.isEmpty(input)) {
            return "";
        }
        return input.replaceAll("<[^>]*>", "");
    }

    public CmsExpressObject(final IExpressionContext context) {
        super();
        this.context = context;
    }

}
