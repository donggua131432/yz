package com.yz.model.educational;

import java.util.List;

/**
 * @author jyt
 * @version 1.0
 */
public class ExamRoomAssignInfo {
    private String eyId;
    private List<ExamRoomConfig> examConfig;

    public String getEyId() {
        return eyId;
    }

    public void setEyId(String eyId) {
        this.eyId = eyId;
    }

    public List<ExamRoomConfig> getExamConfig() {
        return examConfig;
    }

    public void setExamConfig(List<ExamRoomConfig> examConfig) {
        this.examConfig = examConfig;
    }
}
