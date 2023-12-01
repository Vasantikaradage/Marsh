package com.marsh.android.MB360.insurance.enrollment.interfaces;

import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.DependantHelperModel;

public interface DependantListener {
    void onDependantSavedListener(DependantHelperModel dependant, int position);

    void onTwinsAdded(DependantHelperModel twin1, DependantHelperModel twin2, int position,boolean edit);

    void onDependantEditedListener(DependantHelperModel dependant,int position);

    void onDependantDeletedListener(DependantHelperModel dependant);
}
