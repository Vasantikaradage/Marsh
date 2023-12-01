package com.marsh.android.MB360.insurance.enrollment.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.DependantDetailsResponse;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.DependantDetailsResponseNew;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.DependantHelperModel;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.Dependent;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.EmployeeResponse;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.EnrollmentMessage;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.EnrollmentSummaryResponse;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.InstructionResponse;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.MyCoveragesResponse;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.Parent;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.TopupResponse;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.WindowPeriodEnrollmentResponse;

import java.util.List;
import java.util.Map;

public class EnrollmentViewModel extends AndroidViewModel {

    EnrollmentRepository enrollmentRepository;

    public EnrollmentViewModel(@NonNull Application application) {
        super(application);
        enrollmentRepository = new EnrollmentRepository(application);
    }


    public LiveData<InstructionResponse> getInstructions() {
        return enrollmentRepository.getInstructionResponse();
    }

    public LiveData<InstructionResponse> getInstructionsData() {
        return enrollmentRepository.getInstructionResponseData();
    }

    public LiveData<MyCoveragesResponse> getCoverages() {
        return enrollmentRepository.getCoveragesResponse();
    }

    public LiveData<MyCoveragesResponse> getCoveragesData() {
        return enrollmentRepository.getCoveragesData();
    }

    public LiveData<EmployeeResponse> getEmployee() {
        return enrollmentRepository.getEmployeeResponse();
    }

    public LiveData<EmployeeResponse> getEmployeeData() {
        return enrollmentRepository.getEmployeeData();
    }

    public LiveData<List<Dependent>> getDependants(String windowPeriod, String groupChildSrNo, String oeGrpBasInfoSrNo, String employeeSrNo) {
        return enrollmentRepository.getDependants(windowPeriod, groupChildSrNo, oeGrpBasInfoSrNo, employeeSrNo);
    }

    public LiveData<List<Dependent>> getDependantsData() {
        return enrollmentRepository.getDependantsData();
    }


    public LiveData<DependantDetailsResponse> getRelationshipGroup() {
        return enrollmentRepository.getRelationshipGroup();
    }

    public LiveData<DependantDetailsResponse> getRelationshipGroupData() {
        return enrollmentRepository.getRelationshipGroupData();
    }


    public LiveData<List<Parent>> getParental(String isWindowPeriodActive, String groupChildSrNo, String oeGrpBasInfoSrNo, String employeeSrNo, String parentalPremium) {
        return enrollmentRepository.getParental(isWindowPeriodActive, groupChildSrNo, oeGrpBasInfoSrNo, employeeSrNo, parentalPremium);
    }

    public LiveData<List<Parent>> getParentalData() {
        return enrollmentRepository.getParentalData();
    }

    public LiveData<TopupResponse> getTopups() {
        return enrollmentRepository.getTopUps();
    }

    public LiveData<TopupResponse> getTopUpsData() {
        return enrollmentRepository.getTopUpsData();
    }

    public MutableLiveData<EnrollmentSummaryResponse> getSummary() {
        return enrollmentRepository.getSummary();
    }

    public MutableLiveData<EnrollmentSummaryResponse> getSummaryData() {
        return enrollmentRepository.getSummaryData();
    }


    public LiveData<Boolean> getLoading() {
        return enrollmentRepository.loadingState;
    }

    public LiveData<Boolean> getError() {
        return enrollmentRepository.errorState;
    }

    public LiveData<DependantHelperModel> getTwin1() {
        return enrollmentRepository.getTwin1();
    }

    public LiveData<DependantHelperModel> getTwin2() {
        return enrollmentRepository.getTwin2();
    }

    public void setTwin1(DependantHelperModel dependantHelperModel) {
        enrollmentRepository.setTwin1(dependantHelperModel);
    }

    public void setTwin2(DependantHelperModel dependantHelperModel) {
        enrollmentRepository.setTwin2(dependantHelperModel);
    }


    public MutableLiveData<WindowPeriodEnrollmentResponse> getWindowPeriod() {
        return enrollmentRepository.getWindowPeriod();
    }


    //new dependant details work
    public MutableLiveData<DependantDetailsResponseNew> getDependantDetailsNew() {
        return enrollmentRepository.getDependantDetailsNew();
    }

    public MutableLiveData<EnrollmentMessage> addDependant(Map<String, String> dependantOption) {
        return enrollmentRepository.addDependant(dependantOption);
    }

    public MutableLiveData<EnrollmentMessage> deleteDependant(Map<String, String> dependantOption) {
        return enrollmentRepository.deleteDependant(dependantOption);
    }

    public MutableLiveData<EnrollmentMessage> updateDependant(Map<String, String> dependantOption) {
        return enrollmentRepository.updateDependant(dependantOption);
    }


    public MutableLiveData<EnrollmentMessage> addParent(Map<String, String> parentOption) {
        return enrollmentRepository.addParent(parentOption);
    }

    public MutableLiveData<EnrollmentMessage> deleteParent(Map<String, String> dependantOption) {
        return enrollmentRepository.deleteParent(dependantOption);
    }

    public MutableLiveData<EnrollmentMessage> updateParent(Map<String, String> dependantOption) {
        return enrollmentRepository.updateParent(dependantOption);
    }


    

}
