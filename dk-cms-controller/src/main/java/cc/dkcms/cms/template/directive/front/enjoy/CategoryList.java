package cc.dkcms.cms.template.directive.front.enjoy;

import cc.dkcms.cms.common.vo.CategoryVo;
import cc.dkcms.cms.service.ServiceCategory;
import cc.dkcms.cms.template.directive.front.enjoy.base.BaseEnjoyTemplateDirective;
import com.jfinal.aop.Aop;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class CategoryList extends BaseEnjoyTemplateDirective {

    private static final String ATTR_NAME = "categoryList";

    private static final String ATTR_PARENT_ID      = "parentId";
    private static final String ATTR_VAR_NAME       = "varName";
    private static final String ATTR_ORDER_BY       = "orderBy";
    private static final String ATTR_ORDER_SORT     = "orderSort";
    private static final String ATTR_INCLUDE_HIDDEN = "includeHidden";

    @Override
    public void exec(Env env, Scope scope, Writer writer) {


        Map<String, Object> map = getMap(scope);

        Integer parentId = getIntFromMap(map, "parentId", -1);
        if (parentId == -1) {
            parentId = 0;
        }
        Boolean withChild = getBoolFromMap(map, "withChild", true);

        //orderBy
        String orderBy   = getStrFromMap(map, "orderBy", "sort");
        String orderSort = getStrFromMap(map, "orderSort", "desc");


        ServiceCategory  serviceCategory = Aop.get(ServiceCategory.class);
        List<CategoryVo> categoryVoList  = serviceCategory.getChildVoList(parentId);


        if (!withChild) {
            categoryVoList.removeIf(vo -> vo.getSort().equals(-1));
        }


        categoryVoList.sort(new CategoryCompare(orderBy, orderSort));
        int index = 0;
        for (CategoryVo vo : categoryVoList) {
            scope.set("index", index++);
            scope.set("item", vo);
            stat.exec(env, scope, writer);
        }
    }

    @Override
    public boolean hasEnd() {
        return true;
    }


    static class CategoryCompare implements Comparator<CategoryVo> {

        private String orderBy;
        private String orderSort;

        public CategoryCompare(String orderBy, String orderSort) {
            this.orderBy = orderBy;
            this.orderSort = orderSort;
        }

        @Override
        public int compare(CategoryVo o1, CategoryVo o2) {
            if ("sort".equals(orderBy)) {
                if ("desc".equals(orderSort)) {
                    return o2.getSort() - o1.getSort();
                } else {
                    return o1.getSort() - o2.getSort();
                }
            }
            if ("id".equals(orderSort)) {
                return o1.getId() - o2.getId();
            } else {
                return o2.getId() - o1.getId();
            }
        }
    }

}
