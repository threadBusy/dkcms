package cc.dkcms.cms.template.directive.front.enjoy;

import cc.dkcms.cms.common.vo.CategoryVo;
import cc.dkcms.cms.service.ServiceCategory;
import cc.dkcms.cms.template.directive.front.enjoy.base.BaseEnjoyTemplateDirective;
import com.jfinal.aop.Aop;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

public class CategoryUrl extends BaseEnjoyTemplateDirective {
    private static final String ATTR_NAME         = "categoryUrl";
    private static final String ATTR_QUERY_STRING = "queryString";

    @Override
    public void exec(Env env, Scope scope, Writer writer) {


        Integer categoryId = getInt(0, scope, -1);


        if (categoryId < 0) {
            failMsg(writer, " category missing");
            return;
        }

        ServiceCategory categoryService = Aop.get(ServiceCategory.class);
        CategoryVo      vo              = categoryService.getCategoryById(categoryId);
        if (vo == null) {
            failMsg(writer, "getCategoryById return null:" + categoryId);
            return;
        }

        write(writer, vo.getCategoryUrl());

    }


}