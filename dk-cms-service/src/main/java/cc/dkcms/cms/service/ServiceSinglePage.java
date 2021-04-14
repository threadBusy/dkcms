package cc.dkcms.cms.service;

import cc.dkcms.cms.common.Constant;
import cc.dkcms.cms.common.define.Result;
import cc.dkcms.cms.common.vo.SinglePageVo;
import cc.dkcms.cms.dao.DaoSinglePage;
import cc.dkcms.cms.service.converter.ConverterSinglePage;

import java.util.Date;
import java.util.List;

public class ServiceSinglePage {

    ConverterSinglePage converterSinglePage = new ConverterSinglePage();

    public SinglePageVo getPage(Integer pageId) {

        DaoSinglePage entity = DaoSinglePage.dao.findById(pageId);

        return converterSinglePage.convert(entity);
    }

    public List<SinglePageVo> getList() {


        return converterSinglePage.convertList(DaoSinglePage.dao.getList());
    }


    public Result savePage(DaoSinglePage singlePageEntity) {
        try {
            if (singlePageEntity.getId() != null) {
                if (singlePageEntity.update()) {
                    return Result.success();
                }
                return Result.fail("db error");
            }
            singlePageEntity.setCreatedAt(new Date());

            if (singlePageEntity.save()) {
                return Result.success();
            }
            return Result.fail("db error");

        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    public void delete(Integer id) {
        DaoSinglePage entity = new DaoSinglePage();
        entity.setId(id);
        entity.setIsDelete(Constant.TRUE);
        entity.update();
    }
}
