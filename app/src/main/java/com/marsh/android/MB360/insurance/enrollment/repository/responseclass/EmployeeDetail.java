package com.marsh.android.MB360.insurance.enrollment.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeeDetail {

    @SerializedName("Srno")
    @Expose
    private String srno;
    @SerializedName("FIELD_NAME")
    @Expose
    private String fieldName;
    @SerializedName("FIELD_VALUE")
    @Expose
    private String fieldValue;
    @SerializedName("TO_DISPLAY")
    @Expose
    private String toDisplay;
    @SerializedName("TO_EDITABLE")
    @Expose
    private String toEditable;
    @SerializedName("MANDATORY")
    @Expose
    private String mandatory;
    @SerializedName("TABLE_NAME")
    @Expose
    private String tableName;

    private boolean EDIT_STATE = false;

    public boolean isEDIT_STATE() {
        return EDIT_STATE;
    }

    public void setEDIT_STATE(boolean EDIT_STATE) {
        this.EDIT_STATE = EDIT_STATE;
    }

    public String getSrno() {
        return srno;
    }

    public void setSrno(String srno) {
        this.srno = srno;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getToDisplay() {
        return toDisplay;
    }

    public void setToDisplay(String toDisplay) {
        this.toDisplay = toDisplay;
    }

    public String getToEditable() {
        return toEditable;
    }

    public void setToEditable(String toEditable) {
        this.toEditable = toEditable;
    }

    public String getMandatory() {
        return mandatory;
    }

    public void setMandatory(String mandatory) {
        this.mandatory = mandatory;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return "EmployeeDetail{" +
                "srno='" + srno + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", fieldValue='" + fieldValue + '\'' +
                ", toDisplay=" + toDisplay +
                ", toEditable=" + toEditable +
                ", mandatory=" + mandatory +
                ", tableName='" + tableName + '\'' +
                '}';
    }
}
