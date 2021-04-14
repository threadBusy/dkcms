package cc.dkcms.cms.template.directive.front.thymeleaf;

import org.apache.commons.lang.StringUtils;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;

import java.util.Map;

public class UtilTagProcessor {
    public static String getStyle(IProcessableElementTag tag) {

        String ret   = "";
        String style = tag.getAttributeValue("style");
        if (!StringUtils.isEmpty(style)) {
            ret += " style='" + style + "'";
        }
        String cssClass = tag.getAttributeValue("class");
        if (!StringUtils.isEmpty(cssClass)) {
            ret += " class='" + cssClass + "'";
        }
        return ret;
    }

    public static String getAttribute(IProcessableElementTag tag, String key, String def) {
        if (tag == null) {
            return null;
        }
        String val = tag.getAttributeValue(key);
        if (val == null) {
            return def;
        }
        return val;
    }

    public static Boolean getBoolAttribute(IProcessableElementTag tag, String key, Boolean def) {
        String val = getAttribute(tag, key, null);
        try {
            return Boolean.valueOf(val);
        } catch (Exception e) {
            return def;
        }
    }

    public static Integer getIntAttribute(IProcessableElementTag tag, String key, Integer def) {
        String val = getAttribute(tag, key, null);
        try {
            return Integer.valueOf(val);
        } catch (Exception e) {
            return def;
        }
    }

    public static String getAllAttrbutesString(IProcessableElementTag tag) {
        if (tag == null) {
            return null;
        }

        Map<String, String> attrs = tag.getAttributeMap();
        if (attrs.size() < 1) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : attrs.keySet()) {
            if (key.startsWith("cms")) {
                continue;
            }
            stringBuilder.append(" ").append(key).append("=").append("'").append(attrs.get(key)).append("' ");
        }
        return stringBuilder.toString();

    }

    public static String getExpressionValue(ITemplateContext context, String exp) {
        if (context == null || StringUtils.isEmpty(exp)) {
            return null;
        }
        final IEngineConfiguration configuration = context.getConfiguration();

        final IStandardExpressionParser parser =
                StandardExpressions.getExpressionParser(configuration);

        final IStandardExpression expression = parser.parseExpression(context, exp);

        return String.valueOf(expression.execute(context));
    }
}
