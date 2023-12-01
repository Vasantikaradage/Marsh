package com.marsh.android.MB360.insurance.enrollment.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.marsh.android.MB360.R;
import com.marsh.android.MB360.databinding.ItemEnrollmentMyCoverageBinding;
import com.marsh.android.MB360.insurance.enrollment.interfaces.ToolTipListener;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.Coverage;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.WindowPeriodEnrollmentResponse;
import com.marsh.android.MB360.utilities.WindowPeriodCounter;

import java.text.ParseException;
import java.util.List;

public class EnrollmentCoveragesAdapter extends RecyclerView.Adapter<EnrollmentCoveragesAdapter.EnrollmentCoveragesViewHolder> {

    Context context;
    ToolTipListener toolTipListener;
    List<Coverage> coveragesList;
    String tooltipText = "";
    String atleastOneAlpha = ".*[a-zA-Z]+.*";
    WindowPeriodEnrollmentResponse windowResponse;
    boolean TO_SHOW_TIMER;
    Activity activity;
    boolean animations = true;
    int delayAnimate = 400;


    public EnrollmentCoveragesAdapter(Context context, Activity activity, List<Coverage> coveragesList, ToolTipListener toolTipListener, WindowPeriodEnrollmentResponse windowResponse, boolean TO_SHOW_TIMER, boolean animations) {
        this.context = context;
        this.coveragesList = coveragesList;
        this.toolTipListener = toolTipListener;
        this.windowResponse = windowResponse;
        this.TO_SHOW_TIMER = TO_SHOW_TIMER;
        this.activity = activity;
        this.animations = animations;
    }

