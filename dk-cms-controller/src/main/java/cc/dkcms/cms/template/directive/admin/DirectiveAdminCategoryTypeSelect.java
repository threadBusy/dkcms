package cc.dkcms.cms.template.directive.admin;

import cc.dkcms.cms.common.define.CategoryType;
import cc.dkcms.cms.common.vo.UserModelVo;
import cc.dkcms.cms.service.ServiceUserModel;
import cc.dkcms.cms.template.directive.base.AbstractBaseDirective;
import com.jfinal.aop.Aop;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DirectiveAdminCategoryTypeSelect extends AbstractBaseDirective {

    final static Map<Integer, String> typeList = new ConcurrentHashMap<>();

    static {
        typeList.put(CategoryType.ARTICLE.getType(), CategoryType.ARTICLE.getName());
        typeList.put(CategoryType.ALBUM.getType(), CategoryType.ALBUM.getName());

        ServiceUserModel    serviceUserModel = Aop.get(ServiceUserModel.class);
        List<UserModelVo> modelList        = serviceUserModel.getAllModel();
        if (modelList != null && modelList.size() > 0) {
            for (UserModelVo modelVo : modelList) {
                typeList.put(modelVo.getId(), modelVo.getModelName());
            }
        }
    }


    @Override
    public void exec(Env env, Scope scope, Writer writer) {

        String fieldName = String.valueOf(exprList.getFirstExpr().eval(scope));

        Integer oldValueInt = getInt(exprList.getExpr(1).eval(scope), 0);


        String html = "<!-- CategoryTypeSelect start--->\n" +
                "<select name='" + fieldName + "' class='select_01'>\n" +
                getOption(typeList, oldValueInt) +
                "</select>\n" +
                "<!-- CategoryTypeSelect end--->\n";

        write(writer, html);

    }

    private String getOption(Map<Integer, String> list, Integer oldSelect) {
        if (list == null || list.size() == 0) {
            return "";
        }


        StringBuilder stringBuilder = new StringBuilder();
        for (Integer type : list.keySet()) {
            stringBuilder.append("<option value='" + type + "'");
            if (type.equals(oldSelect)) {
                stringBuilder.append("  selected=\"selected\" ");
            }

            stringBuilder.append(">" + list.get(type) + "(" + type + ")" + "</option>\n");
        }
        return stringBuilder.toString();
    }
}
