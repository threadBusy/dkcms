package cc.dkcms.cms.dao.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseDaoModelField<M extends BaseDaoModelField<M>> extends Model<M> implements IBean {

    public void setId(Integer id) {
        set("id", id);
    }

    public Integer getId() {
        return getInt("id");
    }

    public void setModelId(Integer modelId) {
        set("model_id", modelId);
    }

    public Integer getModelId() {
        return getInt("model_id");
    }

    public void setType(Integer type) {
        set("type", type);
    }

    public Integer getType() {
        return getInt("type");
    }

    public void setName(String name) {
        set("name", name);
    }

    public String getName() {
        return getStr("name");
    }

    public void setFieldName(String fieldName) {
        set("field_name", fieldName);
    }

    public String getFieldName() {
        return getStr("field_name");
    }

    public void setOptions(String options) {
        set("options", options);
    }

    public String getOptions() {
        return getStr("options");
    }

    public void setDefaultValue(String defaultValue) {
        set("default_value", defaultValue);
    }

    public String getDefaultValue() {
        return getStr("default_value");
    }

}