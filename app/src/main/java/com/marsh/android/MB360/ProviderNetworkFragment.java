package com.marsh.android.MB360;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.marsh.android.MB360.databinding.FragmentProviderNetworkBinding;
import com.marsh.android.MB360.insurance.enrollmentstatus.EnrollmentStatusViewModel;
import com.marsh.android.MB360.insurance.hospitalnetwork.repository.HospitalNetworkViewModel;
import com.marsh.android.MB360.insurance.hospitalnetwork.responseclassV1.HospitalInformation;
import com.marsh.android.MB360.insurance.hospitalnetwork.ui.OnHospitalSelectedListener;
import com.marsh.android.MB360.insurance.hospitalnetwork.ui.ProviderAdapter;
import com.marsh.android.MB360.insurance.repository.LoadSessionViewModel;
import com.marsh.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.marsh.android.MB360.insurance.repository.selectedPolicyRepo.SelectedPolicyViewModel;
import com.marsh.android.MB360.insurance.repository.selectedPolicyRepo.responseclass.GroupPolicyData;
import com.marsh.android.MB360.utilities.UtilMethods;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProviderNetworkFragment extends Fragment implements OnHospitalSelectedListener {

    FragmentProviderNetworkBinding binding;
    View view;
    ProviderAdapter adapter;
    HospitalNetworkViewModel hospitalNetworkViewModel;

    EnrollmentStatusViewModel enrollmentStatusViewModel;
    SelectedPolicyViewModel selectedPolicyViewModel;
    LoadSessionViewModel loadSessionViewModel;


    List<com.marsh.android.MB360.insurance.hospitalnetwork.responseclassV1.HospitalInformation> filteredHospitals = new ArrayList<>();
    List<com.marsh.android.MB360.insurance.hospitalnetwork.responseclassV1.HospitalInformation> nonFilteredHospital = new ArrayList<>();
    NavController navController;

    boolean primary = false;
    boolean secondary = false;
    boolean tertiary = false;
    boolean other = false;
    private boolean isMenuProviderAdded = false;


    String error_string = "";
    String error_title = "";

    String empty_state_header = "Provider Network details are not available for this category";
    String empty_state_desc = "Please contact your HR or Marsh Ops. Team";
    MenuProvider menuProvider;


    public ProviderNetworkFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProviderNetworkBinding.inflate(inflater, container, false);
        view = binding.getRoot();


        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isAdded()) {
            initView();
            getHospitals();
        }
    }


    private void initView() {
        setHasOptionsMenu(true);

        hospitalNetworkViewModel = new ViewModelProvider(requireActivity()).get(HospitalNetworkViewModel.class);
        enrollmentStatusViewModel = new ViewModelProvider(this).get(EnrollmentStatusViewModel.class);
        selectedPolicyViewModel = new ViewModelProvider(requireActivity()).get(SelectedPolicyViewModel.class);
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);

        //To Navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        GroupPolicyData groupPolicyData = selectedPolicyViewModel.getSelectedPolicy().getValue();
        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();

        String emp_sr = "";
        try {

            if (loadSessionResponse != null && groupPolicyData != null) {

                emp_sr = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo();
                enrollmentStatusViewModel.getEnrollmentStatus(emp_sr, loadSessionResponse.getGroupInfoData().getGroupchildsrno(), groupPolicyData.getOeGrpBasInfSrNo()).observe(getViewLifecycleOwner(), enrollmentStatusResponse -> {
                    if (enrollmentStatusResponse != null) {
                        if (enrollmentStatusResponse.getEnrollmentStatus() != null) {

                            if (enrollmentStatusResponse.getEnrollmentStatus().getIsWindowPeriodOpen() == 1) {

                                error_title = requireContext().getString(R.string.enrolment_is_in_progress);
                                error_string = requireContext().getString(R.string.please_contact_your_hr_or_marsh_ops_team_for_more_information);
                                if (nonFilteredHospital.size() != 0) {
                                    error_title = empty_state_header;
                                    error_string = empty_state_desc;
                                    binding.imgError.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_no_hospital_found));
                                } else {
                                    binding.imgError.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.during_enrollment));
                                }

                            } else {
                                error_title = "Provider Network list not updated for your corporate";
                                error_string = "Please contact your HR or Marsh Ops. team for more information";
                                binding.imgError.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_no_hospital_found));
                            }


                        } else {
                            error_title = "Provider Network list not updated for your corporate";
                            error_string = "Please contact your HR or Marsh Ops. team";
                            binding.imgError.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_no_hospital_found));
                        }
                    } else {
                        error_title = "Provider Network list not updated for your corporate";
                        error_string = "Please contact your HR or Marsh Ops. team";
                        binding.imgError.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_no_hospital_found));
                    }

                    binding.textError.setText(error_title);
                    binding.textErrorDetails.setText(error_string);
                });

            } else {
                //LoadSession is empty
                retryLoadSession();
                setObserver();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //onclick
        binding.llPrimary.setOnClickListener(v -> {
            if (!primary) {
                primary = true;
                binding.llPrimary.setBackgroundResource(R.drawable.selector_hospital);
                binding.llSecondary.setBackgroundResource(0);
                binding.llTertiary.setBackgroundResource(0);
                binding.llOther.setBackgroundResource(0);

                secondary = false;
                tertiary = false;
                other = false;
                binding.hospitalSearchView.setQuery(null, false);
                binding.hospitalSearchView.setVisibility(View.GONE);
                binding.cancelCard.setVisibility(View.GONE);
                setMenuVisibility(true);
                filterPrimary();
            } else {
                resetFilter();

            }
        });

        binding.llSecondary.setOnClickListener(v -> {
            if (!secondary) {
                primary = false;
                secondary = true;
                binding.llPrimary.setBackgroundResource(0);
                binding.llSecondary.setBackgroundResource(R.drawable.selector_hospital);
                binding.llTertiary.setBackgroundResource(0);
                binding.llOther.setBackgroundResource(0);
                tertiary = false;
                other = false;
                binding.hospitalSearchView.setQuery(null, false);
                binding.hospitalSearchView.setVisibility(View.GONE);
                binding.cancelCard.setVisibility(View.GONE);
                setMenuVisibility(true);
                filterSecondary();
            } else {
                resetFilter();

            }
        });

        binding.llTertiary.setOnClickListener(v -> {
            if (!tertiary) {
                primary = false;
                secondary = false;
                tertiary = true;
                binding.llPrimary.setBackgroundResource(0);
                binding.llSecondary.setBackgroundResource(0);
                binding.llTertiary.setBackgroundResource(R.drawable.selector_hospital);
                binding.llOther.setBackgroundResource(0);
                other = false;
                binding.hospitalSearchView.setQuery(null, false);
                binding.hospitalSearchView.setVisibility(View.GONE);
                binding.cancelCard.setVisibility(View.GONE);
                setMenuVisibility(true);
                filterTertiary();
            } else {
                resetFilter();
            }
        });

        binding.llOther.setOnClickListener(v -> {
            if (!other) {
                primary = false;
                secondary = false;
                tertiary = false;
                other = true;
                binding.llPrimary.setBackgroundResource(0);
                binding.llSecondary.setBackgroundResource(0);
                binding.llTertiary.setBackgroundResource(0);
                binding.llOther.setBackgroundResource(R.drawable.selector_hospital);

                binding.hospitalSearchView.setQuery(null, false);
                binding.hospitalSearchView.setVisibility(View.GONE);
                binding.cancelCard.setVisibility(View.GONE);
                setMenuVisibility(true);
                filterOther();
            } else {
                resetFilter();
            }
        });


        hospitalNetworkViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }

        });


        binding.hospitalSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                filterByText(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!s.isEmpty()) {
                    filterByText(s);
                } else {
                    filterByText("");
                }
                return false;
            }
        });


        binding.cancelCard.setOnClickListener(v -> {

            //Clear query
            binding.hospitalSearchView.setQuery("", false);


            //Collapse the action view
            binding.hospitalSearchView.setVisibility(View.GONE);
            binding.cancelCard.setVisibility(View.GONE);
            setMenuVisibility(true);
            //Collapse the search widget
            //binding.m.collapseActionView();
        });

        //menu
        menuProvider = new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.network_provider_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.search) {
                    if (!nonFilteredHospital.isEmpty()) {
                        if (binding.hospitalSearchView.getVisibility() == View.GONE) {
                            binding.hospitalSearchView.setVisibility(View.VISIBLE);
                            binding.cancelCard.setVisibility(View.VISIBLE);
                            item.setIcon(R.drawable.close_white);
                            setMenuVisibility(false);
                        } else {
                            binding.hospitalSearchView.setVisibility(View.GONE);
                            binding.cancelCard.setVisibility(View.GONE);
                            item.setIcon(R.drawable.search);
                            setMenuVisibility(true);
                        }
                    }
                    return true;
                }
                return false;
            }
        };


    }

    private void retryLoadSession() {
        UtilMethods.retryLoadSession(requireContext(), requireActivity(), requireActivity());
    }

    private void setCounts(List<com.marsh.android.MB360.insurance.hospitalnetwork.responseclassV1.HospitalInformation> hospitalList) {

        binding.hospCount.setText(UtilMethods.PriceFormat(String.valueOf(hospitalList.size())));

        //primary
        binding.priCount.setText(UtilMethods.PriceFormat(String.valueOf(hospitalList.stream().filter(hospital -> (hospital.getHospLevelOfCare().equalsIgnoreCase("primary"))).count())));

        //secondary
        binding.secCount.setText(UtilMethods.PriceFormat(String.valueOf(hospitalList.stream().filter(hospital -> (hospital.getHospLevelOfCare().equalsIgnoreCase("secondary"))).count())));

        //tertiary
        binding.terCount.setText(UtilMethods.PriceFormat(String.valueOf(hospitalList.stream().filter(hospital -> (hospital.getHospLevelOfCare().equalsIgnoreCase("tertiary"))).count())));

        //others
        binding.otherCount.setText(UtilMethods.PriceFormat(String.valueOf(hospitalList.stream().filter(hospital -> (!hospital.getHospLevelOfCare().equalsIgnoreCase("primary")) && (!hospital.getHospLevelOfCare().equalsIgnoreCase("secondary")) && (!hospital.getHospLevelOfCare().equalsIgnoreCase("tertiary"))).count())));


    }

    private void filterByText(String searchedString) {

        //filtering of the  search-view text hospitals
        resetFilter();

        if (!searchedString.isEmpty()) {
            filteredHospitals = nonFilteredHospital.stream().filter(hospital -> (hospital.getHospName().toLowerCase().contains(searchedString.toLowerCase())) || hospital.getHospAddress().toLowerCase().contains(searchedString.toLowerCase())).collect(Collectors.toList());
            adapter = new ProviderAdapter(requireContext(), filteredHospitals, this);
            adapter.notifyItemRangeChanged(0, filteredHospitals.size());
            binding.hospitalCycle.setAdapter(adapter);
            binding.hospCount.setText(UtilMethods.PriceFormat(String.valueOf(filteredHospitals.size())));

            if (filteredHospitals.size() == 0) {
                binding.textError.setText(error_title);
                binding.textErrorDetails.setText(error_string);
                binding.errorLayout.setVisibility(View.VISIBLE);
                binding.hospitalFilterLayout.setVisibility(View.GONE);
                removeMenuProvider();
            } else {
                addMenuProvider();
                binding.errorLayout.setVisibility(View.GONE);
                binding.hospitalFilterLayout.setVisibility(View.VISIBLE);
            }
            setCounts(filteredHospitals);
        } else {
            adapter = new ProviderAdapter(requireContext(), nonFilteredHospital, this);
            adapter.notifyItemRangeChanged(0, nonFilteredHospital.size());
            binding.hospitalCycle.setAdapter(adapter);
            binding.hospCount.setText(UtilMethods.PriceFormat(String.valueOf(nonFilteredHospital.size())));

            if (nonFilteredHospital.size() == 0) {
                binding.textError.setText(error_title);
                binding.textErrorDetails.setText(error_string);
                binding.errorLayout.setVisibility(View.VISIBLE);
                binding.hospitalFilterLayout.setVisibility(View.GONE);
                removeMenuProvider();
            } else {
                addMenuProvider();
                binding.errorLayout.setVisibility(View.GONE);
                binding.hospitalFilterLayout.setVisibility(View.VISIBLE);
            }
            setCounts(nonFilteredHospital);
        }


    }


    private void resetFilter() {

        //reset the filters
        adapter = new ProviderAdapter(requireContext(), nonFilteredHospital, this);
        adapter.notifyItemRangeChanged(0, nonFilteredHospital.size());
        binding.hospitalCycle.setAdapter(adapter);

        if (nonFilteredHospital.size() == 0) {
            binding.textError.setText(error_title);
            binding.textErrorDetails.setText(error_string);
            binding.errorLayout.setVisibility(View.VISIBLE);
            binding.hospitalFilterLayout.setVisibility(View.GONE);
            removeMenuProvider();
        } else {
            binding.errorLayout.setVisibility(View.GONE);
            binding.hospitalFilterLayout.setVisibility(View.VISIBLE);
            addMenuProvider();
        }

        primary = false;
        secondary = false;
        tertiary = false;
        other = false;

        binding.llPrimary.setBackgroundResource(0);
        binding.llSecondary.setBackgroundResource(0);
        binding.llTertiary.setBackgroundResource(0);
        binding.llOther.setBackgroundResource(0);

        setCounts(nonFilteredHospital);
    }

    private void filterPrimary() {

        //filtering of the  Primary hospitals
        filteredHospitals = nonFilteredHospital.stream().filter(hospital -> (hospital.getHospLevelOfCare().equalsIgnoreCase("primary"))).collect(Collectors.toList());
        adapter = new ProviderAdapter(requireContext(), filteredHospitals, this);
        adapter.notifyItemRangeChanged(0, filteredHospitals.size());
        binding.hospitalCycle.setAdapter(adapter);


        if (filteredHospitals.size() == 0) {
            binding.textError.setText(error_title);
            binding.textErrorDetails.setText(error_string);
            if (nonFilteredHospital.size() != 0 && filteredHospitals.size() == 0) {
                binding.hospitalFilterLayout.setVisibility(View.VISIBLE);
                binding.textError.setText(empty_state_header);
                binding.textErrorDetails.setText(empty_state_desc);
                binding.imgError.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_no_hospital_found));
            } else {
                binding.hospitalFilterLayout.setVisibility(View.GONE);

            }
            binding.errorLayout.setVisibility(View.VISIBLE);

            removeMenuProvider();
        } else {
            binding.errorLayout.setVisibility(View.GONE);
            binding.hospitalFilterLayout.setVisibility(View.VISIBLE);
            addMenuProvider();
        }
    }

    private void filterTertiary() {

        filteredHospitals = nonFilteredHospital.stream().filter(hospital -> (hospital.getHospLevelOfCare().equalsIgnoreCase("tertiary"))).collect(Collectors.toList());
        adapter = new ProviderAdapter(requireContext(), filteredHospitals, this);
        adapter.notifyItemRangeChanged(0, filteredHospitals.size());
        binding.hospitalCycle.setAdapter(adapter);


        if (filteredHospitals.size() == 0) {
            binding.textError.setText(error_title);
            binding.textErrorDetails.setText(error_string);
            if (nonFilteredHospital.size() != 0 && filteredHospitals.size() == 0) {
                binding.textError.setText(empty_state_header);
                binding.textErrorDetails.setText(empty_state_desc);
                binding.hospitalFilterLayout.setVisibility(View.VISIBLE);
                binding.imgError.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_no_hospital_found));
            } else {
                binding.hospitalFilterLayout.setVisibility(View.GONE);
            }

            binding.errorLayout.setVisibility(View.VISIBLE);
            removeMenuProvider();
        } else {
            binding.errorLayout.setVisibility(View.GONE);
            binding.hospitalFilterLayout.setVisibility(View.VISIBLE);
            addMenuProvider();
        }
    }

    private void filterOther() {

        filteredHospitals = nonFilteredHospital.stream().filter(hospital -> (!hospital.getHospLevelOfCare().equalsIgnoreCase("primary")) && (!hospital.getHospLevelOfCare().equalsIgnoreCase("secondary")) && (!hospital.getHospLevelOfCare().equalsIgnoreCase("tertiary"))).collect(Collectors.toList());
        adapter = new ProviderAdapter(requireContext(), filteredHospitals, this);
        adapter.notifyItemRangeChanged(0, filteredHospitals.size());
        binding.hospitalCycle.setAdapter(adapter);


        if (filteredHospitals.size() == 0) {
            binding.textError.setText(error_title);
            binding.textErrorDetails.setText(error_string);
            if (nonFilteredHospital.size() != 0 && filteredHospitals.size() == 0) {
                binding.textError.setText(empty_state_header);
                binding.textErrorDetails.setText(empty_state_desc);
                binding.hospitalFilterLayout.setVisibility(View.VISIBLE);
                binding.imgError.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_no_hospital_found));
            } else {
                binding.hospitalFilterLayout.setVisibility(View.GONE);
            }
            removeMenuProvider();
            binding.errorLayout.setVisibility(View.VISIBLE);
        } else {
            binding.errorLayout.setVisibility(View.GONE);
            binding.hospitalFilterLayout.setVisibility(View.VISIBLE);
            addMenuProvider();
        }
    }

    private void filterSecondary() {

        filteredHospitals = nonFilteredHospital.stream().filter(hospital -> (hospital.getHospLevelOfCare().equalsIgnoreCase("secondary"))).collect(Collectors.toList());
        adapter = new ProviderAdapter(requireContext(), filteredHospitals, this);
        adapter.notifyItemRangeChanged(0, filteredHospitals.size());
        binding.hospitalCycle.setAdapter(adapter);


        if (filteredHospitals.size() == 0) {
            binding.textError.setText(error_title);
            binding.textErrorDetails.setText(error_string);
            if (nonFilteredHospital.size() != 0 && filteredHospitals.size() == 0) {
                binding.textError.setText(empty_state_header);
                binding.textErrorDetails.setText(empty_state_desc);
                binding.imgError.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_no_hospital_found));
                binding.hospitalFilterLayout.setVisibility(View.VISIBLE);
            } else {
                binding.hospitalFilterLayout.setVisibility(View.GONE);
            }
            removeMenuProvider();
            binding.errorLayout.setVisibility(View.VISIBLE);
        } else {
            binding.errorLayout.setVisibility(View.GONE);
            binding.hospitalFilterLayout.setVisibility(View.VISIBLE);
            addMenuProvider();
        }
    }

    private void getHospitals() {
        removeMenuProvider();
        ArrayList<HospitalInformation> hospital_list = new ArrayList<>();

        adapter = new ProviderAdapter(requireContext(), hospital_list, this);
        binding.hospitalCycle.setAdapter(adapter);
        hospitalNetworkViewModel.getHospitalsData().observe(getViewLifecycleOwner(), hospitalData -> {
            try {
                if (hospitalData != null) {
                    if (!hospitalData.getHospitalInformation().isEmpty()) {
                        hospital_list.clear();
                        hospital_list.addAll(hospitalData.getHospitalInformation());
                        adapter.notifyItemRangeChanged(0, hospital_list.size());
                        filteredHospitals = hospitalData.getHospitalInformation();
                        nonFilteredHospital = hospitalData.getHospitalInformation();
                        binding.errorLayout.setVisibility(View.GONE);
                        binding.hospitalCycle.setVisibility(View.VISIBLE);
                        binding.hospitalFilterLayout.setVisibility(View.VISIBLE);
                        addMenuProvider();
                    } else {
                        binding.textError.setText(error_title);
                        binding.textErrorDetails.setText(error_string);
                        removeMenuProvider();
                        binding.hospitalFilterLayout.setVisibility(View.GONE);
                        binding.errorLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.textError.setText(error_title);
                    binding.textErrorDetails.setText(error_string);
                    addMenuProvider();
                    binding.hospitalFilterLayout.setVisibility(View.GONE);
                    binding.errorLayout.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                binding.textError.setText(error_title);
                binding.textErrorDetails.setText(error_string);
                removeMenuProvider();
                binding.hospitalFilterLayout.setVisibility(View.GONE);
                binding.errorLayout.setVisibility(View.VISIBLE);
            }

            //to set the count of the total available hospital
            setCounts(hospital_list);

            //perfrom click for primary selection
            if (!primary) {
                binding.llPrimary.performClick();
                binding.llPrimary.setBackgroundResource(R.drawable.selector_hospital);
                binding.llSecondary.setBackgroundResource(0);
                binding.llTertiary.setBackgroundResource(0);
                binding.llOther.setBackgroundResource(0);
            }
        });

    }

    private void setObserver() {
        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();

        if (loadSessionResponse != null) {
            getHospitals();
        } else {

        }

    }


    @Override
    public void selectedHospital(String hospName, String hospAddress, String latitude, String longitude) {
        NavDirections actions = ProviderNetworkFragmentDirections.actionProviderNetworkToMapsFragment(hospName, hospAddress, (longitude), (latitude));
        navController.navigate(actions);
    }


    private void addMenuProvider() {
        if (!isMenuProviderAdded) {
            requireActivity().addMenuProvider(menuProvider, getViewLifecycleOwner());
            isMenuProviderAdded = true;
        }
    }

    private void removeMenuProvider() {
        if (isMenuProviderAdded) {
            requireActivity().removeMenuProvider(menuProvider);
            isMenuProviderAdded = false;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            addMenuProvider();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (getActivity() != null) {
            initView();
            getHospitals();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //cant do this because the viewmodel has the main thread reference
        //hospitalNetworkViewModel.getHospitalsData().removeObservers(getViewLifecycleOwner());
    }
}