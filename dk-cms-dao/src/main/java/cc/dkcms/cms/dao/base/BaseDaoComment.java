package cc.dkcms.cms.dao.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseDaoComment<M extends BaseDaoComment<M>> extends Model<M> implements IBean {

    public void setId(Integer id) {
        set("id", id);
    }

    public Integer getId() {
        return getInt("id");
    }

    public void setAccountId(Integer accountId) {
        set("account_id", accountId);
    }

    public Integer getAccountId() {
        return getInt("account_id");
    }

    public void setArticleId(Integer articleId) {
        set("article_id", articleId);
    }

    public Integer getArticleId() {
        return getInt("article_id");
    }

    public void setPublishAt(java.util.Date publishAt) {
        set("publish_at", publishAt);
    }

    public java.util.Date getPublishAt() {
        return get("publish_at");
    }

    public void setContent(String content) {
        set("content", content);
    }

    public String getContent() {
        return getStr("content");
    }

    /**
     * 1 true 0 false
     */
    public void setIsShow(Integer isShow) {
        set("is_show", isShow);
    }

    /**
     * 1 true 0 false
     */
    public Integer getIsShow() {
        return getInt("is_show");
    }

}