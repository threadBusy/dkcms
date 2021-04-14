package cc.dkcms.cms.template.directive.admin;

import cc.dkcms.cms.service.ServiceTemplateFile;
import cc.dkcms.cms.common.vo.TemplateFileVo;
import com.jfinal.aop.Aop;
import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import org.apache.commons.lang.StringUtils;

import java.util.List;

public class DirectiveAdminTemplateFileSelect extends Directive {


    ServiceTemplateFile serviceTemplateFile = Aop.get(ServiceTemplateFile.class);


    @Override
    public void exec(Env env, Scope scope, Writer writer) {

        String name      = String.valueOf(exprList.getFirstExpr().eval(scope));
        String oldSelect = String.valueOf(exprList.getExpr(1).eval(scope));


        List<TemplateFileVo> list = serviceTemplateFile.getAllFile();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<select name=\"" + name + "\" class=\"select_01\">\n");
        stringBuilder.append("<option value=\"\"></option>");

        for (TemplateFileVo vo : list) {

            stringBuilder.append("<option value=\"" + vo.getFileName() + "\"");

            if (vo.getFileName().equals(oldSelect)) {
                stringBuilder.append("  selected=\"selected\" ");
            }

            stringBuilder.append(">" + vo.getFileName() + "</option>\n");

        }
        stringBuilder.append("</select>");
        if (!StringUtils.isEmpty(oldSelect)) {
            stringBuilder.append("<span>&nbsp;(" + oldSelect + ")</span>");
        }

        write(writer, stringBuilder.toString());


    }
}
