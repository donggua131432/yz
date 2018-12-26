package com.yz.controller.statistics;

import com.yz.core.security.annotation.Rule;
import com.yz.model.statistics.DataExportQuery;
import com.yz.model.statistics.StudentStatQuery;
import com.yz.model.stdService.StudentXuexinQuery;
import com.yz.service.statistics.DataExportService;
import com.yz.service.statistics.StudentStatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * @描述: 数据导出
 */
@RequestMapping("/dataExport")
@Controller
public class DataExportController {
    private static final Logger log = LoggerFactory.getLogger(DataExportController.class);

    @Autowired
    DataExportService dataExportService;

    @RequestMapping("/export")
    public String export() {
        return "/statistics/data_export";
    }

    @RequestMapping("/exportStudentDataXB")
    @Rule("dataExport:xb")
    public void exportStudentDataXB(DataExportQuery query, HttpServletResponse response) {
        dataExportService.exportStudentDataXB(query, response);
    }

    @RequestMapping("/exportStudentDataGK")
    @Rule("dataExport:gk")
    public void exportStudentDataGK(DataExportQuery query, HttpServletResponse response) {
        dataExportService.exportStudentDataGK(query, response);
    }
}
