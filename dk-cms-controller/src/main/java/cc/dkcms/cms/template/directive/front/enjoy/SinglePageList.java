package cc.dkcms.cms.template.directive.front.enjoy;

import cc.dkcms.cms.common.vo.SinglePageVo;
import cc.dkcms.cms.service.ServiceSinglePage;
import cc.dkcms.cms.template.directive.front.enjoy.base.BaseEnjoyTemplateDirective;
import com.jfinal.aop.Aop;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

import java.util.List;

public class SinglePageList extends BaseEnjoyTemplateDirective {

    @Override
    public boolean hasEnd() {
        return true;
    }

    @Override
    public void exec(Env env, Scope scope, Writer writer) {


        ServiceSinglePage serviceSinglePage = Aop.get(ServiceSinglePage.class);

        List<SinglePageVo> list = serviceSinglePage.getList();

        int index = 0;
        for (SinglePageVo vo : list) {
            scope.set("item", vo);
            scope.set("index", index);
            stat.exec(env, scope, writer);
            index++;
        }

    }

}