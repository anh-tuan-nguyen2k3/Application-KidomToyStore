package com.group4.kidomtoystore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group4.kidomtoystore.Models.OrderManagement;
import com.group4.kidomtoystore.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailOrderManagementAdapter extends RecyclerView.Adapter<DetailOrderManagementAdapter.MyViewHolder> {
    Context context;
    ArrayList<OrderManagement> orderManagements;

    public DetailOrderManagementAdapter(Context context, ArrayList<OrderManagement> orderManagements, String preparing) {
        this.context = context;
        this.orderManagements = orderManagements;
    }

    @NonNull
    @Override
    public DetailOrderManagementAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_order,parent,false);
        return new DetailOrderManagementAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailOrderManagementAdapter.MyViewHolder holder, int position) {
        OrderManagement orderManagement = orderManagements.get(position);

        String originalName = orderManagement.getName();
        if (originalName != null) {
            int maxLength = 27; // Độ dài tối đa bạn muốn hiển thị trên một dòng
            String shortenedName = originalName.length() > maxLength ? originalName.substring(0, maxLength - 3) + "..." : originalName;
            holder.productName.setText(shortenedName);
        } else {
            // Handle the case when the name is null
            holder.productName.setText(""); // or set a default text
        }

//        holder.productName.setText(orderManagement.getName());
        holder.productPrice.setText(String.format("%.0fđ", orderManagement.getAmount()));
        holder.productQuantity.setText(String.valueOf(orderManagement.getQuantity()));
        String productThumb = orderManagement.getThumb();
        if (productThumb != null && !productThumb.isEmpty()) {
            Picasso.get().load(productThumb).into(holder.imvThumb);
        } else {
            // Nếu không có URL ảnh, hiển thị ảnh mặc định
            holder.imvThumb.setImageResource(R.color.greyscale_300);
        }
    }

    @Override
    public int getItemCount() {
        return orderManagements.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imvThumb;
        TextView productName, productPrice, productQuantity;
        public MyViewHolder(View view) {
            super(view);
            imvThumb = view.findViewById(R.id.imvThumb);
            productName = view.findViewById(R.id.txtProductName);
            productPrice = view.findViewById(R.id.txtProductPrice);
            productQuantity = view.findViewById(R.id.txtProductQuantity);
        }
    }
}
