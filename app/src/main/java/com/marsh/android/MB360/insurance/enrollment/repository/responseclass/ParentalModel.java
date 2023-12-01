package com.marsh.android.MB360.insurance.enrollment.repository.responseclass;

import java.io.File;

public class ParentalModel extends DependantHelperModel {
    Boolean isHeader;

    public ParentalModel(String relationName, Integer maxCount, String group, Boolean isAdded, String name, String dateOfBirth, Boolean isDifferentlyAble, File document, String age, Boolean canEdit, Boolean canDelete, String gender, Boolean isHeader, String personSrNo, String pair_no, String relationId) {

        super(relationName, maxCount, group, isAdded, name, dateOfBirth, isDifferentlyAble, document, age, canEdit, canDelete, gender, personSrNo, pair_no, relationId);
        this.isHeader = isHeader;
    }

    public Boolean getHeader() {
        return isHeader;
    }

    public void setHeader(Boolean header) {
        isHeader = header;
    }


}
