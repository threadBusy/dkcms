package cc.dkcms.cms.template.directive.admin;

import cc.dkcms.cms.service.ServiceSinglePage;
import cc.dkcms.cms.template.directive.base.AbstractBaseDirective;
import cc.dkcms.cms.common.vo.SinglePageVo;
import com.jfinal.aop.Aop;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

import java.util.List;

public class DirectiveAdminSinglePageSelect extends AbstractBaseDirective {
    @Override
    public void exec(Env env, Scope scope, Writer writer) {


        ServiceSinglePage serviceSinglePage = Aop.get(ServiceSinglePage.class);

        List<SinglePageVo> list = serviceSinglePage.getList();

        // name
        String  name        = getString(0, scope);
        Integer selectValue = getInt(1, scope);

        String html = "<!-- SinglePageSelect start--->\n" +
                "<select name='" + name + "' class='select_01'>\n" +
                getOption(list, selectValue) +
                "</select><!-- SinglePageSelect end--->" +
                "\n";

        write(writer, html);
    }

    private String getOption(List<SinglePageVo> list, Integer oldSelect) {
        if (list == null || list.size() == 0) {
            return "";
        }


        StringBuilder stringBuilder = new StringBuilder();
        for (SinglePageVo vo : list) {
            stringBuilder.append("<option value='" + vo.getId() + "'");
            if (vo.getId().equals(oldSelect)) {
                stringBuilder.append("  selected=\"selected\" ");
            }

            stringBuilder.append(">" + vo.getName() + "(" + vo.getId() + ")" + "</option>\n");
        }
        return stringBuilder.toString();
    }
}
