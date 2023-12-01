package com.marsh.android.MB360.insurance.enrollment.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Relation {
    @SerializedName("Relation")
    @Expose
    private String relation;
    @SerializedName("max_count")
    @Expose
    private Integer maxCount;
    @SerializedName("RelationID")
    @Expose
    private String relationID;
    @SerializedName("group")
    @Expose
    private String group;
    @SerializedName("max_age")
    @Expose
    private Integer maxAge;
    @SerializedName("min_age")
    @Expose
    private Integer minAge;

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    public String getRelationID() {
        return relationID;
    }

    public void setRelationID(String relationID) {
        this.relationID = relationID;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    @Override
    public String toString() {
        return "Relation{" +
                "relation='" + relation + '\'' +
                ", maxCount=" + maxCount +
                ", relationID='" + relationID + '\'' +
                ", group='" + group + '\'' +
                ", maxAge=" + maxAge +
                ", minAge=" + minAge +
                '}';
    }
}
