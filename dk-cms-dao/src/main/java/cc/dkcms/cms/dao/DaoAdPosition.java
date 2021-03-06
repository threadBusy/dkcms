package cc.dkcms.cms.dao;

import cc.dkcms.cms.dao.base.BaseDaoAdPosition;
import com.jfinal.plugin.activerecord.Page;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class DaoAdPosition extends BaseDaoAdPosition<DaoAdPosition> {
    public final static DaoAdPosition dao = new DaoAdPosition().dao();

    public Page<DaoAdPosition> getPageByCategoryId(Integer categoryId, Integer page) {
        String where = "";
        if (categoryId > 0) {
            where = " WHERE category_id = " + categoryId;
        }
        return dao.paginate(
                page, 10,
                "SELECT * ",
                " FROM  "
                        + _getTable().getName()
                        + where
        );

    }
}
