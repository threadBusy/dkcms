package cc.dkcms.cms.template.directive.admin;

import cc.dkcms.cms.common.define.TagType;
import cc.dkcms.cms.common.vo.TagVo;
import cc.dkcms.cms.service.ServiceTag;
import cc.dkcms.cms.template.directive.base.AbstractBaseDirective;
import com.jfinal.aop.Aop;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import org.apache.commons.lang.RandomStringUtils;

import java.util.List;

public class DirectiveAdminTagSelect extends AbstractBaseDirective {
    @Override
    public void exec(Env env, Scope scope, Writer writer) {

        String fieldName = getString(0, scope, "tagId");

        TagType type = TagType.ARTICLE;
        if ("category".equals(getString(1, scope, "article"))) {
            type = TagType.CATEGORY;
        }
        String oldTagIds = getString(2, scope, "");


        ServiceTag  serviceTag = Aop.get(ServiceTag.class);
        List<TagVo> tagList    = serviceTag.getListByType(type);

        if (tagList == null || tagList.size() == 0) {
            write(writer, "<a href='#/admin/tag/index' class='btn_primary' target='_blank'>暂无标签。点此添加一个标签</a>");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<!-- TagSelect start--->\n");
        stringBuilder.append("<div id='" + RandomStringUtils.randomAlphanumeric(6) + "' class=\"common-tag-select\">\n");
        stringBuilder.append("  <input type=\"hidden\" name=\"" + fieldName + "\" value=\"" + oldTagIds + "\">");

        for (TagVo vo : tagList) {
            stringBuilder.append("<div class=\"common-tag-select-option\" data-id=\"" + vo.getId() + "\">" + vo.getTagName() + "</div>");
        }

        stringBuilder.append("</div>\n");
        stringBuilder.append("<!-- TagSelect end--->\n");

        write(writer, stringBuilder.toString());


    }
}