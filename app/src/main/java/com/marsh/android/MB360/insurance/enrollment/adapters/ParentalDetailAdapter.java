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

import com.marsh.android.MB360.R;
import com.marsh.android.MB360.databinding.ItemEnrollmentDependantDetailsBinding;
import com.marsh.android.MB360.databinding.ItemParentalHeaderBinding;
import com.marsh.android.MB360.insurance.enrollment.interfaces.DependantHelper;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.ParentalModel;
import com.marsh.android.MB360.utilities.UtilMethods;

import java.util.List;

public class ParentalDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ParentalModel> parentList;
    Context context;
    DependantHelper dependantHelper;
    boolean isWindowPeriodActive;
    boolean animations;
    int delayAnimate = 400;


    public ParentalDetailAdapter(List<ParentalModel> parentList, Context context, DependantHelper dependantHelper, boolean isWindowPeriodActive, boolean animations) {
        this.parentList = parentList;
        this.context = context;
        this.dependantHelper = dependantHelper;
        this.isWindowPeriodActive = isWindowPeriodActive;
        this.animations = animations;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            ItemEnrollmentDependantDetailsBinding dependantDetailsBinding = ItemEnrollmentDependantDetailsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ParentalDetailViewHolder(dependantDetailsBinding);

        } else {
            ItemParentalHeaderBinding parentalHeaderBinding = ItemParentalHeaderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ParentalDetailHeaderViewHolder(parentalHeaderBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == 0) {
            //header
            if (holder.getAdapterPosition() == 0) {
                ((ParentalDetailHeaderViewHolder) holder).binding.lblHeader.setText("First Set of Parents - Sum Insured - Rs. 3,00,000/-");
            }

            if (holder.getAdapterPosition() == 3) {
                ((ParentalDetailHeaderViewHolder) holder).binding.lblHeader.setText("Second Set of Parents - Sum Insured - Rs. 3,00,000/-");
            }

        } else {

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


            ParentalModel dependantHelperModel = parentList.get(position);
            ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.etName.setText(dependantHelperModel.getRelationName());


            //edit image
            ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.rlSave.setImageDrawable(null);
            if (dependantHelperModel.getIsAdded()) {

                //disable the sample relation name
                ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.lblSampleRelation.setVisibility(View.GONE);

                //disable the add button
                ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.addNewIcon.setVisibility(View.GONE);
                ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.etAge.setText(String.format("%s Years", dependantHelperModel.getAge()));

                //edit image
                ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.rlSave.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pencil_svg));

                //setting the relation icon
                switch (dependantHelperModel.getRelationName().toLowerCase()) {
                    case "father":
                        ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.father));
                        ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.tvType.setText(UtilMethods.capitalizeFirstWord("father"));
                        break;
                    case "father-in-law":
                        ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.father));
                        ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.tvType.setText(UtilMethods.capitalizeFirstWord("father-in-law"));
                        break;
                    case "mother-in-law":
                        ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.mother));
                        ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.tvType.setText(UtilMethods.capitalizeFirstWord("mother-in-law"));
                        break;
                    case "mother":
                        ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.mother));
                        ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.tvType.setText(UtilMethods.capitalizeFirstWord("mother"));
                        break;
                    case "wife":
                        ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.wife));
                        ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.tvType.setText(UtilMethods.capitalizeFirstWord("wife"));
                        break;
                    case "daughter":
                        ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.daughter));
                        ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.tvType.setText(UtilMethods.capitalizeFirstWord("daughter"));
                        break;
                    case "son":
                        ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.son));
                        ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.tvType.setText(UtilMethods.capitalizeFirstWord("son"));
                        break;
                    case "employee":
                        ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.husband));
                        ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.tvType.setText(UtilMethods.capitalizeFirstWord("employee"));
                        break;
                    case "spouse":
                        if (dependantHelperModel.getGender().equalsIgnoreCase("female")) {
                            ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.wife));
                            ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.tvType.setText(UtilMethods.capitalizeFirstWord("spouse"));
                        } else {
                            ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.husband));
                            ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.tvType.setText(UtilMethods.capitalizeFirstWord("spouse"));
                        }

                        break;
                    case "twins":
                        if (dependantHelperModel.getGender().equalsIgnoreCase("MALE")) {
                            ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.son));
                            ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.tvType.setText(UtilMethods.capitalizeFirstWord("son"));
                        } else {
                            ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.daughter));
                            ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.tvType.setText(UtilMethods.capitalizeFirstWord("daughter"));
                        }
                        ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.ivtwins.setVisibility(View.VISIBLE);
                        ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.etAge.setVisibility(View.VISIBLE);
                        ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.etAge.setText("(" + dependantHelperModel.getAge() + " Years)");
                        break;
                    case "other":
                        ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.other));
                        ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.tvType.setText(UtilMethods.capitalizeFirstWord("other"));
                        break;
                    case "partner":
                        ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.other));
                        ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.tvType.setText(UtilMethods.capitalizeFirstWord("partner"));
                        break;
                }
                ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.relIcon.setVisibility(View.VISIBLE);
                ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.tvType.setVisibility(View.VISIBLE);

                //disable the sample relation
                ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.lblSampleRelation.setVisibility(View.GONE);

                //is differently abled
                if (dependantHelperModel.isDifferentlyAble()) {
                    ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.ivdiff.setVisibility(View.VISIBLE);
                    ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.ivdiffdoc.setVisibility(View.VISIBLE);

                    ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.ivdiffdoc.setOnClickListener(v -> {
                        Toast.makeText(context, "TODO: file show dialog", Toast.LENGTH_SHORT).show();
                    });

                } else {
                    ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.ivdiff.setVisibility(View.GONE);
                    ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.ivdiffdoc.setVisibility(View.GONE);
                }


                //make name visible
                ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.etName.setVisibility(View.VISIBLE);
                ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.etName.setText(dependantHelperModel.getName());

                //make age visible
                ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.etAge.setVisibility(View.VISIBLE);
                ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.etAge.setText("(" + dependantHelperModel.getAge() + " Years)");
                //make dob visible
                ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.etDOB.setVisibility(View.VISIBLE);
                ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.etDOB.setText(UtilMethods.getDateToShow(dependantHelperModel.getDateOfBirth()));

                //edit
                ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.rlSave.setOnClickListener(v -> {
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


            } else {

                //dependant when not added.
                ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.addNewIcon.setVisibility(View.VISIBLE);
                ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.ivdiffdoc.setVisibility(View.GONE);
                ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.ivdiff.setVisibility(View.GONE);
                ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.ivtwins.setVisibility(View.GONE);
                ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.relIcon.setVisibility(View.GONE);
                ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.etDOB.setText("");
                ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.etAge.setText("");
                ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.tvType.setText("");

                //sample relation name
                ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.lblSampleRelation.setVisibility(View.VISIBLE);
                ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.lblSampleRelation.setText(dependantHelperModel.getRelationName());
                ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.etName.setText("");

                //edit image
                ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.rlSave.setImageDrawable(null);


                ((ParentalDetailViewHolder) holder).binding.itemDependantForeground.getRoot().setOnClickListener(view -> {
                    if (isWindowPeriodActive) {
                        dependantHelper.onAddDependant(dependantHelperModel, position);
                    } else {
                        //window period is expired
                        Toast.makeText(context, "Window period has expired", Toast.LENGTH_SHORT).show();
                    }
                });


            }



        }

    }

    @Override
    public int getItemCount() {
        return (parentList != null ? parentList.size() : 0);
    }

    @Override
    public int getItemViewType(int position) {
        return parentList.get(position).getHeader() ? 0 : 1;
    }

    public static class ParentalDetailViewHolder extends RecyclerView.ViewHolder {

        public ItemEnrollmentDependantDetailsBinding binding;

        public ParentalDetailViewHolder(@NonNull ItemEnrollmentDependantDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static class ParentalDetailHeaderViewHolder extends RecyclerView.ViewHolder {
        public ItemParentalHeaderBinding binding;

        public ParentalDetailHeaderViewHolder(@NonNull ItemParentalHeaderBinding binding) {
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
