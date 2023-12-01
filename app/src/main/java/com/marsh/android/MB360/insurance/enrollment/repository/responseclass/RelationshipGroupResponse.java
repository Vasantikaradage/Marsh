package com.marsh.android.MB360.insurance.enrollment.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RelationshipGroupResponse {
    @SerializedName("relations")
    @Expose
    private List<Relation> relations = null;

    public List<Relation> getRelations() {
        return relations;
    }

    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }

    @Override
    public String toString() {
        return "RelationshipGroupResponse{" +
                "relations=" + relations +
                '}';
    }
}
