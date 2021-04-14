package cc.dkcms.cms.service.category;

import cc.dkcms.cms.common.vo.CategoryVo;

import java.util.Comparator;

public class CategoryVoCompare implements Comparator<CategoryVo> {
    @Override
    public int compare(CategoryVo o1, CategoryVo o2) {
        if (o2.getSort() != null && o1.getSort() != null) {
            int s = o2.getSort() - o1.getSort();
            if (s != 0) {
                return s;
            }
        }
        return o2.getId() - o1.getId();
    }
}
