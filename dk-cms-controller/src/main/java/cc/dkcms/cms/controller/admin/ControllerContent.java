package cc.dkcms.cms.controller.admin;

import cc.dkcms.cms.common.Constant;
import cc.dkcms.cms.common.define.AjaxResponse;
import cc.dkcms.cms.common.define.ConstArticleDelete;
import cc.dkcms.cms.common.define.Result;
import cc.dkcms.cms.common.util.JsonUtil;
import cc.dkcms.cms.common.vo.*;
import cc.dkcms.cms.controller.base.BaseAdminController;
import cc.dkcms.cms.dao.DaoArticle;
import cc.dkcms.cms.service.ServiceCategory;
import cc.dkcms.cms.service.ServiceComment;
import cc.dkcms.cms.service.ServiceContent;
import cc.dkcms.cms.service.ServiceUserModel;
import com.jfinal.aop.Aop;
import com.jfinal.plugin.activerecord.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cc.dkcms.cms.common.Constant.PATH_WEBROOT;
import static cc.dkcms.cms.common.define.CategoryType.ALBUM;
import static cc.dkcms.cms.common.define.CategoryType.ARTICLE;

@Slf4j
public class ControllerContent extends BaseAdminController {

    ServiceCategory  serviceCategory  = Aop.get(ServiceCategory.class);
    ServiceContent   serviceContent   = Aop.get(ServiceContent.class);
    ServiceUserModel serviceUserModel = Aop.get(ServiceUserModel.class);

    public void index() {

        Integer categoryId = getInt("categoryId", 0);
        Integer page       = getInt("page", 1);


        CategoryVo categoryVo = serviceCategory.getCategoryById(categoryId);
        if (categoryVo == null) {
            failPage("categoryId error");
            return;
        }

        Integer type = categoryVo.getCategoryType();

        Page<ArticleVo> pageInfo = serviceContent.getPageByCategoryIdWithSubCat(
                page, Constant.PAGE_SIZE, categoryId);


        set("list", pageInfo.getList());
        set("pageInfo", pageInfo);
        set("categoryName", categoryVo.getCategoryName());
        set("categoryId", categoryId);
        set("tableTitle", getTableTitle(type, categoryVo.getCategoryName()));

        renderTemplate(getViewNamePrefix(type) + "/index.html");

    }

    public void search() {

        String  keyword = get("keyword", "");
        Integer page    = getInt("page", 1);

        if (StringUtils.isEmpty(keyword)) {
            failPage("?????????????????????", "#/admin/content/list");
            return;
        }

        Page<ArticleVo> pageInfo = serviceContent.getListByKeyword(keyword, page);


        set("list", pageInfo.getList());
        set("pageInfo", pageInfo);
        set("keyword", keyword);
        set("tableTitle", "?????????" + keyword);
    }


    public void save() {

        if (!isPost()) {
            renderJson(AjaxResponse.fail("not post"));
            return;
        }

        // ??????????????????
        DaoArticle entity = getModel(DaoArticle.class, "", true);
        if ("???".equals(get("is_draft"))) {
            entity.setIsDelete(ConstArticleDelete.ARTICLE_DELETE_DRAFT);
        } else {
            entity.setIsDelete(ConstArticleDelete.ARTICLE_DELETE_FALSE);
        }

        if (entity.getCategoryId() == null) {
            renderJson(AjaxResponse.fail("categoryId not exist"));
            return;
        }
        if (StringUtils.isEmpty(entity.getTitle())) {
            renderJson(AjaxResponse.fail("????????????????????????"));
            return;
        }


        // ???????????????model??????
        CategoryVo categoryVo   = serviceCategory.getCategoryById(entity.getCategoryId());
        Integer    categoryType = categoryVo.getCategoryType();

        if (!categoryType.equals(ALBUM.getType()) && !categoryType.equals(ARTICLE.getType())) {
            List<UserModelFieldVo> fieldList = serviceUserModel.getFieldVoList(categoryVo.getCategoryType());
            Map<String, String>    extJson   = new HashMap<>();
            for (UserModelFieldVo field : fieldList) {
                String var = get(field.getFieldName());
                if (!StringUtils.isEmpty(var)) {
                    extJson.put(field.getFieldName(), var);
                }
            }

            entity.setExtJson(JsonUtil.writeValueAsString(extJson));
        }


        //new content ,init some field
        if (entity.getId() == null) {

            entity.setPublishDate(new Date());
            entity.setIsShow(Constant.TRUE);
            entity.setManagerId(getCurrentAdmin().getId());
            if (entity.save()) {
                renderJson(AjaxResponse.success("????????????"));
            } else {
                renderJson(AjaxResponse.success("???????????? db error"));
            }
        } else {
            if (entity.update()) {
                renderJson(AjaxResponse.success("????????????"));
            } else {
                renderJson(AjaxResponse.success("???????????? db error"));
            }
        }

    }

