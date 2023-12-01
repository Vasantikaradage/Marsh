package com.marsh.android.MB360.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.Window;

import com.bumptech.glide.Glide;
import com.marsh.android.MB360.R;
import com.marsh.android.MB360.databinding.WellnessLoadingBinding;

public class LoadingWellnessDialogue {

    Context context;
    Activity activity;
    WellnessLoadingBinding binding;
    private AlertDialog view;

    public LoadingWellnessDialogue(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void showLoading(String loading) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        binding = WellnessLoadingBinding.inflate(inflater);

        builder.setView(binding.getRoot());
        builder.setCancelable(false);
        view = builder.create();
        view.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        view.show();

        // Creating Dynamic
        Rect displayRectangle = new Rect();

        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        view.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), view.getWindow().getAttributes().height);


        Glide.with(context)
                .load(R.drawable.loading)
                .into(binding.loadingGif);
        if (loading.equalsIgnoreCase("")) {
            binding.loadMessage.setText("Loading, Please wait...");
        } else {
            binding.loadMessage.setText(loading);
        }


    }

    public void hideLoading() {
        if (view != null) {
            view.dismiss();
        }


    }
}
