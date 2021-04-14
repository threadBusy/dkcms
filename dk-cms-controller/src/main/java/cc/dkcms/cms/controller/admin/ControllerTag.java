package cc.dkcms.cms.controller.admin;

import cc.dkcms.cms.common.define.AjaxResponse;
import cc.dkcms.cms.common.vo.TagVo;
import cc.dkcms.cms.controller.base.BaseAdminController;
import cc.dkcms.cms.dao.DaoTag;
import cc.dkcms.cms.service.ServiceTag;
import com.jfinal.aop.Aop;
import com.jfinal.plugin.activerecord.Page;

public class ControllerTag extends BaseAdminController {


    ServiceTag serviceTag = Aop.get(ServiceTag.class);

    public void delete() {

        Integer id = getInt("id");
        if (id == null || id < 1) {
            failPage("id error");
            return;
        }

        serviceTag.delete(id);
        msgPage("操作成功", "/admin/tag/index");
    }

    public void index() {

        Integer page    = getInt("page", 1);
        Integer perPage = 10;

        Page<TagVo> pageInfo = serviceTag.getPage(page, perPage);
        set("list", pageInfo.getList());
        set("pageInfo", pageInfo);
    }

    public void save() {
        if (!isPost()) {
            renderJson(AjaxResponse.fail("no post"));
        }
        DaoTag entity = getModel(DaoTag.class, "");
        serviceTag.saveTag(entity);
        renderJson(AjaxResponse.success());
    }

    public void edit() {
        Integer id = getInt("id");
        if (id < 1) {
            failPage("id error");
            return;
        }

        set("action", "add");
        set("tag", serviceTag.getTag(id));
        //in.setViewName("tag/add");

    }


    public void add() {
        set("tag", new DaoTag());
        set("action", "edit");
    }
}
