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
import com.group4.kidomtoystore.Adapters.OrderPaymentMethodAdapter;
import com.group4.kidomtoystore.Models.OrderMethod;
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.ActivityDiscountListBinding;
import com.group4.kidomtoystore.databinding.ActivityPaymentMethodBinding;

import java.util.ArrayList;

public class PaymentMethodActivity extends AppCompatActivity implements OrderPaymentMethodAdapter.OnPaymentMethodClickListener {

    ActivityPaymentMethodBinding binding;
    ArrayList<OrderMethod> orderMethods;
    OrderPaymentMethodAdapter adapter;
    OrderMethod selectedMethod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentMethodBinding.inflate(getLayoutInflater());
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

        binding.tbTitle.setText("Phương thức thanh toán");

        initPaymentMethod();
    }

    private void initPaymentMethod() {
        orderMethods = new ArrayList<>();
        orderMethods.add(new OrderMethod(R.drawable.payment, "COD", "Thanh toán khi nhận hàng", 0));
        orderMethods.add(new OrderMethod(R.drawable.payment, "Ví điện tử", "Thanh toán qua ví điện tử Momo", 0));
        orderMethods.add(new OrderMethod(R.drawable.payment, "Thẻ thanh toán", "Thanh toán qua tài khoản mặc định", 0));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rcvPayment.setLayoutManager(layoutManager);

        adapter = new OrderPaymentMethodAdapter(getApplicationContext(), orderMethods);
        adapter.setOnPaymentMethodClickListener(this);
        binding.rcvPayment.setAdapter(adapter);
    }

    public void onMethodClick(int position) {
        selectedMethod = orderMethods.get(position);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("tag", "payment");
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