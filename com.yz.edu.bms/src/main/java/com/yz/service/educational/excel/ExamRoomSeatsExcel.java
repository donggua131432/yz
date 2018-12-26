package com.yz.service.educational.excel;

import com.yz.model.educational.BdExamSeatsInfoQuery;
import com.yz.model.educational.BdStudentScoreExcel;
import com.yz.model.educational.TemplateExcel;
import com.yz.util.StringUtil;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class ExamRoomSeatsExcel {

    public String excelRender(List<Map<String, Object>> list, BdExamSeatsInfoQuery query, String text) {
        StringBuilder sb = new StringBuilder();
        for (Map<String, Object> map : list) {
            sb.append(getRowDetail(map));
        }
        text = text.replace("$rowdetail", sb.toString());
        text = text.replace("$eyName", query.getEyName());
        text = text.replace("$epName", query.getEpName());
        text = text.replace("$examTime", query.getExamTimeStr());
        text = text.replace("$placeName", query.getPlaceName());
        return text;
    }

    private String getRowDetail(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("<Row ss:AutoFitHeight=\"0\" ss:Height=\"17.0625\">\n" +
                "    <Cell ss:StyleID=\"s70\"><Data ss:Type=\"String\">" + map.get("esNum") + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s71\"><Data ss:Type=\"String\">" + map.get("stdName") + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s71\"><Data ss:Type=\"String\">" + (map.get("std_no") == null ? "" : map.get("std_no")) + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s71\"><Data ss:Type=\"String\">" + map.get("grade") + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s71\"><Data ss:Type=\"String\">" + map.get("unvsName") + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s71\"><Data ss:Type=\"String\">" + map.get("pfsnLevel") + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s71\"><Data ss:Type=\"String\">" + map.get("pfsnName") + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s71\"><Data ss:Type=\"String\">" + (map.get("eypCode") == null ? "" : map.get("eypCode")) + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s70\"/>\n" +
                "   </Row>\n");
        return sb.toString();
    }
}
