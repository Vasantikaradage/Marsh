package com.marsh.android.MB360.monthpicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.marsh.android.MB360.R;
import com.marsh.android.MB360.utilities.LogMyBenefits;

import java.util.ArrayList;
import java.util.Iterator;

public class MonthViewPagerAdapter extends PagerAdapter implements onMonthClick {


    private final Context mContext;
    int year;
    ArrayList<MonthModel> monthList = new ArrayList<>();
    MonthRecyclerAdapter adapter;
    GetMonths getMonths;
    ArrayList<MonthModel> existingMonthList = new ArrayList<>();

    public MonthViewPagerAdapter(Context context, int year, GetMonths getMonths, ArrayList<MonthModel> existingMonthList) {
        mContext = context;
        this.year = year;
        this.getMonths = getMonths;
        this.existingMonthList = existingMonthList;
    }

    @Override
    public int getCount() {

        return 20;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, int position) {
        adapter = new MonthRecyclerAdapter(year + position, monthList, this, existingMonthList);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.month_grid_new, collection, false);
        RecyclerView monthCycle = layout.findViewById(R.id.recyclerView);
        monthCycle.setHasFixedSize(true);
        monthCycle.setAdapter(adapter);
        adapter.notifyItemRangeChanged(0, existingMonthList.size());
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup collection, int position, @NonNull Object object) {
        collection.removeView((View) object);
    }


    @Override
    public void onMonthAdded(MonthModel monthModel) {
        monthList.add(monthModel);
        notifyDataSetChanged();
        LogMyBenefits.e("ADAPTER", "onMonthAdded: " + monthModel.getMonth() + "/" + monthModel.getYear());
        getMonths.getMonths(monthList);
    }

    @Override
    public void onMonthRemoved(MonthModel monthModel) {
        Iterator itr = monthList.iterator();
        while (itr.hasNext()) {
            MonthModel month = (MonthModel) itr.next();
            if (month.getYear().equals(monthModel.getYear()) && month.getMonth().equals(monthModel.getMonth()))
                itr.remove();
        }
        adapter.notifyDataSetChanged();
        LogMyBenefits.e("ADAPTER", "onMonthRemoved: " + monthModel.getMonth() + "/" + monthModel.getYear());
        getMonths.getMonths(monthList);
    }
}
