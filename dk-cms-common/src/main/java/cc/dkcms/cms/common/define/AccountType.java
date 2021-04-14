package cc.dkcms.cms.common.define;


public enum AccountType {

    USER(1),
    ADMIN(2);

    private Integer val;

    AccountType(Integer val) {
        this.val = val;
    }

    public Integer getVal() {
        return val;
    }

    public static AccountType of(Integer val) {
        if (val != null && val == 2) {
            return ADMIN;
        }
        return USER;
    }


}
