package com.group4.kidomtoystore.Activities;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group4.kidomtoystore.Adapters.CartAdapter;
import com.group4.kidomtoystore.Adapters.ProductAdapter;
import com.group4.kidomtoystore.Models.CartItem;
import com.group4.kidomtoystore.Models.Product;
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.ActivityCartBinding;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class CartActivity extends AppCompatActivity implements CartAdapter.QuantityChangedListener {

    ActivityCartBinding binding;
    CartAdapter adapter;
    ArrayList<CartItem> cartItems;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    double totalAmount;
    CartItem deleteItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        binding.layoutCheckOut.setVisibility(View.GONE);

        binding.bottomMenu.setSelectedItemId(R.id.mnCart);
        binding.bottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.mnHome  && !(getApplicationContext() instanceof HomePageFull)){
                    startActivity(new Intent(getApplicationContext(), HomePageFull.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if (id == R.id.mnSearch  && !(getApplicationContext() instanceof SearchTypeActivity)){
                    startActivity(new Intent(getApplicationContext(), SearchTypeActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if (id == R.id.mnCart  && !(getApplicationContext() instanceof CartActivity)){
                    startActivity(new Intent(getApplicationContext(), CartActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if (id == R.id.mnOrder  && !(getApplicationContext() instanceof OrderManagementActivity)){
                    startActivity(new Intent(getApplicationContext(), OrderManagementActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if (id == R.id.mnProfile  && !(getApplicationContext() instanceof AccountSettingActivity)){
                    startActivity(new Intent(getApplicationContext(), AccountSettingActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });

        mAuth = FirebaseAuth.getInstance();
        initCartList();
        addEvents();

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.rcvCart);
    }

    private void initCartList() {
        reference = FirebaseDatabase.getInstance().getReference("Users");
        user = mAuth.getCurrentUser();
        String userId = user.getUid();
        cartItems = new ArrayList<>();
        reference.child(userId).child("CartItems").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double totalAmount = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String fireId = snapshot.child("fireId").getValue(String.class);
                    String itemId = snapshot.child("id").getValue(String.class);
                    String name = snapshot.child("productName").getValue(String.class);
                    Double priceDouble = snapshot.child("productPrice").getValue(Double.class);
                    double price = (priceDouble != null) ? priceDouble.doubleValue() : 0.0;
                    int quantity = snapshot.child("productQuantity").getValue(Integer.class);
                    String thumb = snapshot.child("productThumb").getValue(String.class);
                    Log.d("FirebaseData", "Name: " + name + ", Price: " + price + ", Thumb: " + thumb + ", Quantity: " + quantity);

                    // Check if the item already exists in the cart
                    boolean itemExists = false;
                    for (CartItem item : cartItems) {
                        if (item.getId() != null && item.getId().equals(itemId)) {
                            item.setProductQuantity(item.getProductQuantity() + quantity); // Update quantity
                            itemExists = true;
                            break;
                        }
                    }

                    // If the item does not exist in the cart, add it
                    if (!itemExists) {
                        CartItem item = new CartItem(fireId, itemId, name, price, thumb, quantity);
                        cartItems.add(item);
                    }

                    totalAmount += price * quantity;
                    binding.txtTotalAmount.setText(String.format("%.0fđ", totalAmount));
                    binding.layoutCheckOut.setVisibility(View.VISIBLE);
                    binding.layoutNull.setVisibility(View.GONE);
                }

                if (!cartItems.isEmpty()) {
                    binding.progressbar.setVisibility(View.VISIBLE);
                    binding.rcvCart.setLayoutManager(new LinearLayoutManager(CartActivity.this));
                    adapter = new CartAdapter(CartActivity.this);
                    binding.rcvCart.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                binding.progressbar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });
    }


    private void addEvents() {
        binding.btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, OrderDetailActivity.class);
                intent.putExtra("cartItems", cartItems);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onQuantityChanged(int position, int newQuantity) {
        // Update Firebase database with new quantity
        CartItem updatedItem = cartItems.get(position);

        DatabaseReference itemRef = reference
                .child(user.getUid())
                .child("CartItems")
                .child(updatedItem.getFireId());

        itemRef.child("productQuantity").setValue(newQuantity);
        updatedItem.setProductQuantity(newQuantity);

        double totalAmount = 0;
        for (CartItem item : cartItems) {
            totalAmount += item.getProductPrice() * item.getProductQuantity();
        }
        binding.txtTotalAmount.setText(String.format("%.0fđ", totalAmount));
    }

    public int getCartSize() {
        return this.cartItems.size();
    }

    public CartItem getItemByPosition(int position) {
        return this.cartItems.get(position);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            deleteItem = cartItems.get(position);
            cartItems.remove(position);
            adapter.notifyDataSetChanged();

            DatabaseReference cartItemRef = reference
                    .child(user.getUid())
                    .child("CartItems")
                    .child(deleteItem.getFireId());
            cartItemRef.removeValue();

            Snackbar snackbar = Snackbar.make(binding.getRoot(), "", Snackbar.LENGTH_LONG);

            View customSnackbarView = getLayoutInflater().inflate(R.layout.snackbar_undo, null);

            // Customize Snackbar's view
            snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
            @SuppressLint("RestrictedApi") Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
            snackbarLayout.setPadding(0, 0, 0, 0);
            snackbarLayout.addView(customSnackbarView, 0);

            TextView buttonUndo = customSnackbarView.findViewById(R.id.txtUndo);
            buttonUndo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartItems.add(position, deleteItem);
                    adapter.notifyDataSetChanged();

                    DatabaseReference cartItemRef = reference
                            .child(user.getUid())
                            .child("CartItems")
                            .child(deleteItem.getFireId());
                    cartItemRef.setValue(deleteItem);

                    snackbar.dismiss();
                }
            });
            snackbar.show();

        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(CartActivity.this, R.color.primary_500))
                    .addActionIcon(R.drawable.ic_delete_white)
                    .addSwipeLeftLabel("Xoá")
                    .setSwipeLeftLabelColor(ContextCompat.getColor(CartActivity.this, R.color.white))
                    .setSwipeLeftLabelTypeface(ResourcesCompat.getFont(CartActivity.this, R.font.baloopaaji2medium))
                    .setSwipeLeftLabelTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
}