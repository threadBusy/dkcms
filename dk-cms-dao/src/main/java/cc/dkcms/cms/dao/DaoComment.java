package cc.dkcms.cms.dao;

import cc.dkcms.cms.common.Constant;
import cc.dkcms.cms.dao.base.BaseDaoComment;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class DaoComment extends BaseDaoComment<DaoComment> {

    public static DaoComment dao = new DaoComment().dao();

    public List<DaoComment> getListByArticleId(Integer articleId, Integer start) {

        return find("SELECT  * FROM "
                        + _getTable().getName()
                        + " WHERE article_id = ? AND is_show = 1 ORDER BY id DESC LIMIT ?,?",
                articleId, start,
                Constant.PAGE_SIZE);
    }

    public Page<DaoComment> getListByPage(Integer pageNo, Integer pageSize) {
        return dao.paginate(
                pageNo,
                pageSize,
                "select * ",
                " from " + _getTable().getName()
                        + " order by id desc");
    }

    public Page<DaoComment> getApprovedListByPage(Integer pageNo, Integer pageSize) {
        return dao.paginate(
                pageNo,
                pageSize,
                "SELECT * ",
                " FROM " + _getTable().getName()
                        + " WHERE is_show = 1 ORDER BY ID DESC");
    }
}
