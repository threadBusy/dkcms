package cc.dkcms.cms.dao.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseDaoModel<M extends BaseDaoModel<M>> extends Model<M> implements IBean {

	public void setId(Integer id) {
		set("id", id);
	}

	public Integer getId() {
		return getInt("id");
	}

	public void setModelName(String modelName) {
		set("model_name", modelName);
	}

	public String getModelName() {
		return getStr("model_name");
	}

	public void setFieldList(String fieldList) {
		set("field_list", fieldList);
	}

	public String getFieldList() {
		return getStr("field_list");
	}
	
}
