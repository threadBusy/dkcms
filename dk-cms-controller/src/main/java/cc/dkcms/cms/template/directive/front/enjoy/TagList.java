package cc.dkcms.cms.template.directive.front.enjoy;

import cc.dkcms.cms.common.define.TagType;
import cc.dkcms.cms.common.vo.TagVo;
import cc.dkcms.cms.service.ServiceTag;
import cc.dkcms.cms.template.directive.front.enjoy.base.BaseEnjoyTemplateDirective;
import com.jfinal.aop.Aop;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

import java.util.List;
import java.util.Map;

public class TagList extends BaseEnjoyTemplateDirective {


    @Override
    public boolean hasEnd() {
        return true;
    }

    @Override
    public void exec(Env env, Scope scope, Writer writer) {

        Map<String, Object> map = getMap(scope);

        String  tagType = getStrFromMap(map, "type", "content");
        Integer limit   = getIntFromMap(map, "limit", 100);

        ServiceTag serviceTag = Aop.get(ServiceTag.class);


        List<TagVo> list = serviceTag.getListByType(TagType.getType(tagType));

        int index = 0;
        for (TagVo vo : list) {
            scope.set("item", vo);
            scope.set("index", index);
            stat.exec(env, scope, writer);

            if (index >= limit) {
                return;
            }
            index++;
        }
    }
}