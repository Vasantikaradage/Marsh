package com.marsh.android.MB360.insurance.claims.repository.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.marsh.android.MB360.R;
import com.marsh.android.MB360.databinding.FragmentLoadIntimatedClaimsBinding;
import com.marsh.android.MB360.insurance.claims.repository.ClaimsViewModel;
import com.marsh.android.MB360.insurance.enrollmentstatus.EnrollmentStatusViewModel;
import com.marsh.android.MB360.insurance.repository.LoadSessionViewModel;
import com.marsh.android.MB360.insurance.repository.responseclass.GroupGMCPolicyEmployeeDatum;
import com.marsh.android.MB360.insurance.repository.selectedPolicyRepo.SelectedPolicyViewModel;
import com.marsh.android.MB360.utilities.UtilMethods;

public class LoadIntimatedClaimsFragment extends Fragment {

    FragmentLoadIntimatedClaimsBinding binding;
    View view;
    LoadSessionViewModel loadSessionViewModel;
    SelectedPolicyViewModel selectedPolicyViewModel;
    EnrollmentStatusViewModel enrollmentStatusViewModel;
    ClaimsViewModel claimsViewModel;
    ClaimAdapter adapter;
    String PRODUCT_CODE = "GMC";

    String error_title = "";
    String error_string = "";

    public LoadIntimatedClaimsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoadIntimatedClaimsBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        selectedPolicyViewModel = new ViewModelProvider(requireActivity()).get(SelectedPolicyViewModel.class);
        claimsViewModel = new ViewModelProvider(this).get(ClaimsViewModel.class);
        enrollmentStatusViewModel = new ViewModelProvider(requireActivity()).get(EnrollmentStatusViewModel.class);

        getClaims();

      

        claimsViewModel.getLoadingState().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                showLoading();
            } else {
                hideLoading();
            }
        });

        claimsViewModel.getReloginState().observe(getActivity(), relogin -> {
            if (relogin) {
                UtilMethods.RedirectToLogin(requireActivity());
            } else {
            }
        });






        return view;
    }


    private void getClaims() {
        //to get the  claims data we need some parameters from load session values

        try {


            PRODUCT_CODE = "GMC";
            loadSessionViewModel.getLoadSessionData().observe(requireActivity(), loadSessionResponse -> {
                try {

                    PRODUCT_CODE = "GMC";
                    String employeeSrNo = "";
                    String groupChildSrvNo = "";
                    String oeGrpBasInfSrNo = "";

                    GroupGMCPolicyEmployeeDatum groupGMCPolicyEmployeeDatum;
                    groupGMCPolicyEmployeeDatum = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0);

                    //queries for claims
                    employeeSrNo = groupGMCPolicyEmployeeDatum.getEmployeeSrNo();
                    groupChildSrvNo = groupGMCPolicyEmployeeDatum.getGroupchildsrno();
                    oeGrpBasInfSrNo = groupGMCPolicyEmployeeDatum.getOeGrpBasInfSrNo();


                    claimsViewModel.getClaims(employeeSrNo, groupChildSrvNo, oeGrpBasInfSrNo);

                    enrollmentStatusViewModel.getEnrollmentStatus(employeeSrNo, loadSessionResponse.getGroupInfoData().getGroupchildsrno(), oeGrpBasInfSrNo).observe(requireActivity(), enrollmentStatusResponse -> {
                        if (enrollmentStatusResponse != null) {
                            if (enrollmentStatusResponse.getEnrollmentStatus() != null) {

                                if (enrollmentStatusResponse.getEnrollmentStatus().getIsWindowPeriodOpen() == 1) {

                                    error_title = requireContext().getString(R.string.enrolment_is_in_progress);
                                    error_string = requireContext().getString(R.string.you_can_intimate_a_claim_once_enrollment_period_ends);
                                    binding.imgError.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.during_enrollment));

                                } else {
                                    error_title = requireContext().getString(R.string.no_intimations_found);
                                    error_string = requireContext().getString(R.string.intimation_error_desc);
                                    binding.imgError.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_no_claims));
                                }


                            } else {
                                error_title = requireContext().getString(R.string.no_intimations_found);
                                error_string = requireContext().getString(R.string.intimation_error_desc);
                                binding.imgError.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_no_claims));
                            }
                        } else {
                            error_title = requireContext().getString(R.string.no_intimations_found);
                            error_string = requireContext().getString(R.string.intimation_error_desc);
                            binding.imgError.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_no_claims));
                        }

                        binding.textError.setText(error_title);
                        binding.textErrorDetails.setText(error_string);
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }

            });

            claimsViewModel.getClaimsData().observe(getViewLifecycleOwner(), claimsResponse -> {
                if (claimsResponse != null) {
                    if (claimsResponse.getClaimslist() != null) {

                        if (claimsResponse.getClaimslist().isEmpty()) {
                            binding.errorLayout.setVisibility(View.VISIBLE);


                        } else {
                            adapter = new ClaimAdapter(requireContext(), claimsResponse.getClaimslist());
                            binding.claimsRecyclerView.setAdapter(adapter);
                            adapter.notifyItemRangeChanged(0, claimsResponse.getClaimslist().size());
                            binding.errorLayout.setVisibility(View.GONE);
                        }
                        if (!claimsResponse.getResult().getStatus()) {
                            binding.errorLayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        //No Data found
                        binding.errorLayout.setVisibility(View.VISIBLE);

                    }
                } else {
                    binding.errorLayout.setVisibility(View.VISIBLE);
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
    }

    private void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        getClaims();
    }
}