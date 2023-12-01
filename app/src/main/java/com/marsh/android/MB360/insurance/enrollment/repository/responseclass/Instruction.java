package com.marsh.android.MB360.insurance.enrollment.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Instruction {
    @SerializedName("Srno")
    @Expose
    private Integer srno;
    @SerializedName("Grade")
    @Expose
    private String grade;
    @SerializedName("Designation")
    @Expose
    private String designation;
    @SerializedName("InstructionText")
    @Expose
    private String instructionText;

    public Integer getSrno() {
        return srno;
    }

    public void setSrno(Integer srno) {
        this.srno = srno;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getInstructionText() {
        return instructionText;
    }

    public void setInstructionText(String instructionText) {
        this.instructionText = instructionText;
    }

    @Override
    public String toString() {
        return "Instruction{" +
                "srno=" + srno +
                ", grade='" + grade + '\'' +
                ", designation='" + designation + '\'' +
                ", instructionText='" + instructionText + '\'' +
                '}';
    }
}
