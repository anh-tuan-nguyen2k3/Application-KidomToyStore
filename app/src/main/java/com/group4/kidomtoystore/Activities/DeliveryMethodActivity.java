package com.group4.kidomtoystore.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.group4.kidomtoystore.Adapters.OrderMethodAdapter;
import com.group4.kidomtoystore.Models.OrderMethod;
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.ActivityDeliveryMethodBinding;

import java.util.ArrayList;

public class DeliveryMethodActivity extends AppCompatActivity implements OrderMethodAdapter.OnMethodClickListener {

    ActivityDeliveryMethodBinding binding;
    ArrayList<OrderMethod> orderMethods;
    OrderMethodAdapter adapter;
    OrderMethod selectedMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeliveryMethodBinding.inflate(getLayoutInflater());
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

        binding.tbTitle.setText("Hình thức giao hàng");

        initDeliveryMethod();
    }

    private void initDeliveryMethod() {
        orderMethods = new ArrayList<>();
        orderMethods.add(new OrderMethod(R.drawable.economical_method, "Tiết kiệm", "Thời gian giao hàng dự kiến: 5-7 ngày", 0));
        orderMethods.add(new OrderMethod(R.drawable.basic_method, "Thông thường", "Thời gian giao hàng dự kiến: 3-4 ngày", 15000));
        orderMethods.add(new OrderMethod(R.drawable.fast_method, "Nhanh", "Thời gian giao hàng dự kiến: 2-3 ngày", 25000));
        orderMethods.add(new OrderMethod(R.drawable.instant_method, "Hoả tốc", "Thời gian giao hàng dự kiến: 1-2 ngày", 50000));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rcvDeliveryMethod.setLayoutManager(layoutManager);

        adapter = new OrderMethodAdapter(getApplicationContext(), orderMethods);
        adapter.setOnMethodClickListener(this); // Set the click listener
        binding.rcvDeliveryMethod.setAdapter(adapter);
    }

    // Implement the method from the interface
    @Override
    public void onMethodClick(int position) {
        selectedMethod = orderMethods.get(position);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("tag", "delivery");
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
