package com.group4.kidomtoystore.Adapters;

import static com.group4.kidomtoystore.Adapters.OrderAdapter.MAX_PRODUCT_NAME_LENGTH;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.group4.kidomtoystore.Activities.PreparingOrderDetailActivity;
import com.group4.kidomtoystore.Activities.ReviewActivity;
import com.group4.kidomtoystore.Fragments.ReviewOrderFragment;
import com.group4.kidomtoystore.Models.CartItem;
import com.group4.kidomtoystore.Models.Order;
import com.group4.kidomtoystore.Models.OrderManagement;
import com.group4.kidomtoystore.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PreparingOrderAdapter extends RecyclerView.Adapter<PreparingOrderAdapter.PreparingOrderViewHolder> {

    static final int MAX_PRODUCT_NAME_LENGTH = 25;
    Context context;
    ArrayList<OrderManagement> orderManagements;
    private String mCurrentFragment;
    FirebaseUser user;
    FirebaseAuth mAuth;

    public PreparingOrderAdapter(Context context, ArrayList<OrderManagement> orderManagements, String currentFragment) {
        this.orderManagements = orderManagements;
        this.context = context;
        this.mCurrentFragment = currentFragment;
    }

    @NonNull
    @Override
    public PreparingOrderAdapter.PreparingOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_list,parent,false);
        return new PreparingOrderViewHolder(view);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull PreparingOrderAdapter.PreparingOrderViewHolder holder, @SuppressLint("RecyclerView") int position) {
        OrderManagement orderManagement = orderManagements.get(position);
        String productName = orderManagements.get(position).getName();
        if (productName != null && productName.length() > MAX_PRODUCT_NAME_LENGTH) {
            productName = productName.substring(0, MAX_PRODUCT_NAME_LENGTH - 3) + "...";
        }

        holder.productName.setText(productName);
        holder.productPrice.setText(String.format("%.0fđ", orderManagement.getAmount()));
        holder.productQuantity.setText("Số lượng: " + orderManagement.getQuantity());
        String productThumb = orderManagement.getThumb();
        if (productThumb != null && !productThumb.isEmpty()) {
            Picasso.get().load(productThumb).into(holder.productThumb);
        } else {
            // Nếu không có URL ảnh, hiển thị ảnh mặc định
            holder.productThumb.setImageResource(R.color.greyscale_300);
        }


        // Hiển thị trạng thái đơn hàng
        if (mCurrentFragment.equals("preparing")){
            holder.btnDetail.setText("Chi tiết");
            holder.orderStatus.setText(orderManagement.getStatus());
        }
        else if (mCurrentFragment.equals("ship")) {
            holder.btnDetail.setText("Chi tiết");
            holder.orderStatus.setText(orderManagement.getStatus());
        }
        else if (mCurrentFragment.equals("delivering")) {
            holder.btnDetail.setText("Đã nhận");
            holder.orderStatus.setText(orderManagement.getStatus());
        } else if (mCurrentFragment.equals("review")) {
            holder.btnDetail.setText("Đánh giá");
            holder.orderStatus.setText(orderManagement.getStatus());
        }
        String finalProductName = productName;
        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (context != null) {
//                    Intent intent = new Intent(context, PreparingOrderDetailActivity.class);
//
//                    String orderId = orderManagements.get(position).getId();
//                    intent.putExtra("orderId", orderId);
                    Intent intent = null;
                    if (mCurrentFragment.equals("delivering")) {
                        if (!orderManagements.isEmpty()) {
                            String orderId = orderManagements.get(position).getId();
                            mAuth = FirebaseAuth.getInstance();
                            user = mAuth.getCurrentUser();
                            String userId = user.getUid();
                            // Update the status of the order in Firebase
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference orderRef = database.getReference("Users").child(userId).child("Orders").child(orderId);
                            orderRef.child("status").setValue("Đánh giá");

                            // Remove the item from the list
                            orderManagements.remove(position);
                            // Notify the adapter about the removed item
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, orderManagements.size());

                            if (!orderManagements.isEmpty()) {
                                intent = new Intent(context, OrderManagement.class);
                                intent.putExtra("orderId", orderManagements.get(0).getId()); // Get orderId of the first item after removal
                                intent.putExtra("targetFragment", "review");
                            } else {
                                return;
                            }
                        } else {
                            // Nếu danh sách đã trống từ đầu, không thực hiện intent
                            return;
                        }
                    } else if (mCurrentFragment.equals("review")) {
                        // Nếu là fragment review, chuyển sang ReviewActivity
                        intent = new Intent(context, ReviewActivity.class);
                        String orderId = orderManagements.get(position).getId();
                        int itemPosition = position;
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("itemPosition", itemPosition);
                        intent.putExtra("productName", orderManagement.getName());
                        intent.putExtra("productPrice", orderManagement.getAmount());
                        intent.putExtra("productQuantity", orderManagement.getQuantity());
                        intent.putExtra("productThumb", orderManagement.getThumb());
                        intent.putExtra("productId", orderManagement.getId());
                    } else {
                        // Nếu không phải fragment delivering hoặc review, chuyển sang PreparingOrderDetailActivity
                        intent = new Intent(context, PreparingOrderDetailActivity.class);
                        String orderId = orderManagements.get(position).getId();
                        intent.putExtra("orderId", orderId);
                    }
                    context.startActivity(intent);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return orderManagements.size();
    }


    public static class PreparingOrderViewHolder extends RecyclerView.ViewHolder {
        ImageView productThumb;
        TextView productName, productPrice, orderStatus, productQuantity;
        Button btnDetail;
        LinearLayout layoutItem;

        public PreparingOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            productQuantity = itemView.findViewById(R.id.txtOrderQty);
            productThumb = itemView.findViewById(R.id.imvOrderThumb);
            productName = itemView.findViewById(R.id.txtOrderName);
            productPrice = itemView.findViewById(R.id.txtOrderPrice);
            orderStatus = itemView.findViewById(R.id.txtOrderStatus);
            btnDetail = itemView.findViewById(R.id.btnOrderDetail);
            layoutItem = itemView.findViewById(R.id.layoutItem);
        }
    }
}