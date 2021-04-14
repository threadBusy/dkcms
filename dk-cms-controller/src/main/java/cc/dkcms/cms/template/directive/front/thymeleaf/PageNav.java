package cc.dkcms.cms.template.directive.front.thymeleaf;


import cc.dkcms.cms.common.vo.ArticleVo;
import cc.dkcms.cms.template.render.DkCmsRenderContext;
import com.jfinal.plugin.activerecord.Page;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.LinkedList;

public class PageNav extends AbstractPageTagProcessor {
    private static final String ATTR_NAME = "pageNav";

    public PageNav(final String dialectPrefix) {

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


        // 调用pageNav之前应该调用过articleList,否着这里 获取不到pageInfo
        // 这个pageInfo 不是内置的，是articleList标签设置的
        // 所有，只有在articleList之后，才能调用这个pageInfo
        DkCmsRenderContext dkCmsRenderContext = (DkCmsRenderContext) context.getVariable("dkRenderContext");
        Page<ArticleVo>    pageInfo           = dkCmsRenderContext.getArticleListPageInfo();


        if (pageInfo == null || pageInfo.getList().size() == 0) {
            showMsg(structureHandler, "get pageInfo empty");
            return;
        }

        Integer currentPage = pageInfo.getPageNumber();

        if (currentPage < 1) {
            currentPage = 1;
        }

        String attributeString = getAllAttrbutesString(tag);

        if (pageInfo.getTotalPage() == 1) {
            structureHandler.replaceWith("\n<div" + attributeString + " data-only-1></div>\n", false);
            return;
        }

        String baseUrl = dkCmsRenderContext.getFinalUrl();
        if (!baseUrl.contains("?")) {
            baseUrl = baseUrl + '?';
        } else if (!baseUrl.endsWith("&") && !baseUrl.endsWith("?")) {
            baseUrl = baseUrl + "&";
        }

        int                 jump      = 2;
        int                 totalPage = pageInfo.getTotalPage();
        LinkedList<Integer> pageList  = new LinkedList<>();
        for (int i = currentPage - jump; i <= currentPage + jump; i++) {
            if (i < 1 || i > totalPage) {
                continue;
            }
            pageList.add(i);
        }

        // 如果不是以1开头，并且和和1中间有差距，补上省略号
        if (!pageList.contains(1)) {
            if (currentPage != jump) {
                // -1 代表省略号，无连接
                pageList.addFirst(-1);
            }
            pageList.addFirst(1);
        }
        //如果不包含最后1页，并且和最后一页有差距，补上省略号
        if (!pageList.contains(totalPage)) {
            if (currentPage != (totalPage - 1)) {
                pageList.addLast(-1);
            }
            pageList.addLast(totalPage);
        }


        StringBuilder stringBuilder = new StringBuilder("\n<div" + attributeString + ">\n");
        for (Integer i : pageList) {
            if (i == -1) {
                stringBuilder.append("<div>");
                stringBuilder.append("<a href=\"javascript:void()\"> - </a>");
                stringBuilder.append("</div>");
                continue;
            }
            stringBuilder.append("<div><a href='" + baseUrl + "page=" + i + "' ");
            //stringBuilder.append(UrlHelper.getCategoryListUrl(cmsContext.getCategoryVo(), i, isPreview));
            if (currentPage.equals(i)) {
                stringBuilder.append(" class='s' ");
            }
            stringBuilder.append(">").append(i).append("</a></div>\n");
        }
        stringBuilder.append("</div>\n");


        structureHandler.replaceWith(stringBuilder.toString(), true);


    }
}
