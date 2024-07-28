package com.group4.kidomtoystore.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.group4.kidomtoystore.Adapters.OrderAdapter;
import com.group4.kidomtoystore.Adapters.PreparingOrderAdapter;
import com.group4.kidomtoystore.Models.CartItem;
import com.group4.kidomtoystore.Models.Order;
import com.group4.kidomtoystore.Models.OrderManagement;
import com.group4.kidomtoystore.R;

import java.util.ArrayList;
import java.util.List;


public class PreparingOrderFragment extends Fragment {

    PreparingOrderAdapter preparingOrderAdapter;
    ArrayList<OrderManagement> orderManagements;
    RecyclerView rcvPreparingOrder;
    FirebaseUser user;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    public PreparingOrderFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preparing_order, container, false);

        rcvPreparingOrder = view.findViewById(R.id.rcvPreparing);
        rcvPreparingOrder.setLayoutManager(new LinearLayoutManager(requireContext()));

        orderManagements = new ArrayList<>();
        String currentFragment = "preparing";
        preparingOrderAdapter = new PreparingOrderAdapter(requireContext(), orderManagements, currentFragment);
        rcvPreparingOrder.setAdapter(preparingOrderAdapter);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        String userId = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Orders");

        // Retrieve orders from Firebase
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint({"NotifyDataSetChanged", "MissingInflatedId"})
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String orderId = null;
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    orderId = orderSnapshot.getKey();
                    String orderStatus = orderSnapshot.child("status").getValue(String.class);
                    if (orderStatus != null && orderStatus.equals("Chờ xác nhận")) {
                        double orderPrice = orderSnapshot.child("amount").getValue(Double.class);
                        String orderThumb = "";
                        int orderQuantity = 0;
                        String orderName = "";

                        // Load the first item in the order
                        for (DataSnapshot itemSnapshot : orderSnapshot.child("items").getChildren()) {
                            Log.d("ItemSnapshot", "Key: " + itemSnapshot.getKey());
                            orderThumb = itemSnapshot.child("productThumb").getValue(String.class);
                            orderQuantity = itemSnapshot.child("productQuantity").getValue(Integer.class);
                            orderName = itemSnapshot.child("productName").getValue(String.class);

                            break; // Break after loading the first item
                        }

                        // Create OrderManagement object
                        OrderManagement orderManagement = new OrderManagement(orderId, orderName, orderQuantity, orderPrice, orderStatus, orderThumb);
                        orderManagement.setId(orderId);
                        orderManagements.add(orderManagement);
                    }
                }
                // Kiểm tra danh sách đơn hàng có rỗng không
                if (orderManagements.isEmpty()) {
                    view.findViewById(R.id.layoutNull).setVisibility(View.VISIBLE); // Hiển thị layout_null
                } else {
                    view.findViewById(R.id.layoutNull).setVisibility(View.GONE); // Ẩn layout_null
                }
                // Notify adapter of data changes
                preparingOrderAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PreparingOrderFragment", "Error retrieving orders: " + error.getMessage());
            }
        });
        return view;
    }

}


