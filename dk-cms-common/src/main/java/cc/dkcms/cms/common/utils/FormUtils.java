package cc.dkcms.cms.common.utils;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FormUtils {

    public static final int TYPE_HIDDEN = 0;
    public static final int TYPE_INPUT  = 1;
    public static final int TYPE_SELECT = 2;
    public static final int TYPE_TEXT   = 3;
    public static final int TYPE_IMAGE  = 4; //多张图片
    public static final int TYPE_FILE   = 5;
    public static final int TYPE_BUTTON = 6;

    public static final int TYPE_NUMBER       = 7;
    public static final int TYPE_IMAGE_SINGLE = 8; //单张

    public static final int TYPE_CALENDAR = 9;
    public static final int TYPE_INFO     = 10; // 不可编辑的，用来显示的


    private List<FormItem> formItems = new ArrayList<>();

    public FormUtils addFormItem(String fieldName, int type, String paramName) {
        formItems.add(new FormItem(fieldName, type, paramName, "", "", ""));
        return this;
    }

    public FormUtils addFormItem(String fieldName, int type, String paramName, String defaultVal) {
        formItems.add(new FormItem(fieldName, type, paramName, "", defaultVal, ""));
        return this;
    }

    public FormUtils addFormSelectItem(String fieldName, String paramName, SelectProvider selectProvider) {
        Map<String, Object> data   = selectProvider.getData();
        List<String>        option = new ArrayList<>();
        data.forEach((key, val) -> {
            option.add(key + ":" + val);
        });
        formItems.add(
                new FormItem(
                        fieldName,
                        TYPE_SELECT,
                        paramName,
                        "",
                        StringUtils.join(option, ";"),
                        ""));
        return this;
    }

    public FormUtils addFormItem(String fieldName, int type, String paramName, String defaultVal, String tip) {
        formItems.add(new FormItem(fieldName, type, paramName, tip, defaultVal, ""));
        return this;
    }

    public FormUtils addFormItem(String fieldName, int type, String paramName, String defaultVal, String tip, String placeHolder) {
        formItems.add(new FormItem(fieldName, type, paramName, tip, defaultVal, placeHolder));
        return this;
    }


    public List<FormItem> getFormItems() {
        return formItems;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        formItems.forEach(stringBuilder::append);
        return stringBuilder.toString();
    }

    public static class FormItem {
        private String fieldName;// fieldName
        private int    itemType;
        private String paramName;// 汉字名称
        private String tip;// 字段说明
        private String defaultValue;
        private String placeHolder;

        public FormItem(String fieldName, int itemType, String paramName, String tip,
                        String defaultValue, String placeHolder) {
            this.fieldName = fieldName;
            this.itemType = itemType;
            this.paramName = paramName;
            this.tip = tip;

            this.defaultValue = defaultValue;
            this.placeHolder = placeHolder;
        }


        public String getFieldName() {
            return fieldName;
        }

        public int getItemType() {
            return itemType;
        }

        public String getParamName() {
            return paramName;
        }

        public String getTip() {
            return tip;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public String getPlaceHolder() {
            return placeHolder;
        }

        @Override
        public String toString() {
            return String.format("{field:%s,type:%s}", getFieldName(), getItemType());
        }
    }
}
