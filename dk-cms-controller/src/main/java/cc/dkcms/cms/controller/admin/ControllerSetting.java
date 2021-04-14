package cc.dkcms.cms.controller.admin;

import cc.dkcms.cms.common.Const;
import cc.dkcms.cms.common.define.AjaxResponse;
import cc.dkcms.cms.common.define.StaticPagePathPolicy;
import cc.dkcms.cms.common.helper.FileNameHelper;
import cc.dkcms.cms.controller.base.BaseAdminController;
import cc.dkcms.cms.dao.DaoSetting;
import cc.dkcms.cms.service.ServiceSetting;
import com.jfinal.aop.Inject;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ControllerSetting extends BaseAdminController {


    @Inject
    ServiceSetting serviceSetting;

    public void index() {
        set("groupList", serviceSetting.getGroup());
        set("list", serviceSetting.getAll());
    }

    public void setting() {
        List<String> setting = new ArrayList<>();
        setting.add(Const.SETTING_CAN_REG);
        setting.add(Const.SETTING_CAN_COMMENT);
        setting.add(Const.SETTING_USE_DEFAULT_TEMPLATE);
        setting.add(Const.SETTING_STATIC_PAGE_FILE_PATH_POLICY);

        if (isPost()) {
            setting.forEach(s -> serviceSetting.setValue(s, get(s)));
            // common中的一个变量，需要手动设置过去
            FileNameHelper.POLICY_PATH = StaticPagePathPolicy.of(
                    get(Const.SETTING_STATIC_PAGE_FILE_PATH_POLICY)
            );
            renderJson(AjaxResponse.success("保存成功"));
            return;
        }

        setting.forEach(s -> set(s, serviceSetting.getValue(s)));
    }

    public void save() {
        if (!isPost()) {
            renderJson(AjaxResponse.fail("no post"));
            return;
        }
        DaoSetting entity = getModel(DaoSetting.class, "");
        if (StringUtils.isEmpty(entity.getSvalue())) {
            renderJson(AjaxResponse.fail("值为空"));
            return;
        }
        if (entity.getId() != null) {
            entity.update();
        } else {
            entity.save();
        }
        renderJson(AjaxResponse.fail("保存成功"));
    }

    public void edit() {

        Integer id = getInt("id", 0);
        if (id < 1) {
            msgPage("id missing", "/admin/setting/all");
            return;
        }
        set("setting", serviceSetting.getById(id));

    }

    public void add() {
    }

}
