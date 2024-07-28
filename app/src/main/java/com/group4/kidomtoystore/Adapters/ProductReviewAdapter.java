package com.group4.kidomtoystore.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group4.kidomtoystore.Activities.ImageZoomActivity;
import com.group4.kidomtoystore.Models.Product;
import com.group4.kidomtoystore.Models.ProductReview;
import com.group4.kidomtoystore.R;

import java.util.ArrayList;

public class ProductReviewAdapter extends RecyclerView.Adapter<ProductReviewAdapter.ViewHolder> {
    Context context;
    ArrayList<ProductReview> productReviews;

    public void setProductReviews(ArrayList<ProductReview> productReviews) {
        this.productReviews = productReviews;
    }

    public ProductReviewAdapter(Context context, ArrayList<ProductReview> productReviews) {
        this.context = context;
        this.productReviews = productReviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ProductReview review = productReviews.get(position);

        // Hiển thị rating
        holder.txtRateNumber.setText(String.valueOf(review.getRating()));

        // Hiển thị nội dung đánh giá
        holder.txtReviewContent.setText(review.getContent());
        holder.txtUser.setText(review.getUser());

        // Hiển thị tên người đánh giá
        //holder.txtReviewerName.setText(review.getReviewerName());

        // Hiển thị ảnh người đánh giá
        Glide.with(context)
                .load(review.getImgURL())
                .into(holder.imvReviewImage);
        holder.imvReviewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageZoomActivity.class);
                intent.putExtra("imageURL", review.getImgURL());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productReviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imvReviewImage;
        TextView txtRateNumber, txtReviewContent, txtUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imvReviewImage = itemView.findViewById(R.id.imvReviewImage);
            txtRateNumber = itemView.findViewById(R.id.txtRateNumber);
            txtReviewContent = itemView.findViewById(R.id.txtReviewContent);
            txtUser = itemView.findViewById(R.id.txtReviewerName);

        }
    }
}
