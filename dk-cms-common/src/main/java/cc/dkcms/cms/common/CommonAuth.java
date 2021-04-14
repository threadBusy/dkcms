package cc.dkcms.cms.common;

import cc.dkcms.cms.common.define.Result;
import com.jfinal.core.Controller;
import com.jfinal.kit.AesKit;
import com.jfinal.kit.PropKit;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;

public class CommonAuth {

    private static final Logger logger = LoggerFactory.getLogger(CommonAuth.class);

    static {
        try {
            PropKit.use(Constant.CONFIG_FILE);
        } catch (Exception e) {
            logger.error("CommonAuth load config fail");
        }
    }

    public static String getAesKey() throws Exception {
        String tokenSalt = PropKit.get(Const.TOKEN_SALT);
        if (tokenSalt == null) {
            throw new Exception("token salt is empty");
        }
        String res = DigestUtils.md5Hex(tokenSalt);
        return res;

    }


    public static String generateToken(Integer uid) throws Exception {

        String tokenVersion = PropKit.get(Const.TOKEN_VERSION, "1");

        return Base64.getEncoder().encodeToString(
                AesKit.encrypt(uid + "|" + tokenVersion, getAesKey())
        );
    }

    // do not modify
    public static String generatePassword(String u, String p) {

        String pwd = DigestUtils.md5Hex("*_)&" + p + "219" + u + "(*2,>?");
        logger.info("generatePassword" + pwd);
        return pwd;
    }

    public static boolean rexCheckEmail(String input) {
        if (StringUtils.isEmpty(input)) {
            return false;
        }
        String regStr = "^[0-9a-z]+\\w*@([0-9a-z]+\\.)+[0-9a-z]+$";
        return input.matches(regStr);
    }

    public static boolean rexCheckUsername(String input) {
        // 6-20 位，字母、数字、字符
        //String reg = "^([A-Z]|[a-z]|[0-9]|[`-=[];,./~!@#$%^*()_+}{:?]){6,20}$";
        String regStr = "^([A-Z]|[a-z]|[0-9]){6,20}$";
        return input.matches(regStr);
    }

    public static boolean rexCheckPassword(String input) {
        // 6-20 位，字母、数字、字符
        //String reg = "^([A-Z]|[a-z]|[0-9]|[`-=[];,./~!@#$%^*()_+}{:?]){6,20}$";
        String regStr = "^([A-Z]|[a-z]|[0-9]|[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）――+|{}【】‘；：”“'。，、？]){6,20}$";
        return input.matches(regStr);
    }


    public static Result checkLogin(Controller controller) {
        if (controller == null) {
            return Result.fail("controller is null");
        }
        String token = controller.getCookie(Constant.ADMIN_TOKEN_NAME);

        logger.debug("get token:" + token);
        if (StringUtils.isEmpty(token)) {
            return Result.fail("token cookie is empty");
        }

        byte[] tokenBytes = Base64.getDecoder().decode(token);
        String aesKey     = "";
        try {
            aesKey = getAesKey();
        } catch (Exception e) {
            logger.error("getAesKey load config properties error");
            return Result.fail("getAesKey load config properties error");
        }

        String   tokenString  = AesKit.decryptToStr(tokenBytes, aesKey);
        String[] tokenContent = tokenString.split("\\|");
        if (tokenContent.length != 2) {
            return Result.fail("tokenContent.length != 2");
        }
        String tokenVersion = PropKit.get(Const.TOKEN_VERSION);
        if (!tokenVersion.equals(tokenContent[1])) {
            return Result.fail("tokenVersion not " + tokenVersion);
        }

        Long uid = Long.parseLong(tokenContent[0]);
        logger.debug("check login uid:" + uid);

        Result result = Result.success();
        result.setData(uid);
        return result;
    }
}
