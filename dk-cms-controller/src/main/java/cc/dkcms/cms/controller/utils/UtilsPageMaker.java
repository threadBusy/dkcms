package cc.dkcms.cms.controller.utils;

import cc.dkcms.cms.common.Constant;
import cc.dkcms.cms.common.define.PageGeneratorLogger;
import cc.dkcms.cms.common.define.Result;
import cc.dkcms.cms.common.define.WebSocketCmd;
import cc.dkcms.cms.common.vo.ArticleVo;
import cc.dkcms.cms.common.vo.SinglePageVo;
import cc.dkcms.cms.service.ServiceCategory;
import cc.dkcms.cms.service.ServiceContent;
import cc.dkcms.cms.service.ServicePageMaker;
import cc.dkcms.cms.service.ServiceSinglePage;
import cc.dkcms.cms.template.render.DkCmsRender;
import cc.dkcms.cms.template.render.DkCmsRenderContext;
import cc.dkcms.cms.template.render.DkCmsRenderContextFactory;
import com.jfinal.aop.Aop;
import com.jfinal.plugin.activerecord.Page;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 页面生成逻辑，在后台手动生成，和api调用生成，两处都用了。
 * 所有 抽离了logger，在不同的场景下。输入日志的实现不同
 */
@Slf4j
public class UtilsPageMaker {


    private DkCmsRenderContextFactory factory          = new DkCmsRenderContextFactory();
    private DkCmsRender               dkCmsRender      = new DkCmsRender();
    private ServiceCategory           serviceCategory  = Aop.get(ServiceCategory.class);
    private ServiceContent            serviceContent   = Aop.get(ServiceContent.class);
    private ServicePageMaker          servicePageMaker = Aop.get(ServicePageMaker.class);
    private Set<String>               pagePool         = new TreeSet<>();


    @Setter
    PageGeneratorLogger logger = (cmd, msg) -> {
        log.info("UtilsPageMaker.logger: {cmd:?,msg:?}", cmd, msg);
    };

    public void clearPagePool() {
        this.pagePool.clear();
    }

    public void makeSiteMapFile() {
        servicePageMaker.makeSiteMapFile(logger);
    }

    public void makeSite() {

        makeIndex();
        WebSocketCmd webSocketCmd = new WebSocketCmd("", 0, true);
        makeCategory(webSocketCmd);
        makeContent(webSocketCmd);
        makeAllPage();
    }

    public void makeContent(WebSocketCmd webSocketCmd) {
        __findAllChild(webSocketCmd).forEach(this::__generateContentByCategoryId);
    }


    private void __generateContentByCategoryId(Integer categoryId) {
        if (categoryId == 0) {
            return;
        }
        logger.log("echo", "开始生成该栏目 {" + categoryId + "} 内容页");
        Integer totalPage = serviceContent.getTotalPage(categoryId, Constant.PAGE_SIZE, false);
        for (int i = 1; i <= totalPage; i++) {
            // 列表页
            Page<ArticleVo> pageInfo = serviceContent.getPageByCategoryIdWithOutSubCat(i, Constant.PAGE_SIZE, categoryId);
            // 文章页
            logger.log("echo", "生成第" + i + "页开始");
            for (ArticleVo vo : pageInfo.getList()) {
                doMake(factory.getCategoryArticleContext(vo));
                /*
                DkCmsRenderContext context = factory.getCategoryArticleContext(vo);
                Result             result  = dkCmsRender.renderToFile(context, context.getStaticPageFilePath());
                if (result.getIsSuccess()) {
                    logger.log("echo", "内容页生成成功[ID:" + vo.getId()
                            + "][URL:" + vo.getArticleUrl()
                            + "][标题:" + vo.getTitle() + "]");
                } else {
                    logger.log("echo", "内容页生成异常:" + result.getMsg());
                }*/
            }
            logger.log("echo", "生成第" + i + "页结束");

        }
        logger.log("echo", "栏目生成完成:" + categoryId);

    }

    private List<Integer> __findAllChild(WebSocketCmd webSocketCmd) {

        Integer categoryId = webSocketCmd.getCategoryId();
        Boolean withChild  = webSocketCmd.getWithChild();
        if (categoryId == null || categoryId < 0) {
            logger.log("echo", "categoryId error");
            return new ArrayList<>();
        }

        // 收集所有可能被生成的categoryId
        List<Integer> categoryIds = new ArrayList<>();
        //categoryIds.add(categoryId);
        if (withChild) {
            List<Integer> subCategoryIds = serviceCategory.getChildIdList(categoryId, true);
            if (subCategoryIds != null && subCategoryIds.size() > 0) {
                categoryIds.addAll(subCategoryIds);
            }
        } else {
            // 生成全局的时候，入参categoryId ==0 ,不能进入这里
            if (categoryId > 0) {
                categoryIds.add(categoryId);
            }
        }
        return categoryIds;
    }

    public void makeCategory(WebSocketCmd webSocketCmd) {
        __findAllChild(webSocketCmd)
                .forEach(categoryId -> {
                    DkCmsRenderContext context = factory.getCategoryHomeContext(categoryId);
                    doMake(context);
                    if (context.getFlagNeedMakeList()) {
                        makeCategoryListPage(categoryId);
                    }
                });
    }

    public void makeCategoryListPage(Integer categoryId) {
        int totalPage = serviceContent.getTotalPage(categoryId, Constant.PAGE_SIZE, true);
        logger.log("echo", "栏目共有" + totalPage + "页面");
        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++)
                doMake(factory.getCategoryListContext(categoryId, i, Constant.PAGE_SIZE));
        }
    }


    public void makeIndex() {
        DkCmsRenderContext context = factory.getHomePageContext();
        doMake(context);
        if (context.getFlagNeedMakeList()) {
            makeCategoryListPage(0);
        }
    }

    public void makeAllPage() {
        ServiceSinglePage  serviceSinglePage = Aop.get(ServiceSinglePage.class);
        List<SinglePageVo> list              = serviceSinglePage.getList();
        DkCmsRender        dkCmsRender       = new DkCmsRender();


        for (SinglePageVo vo : list) {
            doMake(factory.getSinglePageContext(vo));
        }

    }

    public void makePage(WebSocketCmd webSocketCmd) {
        Integer pageId = webSocketCmd.getPage();
        if (pageId < 0) {
            logger.log("echo", "pageId error");
            logger.log("bye", "");
            return;
        }
        doMake(factory.getSinglePageContext(pageId));
    }


    private void doMake(DkCmsRenderContext context) {
        assert context != null;
        String fileName = context.getStaticPageFilePath();
        if (pagePool.contains(fileName)) {
            log.error(fileName + "已经生成过，跳过");
            return;
        }
        pagePool.add(fileName);

        Result result = dkCmsRender.renderToFile(context, fileName);
        if (result.getIsSuccess()) {
            logger.log("echo", "生成成功[" + context.getFinalUrl() + "]");
        } else {
            logger.log("echo", "生成错误[" + context.getFinalUrl() + "][{" + result.getMsg() + "}]");
        }
    }

}
