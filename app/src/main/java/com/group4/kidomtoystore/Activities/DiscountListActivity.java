package com.group4.kidomtoystore.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.group4.kidomtoystore.Adapters.OrderMethodAdapter;
import com.group4.kidomtoystore.Models.OrderMethod;
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.ActivityDiscountListBinding;

import java.util.ArrayList;

public class DiscountListActivity extends AppCompatActivity implements OrderMethodAdapter.OnMethodClickListener {

    ActivityDiscountListBinding binding;
    ArrayList<OrderMethod> orderMethods;
    OrderMethodAdapter adapter;
    OrderMethod selectedMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDiscountListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        if (binding.toolbar != null) {
            binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        binding.tbTitle.setText("Mã giảm giá");

        initDeliveryMethod();
    }
    private void initDeliveryMethod() {
        orderMethods = new ArrayList<>();
        orderMethods.add(new OrderMethod(R.drawable.discount, "Freeship 10k", "Giảm 10k phí vận chuyển", 10000));
        orderMethods.add(new OrderMethod(R.drawable.discount, "Freeship 15k", "Giảm 15k phí vận chuyển", 15000));
        orderMethods.add(new OrderMethod(R.drawable.discount, "Discount 30k", "Giảm 30k giá trị đơn hàng", 30000));
        orderMethods.add(new OrderMethod(R.drawable.discount, "Discount 50k", "Giảm 50k giá trị đơn hàng", 50000));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rcvDiscount.setLayoutManager(layoutManager);

        adapter = new OrderMethodAdapter(getApplicationContext(), orderMethods);
        adapter.setOnMethodClickListener(this); // Set the click listener
        binding.rcvDiscount.setAdapter(adapter);
    }

    // Implement the method from the interface
    @Override
    public void onMethodClick(int position) {
        selectedMethod = orderMethods.get(position);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("tag", "discount");
        resultIntent.putExtra("selectedMethod", selectedMethod);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    // Handle the back button press
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}