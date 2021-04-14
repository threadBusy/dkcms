package cc.dkcms.cms.service;

import cc.dkcms.cms.common.define.PageGeneratorLogger;
import cc.dkcms.cms.common.helper.UrlHelper;
import cc.dkcms.cms.common.vo.ArticleVo;
import cc.dkcms.cms.common.vo.CategoryVo;
import cc.dkcms.cms.common.vo.SinglePageVo;
import com.jfinal.aop.Aop;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import static cc.dkcms.cms.common.Constant.PATH_WEBROOT;
import static cc.dkcms.cms.common.Constant.ROOT_CATEGORY_ID;

public class ServicePageMaker {
    private ServiceCategory serviceCategory = Aop.get(ServiceCategory.class);
    private ServiceContent  serviceContent  = Aop.get(ServiceContent.class);

    public void makeSiteMapFile(PageGeneratorLogger logger) {
        ServiceSetting    serviceSetting    = Aop.get(ServiceSetting.class);
        ServiceSinglePage serviceSinglePage = Aop.get(ServiceSinglePage.class);
        String            settingHost       = serviceSetting.getValue("siteHost");
        if (StringUtils.isEmpty(settingHost)) {
            settingHost = "noHost";
        }


        LinkedList<String> allPageList = new LinkedList<>();

        List<Integer> allCategoryId = serviceCategory.getChildIdList(0, true);
        final String  host          = settingHost;
        allCategoryId.forEach(categoryId -> {
            if (categoryId == ROOT_CATEGORY_ID) {
                return;
            }
            CategoryVo categoryVo = serviceCategory.getCategoryVoById(categoryId);
            String     url        = host + UrlHelper.getCategoryHomeUrl(categoryVo, false);
            allPageList.add(url);
            logger.log("echo", url);
            // 栏目列表页面
            Integer totalPage = serviceContent.getTotalPage(categoryVo.getId(), 10, false);
            for (int i = 1; i <= totalPage; i++) {
                Page<ArticleVo> pageInfo = serviceContent.getPage(categoryVo.getId(), i);
                url = host + UrlHelper.getCategoryListUrl(categoryVo, i, false);
                allPageList.add(url);
                logger.log("echo", url);
                for (ArticleVo articleVo : pageInfo.getList()) {
                    url = host + UrlHelper.getCategoryArticleUrl(articleVo, categoryVo, false);
                    allPageList.add(url);
                    logger.log("echo", url);
                }
            }
        });

        List<SinglePageVo> singlePageList = serviceSinglePage.getList();
        if (singlePageList != null) {
            for (SinglePageVo pageVo : singlePageList) {
                String url = host + UrlHelper.getSinglePageUrl(pageVo, false);
                allPageList.add(url);
                logger.log("echo", url);

            }
        }
        logger.log("echo", "开始生成文件");


        try {

            PrintWriter siteMapTxt = new PrintWriter(new File(PATH_WEBROOT + "/sitemap.txt"));
            PrintWriter siteMapXml = new PrintWriter(new File(PATH_WEBROOT + "/sitemap.xml"));
            siteMapXml.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<urlset>");

            if (allPageList.size() == 0) {
                logger.log("echo", "urls empty");
                return;
            }
            String lastMod = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
            allPageList.forEach(item -> {
                siteMapTxt.println(item);
                siteMapXml.print("  <url>\n" +
                        "    <loc>" + item + "</loc>\n" +
                        "    <lastmod>" + lastMod + "</lastmod>\n" +
                        "  </url>\n");
            });
            siteMapXml.print("</urlset>\n");

            siteMapTxt.close();
            siteMapXml.close();


        } catch (IOException e) {
            logger.log("echo", "catch exception when getOutPutFileWriter sitemap.xml:" + e.getMessage());
        }
    }
}
