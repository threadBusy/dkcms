package cc.dkcms.cms.template.directive.front.enjoy;

import cc.dkcms.cms.common.vo.SinglePageVo;
import cc.dkcms.cms.service.ServiceSinglePage;
import cc.dkcms.cms.template.directive.front.enjoy.base.BaseEnjoyTemplateDirective;
import com.jfinal.aop.Aop;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

import java.util.Map;

public class SinglePageUrl extends BaseEnjoyTemplateDirective {

    @Override
    public void exec(Env env, Scope scope, Writer writer) {


        Map<String, Object> map    = getMap(scope);
        Integer             pageId = getIntFromMap(map, "pageId", -1);

        if (pageId < 0) {
            failMsg(writer, " page id missing ");
            return;
        }

        ServiceSinglePage serviceSinglePage = Aop.get(ServiceSinglePage.class);
        SinglePageVo      vo                = serviceSinglePage.getPage(pageId);
        if (vo == null) {
            failMsg(writer, " page not found: " + pageId);
            return;
        }
        write(writer, vo.getPageUrl());
    }

}
