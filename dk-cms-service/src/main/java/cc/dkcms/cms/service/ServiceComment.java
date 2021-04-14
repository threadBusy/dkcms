package cc.dkcms.cms.service;

import cc.dkcms.cms.common.define.Result;
import cc.dkcms.cms.common.vo.CommentVo;
import cc.dkcms.cms.dao.DaoComment;
import cc.dkcms.cms.service.converter.ConverterComment;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

public class ServiceComment {

    ConverterComment converterComment = new ConverterComment();

    public List<CommentVo> getListByArticleId(Integer articleId, Integer start) {
        return converterComment.convertList(DaoComment.dao.getListByArticleId(articleId, start));
    }

    public Page<CommentVo> getListByPage(Integer pageNo, Integer pageSize) {
        return converterComment.convertPage(DaoComment.dao.getListByPage(pageNo, pageSize));
    }

    public Result accept(Integer id) {
        DaoComment comment = new DaoComment();
        comment.setId(id);
        comment.setIsShow(1);
        comment.update();
        //TODO

        return Result.success();
    }

    public Result delete(Integer id) {
        DaoComment.dao.deleteById(id);
        //todo
        return Result.success();
    }

    public Page<CommentVo> getApprovedListByPage(Integer page, Integer limit) {
        return converterComment.convertPage(DaoComment.dao.getListByPage(page, limit));

    }
}
