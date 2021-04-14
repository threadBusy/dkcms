package cc.dkcms.cms.service;

import cc.dkcms.cms.common.define.Result;
import cc.dkcms.cms.common.vo.CategoryVo;
import cc.dkcms.cms.dao.DaoArticle;
import cc.dkcms.cms.dao.DaoCategory;
import cc.dkcms.cms.service.converter.CategoryConverter;
import com.jfinal.aop.Aop;

import java.util.List;

import static cc.dkcms.cms.common.Constant.ROOT_CATEGORY_ID;

public class ServiceCategory {

    private final DaoCategory         categoryDao       = Aop.get(DaoCategory.class);
    private final CategoryConverter   categoryConverter = Aop.get(CategoryConverter.class);
    private final ServiceCategoryTree categoryTree      = Aop.get(ServiceCategoryTree.class);

    /**
     * ServiceCategory 依赖 ServiceCategoryTree 单向
     */


    public CategoryVo getRootVo() {
        return categoryConverter.convert(DaoCategory.dao.getRootCategory());
    }

    public List<CategoryVo> getCategoryListByTagId(Integer tagId) {
        List<DaoCategory> list = categoryDao.getCategoryListByTagId(tagId);
        return categoryConverter.convertList(list);
    }


    public int getChildTotalNum(Integer categoryId) {
        return categoryDao.getChildTotalNum(categoryId);
    }

    /**
     * 方法有bug
     * 1.如果 withSub = true  ，返回 list 中包含 入参cid。否则，不包含
     *
     * @param categoryId
     * @param withSubCategory
     * @return
     */
    public List<Integer> getChildIdList(Integer categoryId, Boolean withSubCategory) {

        if (withSubCategory == null) {
            withSubCategory = false;
        }
        return categoryTree.getChildIdList(categoryId, withSubCategory);

        /*
        if (!withSubCategory) {
            return categoryTree.getChildIdList(categoryId, false);
        }
        List<Integer>  ret   = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(categoryId);
        while (!queue.isEmpty()) {
            Integer id = queue.remove();
            ret.add(id);
            queue.addAll(categoryTree.getChildIdList(id, false));
        }
        return ret;
        */
    }


    public List<CategoryVo> getChildVoList(Integer categoryId) {
        return categoryTree.getChildVoList(categoryId);
    }

    public CategoryVo getCategoryVoById(Integer id) {
        return categoryConverter.convert(categoryDao.findById(id));

    }

    public CategoryVo getCategoryById(Integer id) {
        if (id == null) {
            return null;
        }
        return categoryTree.getVoById(id);
    }


    public String getNameById(Integer categoryId) {
        if (categoryId == ROOT_CATEGORY_ID) {
            return getRootVo().getCategoryName();
        }
        CategoryVo vo = getCategoryById(categoryId);
        if (vo == null) {
            return null;
        }
        return vo.getCategoryName();
    }

    public Result save(DaoCategory daoCategory) {
        if (daoCategory.getId() == null) {
            if (daoCategory.save()) {
                //categoryTree.insert(daoCategory.getId());
                categoryTree.reBuild();
                return Result.success();
            }
            return Result.fail("db error");
        }

        if (daoCategory.update()) {
            //categoryTree.update(daoCategory.getId());
            categoryTree.reBuild();

            return Result.success();
        }
        return Result.fail("db error");

    }
/*
    public Result update(DaoCategory categoryEntity) {
        if (categoryDao.save(categoryEntity)) {
            categoryTree.update(categoryEntity.getId());
            return Result.success();
        }
        return Result.err();
    }

    public Result insert(DaoCategory categoryEntity) {
        if (categoryDao.save(categoryEntity)) {
            DaoCategory entity = categoryDao.getByName(categoryEntity.getCategoryName());
            categoryTree.insert(entity.getId());
            return Result.success();
        }
        return Result.err();
    }*/

    public Result delete(Integer categoryId) {
        if (categoryDao.deleteById(categoryId)) {
            categoryTree.reBuild();
            return Result.success();
        }
        return Result.err("db error");
    }

    public int getArticleTotalNum(Integer categoryId) {
        return DaoArticle.dao.countByCategoryId(categoryId);
    }


    public void incArticleNum(Integer id) {
        categoryTree.incArticleNum(id);
    }

    public void desArticleNum(Integer id) {
        categoryTree.desArticleNum(id);
    }

    public Result eyeOpen(Integer id) {
        if (categoryDao.updateSort(id, 0)) {
            categoryTree.updateSort(id, 0);
            return Result.success();
        }
        return Result.err();
    }

    public Result eyeClose(Integer id) {
        if (categoryDao.updateSort(id, -1)) {
            categoryTree.updateSort(id, -1);
            return Result.success();
        }
        return Result.err();
    }

    public Integer getTotalNum() {
        return categoryTree.getTotalNum();
    }


    public Result changeParentId(Integer cid, Integer newParentId) {
        if (cid == null || newParentId == null) {
            return Result.fail("id missing");
        }


        if (cid.equals(newParentId)) {
            return Result.fail("父栏目不能是自己");
        }

        List<Integer> allChildId = getChildIdList(cid, true);

        if (allChildId != null && allChildId.contains(newParentId)) {
            return Result.fail("不能把栏目移动到子栏目中");
        }


        DaoCategory daoCategory = new DaoCategory();
        daoCategory.setId(cid);
        daoCategory.setParentId(newParentId);
        if (!daoCategory.update()) {
            return Result.fail("db error");
        }
        categoryTree.reBuild();
        return Result.success();


    }
}
