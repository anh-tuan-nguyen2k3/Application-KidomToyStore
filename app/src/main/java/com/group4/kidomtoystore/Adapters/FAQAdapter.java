package com.group4.kidomtoystore.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group4.kidomtoystore.Models.FAQ;
import com.group4.kidomtoystore.R;

import java.util.List;

public class FAQAdapter extends BaseAdapter {
    Context context;
    int item_faq;
    List<FAQ> faqList;

    public FAQAdapter(Context context, int item_faq, List<FAQ> faqList) {
        this.context = context;
        this.item_faq = item_faq;
        this.faqList= faqList;
    }

    @Override
    public int getCount() {
        if (faqList == null) {
            return 0;
        }
        return faqList.size();
    }

    @Override
    public Object getItem(int position) {
        return faqList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        FAQAdapter.ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(item_faq, null);

            holder.imvArrowIcon = view.findViewById(R.id.arrowIcon);
            holder.txtQuestion= view.findViewById(R.id.txtQuestion);
            holder.txtAnswer = view.findViewById(R.id.txtAnswer);
            view.setTag(holder);
        } else {
            holder = (FAQAdapter.ViewHolder) view.getTag();
        }

        FAQ f = faqList.get(position);
        holder.txtQuestion.setText(f.getQuestion());
        holder.txtAnswer.setText(f.getAnswer());
        return view;
    }

    public static class ViewHolder{
        ImageView imvArrowIcon;
        TextView txtQuestion, txtAnswer;
    }
}
