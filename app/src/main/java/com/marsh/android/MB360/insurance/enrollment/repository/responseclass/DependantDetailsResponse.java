package com.marsh.android.MB360.insurance.enrollment.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DependantDetailsResponse {
    @SerializedName("relation_group")
    @Expose
    private String relationGroup;
    @SerializedName("relations")
    @Expose
    private List<Relation> relations = new ArrayList<>();

    public String getRelationGroup() {
        return relationGroup;
    }

    public void setRelationGroup(String relationGroup) {
        this.relationGroup = relationGroup;
    }

    public List<Relation> getRelations() {
        return relations;
    }

    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }

    @Override
    public String toString() {
        return "DependantDetailsResponse{" +
                "relationGroup='" + relationGroup + '\'' +
                ", relations=" + relations +
                '}';
    }
}
