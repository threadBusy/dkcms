package cc.dkcms.cms.common.util;

import cc.dkcms.cms.common.helper.UrlHelper;
import cc.dkcms.cms.common.vo.CategoryVo;

import java.util.LinkedList;

public class PageNavUtils {

    public static String pageNav(Integer totalPage, Integer currentPage, Integer jump, CategoryVo categoryVo) {

        if (totalPage == null || totalPage < 1) {
            return "";
        }
        if (currentPage == null || currentPage < 1) {
            currentPage = 1;
        }
        if (jump == null) {
            jump = 2;
        }

        LinkedList<Integer> pageList = new LinkedList<>();
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


        StringBuilder stringBuilder = new StringBuilder();
        Boolean       isPreview     = CmsUtils.isPreviewMode();
        for (Integer i : pageList) {
            if (i == -1) {
                stringBuilder.append("<div>");
                stringBuilder.append("<a href=\"javascript:void()\"> - </a>");
                stringBuilder.append("</div>");
                continue;
            }
            String baseUrl = UrlHelper.getCategoryListUrl(categoryVo, i, isPreview);


            stringBuilder.append("<div><a href='" + baseUrl + "' ");
            if (currentPage.equals(i)) {
                stringBuilder.append(" class='s' ");
            }
            stringBuilder.append(">").append(i).append("</a></div>\n");
        }

        return stringBuilder.toString();

    }
}
