package com.group4.kidomtoystore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group4.kidomtoystore.Models.OrderMethod;
import com.group4.kidomtoystore.R;

import java.util.ArrayList;

public class OrderMethodAdapter extends RecyclerView.Adapter<OrderMethodAdapter.ViewHolder> {
    private Context context;
    private ArrayList<OrderMethod> orderMethods;
    private OnMethodClickListener mListener;

    public interface OnMethodClickListener {
        void onMethodClick(int position);
    }
    public void setOnMethodClickListener(OnMethodClickListener listener) {
        mListener = listener;
    }
    public OrderMethodAdapter(Context context, ArrayList<OrderMethod> orderMethods) {
        this.context = context;
        this.orderMethods = orderMethods;
    }

    @NonNull
    @Override
    public OrderMethodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_delivery_method, parent, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderMethodAdapter.ViewHolder holder, int position) {
        OrderMethod orderMethod = orderMethods.get(position);

        holder.imvDeliveryIcon.setImageResource(orderMethod.getIcon());
        holder.txtDeliveryTitle.setText(orderMethod.getTitle());
        holder.txtDeliveryPrice.setText(String.format("%.0fđ", orderMethod.getPrice()));
        holder.txtDeliveryDescription.setText(orderMethod.getDescription());

    }

    @Override
    public int getItemCount() {
        return orderMethods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imvDeliveryIcon;
        TextView txtDeliveryTitle, txtDeliveryDescription, txtDeliveryPrice;

        public ViewHolder(@NonNull View itemView, final OnMethodClickListener listener) {
            super(itemView);

            imvDeliveryIcon = itemView.findViewById(R.id.imvDeliveryIcon);
            txtDeliveryTitle = itemView.findViewById(R.id.txtDeliveryTitle);
            txtDeliveryDescription = itemView.findViewById(R.id.txtDeliveryDescription);
            txtDeliveryPrice = itemView.findViewById(R.id.txtDeliveryPrice);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onMethodClick(position);
                        }
                    }
                }
            });
        }
    }

}
