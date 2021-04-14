package cc.dkcms.cms.common;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CommonUtils {


    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMddHHmmssSSS");

    public static String generateConsumeId() {
        return LocalDateTime.now().format(dtf) + RandomStringUtils.randomNumeric(3);
    }


    public static String getCoverImageFromList(String images) {
        if (StringUtils.isEmpty(images)) {
            return "";
        }
        String[] imagesArr = images.split(";");
        if (images.length() < 1) {
            return "";
        }
        return imagesArr[0];
    }

    public static List<Integer> getIntList(String idList) {
        List<Integer> ret = new ArrayList<>();
        if (StringUtils.isEmpty(idList)) {
            return ret;
        }
        String[] ids = idList.split(",");
        if (ids.length == 0) {
            return ret;
        }
        for (String id : ids) {
            try {
                ret.add(Integer.parseInt(id));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;

    }


}
