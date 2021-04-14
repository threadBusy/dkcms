package cc.dkcms.cms.controller.admin;

import cc.dkcms.cms.common.define.AjaxResponse;
import cc.dkcms.cms.common.define.CategoryType;
import cc.dkcms.cms.common.define.Result;
import cc.dkcms.cms.common.define.TagType;
import cc.dkcms.cms.common.vo.CategoryVo;
import cc.dkcms.cms.common.vo.CategoryVoForList;
import cc.dkcms.cms.dao.DaoCategory;
import cc.dkcms.cms.service.ServiceCategory;
import cc.dkcms.cms.service.ServiceCategoryTree;
import cc.dkcms.cms.service.ServiceTag;
import com.jfinal.aop.Aop;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ControllerCategory extends cc.dkcms.cms.controller.base.BaseAdminController {

    private ServiceCategory categoryService = Aop.get(ServiceCategory.class);

    private ServiceCategoryTree serviceCategoryTree = Aop.get(ServiceCategoryTree.class);

    private ServiceTag serviceTag = Aop.get(ServiceTag.class);


    public void list() {

    }

    public void add() {
        Integer pid = getInt("parentId", 0);
        set("parentCategory", categoryService.getCategoryById(pid));
        set("tagList", serviceTag.getListByType(TagType.CATEGORY));
        DaoCategory daoCategory = new DaoCategory();
        daoCategory.setParentId(pid);
        daoCategory.setContent("");
        daoCategory.setCategoryType(CategoryType.ARTICLE.getType());
        set("category", daoCategory);
    }


    public void edit() {
        Integer    id       = getInt("id");
        CategoryVo category = categoryService.getCategoryById(id);
        if (category == null) {
            failPage("id missing");
            return;
        }

        set("parentCategory", categoryService.getCategoryById(category.getParentId()));
        set("category", category);
        set("tagList", serviceTag.getListByType(TagType.CATEGORY));
    }


    public void getChildCategory() {
        Integer categoryId = getInt("id");
        if (categoryId == null || categoryId < 0) {
            renderJson(AjaxResponse.fail("id missing"));
        }
        List<CategoryVoForList> voList = new ArrayList<>();
        categoryService.getChildVoList(categoryId).forEach(vo -> {
            voList.add(vo.getVoForList());
        });
        renderJson(AjaxResponse.success().setData(voList));
    }

    public void save() {
        if (!isPost()) {
            renderJson(AjaxResponse.fail());
            return;
        }

        try {
            DaoCategory daoCategory = getModel(DaoCategory.class, "");
            if (StringUtils.isEmpty(daoCategory.getCategoryName())) {
                renderJson(AjaxResponse.fail("栏目名称不能为空"));
                return;
            }
            if (daoCategory.getParentId() == null) {
                renderJson(AjaxResponse.fail("父栏目错误"));
                return;
            }
            if (daoCategory.getParentId().equals(daoCategory.getId())) {
                renderJson(AjaxResponse.fail("父栏目不能是自己"));
                return;
            }
            if (StringUtils.isEmpty(daoCategory.getPermalink())) {
                renderJson(AjaxResponse.fail("栏目url路径不能为空"));
                return;
            }
            if (DaoCategory.dao.isPermalinkExists(daoCategory.getPermalink(),daoCategory.getId())) {
                renderJson(AjaxResponse.fail("栏目url重复，请修改:" + daoCategory.getPermalink()));
                return;
            }
            if (daoCategory.getSort() == null) {
                daoCategory.setSort(0);
            }
            Result result = categoryService.save(daoCategory);
            if (!result.isSuccess()) {
                renderJson(AjaxResponse.fail(result.getMsg()));
                return;
            }
            renderJson(AjaxResponse.success());
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(AjaxResponse.fail(e.getMessage()));
        }
    }


    public void refresh() {
        serviceCategoryTree.reBuild();
        msgPage("刷新完成", "/admin/category/list");

    }


    public void delete() {
        Integer categoryId = getInt("id");
        if (categoryId == null || categoryId < 1) {
            failPage("编号错误");
            return;
        }

        int childTotalNum = categoryService.getChildTotalNum(categoryId);
        if (childTotalNum > 0) {
            failPage("栏目下还有" + childTotalNum + "个子栏目，不能删除", "#/admin/category/list");
            return;
        }

        if (categoryService.getArticleTotalNum(categoryId) > 0) {
            failPage("栏目下还有内容，不能删除", "#/admin/category/list");
            return;

        }

        Result result = categoryService.delete(categoryId);
        if (!result.isSuccess()) {
            failPage("删除失败" + result.getMsg(), "#/admin/category/list");
            return;

        }

        msgPage("删除成功", "#/admin/category/list");

    }

    public void eyeOpen() {
        Integer id = getInt("id");
        if (id == null || id < 0) {
            failPage("id missing");
        }
        Result result = categoryService.eyeOpen(id);
        if (!result.isSuccess()) {
            failPage(result.getMsg());
            return;
        }
        redirect("/admin/category/list");
    }

    public void eyeClose() {
        Integer id = getInt("id");
        if (id == null || id < 0) {
            failPage("id missing");
        }
        Result result = categoryService.eyeClose(id);
        if (!result.isSuccess()) {
            failPage(result.getMsg());
            return;
        }
        redirect("/admin/category/list");
    }

    public void move() {
        if (isPost()) {
            Integer id          = getInt("id");
            Integer newParentId = getInt("parentId");

            Result result = categoryService.changeParentId(id, newParentId);
            if (!result.getIsSuccess()) {
                renderJson(AjaxResponse.fail(result.getMsg()));
                return;
            }
            renderJson(AjaxResponse.success());
            return;
        }


        Integer    id       = getInt("id");
        CategoryVo category = categoryService.getCategoryById(id);
        if (category == null) {
            failPage("id missing");
            return;
        }

        set("parentCategory", categoryService.getCategoryById(category.getParentId()));
        set("category", category);
        set("tagList", serviceTag.getListByType(TagType.CATEGORY));
    }


}
