package com.marsh.android.MB360.insurance.enrollment.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.marsh.android.MB360.R;
import com.marsh.android.MB360.databinding.FragmentParentalDetailsBinding;
import com.marsh.android.MB360.insurance.enrollment.adapters.ParentalDetailAdapter;
import com.marsh.android.MB360.insurance.enrollment.interfaces.DependantHelper;
import com.marsh.android.MB360.insurance.enrollment.interfaces.DependantListener;
import com.marsh.android.MB360.insurance.enrollment.interfaces.ViewPagerNavigationMenuHelper;
import com.marsh.android.MB360.insurance.enrollment.repository.EnrollmentViewModel;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.DependantDetailsResponse;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.DependantHelperModel;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.Parent;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.ParentalModel;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.Relation;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.WindowPeriodEnrollmentResponse;
import com.marsh.android.MB360.insurance.enrollment.ui.bottomSheets.AddDependantBottomSheet;
import com.marsh.android.MB360.insurance.repository.LoadSessionViewModel;
import com.marsh.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.marsh.android.MB360.utilities.SwipeToDeleteCallBackParental;
import com.marsh.android.MB360.utilities.WindowPeriodCounter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParentalDetailsFragment extends Fragment implements DependantHelper, DependantListener, SwipeToDeleteCallBackParental.RecyclerItemTouchHelperListener, ConfirmationDialogs.DialogActions {
    ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper;
    EnrollmentViewModel enrollmentViewModel;
    LoadSessionViewModel loadSessionViewModel;

    FragmentParentalDetailsBinding binding;
    View view;
    boolean isWindowPeriodActive = false;

    //parental data
    List<ParentalModel> parentalList = new ArrayList<>();

    ParentalDetailAdapter adapter;

    boolean animations = true;

    int animation_count = 0;

    public ParentalDetailsFragment() {
        // Required empty public constructor
    }

    public ParentalDetailsFragment(ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper) {
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
        binding = FragmentParentalDetailsBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //setting the view-model according to the scope
        enrollmentViewModel = new ViewModelProvider(requireActivity()).get(EnrollmentViewModel.class);
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);

        //to show summary option
        viewPagerNavigationMenuHelper.showSummaryOption();

        //getWindowPeriod
        getWindowPeriodTimer();

        getParents();


        binding.swipetoRefresh.setOnRefreshListener(this::getParents);


        enrollmentViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                showLoading();
            } else {
                hideLoading();
            }
        });


        return view;
    }

    private void getParents() {
        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
        if (loadSessionResponse != null) {
            DependantDetailsResponse dependantDetailsResponse = enrollmentViewModel.getRelationshipGroup().getValue();

            enrollmentViewModel.getParental("1", loadSessionResponse.getGroupInfoData().getGroupchildsrno(), loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getOeGrpBasInfSrNo(), loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo(), "0");
        }
    }

    private void getWindowPeriodTimer() {
        WindowPeriodEnrollmentResponse windowPeriod = enrollmentViewModel.getWindowPeriod().getValue();
        if (windowPeriod != null) {
            WindowPeriodCounter windowPeriodCounter = new WindowPeriodCounter(windowPeriod.getWindowPeriod().getWindowEndDateGmc(), requireContext(), requireActivity());
            try {
                CountDownTimer timer = windowPeriodCounter.getTimer(binding.timerParentalDetails);
                if (timer != null) {
                    isWindowPeriodActive = true;
                    timer.start();
                    //swipe to delete
                    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new SwipeToDeleteCallBackParental(requireContext(), 0, ItemTouchHelper.LEFT, this, parentalList);
                    new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvFamilyDetails);
                } else {
                    isWindowPeriodActive = false;
                    binding.timerParentalDetails.setText("Window period has expired");
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

    private void getParentalData() {

        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();

        if (loadSessionResponse != null) {
            DependantDetailsResponse dependantDetailsResponse = enrollmentViewModel.getRelationshipGroup().getValue();

            enrollmentViewModel.getParental("1", loadSessionResponse.getGroupInfoData().getGroupchildsrno(), loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getOeGrpBasInfSrNo(), loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo(), "0");


            enrollmentViewModel.getRelationshipGroup();

            if (dependantDetailsResponse != null) {
                enrollmentViewModel.getParentalData().observe(getViewLifecycleOwner(), parentalData -> {

                    List<Parent> parentList = parentalData == null ? new ArrayList<>() : parentalData;
                    List<Relation> relationList = dependantDetailsResponse.getRelations();
                    parentalList.clear();

                    //remove other relation than parental
                    relationList.removeIf(relation -> relation.getRelation().equalsIgnoreCase("spouse") || relation.getRelation().equalsIgnoreCase("partner") || relation.getRelation().equalsIgnoreCase("son") || relation.getRelation().equalsIgnoreCase("twins") || relation.getRelation().equalsIgnoreCase("daughter"));


                    for (Relation relation : relationList) {

                        parentalList.add(new ParentalModel(relation.getRelation(), 1, "1", false, "", "", false, null, "", true, false, "", false, "", "", relation.getRelationID()));

                    }

                    for (Parent familyMember : parentList) {
                        int index_update = 0;
                        for (ParentalModel dependantHelperModel : parentalList) {
                            if (dependantHelperModel.getRelationID().equalsIgnoreCase(familyMember.getRelationId())) {
                                if (dependantHelperModel.getPersonSrno().equalsIgnoreCase("")) {

                                    if (familyMember.getName().equalsIgnoreCase("")) {
                                        parentalList.set(index_update, new ParentalModel(familyMember.getRelation(), 2, "1", false, "", "", false, null, "", true, false, familyMember.getRelation(), false, "", "0", familyMember.getRelationId()));

                                    } else {
                                        parentalList.set(index_update, new ParentalModel(familyMember.getRelation(), 2, "1", true, familyMember.getName(), familyMember.getDateOfBirthToShow(), false, null, familyMember.getAge(), familyMember.getCanUpdate(), familyMember.getCanDelete(), familyMember.getRelation(), false, familyMember.getPersonSrNo(), "0", familyMember.getRelationId()));
                                    }

                                } else {
                                    parentalList.add(new ParentalModel(familyMember.getRelation(), 2, "1", true, familyMember.getName(), familyMember.getDateOfBirthToShow(), false, null, familyMember.getAge(), familyMember.getCanUpdate(), familyMember.getCanDelete(), familyMember.getRelation(), false, familyMember.getPersonSrNo(), "0", familyMember.getRelationId()));
                                }
                                break;
                            }
                            index_update++;
                        }

                    }


                    parentalList.add(0, new ParentalModel("", 1, "", false, "", "", false, null, "", true, true, "", true, "", "", ""));

                    if (parentalList.size() > 3) {
                        //that means we get the cross parents data too
                        parentalList.add(3, new ParentalModel("", 1, "", false, "", "", false, null, "", true, true, "", true, "", "", ""));
                    }

                    adapter = new ParentalDetailAdapter(parentalList, requireContext(), this, isWindowPeriodActive, animations);
                    binding.rvFamilyDetails.setAdapter(adapter);
                    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new SwipeToDeleteCallBackParental(requireContext(), 0, ItemTouchHelper.LEFT, this, parentalList);
                    new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvFamilyDetails);


                });
            }
        } else {
            //load session is null
        }

    }


    @Override
    public void onAddDependant(DependantHelperModel dependantHelperModel, int position) {
        animations = false;
        AddDependantBottomSheet parentBottomSheet = new AddDependantBottomSheet(this, dependantHelperModel, position);
        parentBottomSheet.setCancelable(true);
        parentBottomSheet.show(getChildFragmentManager(), parentBottomSheet.getTag());
    }

    @Override
    public void onEditDependant(DependantHelperModel dependant, int position) {
        animations = false;
        AddDependantBottomSheet dialog = new AddDependantBottomSheet(this, dependant, position, true);
        dialog.show(getChildFragmentManager(), dialog.getTag());
        dialog.setCancelable(true);
    }

    @Override
    public void onDeleteDependant() {

    }

    @Override
    public void onEditTwin(int position) {

    }

    @Override
    public void onDependantSavedListener(DependantHelperModel dependant, int position) {
        animations = false;
        //here we get the parent and we need to save the parent in the parent list
        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();

      /*  for (DependantHelperModel dependantCycle : parentalList) {
            if (dependantCycle.getRelationName().equalsIgnoreCase(dependant.getRelationName())) {
                dependantCycle.setDateOfBirth(dependant.getDateOfBirth());
                dependantCycle.setName(dependant.getName());
                dependantCycle.setIsAdded(true);
                dependantCycle.setIsDifferentlyAble(dependant.isDifferentlyAble());
                dependantCycle.setDocument(dependant.getDocument());
                dependantCycle.setCanEdit(true);
                dependantCycle.setCanDelete(true);
                dependantCycle.setAge(dependant.getAge());
                adapter.notifyItemChanged(position);
                adapter.notifyItemChanged(position);
            }

        }*/

        //add parent call here
        if (loadSessionResponse != null) {

            try {
                Map<String, String> dependantQueryMap = new HashMap<>();
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
                enrollmentViewModel.addParent(dependantQueryMap).observe(getViewLifecycleOwner(), message -> {
                    if (message != null) {
                        if (message.getMessage() != null) {
                            if (message.getMessage().getStatus()) {
                            }
                            Toast.makeText(requireContext(), message.getMessage().getMessage(), Toast.LENGTH_SHORT).show();
                            getParents();
                        } else {
                            getParents();
                            Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_SHORT).show();

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onTwinsAdded(DependantHelperModel twin1, DependantHelperModel twin2, int position, boolean edit) {
        //we dont need any twins here because we are currently adding parent
    }

    @Override
    public void onDependantEditedListener(DependantHelperModel dependant, int position) {
        //here we listen to the changes to the edited parent which also can be listen by on add-dependant
        animations = false;
        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();

        if (loadSessionResponse != null) {
            Map<String, String> parentOptions = new HashMap<>();


            parentOptions.put("PersonSrNo", dependant.getPersonSrno());
            parentOptions.put("Age", dependant.getAge());
            parentOptions.put("Dependantname", dependant.getName());
            parentOptions.put("Dateofbirth", dependant.getDateOfBirth());
            parentOptions.put("Gender", dependant.getGender());
            parentOptions.put("RelationID", dependant.getRelationID());


            enrollmentViewModel.updateParent(parentOptions).observe(getViewLifecycleOwner(), enrollmentMessage -> {
                if (enrollmentMessage != null) {
                    if (enrollmentMessage.getMessage() != null && enrollmentMessage.getMessage().getStatus()) {
                        //updated
                        getParents();
                        Toast.makeText(requireContext(), enrollmentMessage.getMessage().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            //load session is null
        }
    }

    @Override
    public void onDependantDeletedListener(DependantHelperModel dependant) {
        //here we should call the confirmation dialog with the item touch helper
        //swipe gesture confirmation is being called here
        //and then after confirmation we call delete method which
        //only updates the parent again to empty state!
    }

    // Deleting the dependant
    @Override
    public void onConfirmed(int position, DependantHelperModel dependantHelperModel, ParentalModel parentalModel) {
        // this is on-confirmed of deleting
        animations = false;
        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
        if (loadSessionResponse != null) {

            Map<String, String> parentOptions = new HashMap<>();
            parentOptions.put("EmployeeSrNo", loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo());
            parentOptions.put("GrpChildSrNo", loadSessionResponse.getGroupInfoData().getGroupchildsrno());
            parentOptions.put("PersonSrNo", parentalModel.getPersonSrno());
            parentOptions.put("OeGrpBasInfSrNo", loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getOeGrpBasInfSrNo());
            parentOptions.put("RelationID", parentalModel.getRelationID());
            parentOptions.put("parentalPremium",/*TODO*/ "0");

            enrollmentViewModel.deleteParent(parentOptions).observe(getViewLifecycleOwner(), enrollmentMessage -> {
                if (enrollmentMessage != null) {
                    if (enrollmentMessage.getMessage() != null && enrollmentMessage.getMessage().getStatus()) {
                        //deleted
                        getParents();
                        Toast.makeText(requireContext(), enrollmentMessage.getMessage().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        //something went wrong
                        getParents();
                        Toast.makeText(requireContext(), "Something went Wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            adapter.notifyItemChanged(position);
        } else {
            //load session is null.

        }


    }

    //canceled the delete process
    @Override
    public void onRejected(int position, DependantHelperModel dependantHelperModel, ParentalModel parentalModel) {
        //on canceled deleting
        parentalList.get(position).setCanEdit(true);
        parentalList.get(position).setCanDelete(true);
        adapter.notifyItemChanged(position);
        animations = false;

    }


    @Override
    public void onResume() {
        super.onResume();
      /*  //to show summary option
        viewPagerNavigationMenuHelper.showSummaryOption();
        viewPagerNavigationMenuHelper.showHomeButton();*/


        if (animation_count > 0) {
            animations = false;
        } else {
            animations = true;


            Animation animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_down);
            binding.appCompatImageView3.startAnimation(animation);
            binding.headerHolderParent.startAnimation(animation);
            binding.timerHolder.startAnimation(animation);
            binding.inst1.startAnimation(animation);

            binding.appCompatImageView3.setVisibility(View.VISIBLE);
            binding.headerHolderParent.setVisibility(View.VISIBLE);
            binding.timerHolder.setVisibility(View.VISIBLE);
            binding.inst1.setVisibility(View.VISIBLE);


        }


        //get the parental data
        getParentalData();

        animation_count++;


    }

    @Override
    public void onSwipedParental(RecyclerView.ViewHolder viewHolder, int direction, int position, ParentalModel parentalModel) {
        //start the deleting process
        ConfirmationDialogs confirmationDialogs = new ConfirmationDialogs(requireContext(), this, position, parentalModel);
        confirmationDialogs.setCancelable(false);
        confirmationDialogs.show(getChildFragmentManager(), confirmationDialogs.getTag());
        animations = false;
    }


    private void showLoading() {
        binding.swipetoRefresh.setRefreshing(true);
    }

    private void hideLoading() {
        binding.swipetoRefresh.setRefreshing(false);
    }


}