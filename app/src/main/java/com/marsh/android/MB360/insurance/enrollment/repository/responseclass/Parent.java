package com.marsh.android.MB360.insurance.enrollment.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Parent {
    @SerializedName("Premium")
    @Expose
    private String premium;
    @SerializedName("PremiumType")
    @Expose
    private Object premiumType;
    @SerializedName("SumInsured")
    @Expose
    private Object sumInsured;
    @SerializedName("SiType")
    @Expose
    private Object siType;
    @SerializedName("PersonSrNo")
    @Expose
    private String personSrNo;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Age")
    @Expose
    private String age;
    @SerializedName("DateOfBirthToShow")
    @Expose
    private String dateOfBirthToShow;
    @SerializedName("DateOfBirth")
    @Expose
    private String dateOfBirth;
    @SerializedName("Relation")
    @Expose
    private String relation;
    @SerializedName("RelationId")
    @Expose
    private String relationId;
    @SerializedName("Covered")
    @Expose
    private Boolean covered;
    @SerializedName("CanAdd")
    @Expose
    private Boolean canAdd;
    @SerializedName("CanUpdate")
    @Expose
    private Boolean canUpdate;
    @SerializedName("CanDelete")
    @Expose
    private Boolean canDelete;
    @SerializedName("CanInclude")
    @Expose
    private Boolean canInclude;
    @SerializedName("Reason")
    @Expose
    private String reason;
    @SerializedName("CoveredInPolicyType")
    @Expose
    private Integer coveredInPolicyType;
    @SerializedName("EmployerContri")
    @Expose
    private Object employerContri;
    @SerializedName("EmployeeContri")
    @Expose
    private Object employeeContri;
    @SerializedName("ParentsBaseSumInsured")
    @Expose
    private String parentsBaseSumInsured;
    @SerializedName("SortNo")
    @Expose
    private String sortNo;

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }

    public Object getPremiumType() {
        return premiumType;
    }

    public void setPremiumType(Object premiumType) {
        this.premiumType = premiumType;
    }

    public Object getSumInsured() {
        return sumInsured;
    }

    public void setSumInsured(Object sumInsured) {
        this.sumInsured = sumInsured;
    }

    public Object getSiType() {
        return siType;
    }

    public void setSiType(Object siType) {
        this.siType = siType;
    }

    public String getPersonSrNo() {
        return personSrNo;
    }

    public void setPersonSrNo(String personSrNo) {
        this.personSrNo = personSrNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDateOfBirthToShow() {
        return dateOfBirthToShow;
    }

    public void setDateOfBirthToShow(String dateOfBirthToShow) {
        this.dateOfBirthToShow = dateOfBirthToShow;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public Boolean getCovered() {
        return covered;
    }

    public void setCovered(Boolean covered) {
        this.covered = covered;
    }

    public Boolean getCanAdd() {
        return canAdd;
    }

    public void setCanAdd(Boolean canAdd) {
        this.canAdd = canAdd;
    }

    public Boolean getCanUpdate() {
        return canUpdate;
    }

    public void setCanUpdate(Boolean canUpdate) {
        this.canUpdate = canUpdate;
    }

    public Boolean getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(Boolean canDelete) {
        this.canDelete = canDelete;
    }

    public Boolean getCanInclude() {
        return canInclude;
    }

    public void setCanInclude(Boolean canInclude) {
        this.canInclude = canInclude;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getCoveredInPolicyType() {
        return coveredInPolicyType;
    }

    public void setCoveredInPolicyType(Integer coveredInPolicyType) {
        this.coveredInPolicyType = coveredInPolicyType;
    }

    public Object getEmployerContri() {
        return employerContri;
    }

    public void setEmployerContri(Object employerContri) {
        this.employerContri = employerContri;
    }

    public Object getEmployeeContri() {
        return employeeContri;
    }

    public void setEmployeeContri(Object employeeContri) {
        this.employeeContri = employeeContri;
    }

    public String getParentsBaseSumInsured() {
        return parentsBaseSumInsured;
    }

    public void setParentsBaseSumInsured(String parentsBaseSumInsured) {
        this.parentsBaseSumInsured = parentsBaseSumInsured;
    }

    public String getSortNo() {
        return sortNo;
    }

    public void setSortNo(String sortNo) {
        this.sortNo = sortNo;
    }

    @Override
    public String toString() {
        return "Parent{" +
                "premium='" + premium + '\'' +
                ", premiumType=" + premiumType +
                ", sumInsured=" + sumInsured +
                ", siType=" + siType +
                ", personSrNo='" + personSrNo + '\'' +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", dateOfBirthToShow='" + dateOfBirthToShow + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", relation='" + relation + '\'' +
                ", relationId='" + relationId + '\'' +
                ", covered=" + covered +
                ", canAdd=" + canAdd +
                ", canUpdate=" + canUpdate +
                ", canDelete=" + canDelete +
                ", canInclude=" + canInclude +
                ", reason='" + reason + '\'' +
                ", coveredInPolicyType=" + coveredInPolicyType +
                ", employerContri=" + employerContri +
                ", employeeContri=" + employeeContri +
                ", parentsBaseSumInsured='" + parentsBaseSumInsured + '\'' +
                ", sortNo='" + sortNo + '\'' +
                '}';
    }
}
