package com.marsh.android.MB360.insurance.enrollment.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.aminography.primecalendar.PrimeCalendar;
import com.marsh.android.MB360.R;
import com.marsh.android.MB360.databinding.ItemEnrollmentDependantDetailsBinding;
import com.marsh.android.MB360.insurance.enrollment.interfaces.DependantHelper;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.DependantHelperModel;
import com.marsh.android.MB360.utilities.UtilMethods;

import java.util.Calendar;
import java.util.List;

public class DependantDetailsAdapter extends RecyclerView.Adapter<DependantDetailsAdapter.DependantDetailsViewHolder> {


    List<DependantHelperModel> dependantHelperModelList;
    Context context;
    DependantHelper dependantHelper;
    boolean isWindowPeriodActive;
    boolean animations;
    int delayAnimate = 400;


    public DependantDetailsAdapter(List<DependantHelperModel> relationList, Context context, DependantHelper dependantHelper, boolean isWindowPeriodActive, boolean animations) {
        this.dependantHelperModelList = relationList;
        this.context = context;
        this.dependantHelper = dependantHelper;
        this.isWindowPeriodActive = isWindowPeriodActive;
        this.animations = animations;
    }

    @NonNull
    @Override
    public DependantDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemEnrollmentDependantDetailsBinding binding = ItemEnrollmentDependantDetailsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new DependantDetailsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DependantDetailsViewHolder holder, int position) {
        //check for dependant to be added or  to be edited
        //slide to delete is always available
        //we need to fill the empty cards -> then only we can delete.

        //to support the animation
        holder.itemView.setVisibility(View.GONE);
        if (!animations) {
            holder.itemView.setVisibility(View.VISIBLE);
        }

        if (animations) {
            Animation animation;
            if (position % 2 == 0) {
                animation = AnimationUtils.loadAnimation(context, R.anim.slide_from_left);
                setAnimationLeft(holder.itemView);
            } else {
                animation = AnimationUtils.loadAnimation(context, R.anim.slide_from_right);
                setAnimationRight(holder.itemView);
            }
            //holder.itemView.startAnimation(animation);
        }

        DependantHelperModel dependantHelperModel = dependantHelperModelList.get(position);

        holder.binding.itemDependantForeground.etName.setText(dependantHelperModel.getRelationName());


        //edit image
        holder.binding.itemDependantForeground.rlSave.setImageDrawable(null);
        if (dependantHelperModel.getIsAdded()) {

            //disable the sample relation name
            holder.binding.itemDependantForeground.lblSampleRelation.setVisibility(View.GONE);

            //disable the add button
            holder.binding.itemDependantForeground.addNewIcon.setVisibility(View.GONE);
            holder.binding.itemDependantForeground.etAge.setText(String.format("%s Years", dependantHelperModel.getAge()));

            //edit image
            holder.binding.itemDependantForeground.rlSave.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pencil_svg));

            //setting the relation icon
            switch (dependantHelperModel.getRelationName().toLowerCase()) {
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
                    if (dependantHelperModel.getGender().equalsIgnoreCase("female")) {
                        holder.binding.itemDependantForeground.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.wife));
                        holder.binding.itemDependantForeground.tvType.setText(UtilMethods.capitalizeFirstWord("spouse"));
                    } else {
                        holder.binding.itemDependantForeground.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.husband));
                        holder.binding.itemDependantForeground.tvType.setText(UtilMethods.capitalizeFirstWord("spouse"));
                    }

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
            if (dependantHelperModel.isDifferentlyAble()) {
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
            holder.binding.itemDependantForeground.etDOB.setText(UtilMethods.getDateToShow(dependantHelperModel.getDateOfBirth()));

            //edit
            holder.binding.itemDependantForeground.rlSave.setOnClickListener(v -> {
                if (isWindowPeriodActive) {
                    if (dependantHelperModel.isCanEdit()) {
                        if (dependantHelperModel.getRelationName().equalsIgnoreCase("twins")) {
                            //edit twin invoker
                            //we need to get the twins here. to edit the card
                            dependantHelper.onEditTwin(position);

                        }
                        dependantHelper.onEditDependant(dependantHelperModel, position);
                    } else {
                        //TODO show feedback that user cant be edited
                    }
                } else {
                    Toast.makeText(context, "Window period has expired", Toast.LENGTH_SHORT).show();
                }
            });

            //TODO show premium

            //twins icon
            if (!dependantHelperModel.getPair_no().equalsIgnoreCase("0")) {
                holder.binding.itemDependantForeground.ivtwins.setVisibility(View.VISIBLE);
            } else {
                holder.binding.itemDependantForeground.ivtwins.setVisibility(View.GONE);
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

            //sample relation name
            holder.binding.itemDependantForeground.lblSampleRelation.setVisibility(View.VISIBLE);
            holder.binding.itemDependantForeground.lblSampleRelation.setText(dependantHelperModel.getRelationName());
            holder.binding.itemDependantForeground.etName.setText("");

            //edit image
            holder.binding.itemDependantForeground.rlSave.setImageDrawable(null);


            holder.binding.itemDependantForeground.getRoot().setOnClickListener(view -> {
                if (isWindowPeriodActive) {
                    dependantHelper.onAddDependant(dependantHelperModel, position);
                } else {
                    //window period is expired
                    Toast.makeText(context, "Window period has expired", Toast.LENGTH_SHORT).show();
                }
            });


        }


    }

    private String getAge(PrimeCalendar date_of_birth) {
        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - date_of_birth.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < date_of_birth.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = Integer.valueOf(age);

        return ageInt.toString();
    }

    @Override
    public int getItemCount() {
        return (dependantHelperModelList != null ? dependantHelperModelList.size() : 0);
    }


    public void itemClicked(int position) {

    }

    public static class DependantDetailsViewHolder extends RecyclerView.ViewHolder {
        public ItemEnrollmentDependantDetailsBinding binding;

        public DependantDetailsViewHolder(@NonNull ItemEnrollmentDependantDetailsBinding binding) {
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