    @NonNull
    @Override
    public EnrollmentCoveragesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEnrollmentMyCoverageBinding binding = ItemEnrollmentMyCoverageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new EnrollmentCoveragesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EnrollmentCoveragesViewHolder holder, int position) {
        holder.itemView.setVisibility(View.GONE);
        if (!animations){
            holder.itemView.setVisibility(View.VISIBLE);
        }
        Coverage coverage = coveragesList.get(position);


        //coverage title
        switch (coverage.getPolicyType()) {
            case "1":
                holder.binding.myCoverageTitle.setText(context.getString(R.string.ghi));

                if (TO_SHOW_TIMER) {
                    getWindowPeriodTimer(windowResponse, holder.binding.timerCoverageDetails, "gmc", context, activity);
                }

                //how to show sum insured
                if (coverage.getHowToShowSumInsured().equalsIgnoreCase("0")) {
                    holder.binding.sumInsured.setVisibility(View.VISIBLE);
                    holder.binding.sumInsured.setText(String.format("%s", coverage.getSumInsured()));
                } else {
                    holder.binding.sumInsured.setVisibility(View.VISIBLE);
                    holder.binding.sumInsured.setText(String.format("%s %s", context.getString(R.string.rs), coverage.getSumInsured()));
                    holder.binding.sumInsuredType.setText(String.format("(%s)", coverage.getSumInsureType()));
                }

                //to show premium
                if (coverage.getToShowPremium().equalsIgnoreCase("0")) {
                    holder.binding.premiumAmountTitle.setVisibility(View.GONE);
                    holder.binding.premiumAmount.setVisibility(View.GONE);
                    holder.binding.premiumAmountType.setVisibility(View.GONE);
                    holder.binding.premiumLayout.setVisibility(View.GONE);
                    holder.binding.coverageTooltipImage.setVisibility(View.GONE);

                } else {
                    holder.binding.premiumAmount.setVisibility(View.VISIBLE);
                    holder.binding.premiumAmount.setText(String.format("%s %s", context.getString(R.string.rs), coverage.getPremium()));
                    holder.binding.premiumAmountType.setText(String.format("(%s)", coverage.getPremiumType()));
                    holder.binding.premiumAmountTitle.setVisibility(View.VISIBLE);
                    holder.binding.premiumAmount.setVisibility(View.VISIBLE);
                    holder.binding.premiumAmountType.setVisibility(View.VISIBLE);
                    holder.binding.premiumLayout.setVisibility(View.VISIBLE);
                    holder.binding.coverageTooltipImage.setVisibility(View.VISIBLE);

                }

                break;
            case "2":
                holder.binding.myCoverageTitle.setText(context.getString(R.string.gpa));
                holder.binding.sumInsuredType.setVisibility(View.GONE);

                if (TO_SHOW_TIMER) {
                    getWindowPeriodTimer(windowResponse, holder.binding.timerCoverageDetails, "gpa", context, activity);
                }

                //how to show sum insured
                if (coverage.getHowToShowSumInsured().equalsIgnoreCase("0")) {
                    holder.binding.sumInsured.setVisibility(View.VISIBLE);
                    holder.binding.sumInsured.setText(String.format("%s", coverage.getSumInsured()));

                } else {
                    holder.binding.sumInsured.setVisibility(View.VISIBLE);
                    holder.binding.sumInsured.setText(String.format("%s %s", context.getString(R.string.rs), coverage.getSumInsured()));
                    holder.binding.sumInsuredType.setText(String.format("(%s)", coverage.getSumInsureType()));
                }

                //to show premium
                if (coverage.getToShowPremium().equalsIgnoreCase("0")) {
                    holder.binding.premiumAmountTitle.setVisibility(View.GONE);
                    holder.binding.premiumAmount.setVisibility(View.GONE);
                    holder.binding.premiumLayout.setVisibility(View.GONE);
                    holder.binding.coverageTooltipImage.setVisibility(View.GONE);
                } else {
                    holder.binding.premiumAmount.setVisibility(View.VISIBLE);
                    holder.binding.premiumAmount.setText(String.format("%s %s", context.getString(R.string.rs), coverage.getPremium()));

                    holder.binding.premiumAmountTitle.setVisibility(View.VISIBLE);
                    holder.binding.premiumAmount.setVisibility(View.VISIBLE);
                    holder.binding.premiumLayout.setVisibility(View.VISIBLE);
                    holder.binding.coverageTooltipImage.setVisibility(View.VISIBLE);

                }
                holder.binding.premiumAmountType.setVisibility(View.GONE);
                break;
            case "3":
                holder.binding.myCoverageTitle.setText(context.getString(R.string.gtl));
                holder.binding.sumInsuredType.setVisibility(View.GONE);
                if (TO_SHOW_TIMER) {
                    getWindowPeriodTimer(windowResponse, holder.binding.timerCoverageDetails, "gtl", context, activity);
                }

                //how to show sum insured
                if (coverage.getHowToShowSumInsured().equalsIgnoreCase("0")) {
                    holder.binding.sumInsured.setVisibility(View.VISIBLE);
                    holder.binding.sumInsured.setText(String.format("%s", coverage.getSumInsured()));
                } else {
                    holder.binding.sumInsured.setVisibility(View.VISIBLE);
                    holder.binding.sumInsured.setText(String.format("%s %s", context.getString(R.string.rs), coverage.getSumInsured()));
                    holder.binding.sumInsuredType.setText(String.format("(%s)", coverage.getSumInsureType()));
                }

                //to show premium
                if (coverage.getToShowPremium().equalsIgnoreCase("0")) {
                    holder.binding.premiumAmountTitle.setVisibility(View.GONE);
                    holder.binding.premiumAmount.setVisibility(View.GONE);
                    holder.binding.premiumLayout.setVisibility(View.GONE);
                    holder.binding.coverageTooltipImage.setVisibility(View.GONE);
                } else {
                    holder.binding.premiumAmount.setVisibility(View.VISIBLE);
                    holder.binding.premiumAmount.setText(String.format("%s %s", context.getString(R.string.rs), coverage.getPremium()));

                    holder.binding.premiumAmountTitle.setVisibility(View.VISIBLE);
                    holder.binding.premiumAmount.setVisibility(View.VISIBLE);
                    holder.binding.premiumLayout.setVisibility(View.VISIBLE);
                    holder.binding.coverageTooltipImage.setVisibility(View.VISIBLE);

                }
                holder.binding.premiumAmountType.setVisibility(View.GONE);

                break;
        }

        //tool tip
        if (coverage.getToShowEmployeeContribution().equalsIgnoreCase("0") && coverage.getEmployerContribution().equalsIgnoreCase("0")) {
            //hide tool tip
            holder.binding.coverageTooltipImage.setVisibility(View.GONE);
            tooltipText = "";

        } else if (coverage.getToShowEmployeeContribution().equalsIgnoreCase("0") && coverage.getToShowEmployerContribution().equalsIgnoreCase("1")) {

            holder.binding.coverageTooltipImage.setOnClickListener(v -> {
                tooltipText = "  Premium Contribution" + "\n• Employer Contribution: " + coverage.getEmployerContribution();
                toolTipListener.OnToolTipListener(tooltipText, holder.binding.coverageTooltipImage, holder.binding.coverageItem);
            });

            //to show the Employee Contribution
        } else if (coverage.getToShowEmployeeContribution().equalsIgnoreCase("1") && coverage.getToShowEmployerContribution().equalsIgnoreCase("0")) {

            holder.binding.coverageTooltipImage.setOnClickListener(v -> {
                tooltipText = "  Premium Contribution" + "\n• Employee Contribution: " + coverage.getEmployeeContribution();
                toolTipListener.OnToolTipListener(tooltipText, holder.binding.coverageTooltipImage, holder.binding.coverageItem);
            });

        } else if (coverage.getToShowEmployeeContribution().equalsIgnoreCase("1") && coverage.getToShowEmployeeContribution().equalsIgnoreCase("1")) {


            holder.binding.coverageTooltipImage.setOnClickListener(v -> {
                tooltipText = "  Premium Contribution" + "\n• Employee Contribution: " + coverage.getEmployeeContribution() + "\n• Employer Contribution: " + coverage.getEmployerContribution();
                toolTipListener.OnToolTipListener(tooltipText, holder.binding.coverageTooltipImage, holder.binding.coverageItem);
            });
        } else {
            holder.binding.coverageTooltipImage.setVisibility(View.GONE);
        }

        if (TO_SHOW_TIMER) {
            holder.binding.timerCoverageDetails.setVisibility(View.VISIBLE);
        } else {
            holder.binding.timerCoverageDetails.setVisibility(View.GONE);
        }


        if (animations) {

            if (position % 2 == 0) {
                setAnimationLeft(holder.itemView);
            } else {
                setAnimationRight(holder.itemView);
            }
            // holder.itemView.startAnimation(animation);

        }

    }

    private void getWindowPeriodTimer(WindowPeriodEnrollmentResponse windowPeriod, TextView timerText, String page, Context context, Activity activity) {

        String windowPeriodEndDate = "";
        if (page.equalsIgnoreCase("gmc")) {
            windowPeriodEndDate = windowPeriod.getWindowPeriod().getWindowEndDateGmc();
        } else if (page.equalsIgnoreCase("gpa")) {
            windowPeriodEndDate = windowPeriod.getWindowPeriod().getWindowEndDateGpa();
        } else if (page.equalsIgnoreCase("gtl")) {
            windowPeriodEndDate = windowPeriod.getWindowPeriod().getWindowEndDateGtl();

        }


        if (windowPeriod != null) {
            WindowPeriodCounter windowPeriodCounter = new WindowPeriodCounter(windowPeriodEndDate, context, activity);
            try {
                CountDownTimer timer = windowPeriodCounter.getTimer(timerText);
                if (timer != null) {

                    timer.start();
                } else {
                    timerText.setText("Window period has expired");
                }
            } catch (ParseException e) {

                Toast.makeText(context, "Unable to retrieve window period date.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        } else {
            //no window period available
        }


    }


    @Override
    public int getItemCount() {
        return (coveragesList != null ? coveragesList.size() : 0);
    }


    public static class EnrollmentCoveragesViewHolder extends RecyclerView.ViewHolder {
        ItemEnrollmentMyCoverageBinding binding;

        public EnrollmentCoveragesViewHolder(@NonNull ItemEnrollmentMyCoverageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void setAnimationLeft(final View view) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
                if (view != null) {
                    view.startAnimation(animation);
                    view.setVisibility(View.VISIBLE);
                }
            }
        }, delayAnimate);
        delayAnimate += 300;
    }

    private void setAnimationRight(final View view) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_from_right);
                if (view != null) {
                    view.startAnimation(animation);
                    view.setVisibility(View.VISIBLE);
                }
            }
        }, delayAnimate);
        delayAnimate += 300;
    }
}
