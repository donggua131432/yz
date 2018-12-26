package com.yz.model;

import java.io.Serializable;

public class BccFieldCheck implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2444239203281098188L;

	private String checkId;

    private String fieldName;

    private String rules;

    private String replaceChar;
    
    private String fieldDesc;
    
    private String message;
    
    private String excuteType;
    
    private String allowNull;

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId == null ? null : checkId.trim();
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName == null ? null : fieldName.trim();
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules == null ? null : rules.trim();
    }

    public String getReplaceChar() {
        return replaceChar;
    }

    public void setReplaceChar(String replaceChar) {
        this.replaceChar = replaceChar == null ? null : replaceChar.trim();
    }

    public String getFieldDesc() {
		return fieldDesc;
	}

	public void setFieldDesc(String fieldDesc) {
		this.fieldDesc = fieldDesc == null ? null : fieldDesc.trim();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message == null ? null : message.trim();
	}

	public String getExcuteType() {
		return excuteType;
	}

	public void setExcuteType(String excuteType) {
		this.excuteType = excuteType;
	}

	public String getAllowNull() {
		return allowNull;
	}

	public void setAllowNull(String allowNull) {
		this.allowNull = allowNull;
	}

}