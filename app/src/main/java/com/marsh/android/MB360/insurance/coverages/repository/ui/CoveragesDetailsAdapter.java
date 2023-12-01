package com.marsh.android.MB360.insurance.coverages.repository.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.marsh.android.MB360.R;
import com.marsh.android.MB360.databinding.CoverageDependantsItemBinding;
import com.marsh.android.MB360.insurance.coverages.repository.responseclass.CoveragesDatum;

import java.util.List;
import java.util.StringTokenizer;

public class CoveragesDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<CoveragesDatum> coveragesDetailsList;
    Context context;

    public CoveragesDetailsAdapter(List<CoveragesDatum> coveragesDetailsList, Context context) {
        this.coveragesDetailsList = coveragesDetailsList;
        this.context = context;
    }


    @NonNull
    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CoverageDependantsItemBinding binding = CoverageDependantsItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CoveragesDetailsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CoveragesDetailsViewHolder) holder).binding.name.setText(coveragesDetailsList.get(position).getPersonName());
        ((CoveragesDetailsViewHolder) holder).binding.age.setText(coveragesDetailsList.get(position).getAge());
        String DOB = coveragesDetailsList.get(position).getDateOfBirth();
        try {
            // DOB = UtilMethods.DATE_FORMAT.parse(coveragesDetailsList.get(position).getDateOfBirth()).toString();
            StringTokenizer tokenizer = new StringTokenizer(DOB, "-");

            String date = tokenizer.nextToken();
            String month = tokenizer.nextToken();
            //to capitalize the first word in month name and lower the rest
            month = month.substring(0, 1).toUpperCase() + month.substring(1).toLowerCase();
            String year = tokenizer.nextToken();

            DOB = date + "-" + month + "-" + year;


        } catch (Exception e) {
            e.printStackTrace();
        }
        ((CoveragesDetailsViewHolder) holder).binding.dob.setText(DOB);

        //to change the DOB (PATTERN = DD/Mmm/yyyy)
        ((CoveragesDetailsViewHolder) holder).binding.relation.setText(coveragesDetailsList.get(position).getRelation());

       /* if (coveragesDetailsList.get(position).getGender().equals("MALE")) {
            ((CoveragesDetailsViewHolder) holder).binding.genderImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_by_male));
        } else {
            ((CoveragesDetailsViewHolder) holder).binding.genderImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_by_woman));
        }*/


        if (coveragesDetailsList.get(position).getRelation().equals("FATHER") || coveragesDetailsList.get(position).getRelation().equals("FATHER-IN-LAW")) {
            ((CoveragesDetailsViewHolder) holder).binding.genderImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_by_male));

        } else if (coveragesDetailsList.get(position).getRelation().equals("MOTHER") || coveragesDetailsList.get(position).getRelation().equals("MOTHER-IN-LAW")) {
            ((CoveragesDetailsViewHolder) holder).binding.genderImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_by_woman));

        } else if (coveragesDetailsList.get(position).getRelation().equals("SON")) {
            ((CoveragesDetailsViewHolder) holder).binding.genderImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_by_son));

        } else if (coveragesDetailsList.get(position).getRelation().equals("DAUGHTER")) {
            ((CoveragesDetailsViewHolder) holder).binding.genderImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_by_daughter));

        } else if (coveragesDetailsList.get(position).getRelation().equals("EMPLOYEE")) {
            if (coveragesDetailsList.get(position).getGender().equalsIgnoreCase("male")) {
                ((CoveragesDetailsViewHolder) holder).binding.genderImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_by_male));

            } else {
                ((CoveragesDetailsViewHolder) holder).binding.genderImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_by_woman));
            }
        } else if (coveragesDetailsList.get(position).getRelation().equals("SPOUSE")) {
            if (coveragesDetailsList.get(position).getGender().equalsIgnoreCase("male")) {
                ((CoveragesDetailsViewHolder) holder).binding.genderImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_by_male));
            } else {
                ((CoveragesDetailsViewHolder) holder).binding.genderImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_by_woman));
            }
        } else if (coveragesDetailsList.get(position).getRelation().equals("PARTNER")) {
            if (coveragesDetailsList.get(position).getGender().equalsIgnoreCase("male")) {
                ((CoveragesDetailsViewHolder) holder).binding.genderImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_by_male));
            } else if (coveragesDetailsList.get(position).getGender().equalsIgnoreCase("female")) {
                ((CoveragesDetailsViewHolder) holder).binding.genderImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_by_woman));
            } else {
                ((CoveragesDetailsViewHolder) holder).binding.genderImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_by_rainbow));
            }
        }


    }

    @Override
    public int getItemCount() {
        if (coveragesDetailsList != null) {
            return coveragesDetailsList.size();
        } else {
            return 0;
        }
    }

    public static class CoveragesDetailsViewHolder extends RecyclerView.ViewHolder {
        CoverageDependantsItemBinding binding;

        public CoveragesDetailsViewHolder(@NonNull CoverageDependantsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
