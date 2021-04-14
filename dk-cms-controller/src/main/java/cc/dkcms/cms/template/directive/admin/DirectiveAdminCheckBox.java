package cc.dkcms.cms.template.directive.admin;

import cc.dkcms.cms.template.directive.base.AbstractBaseDirective;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DirectiveAdminCheckBox extends AbstractBaseDirective {
    @Override
    public void exec(Env env, Scope scope, Writer writer) {


        String name  = getString(0, scope);
        String value = getString(1, scope);

        String       optionsString = getString(2, scope);
        List<String> options       = new ArrayList<>();

        if (!StringUtils.isEmpty(optionsString)) {
            options.addAll(Arrays.asList(StringUtils.split(optionsString, ",")));
        }

        String isMultiple = "false";

        try {
            isMultiple = String.valueOf(exprList.getExpr(3).eval(scope));
        } catch (Exception e) {
            //e.printStackTrace();
        }


        String       checkboxId = RandomStringUtils.randomAlphabetic(8);
        List<String> html       = new ArrayList<>();
        html.add("<div class='dkcms-checkbox' id='" + checkboxId + "' data-multiple='" + isMultiple + "'>                                       ");
        html.add("    <input type='hidden' name='" + name + "' value='" + value + "'>                      ");
        for (String option : options) {
            html.add("    <div class='cms-checkbox-option' data-value='" + option + "'> " + option + "</div> ");
        }
        html.add("</div>   ");

        write(writer, StringUtils.join(html, "\n"));
    }
}
