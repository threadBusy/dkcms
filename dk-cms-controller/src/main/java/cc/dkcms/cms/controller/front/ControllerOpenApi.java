package cc.dkcms.cms.controller.front;

import cc.dkcms.cms.common.Constant;
import cc.dkcms.cms.common.define.AjaxResponse;
import cc.dkcms.cms.common.define.ConstArticleDelete;
import cc.dkcms.cms.common.define.Result;
import cc.dkcms.cms.common.define.WebSocketCmd;
import cc.dkcms.cms.common.vo.CategoryVo;
import cc.dkcms.cms.common.vo.CategoryVoForOpenApi;
import cc.dkcms.cms.controller.utils.UtilsPageMaker;
import cc.dkcms.cms.dao.DaoArticle;
import cc.dkcms.cms.service.ServiceApiToken;
import cc.dkcms.cms.service.ServiceCategory;
import com.jfinal.aop.Aop;
import com.jfinal.core.Controller;
import com.jfinal.core.NotAction;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ControllerOpenApi extends Controller {

    ServiceApiToken serviceApiToken = Aop.get(ServiceApiToken.class);
    ServiceCategory serviceCategory = Aop.get(ServiceCategory.class);


    @NotAction
    private Result verifyToken() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return Result.err("request is null");
        }
        return serviceApiToken.verify(request);
    }


    public void generateSiteHomePage() {
        if (!(verifyToken().isSuccess())) {
            renderJson(AjaxResponse.fail("token verify fail"));
            return;
        }
        UtilsPageMaker utilsPageMaker = Aop.get(UtilsPageMaker.class);
        utilsPageMaker.makeIndex();


        renderJson(AjaxResponse.success());
    }


    public void generateCategoryPage() {

        if (!(verifyToken().isSuccess())) {
            renderJson(AjaxResponse.fail("token verify fail"));
            return;
        }
        Integer categoryId = getInt("categoryId");
        if (categoryId == null || categoryId < 1) {
            renderJson(AjaxResponse.fail("categoryId missing"));
            return;
        }
        Boolean withChild = getBoolean("withChild", false);

        UtilsPageMaker utilsPageMaker = Aop.get(UtilsPageMaker.class);
        WebSocketCmd   webSocketCmd   = new WebSocketCmd("", categoryId, withChild);

        utilsPageMaker.makeCategory(webSocketCmd);

        renderJson(AjaxResponse.success());
    }


    public void postContent() {

        if (!(verifyToken().isSuccess())) {
            renderJson(AjaxResponse.fail("token verify fail"));
            return;
        }
        DaoArticle entity = getModel(DaoArticle.class, "");
        if (entity == null) {
            renderJson(AjaxResponse.fail("get data fail"));
            return;
        }
        if (StringUtils.isEmpty(entity.getTitle())) {
            renderJson(AjaxResponse.fail("title empty"));
            return;

        }
        if (StringUtils.isEmpty(entity.getContent())) {
            renderJson(AjaxResponse.fail("content empty"));
            return;

        }
        Integer categoryId = entity.getCategoryId();
        if (categoryId == null || serviceCategory.getNameById(categoryId) == null) {
            renderJson(AjaxResponse.fail("categoryId error:" + categoryId));
            return;

        }
        entity.setContent(entity.getContent().replace("'", "\\"));
        entity.setIsDelete(ConstArticleDelete.ARTICLE_DELETE_DRAFT);
        entity.setPublishDate(new Date());
        entity.setIsShow(Constant.TRUE);

        String saveAsDraft = get("saveAsDraft");
        if ("false".equals(saveAsDraft)) {
            entity.setIsDelete(ConstArticleDelete.ARTICLE_DELETE_FALSE);
        }
        if (entity.save()) {
            renderJson(AjaxResponse.success());
            return;
        }

        renderJson(AjaxResponse.fail());
    }

    public void getCategoryTree() {
        if (!(verifyToken().isSuccess())) {
            renderJson(AjaxResponse.fail("token verify fail"));
            return;
        }

        List<CategoryVoForOpenApi> simpleCategoryVos = new ArrayList<>();

        LinkedList<CategoryVo> categoryVoList = new LinkedList<>();
        categoryVoList.add(serviceCategory.getRootVo());
        while (categoryVoList.size() > 0) {
            CategoryVo vo = categoryVoList.poll();
            simpleCategoryVos.add(vo.getSimpleVo());

            List<CategoryVo> children = vo.getChild();
            if (children != null && children.size() > 0) {
                categoryVoList.addAll(vo.getChild());
            }
        }
        renderJson(AjaxResponse.success().setData(simpleCategoryVos));
    }
}