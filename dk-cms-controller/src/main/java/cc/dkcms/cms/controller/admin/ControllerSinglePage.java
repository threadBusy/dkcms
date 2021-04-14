package cc.dkcms.cms.controller.admin;

import cc.dkcms.cms.common.define.AjaxResponse;
import cc.dkcms.cms.common.define.Result;
import cc.dkcms.cms.common.vo.SinglePageVo;
import cc.dkcms.cms.controller.base.BaseAdminController;
import cc.dkcms.cms.dao.DaoSinglePage;
import cc.dkcms.cms.service.ServiceSinglePage;
import com.jfinal.aop.Aop;

public class ControllerSinglePage extends BaseAdminController {


    ServiceSinglePage serviceSinglePage = Aop.get(ServiceSinglePage.class);

    public void index() {
        redirect("/admin/singlePage/list");
    }

    public void list() {

        set("list", serviceSinglePage.getList());
    }

    public void edit() {
        Integer pageId = getInt("pageId");
        if (pageId < 1) {
            failPage("page Id missing");
            return;
        }

        set("action", "edit");
        set("page", serviceSinglePage.getPage(pageId));

    }

    public void save() {

        if (!isPost()) {
            renderJson(AjaxResponse.fail("no post"));
            return;
        }
        DaoSinglePage singlePageEntity = getModel(DaoSinglePage.class, "");
        Result        result           = serviceSinglePage.savePage(singlePageEntity);
        if (result.getIsSuccess()) {
            renderJson(AjaxResponse.success());
            return;
        }
        renderJson(AjaxResponse.fail(result.getMsg()));

    }

    public void delete() {
        Integer id = getInt("pageId");
        if (id < 1) {
            failPage("id error");
            return;
        }
        serviceSinglePage.delete(id);
        redirect("/admin/singlePage/list");
    }

    public void add() {
        set("action", "add");
        set("page", new SinglePageVo());
    }
}
