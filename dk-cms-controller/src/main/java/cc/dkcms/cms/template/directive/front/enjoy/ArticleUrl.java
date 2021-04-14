package cc.dkcms.cms.template.directive.front.enjoy;


import cc.dkcms.cms.common.vo.ArticleVo;
import cc.dkcms.cms.service.ServiceContent;
import cc.dkcms.cms.template.directive.front.enjoy.base.BaseEnjoyTemplateDirective;
import com.jfinal.aop.Aop;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

public class ArticleUrl extends BaseEnjoyTemplateDirective {

    @Override
    public void exec(Env env, Scope scope, Writer writer) {


        Integer articleId = getInt(0, scope);

        if (articleId < 0) {
            failMsg(writer, " article id missing");
            return;
        }
        ServiceContent articleService = Aop.get(ServiceContent.class);

        ArticleVo vo = articleService.getArticleVo(articleId);
        if (vo == null) {
            failMsg(writer, " article not found " + articleId);
            return;
        }
        write(writer, vo.getArticleUrl());
    }

}