package cc.dkcms.cms.controller.front;

import cc.dkcms.cms.common.CommonAuth;
import cc.dkcms.cms.common.Constant;
import cc.dkcms.cms.common.define.AjaxResponse;
import cc.dkcms.cms.common.define.Result;
import cc.dkcms.cms.common.vo.CommentVo;
import cc.dkcms.cms.dao.DaoAccount;
import cc.dkcms.cms.dao.DaoComment;
import cc.dkcms.cms.service.ServiceAccount;
import cc.dkcms.cms.service.ServiceComment;
import cc.dkcms.cms.template.render.DkCmsRender;
import cc.dkcms.cms.template.render.DkCmsRenderContext;
import cc.dkcms.cms.template.render.DkCmsRenderContextFactory;
import com.jfinal.aop.Aop;
import com.jfinal.core.Controller;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ControllerSite extends Controller {

    private DkCmsRenderContextFactory contextFactory
            = new DkCmsRenderContextFactory(this);

    public void index() {
        __doRender(contextFactory.getHomePageContext());
    }

    public void category() {

        Integer categoryId = getInt("categoryId", 0);

        if (categoryId < 1) {
            renderText("categoryId missing:" + categoryId);
            return;
        }
        __doRender(contextFactory.getCategoryHomeContext(categoryId));
    }

    public void content() {
        Integer articleId = getInt("articleId", 0);
        if (articleId < 1) {
            renderText("articleId missing");
            return;
        }
        __doRender(contextFactory.getCategoryArticleContext(articleId));
    }

    public void list() {
        Integer categoryId = getInt("categoryId", 0);
        Integer page       = getInt("page", 1);

        if (categoryId < 0) {
            categoryId = 0;
        }
        // 列表页默认的pageSize 可能要做成配置
        __doRender(contextFactory.getCategoryListContext(categoryId, page, Constant.PAGE_SIZE));
    }


    public void page() {
        Integer pageId = getInt("pageId", 1);

        if (pageId < 1) {
            renderText("pageId missing:" + pageId);
            return;
        }
        __doRender(contextFactory.getSinglePageContext(pageId));
    }

    public void tag() {
        Integer tagId = getInt("tagId", 1);

        if (tagId < 1) {
            renderText("tagId missing:" + tagId);
            return;
        }
        __doRender(contextFactory.getTagArticleListPageContext(tagId));
    }


    private void __doRender(DkCmsRenderContext context) {
        DkCmsRender dkCmsRender = new DkCmsRender();
        Result      result      = dkCmsRender.renderToResponse(context, getResponse());

        if (result.isSuccess()) {
            renderNull();
            return;
        }
        renderText(result.getMsg());
    }


    ServiceAccount serviceAccount = Aop.get(ServiceAccount.class);

    // 给站点提供用户信息
    public void userInfo() {
        Result result = CommonAuth.checkLogin(this);
        if (!result.getIsSuccess()) {
            renderJson(AjaxResponse.fail("no login"));
            return;
        }
        Long       accountId = (Long) result.getData();
        DaoAccount account   = serviceAccount.getById(accountId);
        if (account == null) {
            renderJson(AjaxResponse.fail("no login"));
            return;
        }
        String logo = account.getLogo();
        if (StringUtils.isEmpty(logo)) {
            logo = "/assets/image/default-user-logo.png";
        }
        Map<String, String> data = new HashMap<>();
        data.put("nickname", account.getUsername());
        data.put("logo", logo);

        renderJson(AjaxResponse.success().setData(data));

    }

    ServiceComment serviceComment = Aop.get(ServiceComment.class);

    public void getComment() {
        Integer articleId = getInt("id");
        Integer start     = getInt("start", 0);
        if (articleId == null) {
            renderJson(AjaxResponse.fail("article id missing"));
            return;
        }

        List<CommentVo> list = serviceComment.getListByArticleId(articleId, start);
        renderJson(AjaxResponse.success().setData(list));
    }

    public void saveComment() {

        Result result = CommonAuth.checkLogin(this);
        if (!result.getIsSuccess()) {
            renderJson(AjaxResponse.fail("no login"));
            return;
        }
        Long       accountId = (Long) result.getData();
        DaoAccount account   = serviceAccount.getById(accountId);
        if (account == null) {
            renderJson(AjaxResponse.fail("no login"));
            return;
        }

        Integer articleId = getInt("id");
        String  comment   = get("comment");
        if (StringUtils.isEmpty(comment)) {
            renderJson(AjaxResponse.fail("comment empty"));
            return;
        }

        DaoComment daoComment = new DaoComment();
        // risk todo
        daoComment.setAccountId(Integer.valueOf(String.valueOf(accountId)));
        daoComment.setArticleId(articleId);
        daoComment.setContent(comment);
        daoComment.setPublishAt(new Date());
        if (!daoComment.save()) {
            renderJson(AjaxResponse.fail("db error"));
            return;
        }
        renderJson(AjaxResponse.success("comment empty"));

    }

    public void search() {
        String keyword = get("kw");

        if (StringUtils.isEmpty(keyword)) {
            renderJson(AjaxResponse.fail("keyword missing"));
            return;
        }
        __doRender(contextFactory.getSearchPageContext(keyword));
    }
}
