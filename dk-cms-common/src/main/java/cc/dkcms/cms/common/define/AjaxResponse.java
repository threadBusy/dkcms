package cc.dkcms.cms.common.define;

public class AjaxResponse {

    public static final int SUCCESS = 0;
    public static final int FAIL    = 1;

    private int    code = SUCCESS;
    private String msg  = "success";
    private Object data;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public AjaxResponse setData(Object data) {
        this.data = data;
        return this;
    }


    public static AjaxResponse fail(String msg) {
        AjaxResponse ajaxResponse = new AjaxResponse();
        ajaxResponse.setCode(FAIL);
        ajaxResponse.setMsg(msg);
        return ajaxResponse;
    }

    public static AjaxResponse fail() {
        return AjaxResponse.fail("fail");
    }

    public static AjaxResponse success(String msg) {
        AjaxResponse ajaxResponse = new AjaxResponse();
        ajaxResponse.setCode(SUCCESS);
        ajaxResponse.setMsg(msg);
        return ajaxResponse;
    }

    public static AjaxResponse success() {
        return success("success");
    }
}