    public void edit() {

        Integer articleId = getInt("id");
        if (articleId < 1) {
            failPage("id ?????????:" + articleId);
            return;
        }
        ArticleVo  articleVo  = serviceContent.getArticleVo(articleId);
        CategoryVo categoryVo = serviceCategory.getCategoryById(articleVo.getCategoryId());
        if (categoryVo == null) {
            failPage("????????????????????????:" + articleVo.getCategoryId());
            return;
        }


        Integer type = categoryVo.getCategoryType();
         /*
        if (categoryVo.getCategoryType() > 0) {
            UserModelVo model = serviceUserModel.getVoById(categoryVo.getCategoryType());
            set("fieldList", model.getFieldVoList());
        } else {
            set("fieldList", new ArrayList());
        }*/
        set("fieldList", serviceUserModel.getFieldVoList(categoryVo.getCategoryType()));

        set("article", articleVo);
        set("tableTitle", getTableTitle(type, categoryVo.getCategoryName()));
        set("action", "edit");
        render(getViewNamePrefix(type) + "/add.html");

    }


    public void add() {

        Integer    categoryId = getInt("categoryId", 0);
        CategoryVo categoryVo = serviceCategory.getCategoryById(categoryId);
        if (categoryVo == null) {
            failPage("categoryId error");
            return;
        }

        Integer type         = categoryVo.getCategoryType();
        String  categoryName = categoryVo.getCategoryName();

        ArticleVo vo = new ArticleVo();
        vo.setCategoryId(categoryId);
        vo.setCategoryName(categoryName);
        vo.setContent("");

        /*
        if (categoryVo.getCategoryType() > 0) {
            UserModelVo model = serviceUserModel.getVoById(categoryVo.getCategoryType());
            set("fieldList", model.getFieldVoList());
        } else {
            set("fieldList", new ArrayList());
        }*/
        set("fieldList", serviceUserModel.getFieldVoList(categoryVo.getCategoryType()));
        set("article", vo);
        set("action", "add");
        set("tableTitle", getTableTitle(type, categoryName));
        render(getViewNamePrefix(type) + "/add.html");
    }

    // ???????????????
    public void recycleList() {
        Integer page = getInt("page", 1);

        Page<ArticleVo> pageInfo = serviceContent.getRecyclePage(page, Constant.PAGE_SIZE);
        set("list", pageInfo.getList());
        set("pageInfo", pageInfo);
    }

    // ???????????????
    public void draftList() {

        Integer page = getInt("page", 1);

        Page<ArticleVo> pageInfo = serviceContent.getDraftPage(page, Constant.PAGE_SIZE);
        set("list", pageInfo.getList());
        set("pageInfo", pageInfo);
    }

    public void moveInRecycle() {
        Result result = serviceContent.toggleRecycleStatus(getInt("id"));
        if (!result.getIsSuccess()) {
            failPage("????????????:" + result.getMsg(), "#/admin/content/list");
            return;
        }
        msgPage("???????????????????????????", "#/admin/content/recycleList");
    }

    public void moveOutRecycle() {
        Result result = serviceContent.toggleRecycleStatus(getInt("id"));
        if (!result.getIsSuccess()) {
            failPage("????????????:" + result.getMsg(), "#/admin/content/recycleList");
            return;
        }
        msgPage("???????????????????????????", "#/admin/content/list");
    }

    public void delete() {

        if (!DaoArticle.dao.deleteById(getInt("id"))) {
            failPage("????????????", "#/admin/content/recycleList");
            return;
        }
        msgPage("????????????", "#/admin/content/list");
    }

    public void draftPublish() {
        Result result = serviceContent.draftPublish(getInt("id"));
        if (!result.getIsSuccess()) {
            failPage("????????????:" + result.getMsg(), "#/admin/content/draftList");
            return;
        }
        msgPage("????????????", "#/admin/content/list");
    }

    /// ????????? util ??????

    private String getFileSize(String image) {
        File file = new File(PATH_WEBROOT + image);
        if (!file.exists()) {
            return "notFound";
        }
        return FileUtils.byteCountToDisplaySize(FileUtils.sizeOf(file));
    }

    private String getTableTitle(Integer type, String categoryName) {
        String tableTitle = "????????????";
        if (type != null) {
            if (type.equals(ALBUM.getType())) {
                tableTitle = "????????????";
            } else if (!type.equals(ARTICLE.getType())) {
                UserModelVo model = serviceUserModel.getVoById(type);
                tableTitle = model.getModelName() + "??????";
            }
        }
        return tableTitle + "[" + categoryName + "]";

    }

    private String getViewNamePrefix(Integer type) {

        String base = "/view/admin/content";
        if (type == null) {
            return base;
        }
        if (type.equals(ALBUM.getType())) {
            return base + "album";
        } else if (!type.equals(ARTICLE.getType())) {
            return base + "model";
        }
        return base;
    }


    ServiceComment serviceComment = Aop.get(ServiceComment.class);

    public void comment() {
        Integer         page     = getInt("page", 1);
        Page<CommentVo> pageInfo = serviceComment.getListByPage(page, Constant.PAGE_SIZE);
        set("list", pageInfo.getList());
        set("pageInfo", pageInfo);
    }

    public void commentAccept() {

        Integer id = getInt("id", -1);
        if (id < 0) {
            renderJson(AjaxResponse.fail("id missing"));
            return;
        }
        serviceComment.accept(id);
        renderJson(AjaxResponse.success());

    }

    public void commentDelete() {
        Integer id = getInt("id", -1);
        if (id < 0) {
            renderJson(AjaxResponse.fail("id missing"));
            return;
        }
        serviceComment.delete(id);
        renderJson(AjaxResponse.success());
    }
}