package cc.dkcms.cms.template.directive.common;

import cc.dkcms.cms.service.ServiceSetting;
import com.jfinal.aop.Aop;
import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import org.apache.commons.lang.StringUtils;

public class DirectiveGetSetting extends Directive {

    public static final ServiceSetting serviceSetting = Aop.get(ServiceSetting.class);

    @Override
    public void exec(Env env, Scope scope, Writer writer) {
        String key = String.valueOf(exprList.getFirstExpr().eval(scope));
        String val = "noValue";
        if (!StringUtils.isEmpty(key)) {
            val = serviceSetting.getValue(key);
        }

        write(writer, val);
    }
}
