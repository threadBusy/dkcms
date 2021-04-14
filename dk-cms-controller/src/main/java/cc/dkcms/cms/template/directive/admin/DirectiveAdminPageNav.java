package cc.dkcms.cms.template.directive.admin;

import cc.dkcms.cms.common.define.RequestVar;
import cc.dkcms.cms.template.directive.base.AbstractBaseDirective;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import org.apache.commons.lang.StringUtils;

import java.util.LinkedList;
import java.util.Map;

public class DirectiveAdminPageNav extends AbstractBaseDirective {
    @Override
    public void exec(Env env, Scope scope, Writer writer) {

        Map data = scope.getData();
        if (data == null || data.size() == 0) {
            write(writer, "<!--DirectiveAdminPageNav not found $pageInfo-->");
            return;
        }

        Page page = (Page) data.get("pageInfo");
        if (page == null) {
            write(writer, "<!--DirectiveAdminPageNav $pageInfo is null -->");
            return;
        }


        int totalPage   = page.getTotalPage();
        int currentPage = page.getPageNumber();


        RequestVar var = (RequestVar) data.get("request");

        String requestUrl         = "";
        String requestQueryString = "";

        if (var != null) {
            requestUrl = var.getRequestUri();
            requestQueryString = var.getQueryString();
        }


        if (totalPage == 0) {
            write(writer, "<!--DirectiveAdminPageNav totalPage == 0 -->");
            return;
        }

        LinkedList<Integer> pageList = new LinkedList<>();
        for (int i = currentPage - 2; i <= currentPage + 2; i++) {
            if (i < 1 || i > totalPage) {
                continue;
            }
            pageList.add(i);
        }

        if (!pageList.contains(1)) {
            if (currentPage != 2) {
                // -1 代表省略号，无连接
                pageList.addFirst(-1);
            }
            pageList.addFirst(1);
        }
        if (!pageList.contains(totalPage)) {
            if (currentPage != (totalPage - 1)) {
                pageList.addLast(-1);
            }
            pageList.addLast(totalPage);
        }


        if (!StringUtils.isEmpty(requestQueryString) && requestQueryString.contains("page=")) {
            requestQueryString = requestQueryString.replaceAll("&page=\\d+", "");
        }

        String urlPrefix = requestUrl + "?" + requestQueryString;


        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<div class=\"pager_container\">");
        for (Integer i : pageList) {
            if (i == -1) {
                stringBuilder.append("<div class=\"pager\">");
                stringBuilder.append("<a href=\"javascript:void()\"> - </a>");
                stringBuilder.append("</div>");
                continue;
            }
            stringBuilder.append("<div class=\"pager\">");
            stringBuilder.append("<a href='javascript:void()' data-href=\"").append(urlPrefix).append("&page=").append(i).append("\">").append(i).append("</a>");
            stringBuilder.append("</div>");
        }
        stringBuilder.append("</div>");


        write(writer, stringBuilder.toString());

    }
}
