package com.marsh.android.MB360.insurance.enrollment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.marsh.android.MB360.R;
import com.marsh.android.MB360.databinding.ItemEnrollmentDependantDetailsBinding;
import com.marsh.android.MB360.insurance.enrollment.interfaces.DependantListenerNew;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.Dependent;
import com.marsh.android.MB360.utilities.UtilMethods;

import java.util.ArrayList;
import java.util.List;

public class DependantDetailsAdapterNew extends RecyclerView.Adapter<DependantDetailsAdapterNew.DependantDetailsViewHolderNew> {

    List<Dependent> dependentList = new ArrayList<>();
    Context context;
    DependantListenerNew dependantListener;

    public DependantDetailsAdapterNew(List<Dependent> dependentList, Context context, DependantListenerNew dependantListener) {
        this.dependentList = dependentList;
        this.context = context;
        this.dependantListener = dependantListener;
    }

    @NonNull
    @Override
    public DependantDetailsViewHolderNew onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEnrollmentDependantDetailsBinding binding = ItemEnrollmentDependantDetailsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DependantDetailsViewHolderNew(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DependantDetailsViewHolderNew holder, int position) {
        //check for dependant to be added or  to be edited
        //slide to delete is always available
        //we need to fill the empty cards -> then only we can delete.
        Dependent dependantHelperModel = dependentList.get(position);

        holder.binding.itemDependantForeground.etName.setText(dependantHelperModel.getRelation().toUpperCase());


        //edit image
        holder.binding.itemDependantForeground.rlSave.setImageDrawable(null);
        if (dependantHelperModel.getIsAdded()) {
            //disable the add button
            holder.binding.itemDependantForeground.addNewIcon.setVisibility(View.GONE);
            holder.binding.itemDependantForeground.etAge.setText(String.format("%s Years", dependantHelperModel.getAge()));

            //edit image
            holder.binding.itemDependantForeground.rlSave.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pencil_svg));

