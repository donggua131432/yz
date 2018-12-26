package com.yz.service.educational.excel;

public class TemplateExcelFactory {
    public static BaseTemplateExcel createTemplateExcel(String unvsId) {
        BaseTemplateExcel templateExcel = null;
        switch (unvsId) {
            // 嘉应学院
            case "1":
                templateExcel = new JYTemplateExcel();
                break;
            // 仲恺农业工程学院
            case "2":
                templateExcel = new ZKTemplateExcel();
                break;
            // 广东金融学院
            case "5":
                templateExcel = new GJTemplateExcel();
                break;
            // 韩山师范学院
            case "29":
                templateExcel = new HSTemplateExcel();
                break;
            // 汕尾职业技术学院
            case "31":
                templateExcel = new SYZTemplateExcel();
                break;
            // 广州市广播电视大学
            case "51":
                templateExcel = new GDTemplateExcel();
                break;
        }
        return templateExcel;
    }
}
