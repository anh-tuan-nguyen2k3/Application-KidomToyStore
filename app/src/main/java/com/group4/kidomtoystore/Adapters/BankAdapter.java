package com.group4.kidomtoystore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.group4.kidomtoystore.Models.Bank;
import com.group4.kidomtoystore.R;

import java.util.List;

public class BankAdapter extends BaseAdapter {
    private Context context;
    private int item_bank;
    private List<Bank> bankList;
    private int selectedBankPosition = -1;

    public BankAdapter(Context context, int item_bank, List<Bank> bankList) {
        this.context = context;
        this.item_bank = item_bank;
        this.bankList = bankList;
    }

    public void setSelectedBankPosition(int position) {
        selectedBankPosition = position;
        notifyDataSetChanged(); // Cập nhật lại giao diện
    }

    public int getSelectedBankPosition() {
        return selectedBankPosition;
    }

    @Override
    public int getCount() {
        return (bankList == null) ? 0 : bankList.size();
    }

    @Override
    public Object getItem(int position) {
        return bankList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(item_bank, null);
            holder.imvBankThumb = convertView.findViewById(R.id.imvBankThumb);
            holder.txtBankName = convertView.findViewById(R.id.txtBankName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Bank bank = bankList.get(position);
        holder.imvBankThumb.setImageResource(bank.getBankThumb());
        holder.txtBankName.setText(bank.getBankName());

        // Kiểm tra xem item hiện tại có phải là item được chọn hay không
        if (position == selectedBankPosition) {
            // Thiết lập border cho item
            convertView.setBackgroundResource(R.drawable.input_field_chosen);
        } else {
            // Nếu không phải là ngân hàng đã chọn, xóa border
            convertView.setBackgroundResource(android.R.color.transparent);
        }

        return convertView;
    }

    // Lớp ViewHolder để tối ưu việc ánh xạ view
    private static class ViewHolder {
        ImageView imvBankThumb;
        TextView txtBankName;
    }
}
