package cc.dkcms.cms.service;

import cc.dkcms.cms.common.define.Result;
import cc.dkcms.cms.common.vo.AdCategoryVo;
import cc.dkcms.cms.common.vo.AdPositionVo;
import cc.dkcms.cms.dao.DaoAdCategory;
import cc.dkcms.cms.dao.DaoAdPosition;
import cc.dkcms.cms.service.converter.ConverterAdCategory;
import cc.dkcms.cms.service.converter.ConverterAdPosition;
import com.jfinal.plugin.activerecord.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceAd {

    ConverterAdCategory converterAdCategory = new ConverterAdCategory();
    ConverterAdPosition converterAdPosition = new ConverterAdPosition();

    public List<AdCategoryVo> getCategoryList() {
        return converterAdCategory.convertList(
                DaoAdCategory.dao.findAll()
        );

    }

    public Result newCategory(String cate) {
        DaoAdCategory entity = new DaoAdCategory();
        entity.setCategoryName(cate);

        if (entity.save()) {
            return Result.success();
        }
        return Result.err("");
    }


    public String getCategoryById(Integer categoryId) {

        DaoAdCategory daoAdCategory = DaoAdCategory.dao.findById(categoryId);
        if (daoAdCategory != null) {
            return daoAdCategory.getCategoryName();
        }
        return null;
    }

    public Page<AdPositionVo> getPage(Integer categoryId, Integer page) {
        return converterAdPosition.convertPage(
                DaoAdPosition.dao.getPageByCategoryId(categoryId, page)
        );

    }


    public Map<Integer, String> getCategoryMap() {
        Map<Integer, String> ret = new HashMap<>();
        DaoAdCategory.dao.findAll().forEach(entity -> {
            ret.put(entity.getId(), entity.getCategoryName());
        });
        return ret;
    }

    public AdPositionVo getAdPositionVo(Integer id) {
        return converterAdPosition.convert(
                DaoAdPosition.dao.findById(id)
        );
    }

    public Result saveAdPosition(DaoAdPosition entity) {

        if (entity.getId() == null) {
            if (entity.save()) {
                return Result.success();
            }
            return Result.fail("db error");
        }
        if (entity.update()) {
            return Result.success();
        }
        return Result.fail("db error");

    }

    public Result deleteAdPosition(Integer id) {

        if (DaoAdPosition.dao.deleteById(id)) {
            return Result.success();
        }
        return Result.fail("db error");
    }
}
