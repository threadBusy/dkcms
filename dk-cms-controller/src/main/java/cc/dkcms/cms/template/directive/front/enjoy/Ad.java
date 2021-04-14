package cc.dkcms.cms.template.directive.front.enjoy;

import cc.dkcms.cms.common.vo.AdPositionVo;
import cc.dkcms.cms.service.ServiceAd;
import cc.dkcms.cms.template.directive.front.enjoy.base.BaseEnjoyTemplateDirective;
import com.jfinal.aop.Aop;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

import java.util.Map;

public class Ad extends BaseEnjoyTemplateDirective {


    @Override
    public void exec(Env env, Scope scope, Writer writer) {

        Map<String, Object> map = getMap(scope);

        Integer id = getIntFromMap(map, "id", -1);
        if (id < 0) {
            failMsg(writer, "ad id missing");
            return;
        }

        ServiceAd    serviceAd = Aop.get(ServiceAd.class);
        AdPositionVo adVo      = serviceAd.getAdPositionVo(id);
        String       size      = "width: " + adVo.getWidth() + "px;height: " + adVo.getHeight() + "px";
        String html =
                "<div style=\"" + size + ";overflow: hidden;border:1px solid #f1f1f1\">\n"
                        + "    <img src=\"" + adVo.getContentImage() + "\" style=\"" + size + ";overflow: hidden;\">\n" +
                        "</div>";

        write(writer, html);

    }
}
