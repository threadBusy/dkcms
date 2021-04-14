package cc.dkcms.cms.template.directive.admin;

import com.jfinal.i18n.I18n;
import com.jfinal.i18n.Res;
import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

import java.nio.charset.StandardCharsets;

public class DirectiveAdminGetI18nMessage extends Directive {

    private static Res res;

    static {
        res = I18n.use("messages", "zh_CN");
    }

    @Override
    public void exec(Env env, Scope scope, Writer writer) {
        try {
            String key = String.valueOf(exprList.getExpr(0));
            String val = new String(res.get(key).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            write(writer, val);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
