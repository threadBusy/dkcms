package cc.dkcms.cms.common.helper;


import cc.dkcms.cms.common.define.StaticPagePathPolicy;
import cc.dkcms.cms.common.vo.ArticleVo;
import cc.dkcms.cms.common.vo.CategoryVo;
import cc.dkcms.cms.common.vo.SinglePageVo;
import cc.dkcms.cms.common.vo.TagVo;
import com.jfinal.ext.kit.DateKit;
import org.apache.commons.lang.StringUtils;

import java.util.LinkedList;

import static cc.dkcms.cms.common.Constant.ROOT_CATEGORY_ID;
import static cc.dkcms.cms.common.define.StaticPagePathPolicy.CATEGORY_AS_PATH;
import static cc.dkcms.cms.common.define.StaticPagePathPolicy.DATE_AS_PATH;


public class FileNameHelper {

    // ONSTART 的时候要从db里读取，设置这个值
    //    // change 的时候，要更新这个值
    public static StaticPagePathPolicy POLICY_PATH = CATEGORY_AS_PATH;

    public static String getHomePageFileName() {
        return "index.html";
    }


    public static String getCategoryFileName(CategoryVo vo) {

        String path = getCategoryPath(vo);
        if (!StringUtils.isEmpty(path)) {
            return path + "/index.html";
        }
        return "index.html";
    }


    public static String getCategoryListFileName(CategoryVo vo, Integer page) {


        String fileName = "list-" + page + ".html";
        String path     = getCategoryPath(vo);
        if (!StringUtils.isEmpty(path)) {
            return path + "/" + fileName;
        }
        return fileName;

    }

    public static String getCategoryArticleFileName(ArticleVo articleVo, CategoryVo categoryVo) {
        String publishDateStr = DateKit.toStr(articleVo.getPublishDate(), "yyyyMMdd");
        //String fileName       = publishDateStr + articleVo.getId() + ".html";
        String fileName = articleVo.getId() + ".html";
        if (!StringUtils.isEmpty(articleVo.getPermalink())) {
            fileName = articleVo.getPermalink() + ".html";
        }
        String path = getCategoryPath(categoryVo);
        if (!StringUtils.isEmpty(path)) {
            return path + "/" + publishDateStr + "/" + fileName;
        }
        return fileName;
    }


    public static String getSinglePageFileName(SinglePageVo vo) {
        if (!StringUtils.isEmpty(vo.getPermalink())) {
            return "page-" + vo.getPermalink() + ".html";
        }
        return "page-" + vo.getId() + ".html";
    }


    private static String getCategoryPath(CategoryVo vo) {


        if (DATE_AS_PATH.equals(POLICY_PATH)) {
            return StringUtils.isEmpty(vo.getPermalink()) ? String.valueOf(vo.getId()) : vo.getPermalink();
        }

        LinkedList<String> fileName = new LinkedList<>();
        while ((vo != null)
                && (!vo.getId().equals(ROOT_CATEGORY_ID))) {
            if (StringUtils.isEmpty(vo.getPermalink())) {
                fileName.addFirst(String.valueOf(vo.getId()));
            } else {
                fileName.addFirst(vo.getPermalink());
            }
            vo = vo.getParentVo();
        }
        return StringUtils.join(fileName, "/");
    }

    public static String getArticleListByTagFileName(TagVo vo) {
        return "page-" + vo.getId() + ".html";
    }
}