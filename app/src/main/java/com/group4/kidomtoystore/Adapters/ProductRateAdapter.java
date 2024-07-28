package com.group4.kidomtoystore.Adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group4.kidomtoystore.Models.ProductRate;
import com.group4.kidomtoystore.R;

import java.util.ArrayList;

public class ProductRateAdapter extends RecyclerView.Adapter<ProductRateAdapter.ViewHolder> {
    Context context;
    ArrayList<ProductRate> productRates;

    public ProductRateAdapter(Context context, ArrayList<ProductRate> productRates) {
        this.context = context;
        this.productRates = productRates;
    }

    @NonNull
    @Override
    public ProductRateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_product_rate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductRateAdapter.ViewHolder holder, int position) {
        holder.txtRateNumber.setText(String.valueOf(productRates.get(position).getRateNumber()));
    }

    @Override
    public int getItemCount() {
        return productRates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtRateNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRateNumber = itemView.findViewById(R.id.txtRateNumber);
        }
    }
}
