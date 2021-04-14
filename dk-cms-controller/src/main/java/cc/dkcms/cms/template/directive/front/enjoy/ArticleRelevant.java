package cc.dkcms.cms.template.directive.front.enjoy;

import cc.dkcms.cms.common.vo.ArticleVo;
import cc.dkcms.cms.service.ServiceContent;
import cc.dkcms.cms.template.directive.front.enjoy.base.BaseEnjoyTemplateDirective;
import com.jfinal.aop.Aop;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

import java.util.List;

public class ArticleRelevant extends BaseEnjoyTemplateDirective {

    private ServiceContent serviceContent = Aop.get(ServiceContent.class);

    @Override
    public boolean hasEnd() {
        return true;
    }

    @Override
    public void exec(Env env, Scope scope, Writer writer) {


        Integer articleId = getInt(0, scope);
        if (articleId < 0) {
            failMsg(writer, " article id missing");
            return;
        }


        List<ArticleVo> list = serviceContent.getRelevantList(articleId, 5);
        if (list == null || list.size() == 0) {
            failMsg(writer, " relevant not found");
            return;
        }

        int index = 0;
        for (ArticleVo vo : list) {
            scope.set("item", vo);
            scope.set("index", index);
            stat.exec(env, scope, writer);
            index++;
        }


    }
}
