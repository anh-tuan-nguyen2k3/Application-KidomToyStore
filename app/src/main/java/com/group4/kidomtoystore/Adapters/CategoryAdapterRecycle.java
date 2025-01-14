package com.group4.kidomtoystore.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group4.kidomtoystore.Activities.CategoryActivity;
import com.group4.kidomtoystore.Models.Category;
import com.group4.kidomtoystore.R;

import java.util.ArrayList;

public class CategoryAdapterRecycle extends RecyclerView.Adapter<CategoryAdapterRecycle.ViewHolder> {

    Context context;
    ArrayList<Category> categories;

    public CategoryAdapterRecycle(Context context, ArrayList<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_category,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.imvCategory.setImageResource(categories.get(position).getCategoryImage());
        holder.txtCategory.setText(categories.get(position).getCategoryName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("categoryName", categories.get(position).getCategoryName());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {

        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imvCategory;
        TextView txtCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imvCategory = itemView.findViewById(R.id.imvCategory);
            txtCategory = itemView.findViewById(R.id.txtCategory);
        }
    }

}
