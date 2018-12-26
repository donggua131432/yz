package com.yz.service.educational.doc;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseDoc {

    private String docPath;

    public String getDocPath() {
        return docPath;
    }

    public void setDocPath(String docPath) {
        this.docPath = docPath;
    }

    /**
     * 专业名称替换
     *
     * @param pfsnName
     * @return
     */
    public String pfsnNameConvert(String pfsnName) {
        Pattern r = Pattern.compile("(\\[([^\\]]*)\\])|(\\(([^\\)]*)\\))");
        Matcher m = r.matcher(pfsnName);
        return m.replaceAll("");
    }

    /**
     * 专业层次转换
     *
     * @param pfsnLevel
     * @return
     */
    public String pfsnLevelConvertSimple(String pfsnLevel) {
        if (pfsnLevel.equals("1")) {
            return "本科";
        } else if (pfsnLevel.equals("5")) {
            return "专科";
        }
        return "";
    }

    public static String doubleFormat(double value) {

        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        /*
         * setMinimumFractionDigits设置成2
         *
         * 如果不这么做，那么当value的值是100.00的时候返回100
         *
         * 而不是100.00
         */
        //nf.setMinimumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        /*
         * 如果想输出的格式用逗号隔开，可以设置成true
         */
        nf.setGroupingUsed(false);
        return nf.format(value);
    }
}
