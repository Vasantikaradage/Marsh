package com.marsh.android.MB360.insurance.utilities.ui;


import static com.marsh.android.MB360.BuildConfig.DOWNLOAD_BASE_URL;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.marsh.android.MB360.R;
import com.marsh.android.MB360.insurance.utilities.repository.responseclass.UTILITIESDATum;
import com.marsh.android.MB360.utilities.LogMyBenefits;
import com.marsh.android.MB360.utilities.LogTags;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UtilitiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<UTILITIESDATum> utilitiesList;
    Activity activity;
    private final boolean[] isDownloading;

    UtilitiesOnCLickInterface utilitiesOnCLickInterface;

    public UtilitiesAdapter(Context context, List<UTILITIESDATum> utilitiesList, Activity activity, UtilitiesOnCLickInterface utilitiesOnCLickInterface) {
        this.context = context;
        this.utilitiesList = utilitiesList;
        this.activity = activity;
        isDownloading = new boolean[utilitiesList.size()];
        Arrays.fill(isDownloading, false);
        this.utilitiesOnCLickInterface = utilitiesOnCLickInterface;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_utilities, parent, false);
        return new UtilitiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final UTILITIESDATum util = utilitiesList.get(position);

        ((UtilitiesViewHolder) holder).utilities_name.setText(util.getDownloadDisplayName());

        if (isDownloading[position]) {
            ((UtilitiesViewHolder) holder).downloadProgress.setVisibility(View.VISIBLE);
            ((UtilitiesViewHolder) holder).utilities_download.setVisibility(View.GONE);
        } else {
            ((UtilitiesViewHolder) holder).downloadProgress.setVisibility(View.GONE);
            ((UtilitiesViewHolder) holder).utilities_download.setVisibility(View.VISIBLE);
        }


        switch (util.getFileType()) {
            case "PDF":
                ((UtilitiesViewHolder) holder).utilities_icon.setImageResource(R.drawable.ic_by_pdf);
                break;
            case "EXCEL":
                ((UtilitiesViewHolder) holder).utilities_icon.setImageResource(R.drawable.ic_by_xlsx);
                break;
            case "DOCX":
                ((UtilitiesViewHolder) holder).utilities_icon.setImageResource(R.drawable.ic_by_doc);
                break;
            default:
                ((UtilitiesViewHolder) holder).utilities_icon.setImageResource(R.drawable.ic_by_file_other);
                break;

        }
        ((UtilitiesViewHolder) holder).item_utilities.setOnClickListener(view -> {
            if (!isAnyDownloading()) {
                ((UtilitiesViewHolder) holder).downloadProgress.setVisibility(View.VISIBLE);
                ((UtilitiesViewHolder) holder).utilities_download.setVisibility(View.GONE);
                utilitiesOnCLickInterface.onClick(position, util);
            }
        });

        ((UtilitiesViewHolder) holder).utilities_download.setOnClickListener(view -> {
            if (!isAnyDownloading()) {
                ((UtilitiesViewHolder) holder).downloadProgress.setVisibility(View.VISIBLE);
                ((UtilitiesViewHolder) holder).utilities_download.setVisibility(View.GONE);
                utilitiesOnCLickInterface.onClick(position, util);
            }
        });


    }

    @Override
    public int getItemCount() {
        if (utilitiesList != null) {
            return utilitiesList.size();
        } else {
            return 0;
        }
    }

    public static class UtilitiesViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout item_utilities;
        TextView utilities_name;
        ImageView utilities_icon, utilities_download;
        ProgressBar downloadProgress;

        public UtilitiesViewHolder(@NonNull View itemView) {
            super(itemView);
            item_utilities = itemView.findViewById(R.id.item_utilities_card);
            utilities_name = itemView.findViewById(R.id.item_utilities_name);
            utilities_icon = itemView.findViewById(R.id.item_utilities_icon);
            utilities_download = itemView.findViewById(R.id.item_utilities_download);
            downloadProgress = itemView.findViewById(R.id.item_download_progress);
        }
    }


    public void startDownloading(int position) {
        isDownloading[position] = true;
        notifyItemChanged(position);

    }

    public void finishDownloading(int position) {
        isDownloading[position] = false;
        notifyItemChanged(position);
    }

    private boolean isAnyDownloading() {
        for (boolean downloading : isDownloading) {
            if (downloading) {
                return true;
            }
        }
        return false;
    }
}
