package cc.dkcms.cms.template.directive.front.enjoy;

import cc.dkcms.cms.common.vo.ArticleVo;
import cc.dkcms.cms.service.ServiceContent;
import cc.dkcms.cms.template.directive.front.enjoy.base.BaseEnjoyTemplateDirective;
import com.jfinal.aop.Aop;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

import java.util.Map;


public class ArticleDetail extends BaseEnjoyTemplateDirective {
    private static final String ATTR_NAME     = "article";
    private static final String ATTR_ID       = "articleId";
    private static final String ATTR_VAR_NAME = "varName";

    @Override
    public boolean hasEnd() {
        return true;
    }

    @Override
    public void exec(Env env, Scope scope, Writer writer) {


        ServiceContent articleService = Aop.get(ServiceContent.class);
        assert articleService != null;
        Map<String, Object> map = getMap(scope);

        Integer articleId = getIntFromMap(map, "id", -1);
        if (articleId < 0) {
            failMsg(writer, "article id missing");
            return;
        }


        ArticleVo articleVo = articleService.getArticleVo(articleId);
        if (articleVo == null) {
            failMsg(writer, "article not found :" + articleId);
            return;
        }

        scope.set("article", articleVo);
        stat.exec(env, scope, writer);
    }
}
