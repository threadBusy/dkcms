package cc.dkcms.cms.common.define;

public enum TagType {
    ARTICLE(1),
    CATEGORY(2);

    private int code;

    TagType(int code) {
        this.code = code;
    }

    public static TagType getType(String type) {
        if ("category".equals(type)) {
            return CATEGORY;
        }
        return ARTICLE;
    }


    public static TagType fromCode(Byte tagType) {
        if (tagType == 2) {
            return CATEGORY;
        }
        return ARTICLE;
    }

    public int getCode() {
        return code;
    }
}
