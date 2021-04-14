package cc.dkcms.cms.common.vo;


import lombok.Data;
import org.apache.commons.lang.StringEscapeUtils;

@Data
public class AdPositionVo {
    private Integer id;

    private Integer categoryId;

    private String name;
    private String description;

    private Integer width;
    private Integer height;

    private String content;
    private String contentImage;

    private Byte isValid;

    private String contentInvalid;

    private String code1;
    private String code2;
    private String escapeCode1;
    private String escapeCode2;

    public String getCode1() {
        return "<cms:ad id=\"" + id + "\"></cms:ad>";
    }

    public String getCode2() {
        return "#ad(" + id + ")";
    }


    public String getEscapeCode1() {
        return StringEscapeUtils.escapeHtml(getCode1());
    }

    public String getEscapeCode2() {
        return StringEscapeUtils.escapeHtml(getCode2());
    }
}
