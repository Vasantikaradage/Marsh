package com.marsh.android.MB360.insurance.enrollment.adapters;

import android.content.Context;
import android.os.SystemClock;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.marsh.android.MB360.R;
import com.marsh.android.MB360.databinding.ItemEnrollmentEmployeeDetailsBinding;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.EmployeeDetail;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.WindowPeriodEnrollmentResponse;
import com.marsh.android.MB360.utilities.LogMyBenefits;

import java.util.List;

public class EmployeeDetailsAdapter extends RecyclerView.Adapter<EmployeeDetailsAdapter.EmployeeDetailViewHolder> {

    Context context;
    List<EmployeeDetail> employeeDetailList;
    WindowPeriodEnrollmentResponse windowPeriod;
    boolean isWindowPeriodActive;

    public EmployeeDetailsAdapter(Context context, List<EmployeeDetail> employeeDetailList, WindowPeriodEnrollmentResponse windowPeriod, boolean isWindowPeriodActive) {
        this.context = context;
        this.employeeDetailList = employeeDetailList;
        this.windowPeriod = windowPeriod;
        this.isWindowPeriodActive = isWindowPeriodActive;

    }

    @NonNull
    @Override
    public EmployeeDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEnrollmentEmployeeDetailsBinding binding = ItemEnrollmentEmployeeDetailsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new EmployeeDetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeDetailViewHolder holder, int position) {
        EmployeeDetail employeeDetail = employeeDetailList.get(position);

        if (employeeDetail.getToDisplay().equalsIgnoreCase("0")) {
            //set the item as invisible
            holder.binding.itemEmployeeDetailsLayout.setVisibility(View.GONE);
            holder.binding.itemEmployeeDetailLabel.setVisibility(View.GONE);
            holder.binding.itemEmployeeDetailText.setVisibility(View.GONE);
            holder.binding.itemEditEmployee.setVisibility(View.GONE);

            holder.binding.itemEmployeeDetailTextView.setVisibility(View.GONE);
        } else {
            //set the item as visible
            holder.binding.itemEmployeeDetailLabel.setVisibility(View.VISIBLE);
            holder.binding.itemEmployeeDetailText.setVisibility(View.VISIBLE);
            holder.binding.itemEmployeeDetailTextView.setVisibility(View.GONE);
            holder.binding.itemEditEmployee.setVisibility(View.VISIBLE);
            holder.binding.itemEmployeeDetailLabel.setText(employeeDetail.getFieldName());


            //if employee-details are present from the api itself
            if (!employeeDetail.getFieldValue().equalsIgnoreCase("")) {
                holder.binding.itemEmployeeDetailText.setText(employeeDetail.getFieldValue());
                holder.binding.itemEmployeeDetailTextView.setText(employeeDetail.getFieldValue());
            } else {
                holder.binding.itemEmployeeDetailText.setHint(String.format("Enter the %s", employeeDetail.getFieldName()));
            }

            if (employeeDetail.getToEditable().equalsIgnoreCase("0")) {
                holder.binding.itemEmployeeDetailText.setInputType(InputType.TYPE_NULL);
                holder.binding.itemEmployeeDetailTextView.setVisibility(View.VISIBLE);
                holder.binding.itemEmployeeDetailText.setVisibility(View.GONE);
                holder.binding.itemEditEmployee.setVisibility(View.GONE);
            } else {
                holder.binding.itemEditEmployee.setVisibility(View.VISIBLE);
                holder.binding.itemEmployeeDetailText.setVisibility(View.VISIBLE);
                holder.binding.itemEmployeeDetailText.setInputType(InputType.TYPE_CLASS_TEXT);
                holder.binding.itemEmployeeDetailTextView.setVisibility(View.GONE);

            }
        }

        holder.binding.itemEmployeeDetailText.setOnFocusChangeListener((view, b) -> {

            employeeDetail.setEDIT_STATE(!employeeDetail.isEDIT_STATE());
            if (employeeDetail.isEDIT_STATE()) {
                holder.binding.itemEditEmployee.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_tick));
            } else {
                holder.binding.itemEditEmployee.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pencil_svg));

            }
        });

        holder.binding.itemEditEmployee.setOnClickListener(v -> {
            if (employeeDetail.isEDIT_STATE()) {
                holder.binding.itemEmployeeDetailText.clearFocus();
                holder.binding.itemEditEmployee.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pencil_svg));
                hideKeyboard(v);

            } else {
                holder.binding.itemEmployeeDetailText.requestFocus();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(holder.binding.itemEmployeeDetailTextView, InputMethodManager.SHOW_FORCED);

                holder.binding.itemEmployeeDetailText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0f, 0f, 0));
                holder.binding.itemEmployeeDetailText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0f, 0f, 0));
                try {
                    holder.binding.itemEmployeeDetailText.setSelection(holder.binding.itemEmployeeDetailText.getText().length());
                } catch (Exception e) {
                    LogMyBenefits.d("ERROR", "onBindViewHolder:length is null ");
                }
            }
        });

        //validation
        if (employeeDetail.getFieldName().toLowerCase().contains("age")) {
            holder.binding.itemEmployeeDetailText.setInputType(InputType.TYPE_CLASS_NUMBER);
            holder.binding.itemEmployeeDetailText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
        }
        if (employeeDetail.getFieldName().toLowerCase().contains("mobile")) {
            holder.binding.itemEmployeeDetailText.setInputType(InputType.TYPE_CLASS_NUMBER);
            holder.binding.itemEmployeeDetailText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        }


        //if window period is expired freeze the interaction
        if (!isWindowPeriodActive) {
            holder.binding.itemEmployeeDetailText.setInputType(InputType.TYPE_NULL);
            holder.binding.itemEmployeeDetailTextView.setVisibility(View.VISIBLE);
            holder.binding.itemEmployeeDetailText.setVisibility(View.GONE);
            holder.binding.itemEditEmployee.setVisibility(View.GONE);

            if (!employeeDetail.getToDisplay().equalsIgnoreCase("0")) {
                if (holder.binding.itemEmployeeDetailTextView.getText().toString().trim().isEmpty()) {
                    holder.binding.itemEmployeeDetailTextView.setText("-");
                }
            }
        }

    }

    private void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public int getItemCount() {
        return (employeeDetailList != null ? employeeDetailList.size() : 0);
    }

    public static class EmployeeDetailViewHolder extends RecyclerView.ViewHolder {
        ItemEnrollmentEmployeeDetailsBinding binding;

        public EmployeeDetailViewHolder(@NonNull ItemEnrollmentEmployeeDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
