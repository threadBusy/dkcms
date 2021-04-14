package cc.dkcms.cms.service.converter;

import cc.dkcms.cms.dao.DaoArticle;
import cc.dkcms.cms.dao.DaoCategory;
import cc.dkcms.cms.dao.DaoModel;
import cc.dkcms.cms.common.define.CategoryType;
import cc.dkcms.cms.common.vo.CategoryVo;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

import static cc.dkcms.cms.common.Constant.ROOT_CATEGORY_ID;

@Slf4j
public class CategoryConverter extends Do2VoConverter<DaoCategory, CategoryVo> {


    private final DaoCategory categoryDao = DaoCategory.dao;


    /**
     * ServiceCategoryTree 构造的时候，会调用convert 生成vo。
     * 故，凡事，依赖父子关系的，都不能在此构造。
     * 放在 ServiceCategoryTree 里第二部组装环节；
     *
     * @param entity
     * @return
     */
    @Override
    public CategoryVo convert(DaoCategory entity) {
        if (entity == null) {
            return null;
        }
        CategoryVo vo = new CategoryVo();
        copy(entity, vo);
        // 保证vo出场的时候，都是用list的
        vo.setChild(new LinkedList<>());
        vo.setCategoryTypeString(getCategoryTypeString(vo.getCategoryType()));
        if (entity.getId() == ROOT_CATEGORY_ID) {
            vo.setLevel(0);
        }

        // 构造vo的时候，不能构建url，因为url要读取父子结构，此时还没建立起来
        /*
        if (entity.getId() == ROOT_CATEGORY_ID) {
            vo.setCategoryUrl(
                    UrlHelper.getSiteHomePage(CmsUtils.isPreviewMode())
            );
            return vo;
        } else {
            vo.setCategoryUrl(
                    UrlHelper.getCategoryHomeUrl(vo, CmsUtils.isPreviewMode())
            );
        }
        */
        vo.setTotalCount(DaoArticle.dao.countByCategoryId(entity.getId()));

        if (vo.getCoverImage() == null) {
            vo.setCoverImage("");
        }

        return vo;
    }

    private String getCategoryTypeString(Integer type) {
        if (type == null || type.equals(CategoryType.ARTICLE.getType())) {
            return "文章";
        } else if (type.equals(CategoryType.ALBUM.getType())) {
            return "图集";
        } else {
            DaoModel model = DaoModel.dao.findById(type);
            if (model != null) {
                return model.getModelName();
            }
            return "未知";
        }
    }


}
