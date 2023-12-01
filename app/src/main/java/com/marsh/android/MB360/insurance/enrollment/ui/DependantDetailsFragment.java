package com.marsh.android.MB360.insurance.enrollment.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.marsh.android.MB360.R;
import com.marsh.android.MB360.databinding.FragmentDependantDetailsBinding;
import com.marsh.android.MB360.insurance.enrollment.adapters.DependantDetailsAdapter;
import com.marsh.android.MB360.insurance.enrollment.interfaces.DependantHelper;
import com.marsh.android.MB360.insurance.enrollment.interfaces.DependantListener;
import com.marsh.android.MB360.insurance.enrollment.interfaces.ViewPagerNavigationMenuHelper;
import com.marsh.android.MB360.insurance.enrollment.repository.EnrollmentViewModel;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.DependantDetailsResponse;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.DependantHelperModel;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.Dependent;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.ParentalModel;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.Relation;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.WindowPeriodEnrollmentResponse;
import com.marsh.android.MB360.insurance.enrollment.ui.bottomSheets.AddDependantBottomSheet;
import com.marsh.android.MB360.insurance.enrollment.ui.bottomSheets.AddTwinsBottomSheet;
import com.marsh.android.MB360.insurance.repository.LoadSessionViewModel;
import com.marsh.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.marsh.android.MB360.utilities.LogMyBenefits;
import com.marsh.android.MB360.utilities.LogTags;
import com.marsh.android.MB360.utilities.SwipeToDeleteCallback;
import com.marsh.android.MB360.utilities.WindowPeriodCounter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DependantDetailsFragment extends Fragment implements DependantHelper, DependantListener, SwipeToDeleteCallback.RecyclerItemTouchHelperListener, ConfirmationDialogs.DialogActions {

    //TODO : SOLVE THE ISSUES FOR DELETING THE TWIN.
    //TODO : SOLVE THE ISSUES FOR THE EDITING THE PEOPLE.......................✅
    //TODO : REFINE THE
    //TODO : UNIT TEST ()


    FragmentDependantDetailsBinding binding;
    View view;
    //view-model for enrollment
    EnrollmentViewModel enrollmentViewModel;
    LoadSessionViewModel loadSessionViewModel;
    //recyclerview adapter
    DependantDetailsAdapter adapter;
    ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper;
    boolean isWindowPeriodActive = false;

    //data list for adding deleting and  editing of the dependant.
    List<DependantHelperModel> dependantList = new ArrayList<>();

    DependantHelperModel lgbtqModel = new DependantHelperModel("PARTNER", 1, "1", false, "", "", false, null, "", true, true, "", "22");

    boolean SPOUSE = true;
    boolean LGBTQ = true; //done (need special handling of the bottom sheet)
    boolean daughter_not_married = false; //will be done we get the max age.
    boolean parents_allowed = false; //done
    boolean cross_combination = false;//need info
    boolean twins_allowed = true; //twins needed


    boolean animations = true;
    int animation_count = 0;

    public DependantDetailsFragment() {
        // Required empty public constructor
    }

    public DependantDetailsFragment(ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper) {
        this.viewPagerNavigationMenuHelper = viewPagerNavigationMenuHelper;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDependantDetailsBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //to show summary option
        viewPagerNavigationMenuHelper.showSummaryOption();

        //sharing the view model scope same as the activity (insurance activity)
        enrollmentViewModel = new ViewModelProvider(requireActivity()).get(EnrollmentViewModel.class);
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);


        enrollmentViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                showLoading();
            } else {
                hideLoading();
            }
        });


        binding.swipetoRefresh.setOnRefreshListener(this::getDependant);

        binding.instCard1.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            viewPagerNavigationMenuHelper.onHideMenu();
            if (scrollY < 40) {
                viewPagerNavigationMenuHelper.onShowMenu();
            } else {
                viewPagerNavigationMenuHelper.onHideMenu();
            }
        });


        //get the relationship group
        enrollmentViewModel.getRelationshipGroup();

        getDependant();

        getWindowPeriodTimer();

        return view;
    }

    private void getWindowPeriodTimer() {
        WindowPeriodEnrollmentResponse windowPeriod = enrollmentViewModel.getWindowPeriod().getValue();
        if (windowPeriod != null) {
            WindowPeriodCounter windowPeriodCounter = new WindowPeriodCounter(windowPeriod.getWindowPeriod().getWindowEndDateGmc(), requireContext(), requireActivity());
            try {
                CountDownTimer timer = windowPeriodCounter.getTimer(binding.timerDependantDetails);
                if (timer != null) {
                    isWindowPeriodActive = true;
                    timer.start();
                    //swipe to delete
                    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new SwipeToDeleteCallback(requireContext(), 0, ItemTouchHelper.LEFT, this, dependantList);
                    new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.dependantCycle);

                } else {
                    isWindowPeriodActive = false;
                    binding.timerDependantDetails.setText("Window period has expired");
                }
            } catch (ParseException e) {
                //unable to parse date
                isWindowPeriodActive = false;
                Toast.makeText(requireContext(), "Unable to retrieve window period date.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        } else {
            //no window period available
        }
    }

    private void showLoading() {
        binding.swipetoRefresh.setRefreshing(true);
    }

    private void hideLoading() {
        binding.swipetoRefresh.setRefreshing(false);
    }


    private void getDependant() {
        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
        if (loadSessionResponse != null) {
            DependantDetailsResponse dependantDetailsResponse = enrollmentViewModel.getRelationshipGroup().getValue();

            enrollmentViewModel.getDependants("1",
                    loadSessionResponse.getGroupInfoData().getGroupchildsrno(),
                    loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getOeGrpBasInfSrNo(),
                    loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo());
        }
    }

    private void getDependantDetails() {
        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
        if (loadSessionResponse != null) {
            DependantDetailsResponse dependantDetailsResponse = enrollmentViewModel.getRelationshipGroup().getValue();

            enrollmentViewModel.getDependants("1",
                    loadSessionResponse.getGroupInfoData().getGroupchildsrno(),
                    loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getOeGrpBasInfSrNo(),
                    loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo());


            if (dependantDetailsResponse != null) {
                enrollmentViewModel.getDependantsData().observe(getViewLifecycleOwner(), dependents -> {

                    List<Dependent> exDependants = dependents == null ? new ArrayList<>() : dependents;
                    dependantList.clear();

                    for (Relation relation : dependantDetailsResponse.getRelations()) {
                        dependantList.add(new DependantHelperModel(relation.getRelation(), 2, "1", false, "", "", false, null, "", false, false, "", "", "", relation.getRelationID()));
                    }
                    for (Dependent familyMember : exDependants) {
                        int index_update = 0;
                        for (DependantHelperModel dependantHelperModel : dependantList) {
                            if (dependantHelperModel.getRelationID().equalsIgnoreCase(familyMember.getRelationID())) {
                                if (dependantHelperModel.getPersonSrno().equalsIgnoreCase("")) {
                                    dependantList.set(index_update, new DependantHelperModel(familyMember.getRelation(), 2, "1", true, familyMember.getName(), familyMember.getDateOfBirth(), familyMember.getIsDisabled(), null, familyMember.getAge(), familyMember.getCanUpdate(), familyMember.getCanDelete(), familyMember.getGender(), familyMember.getPersonSrNo(), familyMember.getPairNo(), familyMember.getRelationID()));


                                    if (familyMember.getRelation().equalsIgnoreCase("spouse")) {
                                        LGBTQ = false;
                                    } else if (familyMember.getRelation().equalsIgnoreCase("partner")) {
                                        SPOUSE = false;
                                    } else if (familyMember.getIsTwins().equalsIgnoreCase("yes")) {
                                        twins_allowed = false;
                                    }

                                } else {

                                    LogMyBenefits.d("TWINS", "getDependantDetails: " + familyMember.getName() + " NAME: " + familyMember.getIsTwins());
                                    if (familyMember.getRelation().equalsIgnoreCase("spouse")) {
                                        LGBTQ = false;
                                    } else if (familyMember.getRelation().equalsIgnoreCase("partner")) {
                                        SPOUSE = false;
                                    } else if (familyMember.getIsTwins().equalsIgnoreCase("yes")) {
                                        twins_allowed = false;
                                    }

                                    dependantList.add(new DependantHelperModel(familyMember.getRelation(), 2, "1", true, familyMember.getName(), familyMember.getDateOfBirth(), familyMember.getIsDisabled(), null, familyMember.getAge(), familyMember.getCanUpdate(), familyMember.getCanDelete(), familyMember.getGender(), familyMember.getPersonSrNo(), familyMember.getPairNo(), familyMember.getRelationID()));
                                }
                                break;
                            }
                            index_update++;
                        }

                    }


                    if (!parents_allowed) {
                        dependantList.removeIf(dependantParent -> dependantParent.getRelationName().equalsIgnoreCase("mother") || dependantParent.getRelationName().equalsIgnoreCase("father") || dependantParent.getRelationName().equalsIgnoreCase("mother-in-law") || dependantParent.getRelationName().equalsIgnoreCase("mother in law") || dependantParent.getRelationName().equalsIgnoreCase("father-in-law") || dependantParent.getRelationName().equalsIgnoreCase("father in law"));
                    }

                    if (!LGBTQ) {
                        dependantList.removeIf(dependantParent -> dependantParent.getRelationName().equalsIgnoreCase("partner"));
                    }

                    if (!SPOUSE) {
                        dependantList.removeIf(dependantParent -> dependantParent.getRelationName().equalsIgnoreCase("spouse"));
                    }

                    if (!twins_allowed) {
                        dependantList.removeIf(dependantParent -> dependantParent.getRelationName().equalsIgnoreCase("twins"));
                    }

                    //adapter
                    adapter = new DependantDetailsAdapter(dependantList, requireContext(), this, isWindowPeriodActive, animations);
                    binding.dependantCycle.setAdapter(adapter);


                });
            }


        } else {
            //load-session is null
        }


    }


    //on dependant clicked from recyclerview.
    @Override
    public void onAddDependant(DependantHelperModel dependant, int position) {
        animations = false;
        //here we need to add the interface to get the details of the dependant
        //so that we can get the details of the dependant added and update the recyclerview.

        if (dependant.getRelationName().toLowerCase().equals("twins")) {
            AddTwinsBottomSheet twinsDialog = new AddTwinsBottomSheet(this, position);
            twinsDialog.setCancelable(true);
            twinsDialog.show(getChildFragmentManager(), twinsDialog.getTag());
        } else {
            AddDependantBottomSheet dialog = new AddDependantBottomSheet(this, dependant, position);
            dialog.show(getChildFragmentManager(), dialog.getTag());
            dialog.setCancelable(true);
        }


    }

    @Override
    public void onEditDependant(DependantHelperModel dependant, int position) {
        animations = false;
        //here we edit the dependant the details
        //tip :- here we follow same procedure as adding the data
        //as the data in dependant details is never added but retained the state from empty
        //to filled dependant state.

        //we don't need this case
        if (dependant.getRelationName().toLowerCase().equals("twins")) {/* AddTwinsBottomSheet twinsDialog = new AddTwinsBottomSheet(this, position);
                twinsDialog.show(getChildFragmentManager(), twinsDialog.getTag());
                twinsDialog.setCancelable(true);*/
        } else {
            AddDependantBottomSheet dialog = new AddDependantBottomSheet(this, dependant, position, true);
            dialog.show(getChildFragmentManager(), dialog.getTag());
            dialog.setCancelable(true);
        }


    }

    @Override
    public void onDeleteDependant() {
        animations = false;
    }

    @Override
    public void onEditTwin(int position) {
        animations = false;
        AddTwinsBottomSheet twinsDialog = new AddTwinsBottomSheet(this, position, true);
        twinsDialog.show(getChildFragmentManager(), twinsDialog.getTag());
        twinsDialog.setCancelable(true);

    }

    //on dependant changed in bottom sheets
    @Override
    public void onDependantSavedListener(DependantHelperModel dependant, int position) {
        animations = false;
        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
        for (DependantHelperModel dependantCycle : dependantList) {
            if (dependantCycle.getRelationName().equalsIgnoreCase(dependant.getRelationName())) {
                dependantCycle.setDateOfBirth(dependant.getDateOfBirth());
                dependantCycle.setName(dependant.getName());
                dependantCycle.setIsAdded(true);
                dependantCycle.setIsDifferentlyAble(dependant.isDifferentlyAble());
                dependantCycle.setDocument(dependant.getDocument());
                dependantCycle.setCanEdit(true);
                dependantCycle.setCanDelete(true);
                dependantCycle.setAge(dependant.getAge());
            }


        }

        if (LGBTQ) {
            if (dependant.getRelationName().equalsIgnoreCase("spouse")) {
                dependantList.removeIf(dependantHelperModel -> (dependantHelperModel.getRelationName().equalsIgnoreCase("partner")));
            } else if (dependant.getRelationName().equalsIgnoreCase("partner")) {
                dependantList.removeIf(dependantHelperModel -> (dependantHelperModel.getRelationName().equalsIgnoreCase("spouse")));
            }
        }
        //api call of adding the dependant
        if (loadSessionResponse != null) {
            Map<String, String> dependantQueryMap = new HashMap<>();
            try {
                dependantQueryMap.put("Employeesrno", loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo());
                dependantQueryMap.put("Relationid", dependant.getRelationID());
                dependantQueryMap.put("Personname", dependant.getName());
                dependantQueryMap.put("Dateofmarriage", /* TODO */ "");
                dependantQueryMap.put("Windowperiodactive", "1");
                dependantQueryMap.put("Grpchildsrno", loadSessionResponse.getGroupInfoData().getGroupchildsrno());
                dependantQueryMap.put("Oegrpbasinfosrno", loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getOeGrpBasInfSrNo());
                dependantQueryMap.put("Gender", dependant.getGender());
                dependantQueryMap.put("IsTwins", "0");
                dependantQueryMap.put("ParentalPremium", "0");
                dependantQueryMap.put("Age", dependant.getAge());
                dependantQueryMap.put("Dateofbirth", dependant.getDateOfBirth());

                LogMyBenefits.d(LogTags.ENROLLMENT, "Add dependant: " + dependantQueryMap);


                //call for adding dependant
                enrollmentViewModel.addDependant(dependantQueryMap).observe(getViewLifecycleOwner(), message -> {
                    if (message != null) {
                        if (message.getMessage() != null && message.getMessage().getStatus()) {
                            Toast.makeText(requireContext(), message.getMessage().getMessage(), Toast.LENGTH_SHORT).show();
                            getDependant();
                        } else {
                            getDependant();
                            Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_SHORT).show();

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            LogMyBenefits.e(LogTags.ENROLLMENT, "Dependant Details: load-session is null");
        }


    }

    @Override
    public void onTwinsAdded(DependantHelperModel twin1, DependantHelperModel twin2, int position, boolean edit) {
        //here we add twins with 2 dependant classes.
        animations = false;
        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
      /*  if (edit) {
            enrollmentViewModel.setTwin1(twin1);
            enrollmentViewModel.setTwin2(twin2);
            dependantList.removeIf(dependantHelperModel -> (dependantHelperModel.getRelationName().equalsIgnoreCase("twins")));
            int twins_position = 3;
            if (dependantList.contains(lgbtqModel)) {
                if (dependantList.get(1).getIsAdded()) {
                    twins_position = 3;
                }
                twins_position = 4;
            } else {
                twins_position = 3;
            }

            dependantList.add(twins_position, twin1);
            dependantList.add(twins_position + 1, twin2);

            adapter.notifyDataSetChanged();

        } else {

            for (DependantHelperModel dependantCycle : dependantList) {
                if (dependantCycle.getRelationName().equalsIgnoreCase(twin1.getRelationName())) {
                    dependantCycle.setDateOfBirth(twin1.getDateOfBirth());
                    dependantCycle.setName(twin1.getName());
                    dependantCycle.setIsAdded(true);
                    dependantCycle.setAge(twin1.getAge());
                    dependantCycle.setIsDifferentlyAble(twin1.isDifferentlyAble());
                    dependantCycle.setDocument(twin1.getDocument());
                    dependantCycle.setCanDelete(true);
                    enrollmentViewModel.setTwin1(dependantCycle);
                }
                adapter.notifyItemChanged(position);
            }

            enrollmentViewModel.setTwin2(twin2);
            dependantList.add(position + 1, twin2);
            adapter.notifyItemInserted(position + 1);
        }*/

        if (loadSessionResponse != null) {
            Map<String, String> dependantQueryMap = new HashMap<>();
            try {
                dependantQueryMap.put("Employeesrno", loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo());
                dependantQueryMap.put("Relationid", twin1.getRelationID());
                dependantQueryMap.put("Personname", twin1.getName());
                dependantQueryMap.put("Dateofmarriage", /* TODO */ "");
                dependantQueryMap.put("Windowperiodactive", "1");
                dependantQueryMap.put("Grpchildsrno", loadSessionResponse.getGroupInfoData().getGroupchildsrno());
                dependantQueryMap.put("Oegrpbasinfosrno", loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getOeGrpBasInfSrNo());
                dependantQueryMap.put("Gender", twin1.getGender());
                dependantQueryMap.put("IsTwins", "1");
                dependantQueryMap.put("ParentalPremium", "0");
                dependantQueryMap.put("Age", twin1.getAge());
                dependantQueryMap.put("Dateofbirth", twin1.getDateOfBirth());

                LogMyBenefits.d(LogTags.ENROLLMENT, "Add dependant: " + dependantQueryMap);


                //call for adding dependant
                enrollmentViewModel.addDependant(dependantQueryMap).observe(getViewLifecycleOwner(), message -> {
                    if (message != null) {
                        if (message.getMessage() != null && message.getMessage().getStatus()) {
                            Toast.makeText(requireContext(), message.getMessage().getMessage(), Toast.LENGTH_SHORT).show();
                            getDependant();
                        } else {
                            getDependant();
                            Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_SHORT).show();

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                dependantQueryMap.put("Employeesrno", loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo());
                dependantQueryMap.put("Relationid", twin2.getRelationID());
                dependantQueryMap.put("Personname", twin2.getName());
                dependantQueryMap.put("Dateofmarriage", /* TODO */ "");
                dependantQueryMap.put("Windowperiodactive", "1");
                dependantQueryMap.put("Grpchildsrno", loadSessionResponse.getGroupInfoData().getGroupchildsrno());
                dependantQueryMap.put("Oegrpbasinfosrno", loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getOeGrpBasInfSrNo());
                dependantQueryMap.put("Gender", twin2.getGender());
                dependantQueryMap.put("IsTwins", "1");
                dependantQueryMap.put("ParentalPremium", "0");
                dependantQueryMap.put("Age", twin2.getAge());
                dependantQueryMap.put("Dateofbirth", twin2.getDateOfBirth());

                LogMyBenefits.d(LogTags.ENROLLMENT, "Add dependant: " + dependantQueryMap);


                //call for adding dependant
                enrollmentViewModel.addDependant(dependantQueryMap).observe(getViewLifecycleOwner(), message -> {
                    if (message != null) {
                        if (message.getMessage() != null && message.getMessage().getStatus()) {
                            Toast.makeText(requireContext(), message.getMessage().getMessage(), Toast.LENGTH_SHORT).show();
                            getDependant();
                        } else {
                            getDependant();
                            Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_SHORT).show();

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            LogMyBenefits.e(LogTags.ENROLLMENT, "Dependant Details: load-session is null");
        }


    }

    @Override
    public void onDependantEditedListener(DependantHelperModel dependant, int position) {
        animations = false;
        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
        if (loadSessionResponse != null) {
            //here we update the dependant
            Map<String, String> dependantQueryMap = new HashMap<>();
            dependantQueryMap.put("Personsrno", dependant.getPersonSrno());
            dependantQueryMap.put("Age", dependant.getAge());
            dependantQueryMap.put("Dateofbirth", dependant.getDateOfBirth());
            dependantQueryMap.put("Dependantname", dependant.getName());
            dependantQueryMap.put("Relationid", dependant.getRelationID());
            dependantQueryMap.put("Gender", dependant.getGender());


            enrollmentViewModel.updateDependant(dependantQueryMap).observe(getViewLifecycleOwner(), message -> {
                if (message != null) {
                    if (message.getMessage().getStatus()) {
                        Toast.makeText(requireContext(), message.getMessage().getMessage(), Toast.LENGTH_SHORT).show();
                        getDependant();
                    } else {
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            LogMyBenefits.d(LogTags.ENROLLMENT, "onDependantEditedListener: " + dependantQueryMap);

        } else {
            LogMyBenefits.d(LogTags.ENROLLMENT, "onDependantEditedListener: Load-session is null");
        }

    }

    @Override
    public void onDependantDeletedListener(DependantHelperModel dependant) {
        animations = false;
    }


    @Override
    public void onConfirmed(int position, DependantHelperModel deleteDependant, ParentalModel parentalModel) {
        animations = false;
        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
        //here we get the confirmation for deleting a dependant
        //and we delete
        if (loadSessionResponse != null) {
            try {
                //deletion of the dependant api call
                Map<String, String> dependantOption = new HashMap<>();
                dependantOption.put("EmployeeSrNo", loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo());
                dependantOption.put("GrpChildSrNo", loadSessionResponse.getGroupInfoData().getGroupchildsrno());
                dependantOption.put("PersonSrNo", deleteDependant.getPersonSrno());


                enrollmentViewModel.deleteDependant(dependantOption).observe(getViewLifecycleOwner(), enrollmentMessage -> {
                    if (enrollmentMessage.getMessage().getStatus()) {
                        getDependant();

                        //todo add the partner if spouse is deleted
                        if (deleteDependant.getRelationName().equalsIgnoreCase("spouse")) {
                            SPOUSE = true;
                            try {
                                List<Relation> relationList = enrollmentViewModel.getRelationshipGroupData().getValue().getRelations();

                                for (Relation relation : relationList) {
                                    if (relation.getRelation().equalsIgnoreCase("partner")) {
                                        LGBTQ = true;
                                        dependantList.add(1, lgbtqModel);
                                    } else {
                                        LGBTQ = false;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                LGBTQ = false;
                            }

                        }
                        if (deleteDependant.getRelationName().equalsIgnoreCase("partner")) {
                            LGBTQ = true;
                            SPOUSE = true;
                        }


                    } else {
                        //something went wrong
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRejected(int position, DependantHelperModel dependantHelperModel, ParentalModel parentalModel) {
        //user does not want to delete the dependant.
        animations = false;
        dependantList.get(position).setCanEdit(true);
        dependantList.get(position).setCanDelete(true);
        adapter.notifyItemChanged(position);
    }


    @Override
    public void onDependantSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position, DependantHelperModel dependantHelperModel) {
        //here we can listen that it is swiped
        //logic of resetting the data
        //ask for confirmation
        animations = false;
        ConfirmationDialogs confirmationDialogs = new ConfirmationDialogs(requireContext(), this, position, dependantHelperModel);
        confirmationDialogs.setCancelable(false);
        confirmationDialogs.show(getChildFragmentManager(), confirmationDialogs.getTag());
    }


    @Override
    public void onResume() {
        super.onResume();

        if (animation_count > 0) {
            animations = false;
        } else {
            animations = true;


            Animation animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_down);
            binding.headerDependantImage.startAnimation(animation);
            binding.headerDependantText.startAnimation(animation);
            binding.timerHolder.startAnimation(animation);
            binding.inst1.startAnimation(animation);

            binding.headerDependantImage.setVisibility(View.VISIBLE);
            binding.headerDependantText.setVisibility(View.VISIBLE);
            binding.timerHolder.setVisibility(View.VISIBLE);
            binding.inst1.setVisibility(View.VISIBLE);

        }

        getDependantDetails();
        animation_count++;

    }
}