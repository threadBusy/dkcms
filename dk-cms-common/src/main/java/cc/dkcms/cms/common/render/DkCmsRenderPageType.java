package cc.dkcms.cms.common.render;

/**
 * 渲染的页面种类
 */
public enum DkCmsRenderPageType {
    SITE_HOMEPAGE,
    CATEGORY_HOME,
    CATEGORY_LIST,
    CATEGORY_ARTICLE,
    SINGLE_PAGE,
    SITE_MAP,
    ARTICLE_LIST_BY_TAG,
    SEARCH_RESULT;

    public static DkCmsRenderPageType of(Integer type) {

        DkCmsRenderPageType[] valueArray = DkCmsRenderPageType.values();
        if (type <= (valueArray.length - 1)) {
            return valueArray[type];
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name();
    }}
