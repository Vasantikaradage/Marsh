package com.marsh.android.MB360.insurance.queries.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.marsh.android.MB360.insurance.queries.responseclass.Message;
import com.marsh.android.MB360.insurance.queries.responseclass.QueryDetails;
import com.marsh.android.MB360.insurance.queries.responseclass.QueryResponse;

import okhttp3.RequestBody;

public class QueryViewModel extends AndroidViewModel {

    QueryRepository queryRepository;

    public QueryViewModel(@NonNull Application application) {
        super(application);
        queryRepository = new QueryRepository(application);
    }


    public LiveData<QueryResponse> getQueries(String empSrNo) {
        return queryRepository.getQuery(empSrNo);
    }

    public LiveData<QueryResponse> getQueriesData() {
        return queryRepository.getQueryData();
    }

    public LiveData<QueryDetails> getQueryDetails(String custQuerySrNo) {
        return queryRepository.getQueryDetails(custQuerySrNo);
    }

    public LiveData<QueryDetails> getQueryDetailsData() {
        return queryRepository.getQueryDetailsData();
    }


    public LiveData<Boolean> getLoading() {
        return queryRepository.getLoading();
    }

    public LiveData<Boolean> getError() {
        return queryRepository.getError();
    }

    public LiveData<Boolean> getLoadingDetails() {
        return queryRepository.loadingDetailState;
    }

    public LiveData<Boolean> getErrorDetails() {
        return queryRepository.errorDetailState;
    }


    public LiveData<Message> marksolve(String eqCustQrySrNo) {

        return   queryRepository.markResolve(eqCustQrySrNo);
    }

    public LiveData<Message> addQuery(RequestBody builder){
        return queryRepository.addQuery(builder);
    }

    public void resetDetails (){
        queryRepository.resetDetails();
    }

    public void setLoadingFromFilePicker (){
        queryRepository.setLoadingFromFilePicker();
    }

    public MutableLiveData<Boolean> getReloginState() {
        return queryRepository.reloginState;
    }

}
