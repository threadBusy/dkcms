package cc.dkcms.cms.template.directive.admin;

import cc.dkcms.cms.service.ServiceCategory;
import cc.dkcms.cms.template.directive.base.AbstractBaseDirective;
import cc.dkcms.cms.common.vo.CategoryVo;
import com.jfinal.aop.Aop;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

import java.util.List;

import static cc.dkcms.cms.common.Constant.ROOT_CATEGORY_ID;

public class DirectiveAdminCategorySelect extends AbstractBaseDirective {
    @Override
    public void exec(Env env, Scope scope, Writer writer) {

        ServiceCategory  serviceCategory = Aop.get(ServiceCategory.class);
        List<CategoryVo> list            = serviceCategory.getChildVoList(ROOT_CATEGORY_ID);

        String  fieldName   = getString(0, scope);
        Integer selectValue = getInt(1, scope);


        String html = "<!-- CategorySelector start--->\n" +
                "<select name='" + fieldName + "' class='select_01'>\n" +
                getOption(list, 0, selectValue) +
                "</select><!-- CategorySelector end--->" +
                "\n";

        write(writer, html);
    }

    private String getOption(List<CategoryVo> list, int level, Integer oldSelect) {
        if (list == null || list.size() == 0) {
            return "";
        }
        StringBuilder levelSb = new StringBuilder();
        level += 1;

        for (int i = 1; i < level; i++) {
            levelSb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        }
        levelSb.append("↳");
        //↳
        String levelStr = levelSb.toString();

        StringBuilder stringBuilder = new StringBuilder();
        for (CategoryVo vo : list) {

            stringBuilder.append("<option value='" + vo.getId() + "'");
            if (vo.getId().equals(oldSelect)) {
                stringBuilder.append("  selected=\"selected\" ");
            }

            stringBuilder.append(">" + levelStr + vo.getCategoryName() + "(" + vo.getId() + ")" + "</option>\n");

            if (vo.getChild() != null) {
                stringBuilder.append(getOption(vo.getChild(), level, oldSelect));
            }
        }
        return stringBuilder.toString();
    }


}
