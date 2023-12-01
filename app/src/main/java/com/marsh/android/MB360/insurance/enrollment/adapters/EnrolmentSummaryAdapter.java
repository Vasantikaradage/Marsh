package com.marsh.android.MB360.insurance.enrollment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.marsh.android.MB360.databinding.ItemEnrollSummaryBinding;
import com.marsh.android.MB360.databinding.ItemEnrollSummaryFooterBinding;
import com.marsh.android.MB360.databinding.ItemHeaderEnrollmentSummaryBinding;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.SummaryItem;

import java.util.List;

public class EnrolmentSummaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<SummaryItem> summaryList;
    Context context;

    public EnrolmentSummaryAdapter(List<SummaryItem> summaryList, Context context) {
        this.summaryList = summaryList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        switch (viewType) {
            case 0:
                ItemHeaderEnrollmentSummaryBinding binding = ItemHeaderEnrollmentSummaryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                viewHolder = new EnrollmentSummaryHeaderViewHolder(binding);
                return viewHolder;
            case 1:
                ItemEnrollSummaryBinding binding_summary = ItemEnrollSummaryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                viewHolder = new EnrollmentSummaryViewHolder(binding_summary);
                return viewHolder;
            case 2:
                ItemEnrollSummaryFooterBinding binding_footer = ItemEnrollSummaryFooterBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                viewHolder = new EnrollmentSummaryFooterViewHolder(binding_footer);
                return viewHolder;
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case 0:
                ((EnrollmentSummaryHeaderViewHolder) holder).binding.headerTxt.setText(summaryList.get(position).getKey());
                break;
            case 1:
                ((EnrollmentSummaryViewHolder) holder).binding.tvKey.setText(summaryList.get(position).getKey());
                ((EnrollmentSummaryViewHolder) holder).binding.tvValue.setText(summaryList.get(position).getValue());
                break;
            case 2:
                //just a footer
                ((EnrollmentSummaryFooterViewHolder) holder).binding.enrollSummaryFooterView.setVisibility(View.VISIBLE);
                break;
            case 3:
                // new footer
            default:

        }

    }

    @Override
    public int getItemCount() {
        return (summaryList != null ? summaryList.size() : 0);
    }

    @Override
    public int getItemViewType(int position) {
        return summaryList.get(position).getId();
    }

    public static class EnrollmentSummaryHeaderViewHolder extends RecyclerView.ViewHolder {
        ItemHeaderEnrollmentSummaryBinding binding;

        public EnrollmentSummaryHeaderViewHolder(@NonNull ItemHeaderEnrollmentSummaryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public static class EnrollmentSummaryViewHolder extends RecyclerView.ViewHolder {
        ItemEnrollSummaryBinding binding;

        public EnrollmentSummaryViewHolder(@NonNull ItemEnrollSummaryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static class EnrollmentSummaryFooterViewHolder extends RecyclerView.ViewHolder {
        ItemEnrollSummaryFooterBinding binding;

        public EnrollmentSummaryFooterViewHolder(@NonNull ItemEnrollSummaryFooterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
