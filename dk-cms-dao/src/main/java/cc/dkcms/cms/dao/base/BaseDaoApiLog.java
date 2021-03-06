package cc.dkcms.cms.dao.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseDaoApiLog<M extends BaseDaoApiLog<M>> extends Model<M> implements IBean {

	public void setId(Long id) {
		set("id", id);
	}

	public Long getId() {
		return getLong("id");
	}

	/**
	 * Token
	 */
	public void setToken(String token) {
		set("token", token);
	}

	/**
	 * Token
	 */
	public String getToken() {
		return getStr("token");
	}

	public void setCreatedAt(java.util.Date createdAt) {
		set("created_at", createdAt);
	}

	public java.util.Date getCreatedAt() {
		return get("created_at");
	}

	/**
	 * apiname
	 */
	public void setApiName(String apiName) {
		set("api_name", apiName);
	}

	/**
	 * apiname
	 */
	public String getApiName() {
		return getStr("api_name");
	}

	/**
	 * 参数
	 */
	public void setParameters(String parameters) {
		set("parameters", parameters);
	}

	/**
	 * 参数
	 */
	public String getParameters() {
		return getStr("parameters");
	}

	public void setResult(String result) {
		set("result", result);
	}

	public String getResult() {
		return getStr("result");
	}
	
}
