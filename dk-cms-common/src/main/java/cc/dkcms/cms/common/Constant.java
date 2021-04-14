package cc.dkcms.cms.common;

public class Constant {

    public static final String SOFT_NAME     = "多客CMS(DKCMS)";
    public static final String SOFT_VERSION  = "V1.1";
    public static final String SOFT_AUTHOR   = "恩多线程信息科技有限公司";
    public static final String SOFT_WEBSITE  = "https://www.dkcms.cc";
    public static final String SOFT_CONTRACT = "biz@365d.ink";

    // 许可证

    public static final Integer pageSize    = 10;
    public static final Integer TRUE        = 1;
    public static final Integer FALSE       = 0;
    public static final String  CONFIG_FILE = "config.properties";

    public static final String LOGIN_URL                    = "/login";
    public static final String REGISTER_URL                 = "/register";
    public static final String ADMIN_LOGIN_SUCCESS_JUMP_URL = "/dkcms";
    public static final String USER_LOGIN_SUCCESS_JUMP_URL  = "/";

    public static final String ADMIN_TOKEN_NAME = "__DKCMS_TONYA__";


    // pageInfo
    // 默认每页数量，page_info 里用；
    public static final int PAGE_SIZE = 10;


    // go_back;
    public static final String URL_GO_BACK = "javascript:history.go(-1)";


    public static final String PREVIEW_URL_PATH = "/site";
    public static final String INSTALL_PAGE_URL = "/install";
    public static final String LOG_JS_URL       = "/assets/js/logback";


    public static final String GENERATE_NAME =
            "    <meta name=\"pageGenerator\" value=\""
                    + Constant.SOFT_NAME + "(" + Constant.SOFT_WEBSITE + ")\">";



    public static String PATH_WEBROOT;
    public static String PATH_TEMPLATE;
    public static String PATH_CONFIG;

    public static final int ROOT_CATEGORY_ID = 0;


    static {

        String userDir = System.getProperty("user.dir");
        PATH_TEMPLATE = userDir + "/template/";
        PATH_WEBROOT = userDir + "/webroot/";
        PATH_CONFIG = userDir + "/config/";

    }

}
