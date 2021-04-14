package cc.dkcms.cms.template.directive.front.thymeleaf;

import cc.dkcms.cms.common.util.CmsUtils;
import cc.dkcms.cms.common.vo.CategoryVo;
import cc.dkcms.cms.service.ServiceCategory;
import com.jfinal.aop.Aop;

import org.apache.commons.lang.StringUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.Map;

public class CategoryUrl extends AbstractPageTagProcessor {
    private static final String ATTR_NAME         = "categoryUrl";
    private static final String ATTR_QUERY_STRING = "queryString";

    public CategoryUrl(final String dialectPrefix) {

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


        String strCategoryId = tag.getAttributeValue("cms:categoryUrl");
        if (StringUtils.isEmpty(strCategoryId)) {
            error(handler, "strCategoryIdMissing");
            return;
        }
        try {
            Integer     categoryId      = Integer.valueOf(String.valueOf(getExpressionValue(context, strCategoryId)));
            ServiceCategory categoryService = Aop.get(ServiceCategory.class);
            if (categoryService == null) {
                error(handler, "spring getBean return null");
                return;
            }
            CategoryVo vo = categoryService.getCategoryById(categoryId);
            if (vo == null) {
                error(handler, "getCategoryById return null:" + categoryId);
                return;
            }

            String rawUrl   = vo.getCategoryUrl();
            String finalUrl = "";

            Map qs = getQueryStringMap(context,tag);
            if (qs == null || qs.size() == 0) {
                handler.setAttribute("href", rawUrl);
                return;
            }

            if (CmsUtils.isStaticPageMode()) {
                StringBuilder qsStringBuilder = new StringBuilder();
                qs.forEach((k, v) -> qsStringBuilder.append("-" + k + "-" + v));
                finalUrl = rawUrl.substring(0, rawUrl.length() - 5)
                        + qsStringBuilder.toString() + ".html";
            } else {
                StringBuilder qsStringBuilder = new StringBuilder();
                if (!rawUrl.contains("?")) {
                    qsStringBuilder.append("?");
                }
                qs.forEach((k, v) -> qsStringBuilder.append("&" + k + "=" + v));
                finalUrl = rawUrl + qsStringBuilder.toString();
            }
            handler.setAttribute("href", finalUrl);


        } catch (Exception e) {
            e.printStackTrace();
            error(handler, "pageIdParseException:" + strCategoryId);

        }

    }

    private void error(IElementTagStructureHandler handler, String msg) {
        handler.setAttribute("data-error", msg);

    }
}