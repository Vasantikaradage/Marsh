package com.marsh.android.MB360.insurance.enrollment.interfaces;

import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.DependantHelperModel;

public interface DependantHelper {
    //when adding the dependant
    void onAddDependant(DependantHelperModel dependantHelperModel,int position);

    //when editing the dependant
    void onEditDependant(DependantHelperModel dependant, int position);

    //when deleting the dependant
    void onDeleteDependant();

    //on twin edit
    void onEditTwin(int position);
}
