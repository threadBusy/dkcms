package cc.dkcms.cms.template.directive.front.enjoy;

import cc.dkcms.cms.common.vo.CommentVo;
import cc.dkcms.cms.service.ServiceComment;
import cc.dkcms.cms.template.directive.front.enjoy.base.BaseEnjoyTemplateDirective;
import com.jfinal.aop.Aop;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

import java.util.Map;

public class CommentList extends BaseEnjoyTemplateDirective {
    @Override
    public void exec(Env env, Scope scope, Writer writer) {

        Map<String, Object> argv = getMap(scope);

        Integer page  = getIntFromMap(argv, "page", 1);
        Integer limit = getIntFromMap(argv, "limit", 10);


        ServiceComment serviceComment = Aop.get(ServiceComment.class);

        Page<CommentVo> pageInfo = serviceComment.getApprovedListByPage(page, limit);

        if (pageInfo == null || pageInfo.getTotalRow() == 0) {
            failMsg(writer, "no comment");
            return;
        }
        int index = 0;
        for (CommentVo vo : pageInfo.getList()) {
            scope.set("item", vo);
            scope.set("index", index);
            stat.exec(env, scope, writer);
            index++;
        }
    }

    @Override
    public boolean hasEnd() {
        return true;
    }
}