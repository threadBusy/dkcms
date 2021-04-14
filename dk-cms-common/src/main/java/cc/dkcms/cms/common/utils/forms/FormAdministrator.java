package cc.dkcms.cms.common.utils.forms;

import cc.dkcms.cms.common.utils.FormUtils;

import java.util.List;

import static cc.dkcms.cms.common.utils.FormUtils.*;

public class FormAdministrator {

    public static List<FormUtils.FormItem> get() {


        FormUtils formUtils = new FormUtils();
        formUtils
                .addFormItem("id", TYPE_HIDDEN, "编号")
                .addFormItem("username", TYPE_INPUT, "登录用户名")
                .addFormItem("password", TYPE_INPUT, "登录密码")

                .addFormItem("realname", TYPE_INPUT, "管理员姓名")
                .addFormItem("telephone", TYPE_INPUT, "联系电话")
                .addFormItem("email", TYPE_INPUT, "联系邮箱")
                .addFormItem("enable", TYPE_SELECT, "初始状态", "可以正常登录:1;暂不能登录:0");


        return formUtils.getFormItems();

    }
}
