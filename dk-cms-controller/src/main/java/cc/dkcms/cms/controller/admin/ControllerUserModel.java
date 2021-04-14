package cc.dkcms.cms.controller.admin;

import cc.dkcms.cms.common.define.AjaxResponse;
import cc.dkcms.cms.common.vo.UserModelFieldVo;
import cc.dkcms.cms.common.vo.UserModelVo;
import cc.dkcms.cms.controller.base.BaseAdminController;
import cc.dkcms.cms.dao.DaoModel;
import cc.dkcms.cms.dao.DaoModelField;
import cc.dkcms.cms.service.ServiceUserModel;
import com.jfinal.aop.Aop;
import org.apache.commons.lang.StringUtils;

public class ControllerUserModel extends BaseAdminController {

    ServiceUserModel serviceUserModel = Aop.get(ServiceUserModel.class);

    public void index() {
        set("list", serviceUserModel.getAllModel());

    }

    public void add() {
        set("model", new UserModelVo());
        set("action", "edit");
    }

    public void edit() {
        Integer id = getInt("id");
        if (id < 1) {
            failPage("id missing");
            return;
        }
        set("model", serviceUserModel.getVoById(id));
        set("field", new UserModelFieldVo());
    }

    public void saveModel() {

        if (!isPost()) {
            renderJson(AjaxResponse.fail("not post"));
            return;
        }

        String name = get("model_name");
        if (StringUtils.isEmpty(name)) {
            renderJson(AjaxResponse.fail("modelName empty"));
            return;
        }

        if (serviceUserModel.checkExist(name)) {
            renderJson(AjaxResponse.fail("modelName has exists:" + name));
            return;
        }
        DaoModel entity = new DaoModel();
        entity.setModelName(name);
        if (serviceUserModel.save(entity)) {
            renderJson(AjaxResponse.fail("modelName save error"));
            return;
        }
        Integer id = serviceUserModel.getIdByName(name);
        renderJson(AjaxResponse.success(String.valueOf(id)));
    }


    public void saveField() {

        if (!isPost()) {
            renderJson(AjaxResponse.fail("no post"));
            return;
        }
        DaoModelField entity = getModel(DaoModelField.class, "");
        if (serviceUserModel.saveField(entity)) {
            renderJson(AjaxResponse.success(String.valueOf(entity.getModelId())));
            return;
        }
        renderJson(AjaxResponse.fail("保存失败"));

    }


    public void deleteField() {
        Integer          fieldId = getInt("id");
        UserModelFieldVo fieldVo = serviceUserModel.getFieldVo(fieldId);
        if (fieldVo == null) {
            failPage("fieldId  missing", "#/admin/ad/list");
            return;
        }
        serviceUserModel.deleteField(fieldId);
        msgPage("操作成功", "/admin/model/edit?id=" + fieldVo.getModelId());
    }

    public void editField() {
        Integer          fieldId = getInt("id");
        UserModelFieldVo fieldVo = serviceUserModel.getFieldVo(fieldId);
        if (fieldVo == null) {
            failPage("fieldId  error", "#/admin/ad/list");
            return;
        }

        set("model", serviceUserModel.getVoById(fieldVo.getModelId()));
        set("field", fieldVo);
        set("action", "editField");
        //in.setViewName("model/edit");
    }


}
