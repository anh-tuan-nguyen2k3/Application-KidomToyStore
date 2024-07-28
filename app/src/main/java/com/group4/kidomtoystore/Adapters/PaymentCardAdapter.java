package com.group4.kidomtoystore.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.group4.kidomtoystore.Models.Address;
import com.group4.kidomtoystore.Models.PaymentCard;
import com.group4.kidomtoystore.R;

import java.util.List;

public class PaymentCardAdapter extends RecyclerView.Adapter {
    private List<PaymentCard> cardList;
    private Context context;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    public PaymentCardAdapter(List<PaymentCard> cardList, Context context) {
        this.cardList = cardList;
        this.context = context;
        this.databaseReference = databaseReference;
        this.mAuth = FirebaseAuth.getInstance();
        this.user = mAuth.getCurrentUser();
    }


    @NonNull
    public PaymentCardAdapter.PaymentCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
//        return new AddressAdapter.AddressViewHolder(view);
        return new PaymentCardAdapter.PaymentCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PaymentCardViewHolder) {
            PaymentCardViewHolder viewHolder = (PaymentCardViewHolder) holder;
            PaymentCard card = cardList.get(position);

            viewHolder.txtBankName.setText(card.getBankName());
            viewHolder.txtBankNumber.setText(card.getBankNumber());
            viewHolder.txtUserName.setText(card.getUserName());
            viewHolder.txtExpireDate.setText(card.getExpiryDate());
            viewHolder.txtCvv.setText(card.getCvv());
            viewHolder.imvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        viewHolder.removeItem(adapterPosition);
                    }
                }
            });
        }
    }


    public int getItemCount() {
        return cardList.size();
    }

    public class PaymentCardViewHolder extends RecyclerView.ViewHolder {
        TextView txtBankName, txtBankNumber, txtUserName, txtExpireDate, txtCvv;
        ImageView imvDelete;

        public PaymentCardViewHolder(@NonNull View itemView) {
            super(itemView);

            txtBankName = itemView.findViewById(R.id.txtBankName);
            txtBankNumber = itemView.findViewById(R.id.txtBankNumber);
            txtUserName = itemView.findViewById(R.id.txtName);
            txtExpireDate = itemView.findViewById(R.id.txtExpireDate);
            txtCvv = itemView.findViewById(R.id.txtCvv);
            imvDelete = itemView.findViewById(R.id.imvDelete);

        }

        public void removeItem(int position) {
            PaymentCard paymentCard = cardList.get(position);
            cardList.remove(position);
            notifyItemRemoved(position);
            DatabaseReference addressRef = databaseReference.child(user.getUid()).child("PaymentCards");
            addressRef.setValue(cardList)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Xoá thẻ thanh toán thành công", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Xoá thẻ thanh toán thất bại", Toast.LENGTH_SHORT).show();
                            cardList.add(position, paymentCard);
                            notifyItemInserted(position);
                        }
                    });
        }
    }
}
