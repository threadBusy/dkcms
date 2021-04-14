package cc.dkcms.cms.interceptor;

import cc.dkcms.cms.common.define.RequestVar;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

import javax.servlet.http.HttpServletRequest;

public class TemplateVariables implements Interceptor {
    @Override
    public void intercept(Invocation inv) {

        Controller controller = inv.getController();
        if (controller != null) {
            HttpServletRequest request = controller.getRequest();


            RequestVar var = new RequestVar();
            var.setFullUrl(request.getRequestURL().toString());
            var.setRequestUri(request.getRequestURI());
            var.setQueryString(request.getQueryString());
            controller.set("request", var);
        }
        inv.invoke();

    }
}
