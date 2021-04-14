package cc.dkcms.cms.common.define;

import lombok.Data;

@Data
public class Result {
    private Boolean isSuccess;
    private String  msg;
    private Object  data;

    public static Result get(Boolean b) {
        return get(b, "");
    }

    public static Result get(Boolean b, String msg) {
        if (b) {
            return success(msg);
        }
        return fail(msg);
    }

    public static Result success() {
        return success("");
    }

    public static Result success(String msg) {
        Result result = new Result();
        result.setIsSuccess(true);
        result.setMsg(msg);
        return result;
    }

    public static Result fail(String msg) {
        Result result = new Result();
        result.setIsSuccess(false);
        result.setMsg(msg);
        return result;
    }

    public static Result err(String msg) {
        return Result.fail(msg);
    }

    public static Result err() {
        return Result.fail("");
    }


    public boolean isSuccess() {
        return isSuccess;
    }
}
