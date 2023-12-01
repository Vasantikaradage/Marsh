package com.marsh.android.MB360.insurance.claims.repository.requestclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewIntimateRequest {

    @SerializedName("groupchildsrno")
    @Expose
    private String groupchildsrno;
    @SerializedName("oegrpbasinfsrno")
    @Expose
    private String oegrpbasinfsrno;
    @SerializedName("employeesrno")
    @Expose
    private String employeesrno;
    @SerializedName("personsrno")
    @Expose
    private String personsrno;
    @SerializedName("diagnosis")
    @Expose
    private String diagnosis;
    @SerializedName("claimamount")
    @Expose
    private String claimamount;
    @SerializedName("doalikelydoa")
    @Expose
    private String doalikelydoa;
    @SerializedName("hospitalname")
    @Expose
    private String hospitalname;
    @SerializedName("hospitallocation")
    @Expose
    private String hospitallocation;
    @SerializedName("typeofclaim")
    @Expose
    private String typeofclaim;

    @SerializedName("beneficiary_name")
    @Expose
    private String beneficiary_name;

    @SerializedName("policy_no")
    @Expose
    private String policy_no;

    @SerializedName("tpacode")
    @Expose
    private String tpacode;

    @SerializedName("POLICY_COMMENCEMENT_DATE")
    @Expose
    private String POLICY_COMMENCEMENT_DATE;

    @SerializedName("POLICY_VALIDUPTO")
    @Expose
    private String POLICY_VALIDUPTO;

    @SerializedName("GROUP_CODE")
    @Expose
    private String GROUP_CODE;


    public NewIntimateRequest() {

        this.groupchildsrno = "";
        this.oegrpbasinfsrno = "";
        this.employeesrno = "";
        this.personsrno = "";
        this.diagnosis = "";
        this.claimamount = "";
        this.doalikelydoa = "";
        this.hospitalname = "";
        this.hospitallocation = "";
        this.typeofclaim = "";
        this.beneficiary_name = "";
        this.policy_no = "";
        this.tpacode = "";
        this.POLICY_COMMENCEMENT_DATE = "";
        this.POLICY_VALIDUPTO = "";
        this.GROUP_CODE = "";

    }

    public String getGroupchildsrno() {
        return groupchildsrno;
    }

    public void setGroupchildsrno(String groupchildsrno) {
        this.groupchildsrno = groupchildsrno;
    }

    public String getOegrpbasinfsrno() {
        return oegrpbasinfsrno;
    }

    public void setOegrpbasinfsrno(String oegrpbasinfsrno) {
        this.oegrpbasinfsrno = oegrpbasinfsrno;
    }

    public String getEmployeesrno() {
        return employeesrno;
    }

    public void setEmployeesrno(String employeesrno) {
        this.employeesrno = employeesrno;
    }

    public String getPersonsrno() {
        return personsrno;
    }

    public void setPersonsrno(String personsrno) {
        this.personsrno = personsrno;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getClaimamount() {
        return claimamount;
    }

    public void setClaimamount(String claimamount) {
        this.claimamount = claimamount;
    }

    public String getDoalikelydoa() {
        return doalikelydoa;
    }

    public void setDoalikelydoa(String doalikelydoa) {
        this.doalikelydoa = doalikelydoa;
    }

    public String getHospitalname() {
        return hospitalname;
    }

    public void setHospitalname(String hospitalname) {
        this.hospitalname = hospitalname;
    }

    public String getHospitallocation() {
        return hospitallocation;
    }

    public void setHospitallocation(String hospitallocation) {
        this.hospitallocation = hospitallocation;
    }


    public String getTypeofclaim() {
        return typeofclaim;
    }

    public void setTypeofclaim(String typeofclaim) {
        this.typeofclaim = typeofclaim;
    }

    public String getBeneficiary_name() {
        return beneficiary_name;
    }

    public void setBeneficiary_name(String beneficiary_name) {
        this.beneficiary_name = beneficiary_name;
    }

    public String getPolicy_no() {
        return policy_no;
    }

    public void setPolicy_no(String policy_no) {
        this.policy_no = policy_no;
    }

    public String getTpacode() {
        return tpacode;
    }

    public void setTpacode(String tpacode) {
        this.tpacode = tpacode;
    }

    public String getPOLICY_COMMENCEMENT_DATE() {
        return POLICY_COMMENCEMENT_DATE;
    }

    public void setPOLICY_COMMENCEMENT_DATE(String POLICY_COMMENCEMENT_DATE) {
        this.POLICY_COMMENCEMENT_DATE = POLICY_COMMENCEMENT_DATE;
    }

    public String getPOLICY_VALIDUPTO() {
        return POLICY_VALIDUPTO;
    }

    public void setPOLICY_VALIDUPTO(String POLICY_VALIDUPTO) {
        this.POLICY_VALIDUPTO = POLICY_VALIDUPTO;
    }

    public String getGROUP_CODE() {
        return GROUP_CODE;
    }

    public void setGROUP_CODE(String GROUP_CODE) {
        this.GROUP_CODE = GROUP_CODE;
    }

    @Override
    public String toString() {
        return "NewIntimateRequest{" +
                "groupchildsrno='" + groupchildsrno + '\'' +
                ", oegrpbasinfsrno='" + oegrpbasinfsrno + '\'' +
                ", employeesrno='" + employeesrno + '\'' +
                ", personsrno='" + personsrno + '\'' +
                ", diagnosis='" + diagnosis + '\'' +
                ", claimamount='" + claimamount + '\'' +
                ", doalikelydoa='" + doalikelydoa + '\'' +
                ", hospitalname='" + hospitalname + '\'' +
                ", hospitallocation='" + hospitallocation + '\'' +
                ", typeofclaim='" + typeofclaim + '\'' +
                ", beneficiary_name='" + beneficiary_name + '\'' +
                ", policy_no='" + policy_no + '\'' +
                ", tpacode='" + tpacode + '\'' +
                ", POLICY_COMMENCEMENT_DATE='" + POLICY_COMMENCEMENT_DATE + '\'' +
                ", POLICY_VALIDUPTO='" + POLICY_VALIDUPTO + '\'' +
                ", GROUP_CODE='" + GROUP_CODE + '\'' +
                '}';
    }
}
