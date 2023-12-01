package com.marsh.android.MB360.insurance.enrollment.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.SummaryFileResponse;


public class SummaryViewModel extends AndroidViewModel {
    SummaryReository summaryReository;

    public SummaryViewModel(@NonNull Application application) {
        super(application);
        summaryReository = new SummaryReository(application);

    }

    public MutableLiveData<SummaryFileResponse> getSummary(String empSrNo) {
        return summaryReository.getSummaryData(empSrNo);
    }

    public MutableLiveData<SummaryFileResponse> getSummaryDetailsData() {
        return summaryReository.getSummeryDetailsData();
    }

    public MutableLiveData<Boolean> getLoadingState() {
        return summaryReository.loadingState;
    }

    public MutableLiveData<Boolean> getErrorState() {
        return summaryReository.errorState;
    }
}
