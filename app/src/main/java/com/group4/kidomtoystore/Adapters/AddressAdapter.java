package com.group4.kidomtoystore.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.group4.kidomtoystore.Models.Address;
import com.group4.kidomtoystore.R;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private List<Address> addressList;
    private Context context;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser user;



    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public AddressAdapter(Context context, List<Address> addressList, DatabaseReference databaseReference) {
        this.context = context;
        this.addressList = addressList;
        this.databaseReference = databaseReference;
        this.mAuth = FirebaseAuth.getInstance();
        this.user = mAuth.getCurrentUser();
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address_card, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        final Address address = addressList.get(position);

        holder.txtAddressType.setText(address.getAddressType());
        holder.txtAddress.setText(address.getAddressDetail());
        holder.txtFullName.setText(address.getFullName());
        holder.txtPhone.setText(address.getPhone());
        holder.imvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    removeItem(adapterPosition);
                }
            }
        });
    }
    public AddressAdapter() {
        // Khởi tạo các giá trị mặc định nếu cần
        addressList = new ArrayList<>();
        context = null; // hoặc tham chiếu đến context mặc định
        databaseReference = null; // hoặc tham chiếu đến DatabaseReference mặc định
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }



    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public interface OnAddressClickListener {
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView txtAddressType, txtAddress, txtFullName, txtPhone;
        ImageView imvDelete;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAddressType = itemView.findViewById(R.id.txtAddressType);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtFullName = itemView.findViewById(R.id.txtName);
            txtPhone = itemView.findViewById(R.id.txtPhone);
            imvDelete = itemView.findViewById(R.id.imvDelete);
        }
    }

    public void removeItem(int position) {
        Address address = addressList.get(position);
        addressList.remove(position);
        notifyItemRemoved(position);
        DatabaseReference addressRef = databaseReference.child(user.getUid()).child("Addresses");
        addressRef.setValue(addressList)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Xoá địa chỉ thành công", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Xoá địa chỉ thất bại", Toast.LENGTH_SHORT).show();
                        addressList.add(position, address);
                        notifyItemInserted(position);
                    }
                });
    }
}