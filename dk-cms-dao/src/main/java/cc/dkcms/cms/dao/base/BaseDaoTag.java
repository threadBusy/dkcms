package cc.dkcms.cms.dao.base;

import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseDaoTag<M extends BaseDaoTag<M>> extends Model<M> implements IBean {

	public void setId(Integer id) {
		set("id", id);
	}

	public Integer getId() {
		return getInt("id");
	}

	public void setTagName(String tagName) {
		set("tag_name", tagName);
	}

	public String getTagName() {
		return getStr("tag_name");
	}

	public void setTagType(Integer tagType) {
		set("tag_type", tagType);
	}

	public Integer getTagType() {
		return getInt("tag_type");
	}
	
}
