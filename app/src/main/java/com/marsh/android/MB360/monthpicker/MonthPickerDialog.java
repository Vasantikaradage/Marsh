package com.marsh.android.MB360.monthpicker;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.marsh.android.MB360.R;
import com.marsh.android.MB360.utilities.LogMyBenefits;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;

public class MonthPickerDialog implements GetMonths {

    Activity activity;
    Dialog view;
    int year;
    MonthViewPagerAdapter adapter;
    ArrayList<MonthModel> monthList = new ArrayList<>();
    Button ok, cancel;
    GetMonthsFromDialog getMonthsFromDialog;
    ArrayList<MonthModel> existingMonthList = new ArrayList<>();

    public MonthPickerDialog(Activity activity, GetMonthsFromDialog getMonthsFromDialog, ArrayList<MonthModel> existingMonthList) {
        this.activity = activity;
        this.getMonthsFromDialog = getMonthsFromDialog;
        this.existingMonthList = existingMonthList;
    }

    public void showMonthDialogue() {


        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.month_picker_dialogue, null));
        builder.setCancelable(true);
        view = builder.create();
        view.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        view.requestWindowFeature(Window.FEATURE_NO_TITLE);
        view.show();
        ViewPager viewPager = view.findViewById(R.id.monthViewPager);
        adapter = new MonthViewPagerAdapter(builder.getContext(), Calendar.getInstance().get(Calendar.YEAR), this,existingMonthList);
        viewPager.setAdapter(adapter);

        ImageView leftArrow = view.findViewById(R.id.leftArrow);
        ImageView rightArrow = view.findViewById(R.id.rightArrow);


        TextView yeartext = view.findViewById(R.id.year);
        year = Calendar.getInstance().get(Calendar.YEAR);
        yeartext.setText(String.valueOf(year));

        leftArrow.setOnClickListener(view -> {

            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);

        });
        rightArrow.setOnClickListener(view -> {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    year = Calendar.getInstance().get(Calendar.YEAR);
                } else {
                    year = Calendar.getInstance().get(Calendar.YEAR) + position;

                }
                yeartext.setText(String.valueOf(year));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ok = view.findViewById(R.id.ok_button);
        cancel = view.findViewById(R.id.cancel_button);

        ok.setOnClickListener(v -> {

            for (MonthModel month : monthList
            ) {
                LogMyBenefits.d("ACTIVITY", "getMonths: " + month.getMonth() + "/" + month.getYear());
            }

            getMonthsFromDialog.getMonths(removeDuplicates(monthList));
            view.dismiss();
        });

        cancel.setOnClickListener(v -> {
            monthList.clear();
            view.dismiss();
        });


    }


    @Override
    public void getMonths(ArrayList<MonthModel> monthList) {
        this.monthList.clear();
        this.monthList.addAll(monthList);

    }

    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) {

        // Create a new LinkedHashSet
        Set<T> set = new LinkedHashSet<>();

        // Add the elements to set
        set.addAll(list);

        // Clear the list
        list.clear();

        // add the elements of set
        // with no duplicates to the list
        list.addAll(set);

        // return the list
        return list;
    }
}

