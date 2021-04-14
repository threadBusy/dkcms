package cc.dkcms.cms.common.define;

/**
 * 前提：
 * 1.category 必须有permalink
 * 2.全局permalink不能重复
 */
public enum StaticPagePathPolicy {


    /**
     * /parentCategory/index.html
     * /parentCategory/list-0.html
     * /parentCategory/$(yyyyddmm)$(id).html
     */
    DATE_AS_PATH("data_as_path", "/${categoryUrl}/${publishDate}${id}.html"),

    /**
     * /a/b/c/index.html
     * /a/b/c/list-0.html
     * /a/b/c/id.html
     */
    CATEGORY_AS_PATH("category_as_path", "/${rootCategory}/.../${pCategoryUrl}/${categoryUrl}/${publishDate}${id}.html");


    String policy;
    String desc;

    StaticPagePathPolicy(String v, String desc) {
        this.policy = v;
        this.desc = desc;
    }

    public static StaticPagePathPolicy of(String p) {
        if ("date_as_path".equals(p)) {
            return DATE_AS_PATH;
        }
        return CATEGORY_AS_PATH;
    }

    public String getDesc() {
        return desc;
    }}
