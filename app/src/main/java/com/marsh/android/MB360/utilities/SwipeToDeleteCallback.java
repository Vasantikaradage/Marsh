package com.marsh.android.MB360.utilities;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.marsh.android.MB360.insurance.enrollment.adapters.DependantDetailsAdapter;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.DependantHelperModel;

import java.util.List;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private final RecyclerItemTouchHelperListener listener;
    private final List<DependantHelperModel> dependantData;
    DependantHelperModel dependant;

    public SwipeToDeleteCallback(Context context, int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener, List<DependantHelperModel> dependantData) {
        super(dragDirs, swipeDirs);
        this.dependantData = dependantData;
        this.listener = listener;
    }

    @Override
    public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int position = viewHolder.getAdapterPosition();
        dependant = dependantData.get(position);
        if (dependant.isCanDelete()) {
            return super.getSwipeDirs(recyclerView, viewHolder);
        } else return 0;

    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            final View foregroundView = ((DependantDetailsAdapter.DependantDetailsViewHolder) viewHolder).binding.itemDependantForeground.getRoot();
            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {


            // If only want to allow swiping in one direction, you can actually delete the if-block above and just
            // keep the two lines of code below
            View foreground = ((DependantDetailsAdapter.DependantDetailsViewHolder) viewHolder).binding.itemDependantForeground.getRoot();
            getDefaultUIUtil().onDraw(c, recyclerView, foreground, dX, dY, actionState, isCurrentlyActive);

    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((DependantDetailsAdapter.DependantDetailsViewHolder) viewHolder).binding.itemDependantForeground.getRoot();
        getDefaultUIUtil().clearView(foregroundView);
    }



    private void drawBackground(RecyclerView.ViewHolder viewHolder, float dX, int actionState) {
        final View backgroundView = ((DependantDetailsAdapter.DependantDetailsViewHolder) viewHolder).binding.itemDependantBackground.getRoot();

        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            backgroundView.setLeft((int) Math.max(dX, 0));
        }
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        listener.onDependantSwiped(viewHolder, direction, viewHolder.getAdapterPosition(), dependant);


    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener {
        void onDependantSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position, DependantHelperModel dependantHelperModel);

    }
}
