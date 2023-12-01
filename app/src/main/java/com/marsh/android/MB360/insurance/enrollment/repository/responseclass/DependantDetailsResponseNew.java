package com.marsh.android.MB360.insurance.enrollment.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DependantDetailsResponseNew {

    @SerializedName("Dependent")
    @Expose
    private List<Dependent> dependent = null;

    public List<Dependent> getDependent() {
        return dependent;
    }

    public void setDependent(List<Dependent> dependent) {
        this.dependent = dependent;
    }

    @Override
    public String toString() {
        return "DependantDetailsResponseNew{" +
                "dependent=" + dependent +
                '}';
    }
}
