package com.marsh.android.MB360.insurance.enrollment.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import com.marsh.android.MB360.insurance.enrollment.repository.retrofit.EnrollmentRetrofitClient;
import com.marsh.android.MB360.utilities.LogMyBenefits;
import com.marsh.android.MB360.utilities.LogTags;
import com.marsh.android.MB360.utilities.ResponseException;
import com.marsh.android.MB360.utilities.UtilMethods;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnrollmentRepository {

    Application application;
    public final MutableLiveData<Boolean> loadingState; //main enrollment loading.
    public final MutableLiveData<Boolean> errorState; //main enrollment error.
    public final MutableLiveData<InstructionResponse> instructionLiveData;
    public final MutableLiveData<MyCoveragesResponse> coveragesLiveData;
    public final MutableLiveData<EmployeeResponse> employeeLiveData;
    public final MutableLiveData<DependantDetailsResponse> dependantDetailLiveData;
    public final MutableLiveData<WindowPeriodEnrollmentResponse> windowPeriodMutableLiveData;
    //dependant data from the server
    public final MutableLiveData<List<Dependent>> dependantLiveData;
    FirebaseCrashlytics crashlytics;



    //Window Period loading and error
    public MutableLiveData<Boolean> windowPeriodLoadingState = new MutableLiveData<>(true);
    public MutableLiveData<Boolean> windowPeriodErrorState = new MutableLiveData<>(false);

    //instruction loading and error
    public MutableLiveData<Boolean> instructionLoadingState = new MutableLiveData<>(true);
    public MutableLiveData<Boolean> instructionErrorState = new MutableLiveData<>(false);

    //my coverages loading and error
    public MutableLiveData<Boolean> coverageLoadingState = new MutableLiveData<>(true);
    public MutableLiveData<Boolean> coverageErrorState = new MutableLiveData<>(false);

    //employee details loading and error
    public MutableLiveData<Boolean> employeeLoadingState = new MutableLiveData<>(true);
    public MutableLiveData<Boolean> employeeErrorState = new MutableLiveData<>(false);


    //dependant details loading and error
    public MutableLiveData<Boolean> dependantLoadingState = new MutableLiveData<>(true);
    public MutableLiveData<Boolean> dependantErrorState = new MutableLiveData<>(false);

    //parental details loading and error
    public MutableLiveData<Boolean> parentalLoadingState = new MutableLiveData<>(true);
    public MutableLiveData<Boolean> parentalErrorState = new MutableLiveData<>(false);


    public MutableLiveData<EnrollmentSummaryResponse> summaryLiveData;
    public MutableLiveData<DependantHelperModel> twin1 = new MutableLiveData<>();
    public MutableLiveData<DependantHelperModel> twin2 = new MutableLiveData<>();

    public final MutableLiveData<List<Parent>> parentalData;
    public final MutableLiveData<TopupResponse> topupData;


    //new dependant details
    public final MutableLiveData<DependantDetailsResponseNew> dependantDetailsLiveDataNew;


    public EnrollmentRepository(Application application) {
        this.application = application;
        this.loadingState = new MutableLiveData<>(true);
        this.errorState = new MutableLiveData<>(false);
        this.instructionLiveData = new MutableLiveData<>();
        this.coveragesLiveData = new MutableLiveData<>();
        this.employeeLiveData = new MutableLiveData<>();
        this.dependantDetailLiveData = new MutableLiveData<>();
        this.parentalData = new MutableLiveData<>();
        this.topupData = new MutableLiveData<>();
        this.summaryLiveData = new MutableLiveData<>();
        this.windowPeriodMutableLiveData = new MutableLiveData<>();
        this.dependantDetailsLiveDataNew = new MutableLiveData<>();
        this.dependantLiveData = new MutableLiveData<>();
        crashlytics = FirebaseCrashlytics.getInstance();
    }

    public LiveData<InstructionResponse> getInstructionResponse() {

        loadingState.setValue(false);
        //todo api call for instruction
        /**
         * once the api call is done we make change the
         * loading -> false
         * as we don't need the loading after the
         * successful api call
         * in this case (for testing we are getting the response
         * from the json file)
         * **/
        Call<InstructionResponse> instructionCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().getEnrollmentInstructions();

        instructionCall.enqueue(new Callback<InstructionResponse>() {
            @Override
            public void onResponse(Call<InstructionResponse> call, Response<InstructionResponse> response) {
                if (response.code() == 200) {
                    // we render the response

                } else {
                    //something went wrong.
                }
            }

            @Override
            public void onFailure(Call<InstructionResponse> call, Throwable t) {
                //something went wrong in failure.
            }
        });

        String response = UtilMethods.getAssetJsonData(application, "InstructionResponse.json");
        Type type = new TypeToken<InstructionResponse>() {
        }.getType();
        InstructionResponse instructionResponse = new Gson().fromJson(response, type);
        instructionLiveData.setValue(instructionResponse);
        LogMyBenefits.d(LogTags.ENROLLMENT, "getInstructionResponse: " + instructionResponse.toString());

        return instructionLiveData;
    }


    public LiveData<InstructionResponse> getInstructionResponseData() {
        return instructionLiveData;
    }

    public LiveData<MyCoveragesResponse> getCoveragesResponse() {
        coverageLoadingState.setValue(true);
        Call<MyCoveragesResponse> enrollmentCoveragesCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().getEnrollmentCoverages();

        enrollmentCoveragesCall.enqueue(new Callback<MyCoveragesResponse>() {
            @Override
            public void onResponse(Call<MyCoveragesResponse> call, Response<MyCoveragesResponse> response) {
                try {
                    if (response.code() == 200) {
                        //success
                        //todo here we parse the response.
                        coverageLoadingState.setValue(false);
                        coverageErrorState.setValue(false);


                    } else {
                        //something went wrong.
                    }

                } catch (Exception e) {
                    //error

                }
            }

            @Override
            public void onFailure(Call<MyCoveragesResponse> call, Throwable t) {

            }
        });

        loadingState.setValue(false);
        //todo api call for Coverages
        /**
         * once the api call is done we make change the
         * loading -> false
         * as we don't need the loading after the
         * successful api call
         * in this case (for testing we are getting the response
         * from the json file)
         * **/
        String response = UtilMethods.getAssetJsonData(application, "CoveragesResponse.json");
        Type type = new TypeToken<MyCoveragesResponse>() {
        }.getType();
        MyCoveragesResponse coveragesResponse = new Gson().fromJson(response, type);
        coveragesLiveData.setValue(coveragesResponse);

        return coveragesLiveData;
    }

    public LiveData<MyCoveragesResponse> getCoveragesData() {
        return coveragesLiveData;
    }

    public LiveData<EmployeeResponse> getEmployeeResponse() {
        employeeLoadingState.setValue(true);
        Call<EmployeeResponse> employeeCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().getEnrollmentEmployeeDetails();

        employeeCall.enqueue(new Callback<EmployeeResponse>() {
            @Override
            public void onResponse(Call<EmployeeResponse> call, Response<EmployeeResponse> response) {
                if (response.code() == 200) {
                    try {
                        employeeLoadingState.setValue(false);
                        employeeErrorState.setValue(false);
                        employeeLiveData.setValue(response.body());
                    } catch (Exception exception) {
                        employeeLiveData.setValue(null);
                        employeeLoadingState.setValue(false);
                        employeeErrorState.setValue(true);
                        exception.printStackTrace();
                    }

                } else {
                    //if response code is not 200
                    employeeLiveData.setValue(null);
                    employeeLoadingState.setValue(false);
                    employeeErrorState.setValue(true);
                    ResponseException responseException = new ResponseException("Enrollment -> getEmpresponse -> Response Code:- " + response.code());
                    crashlytics.recordException(responseException);

                }
            }

            @Override
            public void onFailure(Call<EmployeeResponse> call, Throwable t) {
                //if call is not handled
                LogMyBenefits.e(LogTags.ENROLLMENT, "Error: " + t.getLocalizedMessage());

                employeeLiveData.setValue(null);
                employeeLoadingState.setValue(false);
                employeeErrorState.setValue(true);
            }
        });


        loadingState.setValue(false);
        //todo api call for Employee details


        /**
         * once the api call is done we make change the
         * loading -> false
         * as we don't need the loading after the
         * successful api call
         * in this case (for testing we are getting the response
         * from the json file)
         * */
        String response = UtilMethods.getAssetJsonData(application, "EmployeeResponse.json");
        Type type = new TypeToken<EmployeeResponse>() {
        }.getType();
        EmployeeResponse employeeResponse = new Gson().fromJson(response, type);
        employeeLiveData.setValue(employeeResponse);

        return employeeLiveData;
    }

    public LiveData<EmployeeResponse> getEmployeeData() {
        return employeeLiveData;
    }

    /**
     * get dependants from the server
     */
    public MutableLiveData<List<Dependent>> getDependants(String windowPeriodActive, String groupChildSrNo, String oeGrpBasInfoSrNo, String employeeSrNo) {
        //here we also load the dependant screen loading.
        dependantLoadingState.setValue(true);
        dependantErrorState.setValue(false);

        Call<List<Dependent>> dependantCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().getDependants(windowPeriodActive, groupChildSrNo, oeGrpBasInfoSrNo, employeeSrNo);

        dependantCall.enqueue(new Callback<List<Dependent>>() {
            @Override
            public void onResponse(Call<List<Dependent>> call, Response<List<Dependent>> response) {

                if (response.code() == 200) {
                    try {
                        dependantLoadingState.setValue(false);
                        dependantErrorState.setValue(false);
                        LogMyBenefits.d(LogTags.ENROLLMENT, "getDependant: " + response.body());
                        dependantLiveData.setValue(response.body());
                    } catch (Exception e) {
                        e.printStackTrace();
                        dependantLoadingState.setValue(false);
                        dependantErrorState.setValue(true);
                        dependantLiveData.setValue(null);
                        LogMyBenefits.d(LogTags.ENROLLMENT, "getDependant: " + response.body());
                    }
                } else {
                    //something went wrong from server
                    LogMyBenefits.d(LogTags.ENROLLMENT, "getDependant: " + response.body());
                    dependantLoadingState.setValue(false);
                    dependantErrorState.setValue(true);
                    dependantLiveData.setValue(null);

                    ResponseException responseException = new ResponseException("Enrollment -> getDepenedent -> Response Code:- " + response.code() + "groupChildSrNo:- " +groupChildSrNo+"oeGrpBasInfSrNo:-"+oeGrpBasInfoSrNo+"employeeSrNo:-"+employeeSrNo);
                    crashlytics.recordException(responseException);
                }

            }


            @Override
            public void onFailure(Call<List<Dependent>> call, Throwable t) {
                dependantLoadingState.setValue(false);
                dependantErrorState.setValue(true);
                dependantLiveData.setValue(null);
                t.printStackTrace();
            }
        });


        return dependantLiveData;
    }

    public MutableLiveData<List<Dependent>> getDependantsData() {
        return dependantLiveData;
    }

    public MutableLiveData<DependantDetailsResponse> getRelationshipGroup() {
        loadingState.setValue(false);
        //todo api call for Dependant details

      /*  Call<DependantDetailsResponse> dependantDetailsCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().getRelationShipGroup();
        dependantDetailsCall.enqueue(new Callback<DependantDetailsResponse>() {
            @Override
            public void onResponse(Call<DependantDetailsResponse> call, Response<DependantDetailsResponse> response) {
                //on response
                try {
                    if (response.code() == 200) {
                        //response is 200
                        dependantLoadingState.setValue(false);
                        dependantErrorState.setValue(false);
                        dependantDetailLiveData.setValue(response.body());

                    } else {
                        //if response code is not 200
                        dependantLoadingState.setValue(false);
                        dependantErrorState.setValue(true);
                        dependantDetailLiveData.setValue(null);
                    }
                } catch (Exception e) {
                    //something went wrong
                    dependantLoadingState.setValue(false);
                    dependantErrorState.setValue(true);
                    dependantDetailLiveData.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<DependantDetailsResponse> call, Throwable t) {
                //on failure
                LogMyBenefits.e(LogTags.ENROLLMENT, "Error: " + t.getLocalizedMessage());
                dependantLoadingState.setValue(false);
                dependantErrorState.setValue(true);
                dependantDetailLiveData.setValue(null);

            }
        });
*/
        /**
         * once the api call is done we make change the
         * loading -> false
         * as we don't need the loading after the
         * successful api call
         * in this case (for testing we are getting the response
         * from the json file)
         * */
        String response = UtilMethods.getAssetJsonData(application, "RelationGroupResponse.json");
        Type type = new TypeToken<DependantDetailsResponse>() {
        }.getType();
        DependantDetailsResponse dependantDetailsResponse = new Gson().fromJson(response, type);
        dependantDetailLiveData.setValue(dependantDetailsResponse);


        /* we need to merge the relationship group data (allowed relation) to
        actual dependant from the employees family coverage.
        FLOW :-
        get relationship group.
        get dependant from the employee family
        merge the response
        create a list
        handle the addition,update and deletion of the members
        refresh after every interaction */

        LogMyBenefits.d(LogTags.ENROLLMENT, "getRelationshipGroup: " + dependantDetailsResponse);
        return dependantDetailLiveData;
    }

    public MutableLiveData<DependantDetailsResponse> getRelationshipGroupData() {
        return dependantDetailLiveData;
    }

    public MutableLiveData<List<Parent>> getParental(String isWindowPeriodActive, String groupChildSrNo, String oeGrpBasInfoSrNo, String employeeSrNo, String parentalPremium) {
        parentalLoadingState.setValue(true);
        //todo api call for Parental details

        /**
         * once the api call is done we make change the
         * loading -> false
         * as we don't need the loading after the
         * successful api call
         * in this case (for testing we are getting the response
         * from the json file)
         * */
        String response = UtilMethods.getAssetJsonData(application, "Parental.json");
        Type type = new TypeToken<DependantDetailsResponse>() {
        }.getType();
        DependantDetailsResponse dependantDetailsResponse = new Gson().fromJson(response, type);


        Call<List<Parent>> parentCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().getParentalDependant(isWindowPeriodActive, groupChildSrNo, oeGrpBasInfoSrNo, employeeSrNo, parentalPremium);

        parentCall.enqueue(new Callback<List<Parent>>() {
            @Override
            public void onResponse(Call<List<Parent>> call, Response<List<Parent>> response) {
                if (response.code() == 200) {
                    try {
                        parentalLoadingState.setValue(false);
                        parentalErrorState.setValue(false);
                        LogMyBenefits.d(LogTags.ENROLLMENT, "getParental: " + response.body());
                        parentalData.setValue(response.body());
                    } catch (Exception e) {
                        e.printStackTrace();
                        parentalLoadingState.setValue(false);
                        parentalErrorState.setValue(true);
                        parentalData.setValue(null);
                        LogMyBenefits.d(LogTags.ENROLLMENT, "getParental: " + response.body());
                    }
                } else {
                    //something went wrong from server
                    LogMyBenefits.d(LogTags.ENROLLMENT, "getParental: " + response.body());
                    parentalLoadingState.setValue(false);
                    parentalErrorState.setValue(true);
                    parentalData.setValue(null);
                    ResponseException responseException = new ResponseException("Enroolment -> getPerental -> Response Code:- " + response.code());
                    crashlytics.recordException(responseException);
                }
            }

            @Override
            public void onFailure(Call<List<Parent>> call, Throwable t) {
                LogMyBenefits.d(LogTags.ENROLLMENT, "getParental: " + t);
                parentalLoadingState.setValue(false);
                parentalErrorState.setValue(true);
                parentalData.setValue(null);
            }
        });


        return parentalData;
    }

    public MutableLiveData<List<Parent>> getParentalData() {
        return parentalData;
    }


    /**
     * Add Parent call
     */
    public MutableLiveData<EnrollmentMessage> addParent(Map<String, String> dependantOptions) {
        MutableLiveData<EnrollmentMessage> dependantMessage = new MutableLiveData<>();

        Call<EnrollmentMessage> addDependantCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().addDependant(dependantOptions);

        addDependantCall.enqueue(new Callback<EnrollmentMessage>() {
            @Override
            public void onResponse(Call<EnrollmentMessage> call, Response<EnrollmentMessage> response) {
                try {

                    if (response.code() == 200) {
                        //dependant added successfully
                        LogMyBenefits.d(LogTags.ENROLLMENT, "On Parent Added: " + response.body());
                        dependantMessage.setValue(response.body());

                    } else {
                        LogMyBenefits.d(LogTags.ENROLLMENT, "On Parent Added: " + response.body());
                        dependantMessage.setValue(null);
                        ResponseException responseException = new ResponseException("enrollment -> addParent -> Response Code:- " + response.code());
                        crashlytics.recordException(responseException);
                    }
                } catch (Exception e) {
                    LogMyBenefits.d(LogTags.ENROLLMENT, "On Parent Added: " + response.body());
                    dependantMessage.setValue(null);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<EnrollmentMessage> call, Throwable t) {
                dependantMessage.setValue(null);
                t.printStackTrace();
            }
        });

        return dependantMessage;
    }


    //edit parent
    public MutableLiveData<EnrollmentMessage> updateParent(Map<String, String> parentOptions) {
        MutableLiveData<EnrollmentMessage> enrollmentMessage = new MutableLiveData<>();
        Call<EnrollmentMessage> updateParentCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().updateParent(parentOptions);

        updateParentCall.enqueue(new Callback<EnrollmentMessage>() {
            @Override
            public void onResponse(Call<EnrollmentMessage> call, Response<EnrollmentMessage> response) {
                if (response.code() == 200) {
                    try {
                        parentalLoadingState.setValue(false);
                        parentalErrorState.setValue(false);
                        LogMyBenefits.d(LogTags.ENROLLMENT, "updateParent: " + response.body());
                        enrollmentMessage.setValue(response.body());
                    } catch (Exception e) {
                        e.printStackTrace();
                        parentalLoadingState.setValue(false);
                        parentalErrorState.setValue(true);
                        enrollmentMessage.setValue(null);
                        LogMyBenefits.d(LogTags.ENROLLMENT, "updateParent: " + response.body());

                    }
                } else {
                    //something went wrong from server
                    LogMyBenefits.d(LogTags.ENROLLMENT, "updateParent: " + response.body());
                    parentalLoadingState.setValue(false);
                    parentalErrorState.setValue(true);
                    enrollmentMessage.setValue(null);
                    ResponseException responseException = new ResponseException("Enrollment -> updateParent -> Response Code:- " + response.code());
                    crashlytics.recordException(responseException);
                }
            }

            @Override
            public void onFailure(Call<EnrollmentMessage> call, Throwable t) {
                LogMyBenefits.d(LogTags.ENROLLMENT, "updateParent: " + t);
                parentalLoadingState.setValue(false);
                parentalErrorState.setValue(true);
                enrollmentMessage.setValue(null);
            }
        });

        return enrollmentMessage;
    }

    //delete parent
    public MutableLiveData<EnrollmentMessage> deleteParent(Map<String, String> parentOptions) {
        MutableLiveData<EnrollmentMessage> enrollmentMessage = new MutableLiveData<>();
        Call<EnrollmentMessage> deleteParental = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().deleteParent(parentOptions);

        deleteParental.enqueue(new Callback<EnrollmentMessage>() {
            @Override
            public void onResponse(Call<EnrollmentMessage> call, Response<EnrollmentMessage> response) {
                if (response.code() == 200) {
                    try {
                        parentalLoadingState.setValue(false);
                        parentalErrorState.setValue(false);
                        LogMyBenefits.d(LogTags.ENROLLMENT, "deleteParent: " + response.body());
                        enrollmentMessage.setValue(response.body());
                    } catch (Exception e) {
                        e.printStackTrace();
                        parentalLoadingState.setValue(false);
                        parentalErrorState.setValue(true);
                        enrollmentMessage.setValue(null);
                        LogMyBenefits.d(LogTags.ENROLLMENT, "deleteParent: " + response.body());

                    }
                } else {
                    //something went wrong from server
                    LogMyBenefits.d(LogTags.ENROLLMENT, "deleteParent: " + response.body());
                    parentalLoadingState.setValue(false);
                    parentalErrorState.setValue(true);
                    enrollmentMessage.setValue(null);
                    ResponseException responseException = new ResponseException("Enrollment -> deleteParent -> Response Code:- " + response.code());
                    crashlytics.recordException(responseException);
                }
            }

            @Override
            public void onFailure(Call<EnrollmentMessage> call, Throwable t) {
                LogMyBenefits.d(LogTags.ENROLLMENT, "deleteParent: " + t);
                parentalLoadingState.setValue(false);
                parentalErrorState.setValue(true);
                enrollmentMessage.setValue(null);
            }
        });

        return enrollmentMessage;
    }

    public MutableLiveData<TopupResponse> getTopUps() {
        loadingState.setValue(false);
        //todo api call for Top ups details

        /**
         * once the api call is done we make change the
         * loading -> false
         * as we don't need the loading after the
         * successful api call
         * in this case (for testing we are getting the response
         * from the json file)
         * */
        String response = UtilMethods.getAssetJsonData(application, "TopUps.json");
        Type type = new TypeToken<TopupResponse>() {
        }.getType();
        TopupResponse topupResponse = new Gson().fromJson(response, type);
        topupData.setValue(topupResponse);

        return topupData;
    }


    public MutableLiveData<EnrollmentSummaryResponse> getSummary() {
        loadingState.setValue(false);
        //todo api call for summary of enrollment
        /**
         * once the api call is done we make change the
         * loading -> false
         * as we don't need the loading after the
         * successful api call
         * in this case (for testing we are getting the response
         * from the json file)
         * */
        String response = UtilMethods.getAssetJsonData(application, "EnrollmentSummary.json");
        Type type = new TypeToken<EnrollmentSummaryResponse>() {
        }.getType();
        EnrollmentSummaryResponse enrollmentSummaryResponse = new Gson().fromJson(response, type);
        summaryLiveData.setValue(enrollmentSummaryResponse);

        return summaryLiveData;
    }

    public MutableLiveData<EnrollmentSummaryResponse> getSummaryData() {
        return summaryLiveData;
    }

    public MutableLiveData<TopupResponse> getTopUpsData() {
        return topupData;
    }

    //twins details

    public MutableLiveData<DependantHelperModel> getTwin1() {
        return twin1;
    }

    public void setTwin1(DependantHelperModel twin1) {
        this.twin1.setValue(twin1);
    }

    public MutableLiveData<DependantHelperModel> getTwin2() {
        return twin2;
    }

    public void setTwin2(DependantHelperModel twin2) {
        this.twin2.setValue(twin2);
    }


    //enrollment set up

    /**
     * we have to check if user is applicable for the window period
     **/

    public MutableLiveData<WindowPeriodEnrollmentResponse> getWindowPeriod() {
        // here we check if the window period is active.
        // this function will return

        Call<WindowPeriodEnrollmentResponse> windowPeriodCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().getWindowPeriod();

        windowPeriodCall.enqueue(new Callback<WindowPeriodEnrollmentResponse>() {
            @Override
            public void onResponse(Call<WindowPeriodEnrollmentResponse> call, Response<WindowPeriodEnrollmentResponse> response) {
                try {

                    if (response.code() == 200) {
                        windowPeriodMutableLiveData.setValue(response.body());

                    } else {
                        //something went wrong
                    }
                    //fake response
                    windowPeriodLoadingState.setValue(false);
                    windowPeriodErrorState.setValue(false);
                    String responseWindow = UtilMethods.getAssetJsonData(application, "WindowPeriod.json");
                    Type type = new TypeToken<WindowPeriodEnrollmentResponse>() {
                    }.getType();
                    WindowPeriodEnrollmentResponse windowPeriodEnrollmentResponse = new Gson().fromJson(responseWindow, type);
                    windowPeriodMutableLiveData.setValue(windowPeriodEnrollmentResponse);
                    LogMyBenefits.d(LogTags.ENROLLMENT, "getWindowPeriod: " + windowPeriodEnrollmentResponse.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<WindowPeriodEnrollmentResponse> call, Throwable t) {
                //something went wrong call did not happened
                windowPeriodMutableLiveData.setValue(null);
                windowPeriodLoadingState.setValue(false);
                windowPeriodErrorState.setValue(true);

                String responseWindow = UtilMethods.getAssetJsonData(application, "WindowPeriod.json");
                Type type = new TypeToken<WindowPeriodEnrollmentResponse>() {
                }.getType();
                WindowPeriodEnrollmentResponse windowPeriodEnrollmentResponse = new Gson().fromJson(responseWindow, type);
                windowPeriodMutableLiveData.setValue(windowPeriodEnrollmentResponse);
                LogMyBenefits.d(LogTags.ENROLLMENT, "getWindowPeriod: " + windowPeriodEnrollmentResponse.toString());

            }
        });

        return windowPeriodMutableLiveData;
    }


    //new dependant details work
    public MutableLiveData<DependantDetailsResponseNew> getDependantDetailsNew() {

        String response = UtilMethods.getAssetJsonData(application, "dependantdetails.json");
        Type type = new TypeToken<DependantDetailsResponseNew>() {
        }.getType();
        DependantDetailsResponseNew dependantDetailsResponseNew = new Gson().fromJson(response, type);
        dependantDetailsLiveDataNew.setValue(dependantDetailsResponseNew);

        LogMyBenefits.d("NEW DEPENDANT", "getDependantDetailsNew: " + dependantDetailsResponseNew.toString());

        return dependantDetailsLiveDataNew;
    }


    /**
     * Add Dependant call
     */
    public MutableLiveData<EnrollmentMessage> addDependant(Map<String, String> dependantOptions) {
        MutableLiveData<EnrollmentMessage> dependantMessage = new MutableLiveData<>();

        Call<EnrollmentMessage> addDependantCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().addDependant(dependantOptions);

        addDependantCall.enqueue(new Callback<EnrollmentMessage>() {
            @Override
            public void onResponse(Call<EnrollmentMessage> call, Response<EnrollmentMessage> response) {
                try {

                    if (response.code() == 200) {
                        //dependant added successfully
                        LogMyBenefits.d(LogTags.ENROLLMENT, "On Dependant Added: " + response.body());
                        dependantMessage.setValue(response.body());

                    } else {
                        LogMyBenefits.d(LogTags.ENROLLMENT, "On Dependant Added: " + response.body());
                        dependantMessage.setValue(null);
                        ResponseException responseException = new ResponseException("Enrollment -> addDependant -> Response Code:- " + response.code());
                        crashlytics.recordException(responseException);
                    }
                } catch (Exception e) {
                    LogMyBenefits.d(LogTags.ENROLLMENT, "On Dependant Added: " + response.body());
                    dependantMessage.setValue(null);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<EnrollmentMessage> call, Throwable t) {
                dependantMessage.setValue(null);
                t.printStackTrace();
            }
        });

        return dependantMessage;
    }

    /**
     * Delete Dependant call
     */
    public MutableLiveData<EnrollmentMessage> deleteDependant(Map<String, String> dependantOptions) {
        MutableLiveData<EnrollmentMessage> dependantMessage = new MutableLiveData<>();
        Call<EnrollmentMessage> deleteDependantCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().deleteDependant(dependantOptions);

        deleteDependantCall.enqueue(new Callback<EnrollmentMessage>() {
            @Override
            public void onResponse(Call<EnrollmentMessage> call, Response<EnrollmentMessage> response) {
                if (response.code() == 200) {
                    //dependant added successfully
                    LogMyBenefits.d(LogTags.ENROLLMENT, "On Dependant Delete: " + response.body());
                    dependantMessage.setValue(response.body());

                } else {
                    LogMyBenefits.d(LogTags.ENROLLMENT, "On Dependant Delete: " + response.body());
                    dependantMessage.setValue(null);
                    ResponseException responseException = new ResponseException("Enrollment -> deleteDependant -> Response Code:- " + response.code());
                    crashlytics.recordException(responseException);

                }
            }

            @Override
            public void onFailure(Call<EnrollmentMessage> call, Throwable t) {
                dependantMessage.setValue(null);
                t.printStackTrace();
            }
        });

        return dependantMessage;
    }


    public MutableLiveData<EnrollmentMessage> updateDependant(Map<String, String> dependantOptions) {

        MutableLiveData<EnrollmentMessage> dependantMessage = new MutableLiveData<>();

        Call<EnrollmentMessage> editDependantCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().updateDependant(dependantOptions);

        editDependantCall.enqueue(new Callback<EnrollmentMessage>() {
            @Override
            public void onResponse(Call<EnrollmentMessage> call, Response<EnrollmentMessage> response) {
                if (response.code() == 200) {
                    //successful
                    dependantMessage.setValue(response.body());
                    LogMyBenefits.d(LogTags.ENROLLMENT, "Update Dependant: " + response.body());
                } else {
                    //something went wrong
                    dependantMessage.setValue(null);
                    LogMyBenefits.d(LogTags.ENROLLMENT, "Update Dependant: " + response.body());
                    ResponseException responseException = new ResponseException("Enrollment -> update Dependant -> Response Code:- " + response.code());
                    crashlytics.recordException(responseException);

                }
            }

            @Override
            public void onFailure(Call<EnrollmentMessage> call, Throwable t) {
                dependantMessage.setValue(null);
                t.printStackTrace();
            }
        });

        return dependantMessage;
    }
}
