package cc.dkcms.cms.template.directive.front.enjoy;


import cc.dkcms.cms.common.vo.CategoryVo;
import cc.dkcms.cms.service.ServiceCategory;
import cc.dkcms.cms.template.directive.front.enjoy.base.BaseEnjoyTemplateDirective;
import com.jfinal.aop.Aop;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

public class CategoryDetail extends BaseEnjoyTemplateDirective {


    private static final String ATTR_NAME = "category";

    private static final String ATTR_ID       = "categoryId";
    private static final String ATTR_VAR_NAME = "varName";


    @Override
    public boolean hasEnd() {
        return true;
    }

    @Override
    public void exec(Env env, Scope scope, Writer writer) {

        Integer categoryId = getInt(0, scope, -1);

        if (categoryId < 0) {
            failMsg(writer, "missing categoryId");
            return;
        }
        ServiceCategory categoryService = Aop.get(ServiceCategory.class);
        CategoryVo      vo              = categoryService.getCategoryById(categoryId);
        if (vo == null) {
            failMsg(writer, "categoryId not found: " + categoryId);
            return;
        }

        scope.set("category", vo);
        stat.exec(env, scope, writer);

    }
}
