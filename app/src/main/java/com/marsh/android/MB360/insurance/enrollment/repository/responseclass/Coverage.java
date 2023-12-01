package com.marsh.android.MB360.insurance.enrollment.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coverage {

    @SerializedName("policy_type")
    @Expose
    private String policyType;
    @SerializedName("type_of_policy")
    @Expose
    private String typeOfPolicy;
    @SerializedName("sum_insured")
    @Expose
    private String sumInsured;
    @SerializedName("sum_insure_type")
    @Expose
    private String sumInsureType;
    @SerializedName("premium")
    @Expose
    private String premium;
    @SerializedName("premium_type")
    @Expose
    private String premiumType;
    @SerializedName("to_show_premium")
    @Expose
    private String toShowPremium;
    @SerializedName("how_to_show_sum_insured")
    @Expose
    private String howToShowSumInsured;
    @SerializedName("employer_contribution")
    @Expose
    private String employerContribution;
    @SerializedName("employee_contribution")
    @Expose
    private String employeeContribution;
    @SerializedName("to_show_employee_contribution")
    @Expose
    private String toShowEmployeeContribution;
    @SerializedName("to_show_employer_contribution")
    @Expose
    private String toShowEmployerContribution;
    @SerializedName("instruction")
    @Expose
    private String instruction;

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    public String getTypeOfPolicy() {
        return typeOfPolicy;
    }

    public void setTypeOfPolicy(String typeOfPolicy) {
        this.typeOfPolicy = typeOfPolicy;
    }

    public String getSumInsured() {
        return sumInsured;
    }

    public void setSumInsured(String sumInsured) {
        this.sumInsured = sumInsured;
    }

    public String getSumInsureType() {
        return sumInsureType;
    }

    public void setSumInsureType(String sumInsureType) {
        this.sumInsureType = sumInsureType;
    }

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }

    public String getPremiumType() {
        return premiumType;
    }

    public void setPremiumType(String premiumType) {
        this.premiumType = premiumType;
    }

    public String getToShowPremium() {
        return toShowPremium;
    }

    public void setToShowPremium(String toShowPremium) {
        this.toShowPremium = toShowPremium;
    }

    public String getHowToShowSumInsured() {
        return howToShowSumInsured;
    }

    public void setHowToShowSumInsured(String howToShowSumInsured) {
        this.howToShowSumInsured = howToShowSumInsured;
    }

    public String getEmployerContribution() {
        return employerContribution;
    }

    public void setEmployerContribution(String employerContribution) {
        this.employerContribution = employerContribution;
    }

    public String getEmployeeContribution() {
        return employeeContribution;
    }

    public void setEmployeeContribution(String employeeContribution) {
        this.employeeContribution = employeeContribution;
    }

    public String getToShowEmployeeContribution() {
        return toShowEmployeeContribution;
    }

    public void setToShowEmployeeContribution(String toShowEmployeeContribution) {
        this.toShowEmployeeContribution = toShowEmployeeContribution;
    }

    public String getToShowEmployerContribution() {
        return toShowEmployerContribution;
    }

    public void setToShowEmployerContribution(String toShowEmployerContribution) {
        this.toShowEmployerContribution = toShowEmployerContribution;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
