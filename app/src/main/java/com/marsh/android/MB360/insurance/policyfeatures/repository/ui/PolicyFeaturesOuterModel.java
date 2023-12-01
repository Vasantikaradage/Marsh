package com.marsh.android.MB360.insurance.policyfeatures.repository.ui;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class PolicyFeaturesOuterModel implements Parcelable {
    String policyType;
    ArrayList<PolicyInnerRecyclerModel> innerList;

    public PolicyFeaturesOuterModel(String policyType, ArrayList<PolicyInnerRecyclerModel> innerList) {
        this.policyType = policyType;
        this.innerList = innerList;
    }

    protected PolicyFeaturesOuterModel(Parcel in) {
        policyType = in.readString();
        innerList = in.createTypedArrayList(PolicyInnerRecyclerModel.CREATOR);
    }

    public static final Creator<PolicyFeaturesOuterModel> CREATOR = new Creator<PolicyFeaturesOuterModel>() {
        @Override
        public PolicyFeaturesOuterModel createFromParcel(Parcel in) {
            return new PolicyFeaturesOuterModel(in);
        }

        @Override
        public PolicyFeaturesOuterModel[] newArray(int size) {
            return new PolicyFeaturesOuterModel[size];
        }
    };

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    public ArrayList<PolicyInnerRecyclerModel> getInnerList() {
        return innerList;
    }

    public void setInnerList(ArrayList<PolicyInnerRecyclerModel> innerList) {
        this.innerList = innerList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(policyType);
        parcel.writeTypedList(innerList);
    }

    @Override
    public String toString() {
        return "PolicyFeatureMainModel{" +
                "policyType='" + policyType + '\'' +
                ", innerList=" + innerList +
                '}';
    }
}
