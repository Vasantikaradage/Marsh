package com.marsh.android.MB360.insurance.enrollment.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.marsh.android.MB360.R;
import com.marsh.android.MB360.databinding.ItemEnrollmentInstructionBinding;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.Instruction;

import java.util.List;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.InstructionViewHolder> {

    Context context;
    List<Instruction> instructionList;
    String to_show;
    int position_animation = 0;
    boolean animations = true;
    int delayAnimate = 400;

    public InstructionAdapter(Context context, List<Instruction> instructionList, String data_to_show_for, Boolean animations) {
        this.context = context;
        this.instructionList = instructionList;
        to_show = data_to_show_for;
        this.animations = animations;
    }

    @NonNull
    @Override
    public InstructionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEnrollmentInstructionBinding binding = ItemEnrollmentInstructionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new InstructionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionViewHolder holder, int position) {
        /* Animation instruction_animation = AnimationUtils.loadAnimation(context, R.anim.slide_from_right);*/
        //to support the animation
        holder.itemView.setVisibility(View.GONE);
        if (!animations) {
            holder.itemView.setVisibility(View.VISIBLE);
        }
        //conditions regarding rendering are displayed here
        if (to_show.equalsIgnoreCase(instructionList.get(position).getGrade())) {

            //holder.binding.itemInstruction.startAnimation(instruction_animation);

            if (animations) {
                Animation animation;
                if (position_animation == 0 || position_animation % 2 == 0) {
                    animation = AnimationUtils.loadAnimation(context, R.anim.slide_from_left);
                } else {
                    animation = AnimationUtils.loadAnimation(context, R.anim.slide_from_right);
                }
                //holder.itemView.startAnimation(animation);
                setAnimation(holder.itemView);
            }
            holder.binding.itemInstruction.setVisibility(View.VISIBLE);
            position_animation++;
            holder.binding.instructionText.setText(instructionList.get(position).getInstructionText());
        } else {
            if (to_show.equalsIgnoreCase(instructionList.get(position).getDesignation())) {
                // holder.binding.itemInstruction.startAnimation(instruction_animation);

                if (animations) {
                    Animation animation;
                    if (position_animation == 0 || position_animation % 2 == 0) {
                        animation = AnimationUtils.loadAnimation(context, R.anim.slide_from_left);
                    } else {
                        animation = AnimationUtils.loadAnimation(context, R.anim.slide_from_right);
                    }
                    //holder.itemView.startAnimation(animation);
                    setAnimation(holder.itemView);
                }
                holder.binding.itemInstruction.setVisibility(View.VISIBLE);
                position_animation++;
                holder.binding.instructionText.setText(instructionList.get(position).getInstructionText());
            } else {
                if (to_show.equalsIgnoreCase("")) {
                    // holder.binding.itemInstruction.startAnimation(instruction_animation);

                    if (animations) {
                        Animation animation;
                        if (position_animation == 0 || position_animation % 2 == 0) {
                            animation = AnimationUtils.loadAnimation(context, R.anim.slide_from_left);
                        } else {
                            animation = AnimationUtils.loadAnimation(context, R.anim.slide_from_right);
                        }
                        //holder.itemView.startAnimation(animation);
                        setAnimation(holder.itemView);
                    }
                    holder.binding.itemInstruction.setVisibility(View.VISIBLE);
                    position_animation++;
                    holder.binding.instructionText.setText(instructionList.get(position).getInstructionText());
                } else {
                    holder.binding.itemInstruction.setVisibility(View.GONE);
                }
            }
        }





     /*   if (DESIGNATION.equalsIgnoreCase(instructionList.get(position).getToShowDESIGNATION())){
            holder.binding.itemInstruction.setVisibility(View.VISIBLE);
            holder.binding.instructionText.setText(instructionList.get(position).getInstructionText());
        }else{
            holder.binding.itemInstruction.setVisibility(View.GONE);
        }*/
    }

    @Override
    public int getItemCount() {
        return (instructionList != null ? instructionList.size() : 0);
    }

    public static class InstructionViewHolder extends RecyclerView.ViewHolder {
        ItemEnrollmentInstructionBinding binding;

        public InstructionViewHolder(@NonNull ItemEnrollmentInstructionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    private void setAnimation(final View view) {
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
}
