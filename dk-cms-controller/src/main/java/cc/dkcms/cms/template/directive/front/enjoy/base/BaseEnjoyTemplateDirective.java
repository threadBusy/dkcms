package cc.dkcms.cms.template.directive.front.enjoy.base;

import cc.dkcms.cms.common.render.DkCmsRenderPageType;
import cc.dkcms.cms.template.directive.base.AbstractBaseDirective;
import cc.dkcms.cms.template.render.DkCmsRenderContext;
import com.jfinal.template.expr.ast.Expr;
import com.jfinal.template.stat.Scope;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class BaseEnjoyTemplateDirective extends AbstractBaseDirective {


    protected DkCmsRenderContext getDkCmsRenderContext(Scope scope) {
        Object object = scope.get("dkRenderContext");
        if (object != null) {
            return (DkCmsRenderContext) object;

        }
        return null;
    }


    protected Integer tryGuessCategoryId(Scope scope) {

        DkCmsRenderContext context = getDkCmsRenderContext(scope);
        if (context == null) {
            return null;
        }

        DkCmsRenderPageType pageType = context.getPageType();

        if (pageType.equals(DkCmsRenderPageType.CATEGORY_LIST)) {
            return context.getCategoryId();
        } else if (pageType.equals(DkCmsRenderPageType.CATEGORY_HOME)) {
            return context.getCategoryId();
        } else if (pageType.equals(DkCmsRenderPageType.CATEGORY_ARTICLE)) {
            return context.getArticleVo().getCategoryId();
        }

        return null;
    }


    protected Map<String, Object> getMap(Scope scope) {
        try {
            Expr expr = exprList.getFirstExpr();
            if (expr == null) {
                return new HashMap<>();
            }
            Object val = expr.eval(scope);
            if (!(val instanceof HashMap)) {
                return new HashMap<>();
            }
            return (Map<String, Object>) val;
        } catch (Exception e) {
            log.error("catch exception in AbstractBaseDirective.getString " + e.getMessage());

            return new HashMap<>();
        }
    }

    protected Boolean getBoolFromMap(Map<String, Object> map, String key, Boolean def) {
        try {
            String val = getStrFromMap(map, key, null);
            if (StringUtils.isEmpty(val)) {
                return def;
            }
            return Boolean.valueOf(val);
        } catch (Exception e) {
            e.printStackTrace();
            return def;
        }
    }

    protected Integer getIntFromMap(Map<String, Object> map, String key, Integer def) {
        try {
            String val = getStrFromMap(map, key, null);
            if (StringUtils.isEmpty(val)) {
                return def;
            }
            return Integer.valueOf(val);
        } catch (Exception e) {
            e.printStackTrace();
            return def;
        }
    }

    protected String getStrFromMap(Map<String, Object> map, String key, String def) {
        if (map == null || StringUtils.isEmpty(key) || !map.containsKey(key)) {
            return def;
        }
        try {
            return String.valueOf(map.get(key));
        } catch (Exception e) {
            e.printStackTrace();
            return def;
        }
    }

}
