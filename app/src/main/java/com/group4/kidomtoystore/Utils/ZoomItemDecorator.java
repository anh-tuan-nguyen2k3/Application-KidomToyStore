package com.group4.kidomtoystore.Utils;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ZoomItemDecorator extends RecyclerView.ItemDecoration {

    private final float mZoomScale;

    public ZoomItemDecorator(float zoomScale) {
        this.mZoomScale = zoomScale;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);

        // Lấy vị trí của item đầu tiên và cuối cùng hiển thị hoàn toàn
        int firstVisiblePosition = ((LinearLayoutManager) parent.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        int lastVisiblePosition = ((LinearLayoutManager) parent.getLayoutManager()).findLastCompletelyVisibleItemPosition();

        // Kiểm tra xem item có hiển thị hoàn toàn trong RecyclerView không
        if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
            // Tính toán scaleFactor dựa trên vị trí của item trong RecyclerView
            float scaleFactor = 1 - (1 - mZoomScale) * Math.abs(position - state.getItemCount() / 2f) / (state.getItemCount() / 2f);

            // Thiết lập scale cho item
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        } else {
            // Nếu item không hiển thị hoàn toàn, thiết lập scale về bình thường
            view.setScaleX(1f);
            view.setScaleY(1f);
        }
    }
}
