package cc.dkcms.cms.common.define;

public enum ArticleRankType {
    LATEST,
    HOT,
    RECOMMEND;


    public static ArticleRankType getType(String type) {
        if ("recommend".equals(type)) {
            return RECOMMEND;
        }
        if ("hot".equals(type)) {
            return HOT;
        }
        return LATEST;
    }
}