package com.marsh.android.MB360.insurance.enrollment.interfaces;

import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.Dependent;

public interface DependantListenerNew {

    void openDependantBottomSheet(Dependent dependent,int position ,DependantListenerNew dependantListener);

    void onDependantAdded(Dependent dependent, int position);

    void onDependantRemoved();

    void onDependantUpdated();
}
