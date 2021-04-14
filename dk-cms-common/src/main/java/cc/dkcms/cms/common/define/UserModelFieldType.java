package cc.dkcms.cms.common.define;

public enum UserModelFieldType {

    INPUT(1, "文本框"),
    TEXTAREA(2, "文本域"),
    CHECKBOX(3, "多选"),
    SELECT(4, "下拉菜单"),
    FILE(5, "文件框");


    private int    code;
    private String name;

    UserModelFieldType(int code, String name) {
        this.code = code;
        this.name = name;
    }


    public static UserModelFieldType[] getAll() {
        return UserModelFieldType.values();
    }

    public static String getNameByCode(int code) {
        for (UserModelFieldType field : getAll()) {
            if (field.getCode() == code) {
                return field.getName();
            }
        }
        return null;
    }

    public static UserModelFieldType getTypeByCode(int code) {
        for (UserModelFieldType field : getAll()) {
            if (field.getCode() == code) {
                return field;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


}
