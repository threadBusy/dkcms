package cc.dkcms.cms.template.directive.front.enjoy;

import cc.dkcms.cms.common.define.TagType;
import cc.dkcms.cms.common.vo.ArticleVo;
import cc.dkcms.cms.common.vo.CategoryVo;
import cc.dkcms.cms.common.vo.TagVo;
import cc.dkcms.cms.service.ServiceTag;
import cc.dkcms.cms.template.directive.front.enjoy.base.BaseEnjoyTemplateDirective;
import com.jfinal.aop.Aop;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

import java.util.Map;

public class TagArticleList extends BaseEnjoyTemplateDirective {


    @Override
    public void exec(Env env, Scope scope, Writer writer) {


        Map<String, Object> map = getMap(scope);

        Integer tagId = getIntFromMap(map, "tagId", -1);
        Integer limit = getIntFromMap(map, "limit", 100);

        if (tagId < 0) {
            failMsg(writer, "tagId missing");
            return;
        }


        // 读取文章列表
        ServiceTag tagService = Aop.get(ServiceTag.class);
        TagVo      tagVo      = tagService.getTag(tagId);
        if (tagVo == null) {
            failMsg(writer, "tagId not found:" + tagId);
            return;
        }

        scope.set("tagInfo", tagVo);
        if (TagType.ARTICLE.getCode() == tagVo.getTagType()) {
            int index = 0;
            for (ArticleVo vo : tagVo.getArticleVoList()) {
                scope.set("item", vo);
                scope.set("index", index);
                stat.exec(env, scope, writer);

                if (index > limit) {
                    return;
                }
                index++;
            }
        } else {
            int index = 0;
            for (CategoryVo vo : tagVo.getCategoryVoList()) {
                scope.set("item", vo);
                scope.set("index", index);
                stat.exec(env, scope, writer);
                if (index > limit) {
                    return;
                }
                index++;
            }

        }
    }

    @Override
    public boolean hasEnd() {
        return true;
    }

}