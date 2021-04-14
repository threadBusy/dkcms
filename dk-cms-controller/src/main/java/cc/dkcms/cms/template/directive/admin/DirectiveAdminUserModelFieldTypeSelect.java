package cc.dkcms.cms.template.directive.admin;

import cc.dkcms.cms.common.define.UserModelFieldType;
import cc.dkcms.cms.template.directive.base.AbstractBaseDirective;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

public class DirectiveAdminUserModelFieldTypeSelect extends AbstractBaseDirective {

    @Override
    public void exec(Env env, Scope scope, Writer writer) {

        UserModelFieldType[] list = UserModelFieldType.getAll();


        String  name   = String.valueOf(exprList.getFirstExpr().eval(scope));
        Integer oldVal = Integer.valueOf(String.valueOf(exprList.getExpr(1).eval(scope)));


        String html = "<!-- FieldTypeSelect start--->\n" +
                "<select name='" + name + "' class='select_01'>\n" +
                getOption(list, oldVal) +
                "</select><!-- FieldTypeSelect end--->" +
                "\n";

        write(writer, html);
    }

    private String getOption(UserModelFieldType[] list, Integer oldSelect) {
        if (list == null || list.length == 0) {
            return "";
        }


        StringBuilder stringBuilder = new StringBuilder();
        for (UserModelFieldType field : list) {
            stringBuilder.append("<option value='" + field.getCode() + "'");
            if (field.getCode() == (oldSelect)) {
                stringBuilder.append("  selected=\"selected\" ");
            }

            stringBuilder.append(">" + field.getName() + "(" + field.getCode() + ")" + "</option>\n");
        }
        return stringBuilder.toString();
    }
}
