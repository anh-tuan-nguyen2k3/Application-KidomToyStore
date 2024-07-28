//package com.group4.kidomtoystore.Adapters;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.group4.kidomtoystore.Activities.SeachResultActivity;
//import com.group4.kidomtoystore.Activities.SearchTypeActivity;
//import com.group4.kidomtoystore.Models.ResultSearch;
//import com.group4.kidomtoystore.R;
//
//import java.util.List;
//
//public class SearchResultAdapter extends BaseAdapter {
//
//    Activity context;
//    int item_list;
//
//    List<ResultSearch> lists;
//    DatabaseReference reference;
//    String userId;
//
//
//    public SearchResultAdapter(Activity context, int item_list, List<ResultSearch> lists) {
//        this.context = context;
//        this.item_list = item_list;
//        this.lists = lists;
//    }
//
//    @Override
//    public int getCount() {
//        return lists.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return lists.get(i);
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        ViewHolder holder;
//        if (view == null) {
//            holder = new ViewHolder();
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            view = inflater.inflate(item_list, null);
//
//            holder.imvDelete = view.findViewById(R.id.imvDelete);
//            holder.txtSearchResult = view.findViewById(R.id.txtsearchResult);
//            view.setTag(holder);
//        } else {
//            holder = (ViewHolder) view.getTag();
//        }
//        ResultSearch rs = lists.get(i);
//        holder.txtSearchResult.setText(rs.getSearchResult());
//        holder.imvDelete.setImageResource(R.drawable.ic_clost);
//
//        holder.imvDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Xoá mục tại vị trí i
//                lists.remove(i);
//                // Thông báo cho Adapter biết rằng dữ liệu đã thay đổi
//                notifyDataSetChanged();
//
//            }
//        });
//
//        return view;
//    }
//
//
//    public static class ViewHolder {
//        ImageView imvDelete;
//        TextView txtSearchResult;
//
//    }
//}
package com.group4.kidomtoystore.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group4.kidomtoystore.Activities.SeachResultActivity;
import com.group4.kidomtoystore.Activities.SearchTypeActivity;
import com.group4.kidomtoystore.Models.ResultSearch;
import com.group4.kidomtoystore.R;

import java.util.List;

public class SearchResultAdapter extends BaseAdapter {

    Activity context;
    int item_list;

    List<ResultSearch> lists;


    public SearchResultAdapter(Activity context, int item_list, List<ResultSearch> lists) {
        this.context = context;
        this.item_list = item_list;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(item_list, null);

            holder.imvDelete = view.findViewById(R.id.imvDelete);
            holder.txtSearchResult = view.findViewById(R.id.txtsearchResult);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        ResultSearch rs = lists.get(i);
        holder.txtSearchResult.setText(rs.getSearchResult());
        holder.imvDelete.setImageResource(R.drawable.ic_clost);

        holder.imvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xoá mục tại vị trí i
//                lists.remove(i);
                // Thông báo cho Adapter biết rằng dữ liệu đã thay đổi
                notifyDataSetChanged();
                int position = i;
                Log.d("Position", String.valueOf(position));
                Log.d("SearchResult", lists.get(position).getSearchResult());
//                ResultSearch deletedItem = lists.get(position);
//                if (!lists.isEmpty() && position >= 0 && position < lists.size()) {
//
//                } else {
//                    // Xử lý trường hợp danh sách không có phần tử
//                }
                ResultSearch deletedItem = lists.get(position);
//                Log.d("deletedItem", deletedItem.getSearchResult());
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String userId = user.getUid();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Search");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String searchResult = dataSnapshot.getValue(String.class);
                                if (searchResult.equals(deletedItem.getSearchResult())) {
                                    String key = dataSnapshot.getKey();
                                    reference.child(key).removeValue()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Xoá thành công trên Firebase
                                                    // Cập nhật lại dữ liệu trên giao diện nếu cần
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Xử lý khi xoá thất bại trên Firebase
                                                }
                                            });
                                    break;

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                lists.remove(i);



            }
        });

        return view;
    }

    public static class ViewHolder {
        ImageView imvDelete;
        TextView txtSearchResult;

    }
}