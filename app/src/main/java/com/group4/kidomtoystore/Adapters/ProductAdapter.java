package com.group4.kidomtoystore.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.group4.kidomtoystore.Activities.ProductDetailActivity;
import com.group4.kidomtoystore.Models.Product;
import com.group4.kidomtoystore.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.viewholder> {
    static final int MAX_PRODUCT_NAME_LENGTH = 26;
    ArrayList<Product> items;
    Context context;

    public ProductAdapter(ArrayList<Product> items) {
        this.items = items;
    }
    public interface OnItemClickListener {
        void onItemClick(Product product);
    }
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void addItem(Product product) {
        items.add(product);
        notifyItemInserted(items.size() - 1);
    }



    @NonNull
    @Override
    public ProductAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_vertical,parent,false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.viewholder holder, @SuppressLint("RecyclerView") int position) {
//        holder.imvProductThumb.setImageResource(position);
        String productName = items.get(position).getName();
        if (productName.length() > MAX_PRODUCT_NAME_LENGTH) {
            productName = productName.substring(0, MAX_PRODUCT_NAME_LENGTH - 3) + "...";
        }
        double price = items.get(position).getPrice();
        double sale = items.get(position).getSale();

// Tính giá sau khi giảm giá
        double discountedPrice = price - (price * (sale / 100.0));

        holder.txtProductName.setText(productName);
        holder.txtPrice.setText(String.format("%dđ", (int) discountedPrice));
        holder.txtStarNumber.setText(String.valueOf((double) items.get(position).getStar()));
        holder.txtSoldNumber.setText(String.valueOf((int) items.get(position).getStock()));
        holder.txtSalePercent.setText(String.format("-%d%%", (int) items.get(position).getSale()));
//        holder.imvProductThumb.setImageResource(items.get(position).getImagePath());

        String imgURL = items.get(position).getImgURL();
        if (imgURL != null && !imgURL.isEmpty()) {
            // Sử dụng Glide để tải hình ảnh
            Glide.with(context)
                    .load(imgURL)
                    .transform(new CenterCrop(), new RoundedCorners(16))
                    .into(holder.imvProductThumb);
        } else {
            // Xử lý khi đường dẫn hình ảnh là null hoặc rỗng
            holder.imvProductThumb.setImageResource(R.drawable.ic_3cham);
        }

        // Set single line and ellipsize for txtProductName
        holder.txtProductName.setSingleLine(true);
        holder.txtProductName.setEllipsize(TextUtils.TruncateAt.END);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(items.get(position));
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        TextView txtProductName, txtStarNumber, txtSoldNumber, txtPrice, txtSalePercent;
        ImageView imvProductThumb;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            imvProductThumb=itemView.findViewById(R.id.imvProductThumb);
            txtProductName=itemView.findViewById(R.id.txtProductName);
            txtStarNumber=itemView.findViewById(R.id.txtStarNumber);
            txtSoldNumber=itemView.findViewById(R.id.txtSoldNumber);
            txtPrice=itemView.findViewById(R.id.txtPrice);
            txtSalePercent=itemView.findViewById(R.id.txtSalePercent);
        }
    }
}
