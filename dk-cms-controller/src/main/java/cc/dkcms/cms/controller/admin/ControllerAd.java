package cc.dkcms.cms.controller.admin;

import cc.dkcms.cms.common.define.AjaxResponse;
import cc.dkcms.cms.common.define.Result;
import cc.dkcms.cms.common.vo.AdPositionVo;
import cc.dkcms.cms.controller.base.BaseAdminController;
import cc.dkcms.cms.dao.DaoAdPosition;
import cc.dkcms.cms.service.ServiceAd;
import com.jfinal.aop.Aop;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang.StringUtils;

import java.util.List;

public class ControllerAd extends BaseAdminController {


    private ServiceAd serviceAd = Aop.get(ServiceAd.class);

    public void index() {
        Integer categoryId = getInt("categoryId", 0);
        Integer page       = getInt("page", 1);

        String categoryName = "全部类目";
        if (categoryId > 0) {
            categoryName = serviceAd.getCategoryById(categoryId);
        }
        Page<AdPositionVo> pageInfo = serviceAd.getPage(categoryId, page);

        List<AdPositionVo> listVo = pageInfo.getList();

        set("list", listVo);
        set("pageInfo", pageInfo);
        set("categoryName", categoryName);
        set("categoryMap", serviceAd.getCategoryMap());
    }

    public void category() {
        set("list", serviceAd.getCategoryList());

    }

    public void saveCategory() {
        String cate = get("category_name");
        if (StringUtils.isEmpty(cate)) {
            renderJson(AjaxResponse.fail("请填写分类名称"));
            return;
        }
        Result result = serviceAd.newCategory(cate);
        if (!result.getIsSuccess()) {
            renderJson(AjaxResponse.fail(result.getMsg()));
            return;
        }
        renderJson(AjaxResponse.success());
    }


    public void add() {
        set("ad", new AdPositionVo());
        set("action", "add");
        set("categoryMap", serviceAd.getCategoryMap());
    }

    public void edit() {

        Integer id = getInt("id");
        if (id == null || id < 0) {
            failPage("id missing");
        }
        set("ad", serviceAd.getAdPositionVo(id));
        set("action", "add");
        set("categoryMap", serviceAd.getCategoryMap());

    }

    public void save() {

        if (!isPost()) {
            renderJson(AjaxResponse.fail("no post"));
            return;
        }

        DaoAdPosition entity = getModel(DaoAdPosition.class, "");
        if (StringUtils.isEmpty(entity.getName())) {
            renderJson(AjaxResponse.fail("name missing"));
            return;
        }
        if (entity.getCategoryId() < 1) {
            renderJson(AjaxResponse.fail("categoryId missing"));
            return;
        }

        Result result = serviceAd.saveAdPosition(entity);
        if (!result.isSuccess()) {
            renderJson(AjaxResponse.fail(result.getMsg()));
            return;
        }
        renderJson(AjaxResponse.success());
    }


    public void delete() {

        Integer id = getInt("id");
        if (id == null || id < 0) {
            failPage("id missing");
            return;
        }

        serviceAd.deleteAdPosition(id);
        msgPage("删除成功", "/ad/list");
    }

    public void code() {
        Integer id = getInt("id");
        if (id == null || id < 0) {
            failPage("id missing");
        }
        set("categoryMap", serviceAd.getCategoryMap());
        set("ad", serviceAd.getAdPositionVo(id));
    }
}
