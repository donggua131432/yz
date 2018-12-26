package com.yz.model;

/**
 * @描述: 考前确认信息
 * @作者: DuKai
 * @创建时间: 2017/10/24 11:04
 * @版本号: V1.0
 */
public class TestConfirm {

    private String learnId;
    private String stdId;
    private String isPrintNotice;
    private String isPrintProve;
    private String isExamNotice;
    private String isExamSign;


    public String getLearnId() {
        return learnId;
    }

    public void setLearnId(String learnId) {
        this.learnId = learnId;
    }

    public String getStdId() {
        return stdId;
    }

    public void setStdId(String stdId) {
        this.stdId = stdId;
    }

    public String getIsPrintNotice() {
        return isPrintNotice;
    }

    public void setIsPrintNotice(String isPrintNotice) {
        this.isPrintNotice = isPrintNotice;
    }

    public String getIsPrintProve() {
        return isPrintProve;
    }

    public void setIsPrintProve(String isPrintProve) {
        this.isPrintProve = isPrintProve;
    }

    public String getIsExamNotice() {
        return isExamNotice;
    }

    public void setIsExamNotice(String isExamNotice) {
        this.isExamNotice = isExamNotice;
    }

    public String getIsExamSign() {
        return isExamSign;
    }

    public void setIsExamSign(String isExamSign) {
        this.isExamSign = isExamSign;
    }
}
