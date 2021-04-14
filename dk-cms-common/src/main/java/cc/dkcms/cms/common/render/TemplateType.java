package cc.dkcms.cms.common.render;

public enum TemplateType {
    ENJOY,
    THYMELEAF;


    public static TemplateType getTemplateType(String name) {
        if ("thymeleaf".equals(name)) {
            return THYMELEAF;
        }
        return ENJOY;
    }

    @Override
    public String toString() {
        if (this == THYMELEAF) {
            return "thymeleaf";
        }
        return "enjoy";
    }
}
