package cc.dkcms.cms.common.define;

public enum CategoryType {


    ARTICLE(-1, "文章"),
    ALBUM(-2, "图集");

    private Integer type;
    private String  name;


    CategoryType(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public static CategoryType getType(Integer categoryType) {
        CategoryType[] all = values();
        for (CategoryType ct : all) {
            if (ct.getType().equals(categoryType)) {
                return ct;
            }
        }
        return null;
    }

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static String getNameByType(Integer type) {
        CategoryType[] all = values();
        for (CategoryType ct : all) {
            if (ct.getType().equals(type)) {
                return ct.getName();
            }
        }
        return null;
    }
}
