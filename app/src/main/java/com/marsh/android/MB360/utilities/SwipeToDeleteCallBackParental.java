package com.marsh.android.MB360.utilities;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.marsh.android.MB360.insurance.enrollment.adapters.ParentalDetailAdapter;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.ParentalModel;

import java.util.List;

public class SwipeToDeleteCallBackParental extends ItemTouchHelper.SimpleCallback {
    private final SwipeToDeleteCallBackParental.RecyclerItemTouchHelperListener listener;
    private final List<ParentalModel> parentalList;
    ParentalModel parentalModel;

    public SwipeToDeleteCallBackParental(Context context, int dragDirs, int swipeDirs, SwipeToDeleteCallBackParental.RecyclerItemTouchHelperListener listener, List<ParentalModel> parentalList) {
        super(dragDirs, swipeDirs);
        this.parentalList = parentalList;
        this.listener = listener;

    }

    @Override
    public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int position = viewHolder.getAdapterPosition();
        parentalModel = parentalList.get(position);
        if (parentalModel.isCanDelete()) {
            return super.getSwipeDirs(recyclerView, viewHolder);
        } else return 0;

    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            try {
                final View foregroundView = ((ParentalDetailAdapter.ParentalDetailViewHolder) viewHolder).binding.itemDependantForeground.getRoot();
                getDefaultUIUtil().onSelected(foregroundView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        try {
            final View foregroundView = ((ParentalDetailAdapter.ParentalDetailViewHolder) viewHolder).binding.itemDependantForeground.getRoot();
            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, ItemTouchHelper.ACTION_STATE_DRAG, isCurrentlyActive);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        try {
            final View foregroundView = ((ParentalDetailAdapter.ParentalDetailViewHolder) viewHolder).binding.itemDependantForeground.getRoot();
            getDefaultUIUtil().clearView(foregroundView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        try {
            View foregroundView = ((ParentalDetailAdapter.ParentalDetailViewHolder) viewHolder).binding.itemDependantForeground.getRoot();
            float newDX = dX;

      /*  if(newDX <= -350f) {
            newDX = -350f;
        }*/
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, newDX, dY, ItemTouchHelper.ACTION_STATE_DRAG, isCurrentlyActive);

            drawBackground(viewHolder, dX, actionState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawBackground(RecyclerView.ViewHolder viewHolder, float dX, int actionState) {
        try {
            final View backgroundView = ((ParentalDetailAdapter.ParentalDetailViewHolder) viewHolder).binding.itemDependantForeground.getRoot();
            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                backgroundView.setLeft((int) Math.max(dX, 0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (viewHolder instanceof ParentalDetailAdapter.ParentalDetailViewHolder) {
            listener.onSwipedParental(viewHolder, direction, viewHolder.getAdapterPosition(), parentalModel);
        }

    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwipedParental(RecyclerView.ViewHolder viewHolder, int direction, int position, ParentalModel parentalModel);
    }
}
