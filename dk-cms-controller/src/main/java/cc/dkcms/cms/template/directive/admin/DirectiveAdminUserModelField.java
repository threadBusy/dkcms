package cc.dkcms.cms.template.directive.admin;

import cc.dkcms.cms.common.define.UserModelFieldType;
import cc.dkcms.cms.common.vo.UserModelFieldVo;
import cc.dkcms.cms.service.ServiceUserModel;
import cc.dkcms.cms.template.directive.base.AbstractBaseDirective;
import com.jfinal.aop.Aop;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DirectiveAdminUserModelField extends AbstractBaseDirective {

    ServiceUserModel serviceUserModel = Aop.get(ServiceUserModel.class);

    @Override
    public void exec(Env env, Scope scope, Writer writer) {


        int    fieldId = getInt(0, scope);
        String value   = getString(1, scope);


        if (fieldId == 0) {
            failMsg(writer, "CustomField fieldId missing");

            return;
        }

        UserModelFieldVo fieldVo = serviceUserModel.getFieldVo(fieldId);

        if (fieldVo == null) {
            failMsg(writer, "get CustomField error;fieldId:" + fieldId + " ");

            return;
        }

        UserModelFieldType type = UserModelFieldType.getTypeByCode(fieldVo.getType());
        if (type == null) {
            failMsg(writer, "CustomField type error;type:" + fieldVo.getType() + " >");

            return;
        }
        write(writer, getHtml(type, fieldVo, value));

    }

    private String getHtml(UserModelFieldType type, UserModelFieldVo fieldVo, String oldValue) {
        String[]     options     = StringUtils.split(fieldVo.getOptions(), ",");
        List<String> optionsList = new ArrayList<>();
        if (options != null && options.length > 0) {
            optionsList.addAll(Arrays.asList(options));
        }
        LinkedList<String> html = new LinkedList<>();
        html.add("\n<!---CustomModelField start;type=" + type + " -->");
        switch (type) {
            case INPUT:
                html.add(
                        String.format("  <input type='text' name='%s' value='%s' class='input_02'>   ",
                                fieldVo.getFieldName(),
                                oldValue == null ? "" : oldValue)
                );

                break;
            case SELECT:
                html.add("   <select name='" + fieldVo.getFieldName() + "' class='select_01'>    ");
                for (String option : optionsList) {
                    html.add("         <option value='" + option + "' "
                            + (option.equals(oldValue) ? "selected='selected'" : "")
                            + ">" + option + "</option>");

                }
                html.add("    </select>                                                                     ");
                break;


            case TEXTAREA:
                html.add("    <textarea class='textarea_01' name='" + fieldVo.getFieldName() + "'>" + (oldValue == null ? "" : oldValue) + " </textarea>         ");

                break;
            default:
                html.add("<!--default controlbox-->");

        }
        html.add("<!---CustomModelField end; -->\n");
        return StringUtils.join(html, "\n");


    }
}
