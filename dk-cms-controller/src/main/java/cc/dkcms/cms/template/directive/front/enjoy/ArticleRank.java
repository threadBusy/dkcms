package cc.dkcms.cms.template.directive.front.enjoy;

import cc.dkcms.cms.common.define.ArticleRankType;
import cc.dkcms.cms.common.vo.ArticleVo;
import cc.dkcms.cms.service.ServiceContent;
import cc.dkcms.cms.template.directive.front.enjoy.base.BaseEnjoyTemplateDirective;
import com.jfinal.aop.Aop;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

import java.util.List;
import java.util.Map;

public class ArticleRank extends BaseEnjoyTemplateDirective {


    @Override
    public boolean hasEnd() {
        return true;
    }
    @Override
    public void exec(Env env, Scope scope, Writer writer) {

        Map<String, Object> map = getMap(scope);


        Integer categoryId = getIntFromMap(map, "categoryId", 0);
        String  rankType   = getStrFromMap(map, "type", "latest");
        Integer limit      = getIntFromMap(map, "limit", 10);
        Boolean withChild  = getBoolFromMap(map, "withChild", false);

        ArticleRankType articleRankType = ArticleRankType.getType(rankType);

        ServiceContent  serviceContent = Aop.get(ServiceContent.class);
        List<ArticleVo> list           = serviceContent.getRankList(articleRankType, limit, categoryId, withChild);


        int index = 0;
        for (ArticleVo vo : list) {
            scope.set("item", vo);
            scope.set("index", index);
            stat.exec(env, scope, writer);
            index++;
        }


    }
}