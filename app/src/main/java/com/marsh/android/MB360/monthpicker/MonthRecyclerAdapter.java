package com.marsh.android.MB360.monthpicker;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.marsh.android.MB360.R;
import com.marsh.android.MB360.utilities.LogMyBenefits;

import java.util.ArrayList;
import java.util.Calendar;

public class MonthRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    View view;
    ArrayList<String> monthList = new ArrayList();
    Integer year;
    private final onMonthClick onMonthClick;
    ArrayList<MonthModel> mainMonthList = new ArrayList<>();
    ArrayList<MonthModel> existingMonthList = new ArrayList<>();

    public MonthRecyclerAdapter(int year, ArrayList<MonthModel> mainMonthList, onMonthClick onMonthClick, ArrayList<MonthModel> existingMonthList) {
        monthList.add("Jan");
        monthList.add("Feb");
        monthList.add("Mar");
        monthList.add("Apr");
        monthList.add("May");
        monthList.add("Jun");
        monthList.add("Jul");
        monthList.add("Aug");
        monthList.add("Sep");
        monthList.add("Oct");
        monthList.add("Nov");
        monthList.add("Dec");
        this.year = year;
        this.onMonthClick = onMonthClick;
        this.mainMonthList = mainMonthList;
        this.mainMonthList.addAll(existingMonthList);


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_month, parent, false);

        return new MonthViewPagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setIsRecyclable(false);
        ((MonthViewPagerViewHolder) holder).month.setTextColor(ContextCompat.getColor(view.getContext(), R.color.black));

        ((MonthViewPagerViewHolder) holder).month.setText(monthList.get(position));



        if (Calendar.getInstance().get(Calendar.YEAR) == year && Calendar.getInstance().get(Calendar.MONTH) > position) {
            ((MonthViewPagerViewHolder) holder).month.setAlpha(0.2f);
        }


        ((MonthViewPagerViewHolder) holder).month.setOnClickListener(view -> {

            if (((MonthViewPagerViewHolder) holder).month.getCurrentTextColor() == view.getContext().getColor(R.color.greenlightbg2)) {
                onMonthClick.onMonthRemoved(new MonthModel(String.valueOf(year), monthList.get(position)));
                ((MonthViewPagerViewHolder) holder).month.setTextColor(ContextCompat.getColor(view.getContext(), R.color.black));
            } else {
                if (Calendar.getInstance().get(Calendar.YEAR) == year && Calendar.getInstance().get(Calendar.MONTH) > position) {
                } else {
                    onMonthClick.onMonthAdded(new MonthModel(String.valueOf(year), monthList.get(position)));
                    ((MonthViewPagerViewHolder) holder).month.setTextColor(ContextCompat.getColor(view.getContext(), R.color.greenlightbg2));
                    LogMyBenefits.e("RECYCLERVIEW", "onMonthAdded: " + monthList.get(position) + "/" + year);
                }
            }


        });


        for (MonthModel month : mainMonthList) {
            LogMyBenefits.d("FORSSS", "Searching for LIST: " + month.getMonth() + "/" + month.getYear());
            LogMyBenefits.d("FOR", "Searching for year: " + year);
            if (year == Integer.parseInt(month.getYear()) && monthList.contains(month.getMonth())) {
                if (((MonthViewPagerViewHolder) holder).month.getText().equals(month.getMonth())) {
                    ((MonthViewPagerViewHolder) holder).month.setTextColor(ContextCompat.getColor(view.getContext(), R.color.greenlightbg2));
                }
            } else {
                LogMyBenefits.d("FOR", "we did not got the year: " + year);
            }
        }

    }

    @Override
    public int getItemCount() {
        return monthList.size();
    }

    public static class MonthViewPagerViewHolder extends RecyclerView.ViewHolder {

        TextView month;

        public MonthViewPagerViewHolder(@NonNull View itemView) {
            super(itemView);

            month = itemView.findViewById(R.id.month_item);
        }
    }
}
