package com.group4.kidomtoystore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.group4.kidomtoystore.Models.CartItem;
import com.group4.kidomtoystore.R;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    static final int MAX_PRODUCT_NAME_LENGTH = 22;
    Context context;
    private ArrayList<CartItem> cartItems;

    public OrderAdapter(ArrayList<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_order,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        String productName = cartItems.get(position).getProductName();
        if (productName != null && productName.length() > MAX_PRODUCT_NAME_LENGTH) {
            productName = productName.substring(0, MAX_PRODUCT_NAME_LENGTH - 3) + "...";
        }
        holder.txtProductName.setText(productName);
        holder.txtProductPrice.setText(String.format("%.0fđ", cartItems.get(position).getProductPrice()));
        holder.txtProductQuantity.setText(String.valueOf((int) cartItems.get(position).getProductQuantity()));
        holder.txtProductId.setText(cartItems.get(position).getId());

        String imgURL = cartItems.get(position).getProductThumb();
        if (imgURL != null && !imgURL.isEmpty()) {
            // Sử dụng Glide để tải hình ảnh
            Glide.with(context)
                    .load(imgURL)
                    .transform(new CenterCrop(), new RoundedCorners(16))
                    .into(holder.imvThumb);
        } else {
            // Xử lý khi đường dẫn hình ảnh là null hoặc rỗng
            holder.imvThumb.setImageResource(R.drawable.input_field);
        }
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName, txtProductPrice, txtProductQuantity, txtProductId;
        ImageView imvThumb;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            txtProductQuantity = itemView.findViewById(R.id.txtProductQuantity);
            txtProductId = itemView.findViewById(R.id.txtProductId);
            imvThumb = itemView.findViewById(R.id.imvThumb);
        }
    }
}
