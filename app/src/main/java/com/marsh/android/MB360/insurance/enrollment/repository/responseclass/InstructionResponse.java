package com.marsh.android.MB360.insurance.enrollment.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class InstructionResponse {
    @SerializedName("data_to_show_for")
    @Expose
    private String dataToShowFor;
    @SerializedName("instructions")
    @Expose
    private List<Instruction> instructions = new ArrayList<>();

    public String getDataToShowFor() {
        return dataToShowFor;
    }

    public void setDataToShowFor(String dataToShowFor) {
        this.dataToShowFor = dataToShowFor;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }

    @Override
    public String toString() {
        return "InstructionResponse{" +
                "dataToShowFor='" + dataToShowFor + '\'' +
                ", instructions=" + instructions +
                '}';
    }
}
