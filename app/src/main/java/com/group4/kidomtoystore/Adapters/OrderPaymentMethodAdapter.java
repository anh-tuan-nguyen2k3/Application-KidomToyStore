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

public class OrderPaymentMethodAdapter extends RecyclerView.Adapter<OrderPaymentMethodAdapter.ViewHolder> {
    private Context context;
    private ArrayList<OrderMethod> orderMethods;
    private OnPaymentMethodClickListener mListener;

    public interface OnPaymentMethodClickListener {
        void onMethodClick(int position);
    }
    public void setOnPaymentMethodClickListener(OrderPaymentMethodAdapter.OnPaymentMethodClickListener listener) {
        mListener = listener;
    }

    public OrderPaymentMethodAdapter(Context context, ArrayList<OrderMethod> orderMethods) {
        this.context = context;
        this.orderMethods = orderMethods;
    }

    @NonNull
    @Override
    public OrderPaymentMethodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_delivery_method, parent, false);
        return new OrderPaymentMethodAdapter.ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderPaymentMethodAdapter.ViewHolder holder, int position) {
        OrderMethod orderMethod = orderMethods.get(position);

        holder.imvDeliveryIcon.setImageResource(orderMethod.getIcon());
        holder.txtDeliveryTitle.setText(orderMethod.getTitle());
        holder.txtDeliveryDescription.setText(orderMethod.getDescription());
    }

    @Override
    public int getItemCount() {
        return orderMethods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imvDeliveryIcon;
        TextView txtDeliveryTitle, txtDeliveryDescription;

        public ViewHolder(@NonNull View itemView, final OrderPaymentMethodAdapter.OnPaymentMethodClickListener listener) {
            super(itemView);

            imvDeliveryIcon = itemView.findViewById(R.id.imvDeliveryIcon);
            txtDeliveryTitle = itemView.findViewById(R.id.txtDeliveryTitle);
            txtDeliveryDescription = itemView.findViewById(R.id.txtDeliveryDescription);

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
