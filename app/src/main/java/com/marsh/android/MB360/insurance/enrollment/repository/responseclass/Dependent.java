package com.marsh.android.MB360.insurance.enrollment.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dependent {

    @SerializedName("SrNo")
    @Expose
    private Integer srNo;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Relation")
    @Expose
    private String relation;
    @SerializedName("Gender")
    @Expose
    private String gender;
    @SerializedName("RelationID")
    @Expose
    private String relationID;
    @SerializedName("Age")
    @Expose
    private String age;
    @SerializedName("DateOfBirth")
    @Expose
    private String dateOfBirth;
    @SerializedName("PersonSrNo")
    @Expose
    private String personSrNo;
    @SerializedName("ReasonForNotAbleToDelete")
    @Expose
    private String reasonForNotAbleToDelete;
    @SerializedName("IsTwins")
    @Expose
    private String isTwins;
    @SerializedName("ReasonForNotAbleToUpdate")
    @Expose
    private String reasonForNotAbleToUpdate;
    @SerializedName("PairNo")
    @Expose
    private String pairNo;
    @SerializedName("ExtraChildPremium")
    @Expose
    private String extraChildPremium;
    @SerializedName("Premium")
    @Expose
    private String premium;
    @SerializedName("document")
    @Expose
    private String document;
    @SerializedName("enrollment_Reason")
    @Expose
    private String enrollmentReason;
    @SerializedName("parentPercentage")
    @Expose
    private Integer parentPercentage;
    @SerializedName("IsSelected")
    @Expose
    private Boolean isSelected;
    @SerializedName("IsApplicable")
    @Expose
    private Boolean isApplicable;
    @SerializedName("CanDelete")
    @Expose
    private Boolean canDelete;
    @SerializedName("CanUpdate")
    @Expose
    private Boolean canUpdate;
    @SerializedName("IsDisabled")
    @Expose
    private Boolean isDisabled;
    @SerializedName("isAdded")
    @Expose
    private Boolean isAdded;

    public Integer getSrNo() {
        return srNo;
    }

    public void setSrNo(Integer srNo) {
        this.srNo = srNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRelationID() {
        return relationID;
    }

    public void setRelationID(String relationID) {
        this.relationID = relationID;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPersonSrNo() {
        return personSrNo;
    }

    public void setPersonSrNo(String personSrNo) {
        this.personSrNo = personSrNo;
    }

    public String getReasonForNotAbleToDelete() {
        return reasonForNotAbleToDelete;
    }

    public void setReasonForNotAbleToDelete(String reasonForNotAbleToDelete) {
        this.reasonForNotAbleToDelete = reasonForNotAbleToDelete;
    }

    public String getIsTwins() {
        return isTwins;
    }

    public void setIsTwins(String isTwins) {
        this.isTwins = isTwins;
    }

    public String getReasonForNotAbleToUpdate() {
        return reasonForNotAbleToUpdate;
    }

    public void setReasonForNotAbleToUpdate(String reasonForNotAbleToUpdate) {
        this.reasonForNotAbleToUpdate = reasonForNotAbleToUpdate;
    }

    public String getPairNo() {
        return pairNo;
    }

    public void setPairNo(String pairNo) {
        this.pairNo = pairNo;
    }

    public String getExtraChildPremium() {
        return extraChildPremium;
    }

    public void setExtraChildPremium(String extraChildPremium) {
        this.extraChildPremium = extraChildPremium;
    }

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getEnrollmentReason() {
        return enrollmentReason;
    }

    public void setEnrollmentReason(String enrollmentReason) {
        this.enrollmentReason = enrollmentReason;
    }

    public Integer getParentPercentage() {
        return parentPercentage;
    }

    public void setParentPercentage(Integer parentPercentage) {
        this.parentPercentage = parentPercentage;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Boolean getIsApplicable() {
        return isApplicable;
    }

    public void setIsApplicable(Boolean isApplicable) {
        this.isApplicable = isApplicable;
    }

    public Boolean getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(Boolean canDelete) {
        this.canDelete = canDelete;
    }

    public Boolean getCanUpdate() {
        return canUpdate;
    }

    public void setCanUpdate(Boolean canUpdate) {
        this.canUpdate = canUpdate;
    }

    public Boolean getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public Boolean getIsAdded() {
        return isAdded;
    }

    public void setIsAdded(Boolean isAdded) {
        this.isAdded = isAdded;
    }

    @Override
    public String toString() {
        return "Dependent{" +
                "srNo=" + srNo +
                ", name='" + name + '\'' +
                ", relation='" + relation + '\'' +
                ", gender='" + gender + '\'' +
                ", relationID='" + relationID + '\'' +
                ", age='" + age + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", personSrNo='" + personSrNo + '\'' +
                ", reasonForNotAbleToDelete='" + reasonForNotAbleToDelete + '\'' +
                ", isTwins='" + isTwins + '\'' +
                ", reasonForNotAbleToUpdate='" + reasonForNotAbleToUpdate + '\'' +
                ", pairNo='" + pairNo + '\'' +
                ", extraChildPremium='" + extraChildPremium + '\'' +
                ", premium='" + premium + '\'' +
                ", document='" + document + '\'' +
                ", enrollmentReason='" + enrollmentReason + '\'' +
                ", parentPercentage=" + parentPercentage +
                ", isSelected=" + isSelected +
                ", isApplicable=" + isApplicable +
                ", canDelete=" + canDelete +
                ", canUpdate=" + canUpdate +
                ", isDisabled=" + isDisabled +
                ", isAdded=" + isAdded +
                '}';
    }
}