            //setting the relation icon
            switch (dependantHelperModel.getRelation().toLowerCase()) {
                case "father":
                    holder.binding.itemDependantForeground.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.father));
                    holder.binding.itemDependantForeground.tvType.setText(UtilMethods.capitalizeFirstWord("father"));
                    break;
                case "father-in-law":
                    holder.binding.itemDependantForeground.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.father));
                    holder.binding.itemDependantForeground.tvType.setText(UtilMethods.capitalizeFirstWord("father-in-law"));
                    break;
                case "mother":
                    holder.binding.itemDependantForeground.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.mother));
                    holder.binding.itemDependantForeground.tvType.setText(UtilMethods.capitalizeFirstWord("mother-in-law"));
                    break;
                case "mother-in-law":
                    holder.binding.itemDependantForeground.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.mother));
                    holder.binding.itemDependantForeground.tvType.setText(UtilMethods.capitalizeFirstWord("mother"));
                    break;
                case "wife":
                    holder.binding.itemDependantForeground.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.wife));
                    holder.binding.itemDependantForeground.tvType.setText(UtilMethods.capitalizeFirstWord("wife"));
                    break;
                case "daughter":
                    holder.binding.itemDependantForeground.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.daughter));
                    holder.binding.itemDependantForeground.tvType.setText(UtilMethods.capitalizeFirstWord("daughter"));
                    break;
                case "son":
                    holder.binding.itemDependantForeground.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.son));
                    holder.binding.itemDependantForeground.tvType.setText(UtilMethods.capitalizeFirstWord("son"));
                    break;
                case "employee":
                    holder.binding.itemDependantForeground.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.husband));
                    holder.binding.itemDependantForeground.tvType.setText(UtilMethods.capitalizeFirstWord("employee"));
                    break;
                case "spouse":
                    holder.binding.itemDependantForeground.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.husband));
                    holder.binding.itemDependantForeground.tvType.setText(UtilMethods.capitalizeFirstWord("spouse"));
                    break;
                case "twins":
                    if (dependantHelperModel.getGender().equalsIgnoreCase("MALE")) {
                        holder.binding.itemDependantForeground.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.son));
                        holder.binding.itemDependantForeground.tvType.setText(UtilMethods.capitalizeFirstWord("son"));
                    } else {
                        holder.binding.itemDependantForeground.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.daughter));
                        holder.binding.itemDependantForeground.tvType.setText(UtilMethods.capitalizeFirstWord("daughter"));
                    }
                    holder.binding.itemDependantForeground.ivtwins.setVisibility(View.VISIBLE);
                    holder.binding.itemDependantForeground.etAge.setVisibility(View.VISIBLE);
                    holder.binding.itemDependantForeground.etAge.setText("(" + dependantHelperModel.getAge() + " Years)");
                    break;
                case "other":
                    holder.binding.itemDependantForeground.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.other));
                    holder.binding.itemDependantForeground.tvType.setText(UtilMethods.capitalizeFirstWord("other"));
                    break;
                case "partner":
                    holder.binding.itemDependantForeground.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.other));
                    holder.binding.itemDependantForeground.tvType.setText(UtilMethods.capitalizeFirstWord("partner"));
                    break;
            }
            holder.binding.itemDependantForeground.relIcon.setVisibility(View.VISIBLE);
            holder.binding.itemDependantForeground.tvType.setVisibility(View.VISIBLE);

            //disable the sample relation
            holder.binding.itemDependantForeground.lblSampleRelation.setVisibility(View.GONE);

            //is differently abled
            if (dependantHelperModel.getIsDisabled()) {
                holder.binding.itemDependantForeground.ivdiff.setVisibility(View.VISIBLE);
                holder.binding.itemDependantForeground.ivdiffdoc.setVisibility(View.VISIBLE);

                holder.binding.itemDependantForeground.ivdiffdoc.setOnClickListener(v -> {
                    Toast.makeText(context, "TODO: file show dialog", Toast.LENGTH_SHORT).show();
                });

            } else {
                holder.binding.itemDependantForeground.ivdiff.setVisibility(View.GONE);
                holder.binding.itemDependantForeground.ivdiffdoc.setVisibility(View.GONE);
            }


            //make name visible
            holder.binding.itemDependantForeground.etName.setVisibility(View.VISIBLE);
            holder.binding.itemDependantForeground.etName.setText(dependantHelperModel.getName());

            //make age visible
            holder.binding.itemDependantForeground.etAge.setVisibility(View.VISIBLE);
            holder.binding.itemDependantForeground.etAge.setText("(" + dependantHelperModel.getAge() + " Years)");
            //make dob visible
            holder.binding.itemDependantForeground.etDOB.setVisibility(View.VISIBLE);
            holder.binding.itemDependantForeground.etDOB.setText(dependantHelperModel.getDateOfBirth());

            //edit
            holder.binding.itemDependantForeground.rlSave.setOnClickListener(v -> {
                //todo if window period is active
                dependantListener.openDependantBottomSheet(dependantHelperModel, position, dependantListener);

            });

            //TODO show premium
            holder.binding.itemDependantForeground.etPremiumText.setVisibility(View.VISIBLE);
            holder.binding.itemDependantForeground.etPremium.setVisibility(View.VISIBLE);
            holder.binding.itemDependantForeground.etPremium.setText(dependantHelperModel.getPremium());


            if (dependantHelperModel.getIsTwins().equalsIgnoreCase("YES")) {
                holder.binding.itemDependantForeground.ivtwins.setVisibility(View.VISIBLE);
            }

        } else {

            //dependant when not added.
            holder.binding.itemDependantForeground.addNewIcon.setVisibility(View.VISIBLE);
            holder.binding.itemDependantForeground.ivdiffdoc.setVisibility(View.GONE);
            holder.binding.itemDependantForeground.ivdiff.setVisibility(View.GONE);
            holder.binding.itemDependantForeground.ivtwins.setVisibility(View.GONE);
            holder.binding.itemDependantForeground.relIcon.setVisibility(View.GONE);
            holder.binding.itemDependantForeground.etDOB.setText("");
            holder.binding.itemDependantForeground.etAge.setText("");
            holder.binding.itemDependantForeground.tvType.setText("");
            holder.binding.itemDependantForeground.etPremiumText.setVisibility(View.GONE);
            holder.binding.itemDependantForeground.etPremium.setVisibility(View.GONE);

            //edit image
            holder.binding.itemDependantForeground.rlSave.setImageDrawable(null);


            holder.binding.itemDependantForeground.getRoot().setOnClickListener(view -> {
                dependantListener.openDependantBottomSheet(dependantHelperModel, position, dependantListener);

            });


        }
    }

    @Override
    public int getItemCount() {
        return (dependentList != null ? dependentList.size() : 0);
    }

    public static class DependantDetailsViewHolderNew extends RecyclerView.ViewHolder {
        ItemEnrollmentDependantDetailsBinding binding;

        public DependantDetailsViewHolderNew(@NonNull ItemEnrollmentDependantDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
