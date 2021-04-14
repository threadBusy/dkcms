package cc.dkcms.cms.dao;

import cc.dkcms.cms.dao.base.BaseDaoTag;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class DaoTag extends BaseDaoTag<DaoTag> {
    public static final DaoTag dao = new DaoTag().dao();

    public List<DaoTag> getListByType(int type) {
        return dao.find("select * from " + _getTable().getName() + " where tag_type =?", type);
    }


    public Page<DaoTag> getPage(int pageNo, int pageSize) {

        return dao.paginate(pageNo, pageSize,
                "select * ", " from " + _getTable().getName() + "  order by id desc");
    }
}