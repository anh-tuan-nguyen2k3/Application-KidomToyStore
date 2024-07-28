package com.group4.kidomtoystore.Adapters;

import static com.group4.kidomtoystore.Adapters.ProductAdapter.MAX_PRODUCT_NAME_LENGTH;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.group4.kidomtoystore.Models.Product;
import com.group4.kidomtoystore.R;

import java.util.ArrayList;

public class ProductSaleAdapter extends RecyclerView.Adapter<ProductSaleAdapter.viewholder> {
    ArrayList<Product> items;
    Context context;
    private static final int MAX_PRODUCT_NAME_LENGTH = 20;
    private TypedArray colors;


    public ProductSaleAdapter(ArrayList<Product> items, Resources resources) {
        this.items = items;
        // Lấy mảng các màu từ resources
        colors = resources.obtainTypedArray(R.array.product_colors);
    }
    private ProductAdapter.OnItemClickListener listener;

    public void setOnItemClickListener(ProductAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    private int getRandomColor() {
        int index = (int) (Math.random() * colors.length());
        return colors.getColor(index, 0);
    }
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        colors.recycle();
    }

    @NonNull
    @Override
    public ProductSaleAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View infalte = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_sale,parent,false);
        return new viewholder(infalte);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductSaleAdapter.viewholder holder, int position) {
        String productName = items.get(position).getName();
        if (productName.length() > MAX_PRODUCT_NAME_LENGTH) {
            productName = productName.substring(0, MAX_PRODUCT_NAME_LENGTH - 3) + "...";
        }
        holder.txtProductName.setText(productName);
        // Set single line and ellipsize for txtProductName
        holder.txtProductName.setSingleLine(true);
        holder.txtProductName.setEllipsize(TextUtils.TruncateAt.END);

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
        int randomColor = getRandomColor();
        holder.cardView.setCardBackgroundColor(randomColor);

        Drawable background = createDrawableWithColor(randomColor);

        holder.linearLayout.setBackground(background);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(items.get(position));
                }
            }
        });
    }
    private Drawable createDrawableWithColor(int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(color);
        return drawable;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView txtProductName;
        ImageView imvProductThumb;
        LinearLayout linearLayout;
        CardView cardView;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            txtProductName= itemView.findViewById(R.id.txtProductName);
            imvProductThumb=itemView.findViewById(R.id.imvProductThumb);
            cardView=itemView.findViewById(R.id.cvOutSide);
            linearLayout=itemView.findViewById(R.id.lnInCard);

        }
    }
}
