package com.group4.kidomtoystore.Adapters;

import android.annotation.SuppressLint;
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
import com.google.firebase.auth.FirebaseAuth;
import com.group4.kidomtoystore.Models.CartItem;
import com.group4.kidomtoystore.R;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{
    static final int MAX_PRODUCT_NAME_LENGTH = 22;
    Context context;
    private int selectedItem = RecyclerView.NO_POSITION;
    private QuantityChangedListener mListener;

    public interface QuantityChangedListener {
        void onQuantityChanged(int position, int newQuantity);

        CartItem getItemByPosition(int position);

        int getCartSize();
    }

    public CartAdapter(QuantityChangedListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_layout,parent,false);
        return new ViewHolder(inflate);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        CartItem item = this.mListener.getItemByPosition(position);

        String productName = item.getProductName();
        if (productName != null && productName.length() > MAX_PRODUCT_NAME_LENGTH) {
            productName = productName.substring(0, MAX_PRODUCT_NAME_LENGTH - 3) + "...";
        }
        holder.txtProductName.setText(productName);
        holder.txtProductPrice.setText(String.format("%.0fđ", item.getProductPrice()));
        holder.txtProductQuantity.setText(String.valueOf((int) item.getProductQuantity()));
        holder.txtProductId.setText(item.getId());

        String imgURL = item.getProductThumb();
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
        holder.imvMinus.setOnClickListener(v -> {
            decreaseQuantity(position);
        });

        holder.imvPlus.setOnClickListener(v -> {
            increaseQuantity(position);
        });

    }

    @Override
    public int getItemCount() {
        return this.mListener.getCartSize();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName, txtProductPrice, txtProductQuantity, txtProductId;
        ImageView imvThumb, imvMinus, imvPlus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            txtProductQuantity = itemView.findViewById(R.id.txtProductQuantity);
            txtProductId = itemView.findViewById(R.id.txtProductId);
            imvThumb = itemView.findViewById(R.id.imvThumb);
            imvMinus = itemView.findViewById(R.id.imvMinus);
            imvPlus = itemView.findViewById(R.id.imvPlus);
        }
    }

    private void increaseQuantity(int position) {
        // Increase quantity logic
        CartItem item = this.mListener.getItemByPosition(position);
        int currentQuantity = item.getProductQuantity();
        notifyItemChanged(position);
        mListener.onQuantityChanged(position, currentQuantity + 1); // Notify listener of quantity change
    }

    private void decreaseQuantity(int position) {
        // Decrease quantity logic
        CartItem item = this.mListener.getItemByPosition(position);
        int currentQuantity = item.getProductQuantity();
        if (currentQuantity > 1) {
            notifyItemChanged(position);
            mListener.onQuantityChanged(position, currentQuantity - 1); // Notify listener of quantity change
        }
    }
}