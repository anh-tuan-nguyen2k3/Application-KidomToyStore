package com.group4.kidomtoystore.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.group4.kidomtoystore.Adapters.SearchResultAdapter;
import com.group4.kidomtoystore.Models.Product;
import com.group4.kidomtoystore.Models.ResultSearch;
import com.group4.kidomtoystore.Models.SearchResultManager;
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.ActivitySearchTypeBinding;

import java.util.ArrayList;

public class SearchTypeActivity extends BaseActivity {

    ActivitySearchTypeBinding binding;
    private String searchText;
    private boolean isSearch;

    ArrayList<ResultSearch> searchResults, searchResultsCurrent;
    SearchResultAdapter adapter;
    DatabaseReference reference;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySearchTypeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

//        searchResults = new ArrayList<>();
//        searchResults = SearchResultManager.getInstance().getSearchResults();

        addEvents();
        getIntentExtra();


        reference = database.getReference("Users");
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();
        searchResults = new ArrayList<>();

        reference.child(userId).child("Search").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String searchResult = dataSnapshot.getValue(String.class);
                    ResultSearch resultSearch = new ResultSearch(searchResult, R.drawable.ic_clost);
                    searchResults.add(0,resultSearch);
                }
                // Sau khi đã lấy dữ liệu từ Firebase, cập nhật giao diện hiển thị (nếu cần)
                adapter = new SearchResultAdapter(SearchTypeActivity.this, R.layout.item_searchresult, searchResults);
                binding.lvSearchResult.setAdapter(adapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu có
            }
        });
    }
    private void updateListView(String searchText) {
//        boolean isExist = false;
//        for (ResultSearch rs : searchResults) {
//            if (rs.getSearchResult().equals(searchText)) {
//                isExist = true;
//                break;
//            }
//        }
//        // Đưa dữ liệu tìm kiếm vào searchResults nếu không tồn tại
//        if (!isExist) {
//            // Tạo adapter mới từ searchResults và gán cho ListView
//            ResultSearch newSearchResult = new ResultSearch(searchText, R.drawable.ic_clost);
//            searchResults.add(0,newSearchResult);
//            adapter = new SearchResultAdapter(this, R.layout.item_searchresult, searchResults);
//            binding.lvSearchResult.setAdapter(adapter);
//            adapter.notifyDataSetChanged();
//        }
        ResultSearch newSearchResult = new ResultSearch(searchText, R.drawable.ic_clost);
        searchResults.add(0,newSearchResult);
        adapter = new SearchResultAdapter(this, R.layout.item_searchresult, searchResults);
        binding.lvSearchResult.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    //work

    private void performSearch() {
        String text = binding.edtSearch.getText().toString();
        if (!text.isEmpty()) {
            Intent intent = new Intent(SearchTypeActivity.this, SeachResultActivity.class);
            intent.putExtra("text", text);
            intent.putExtra("isSearch", true);
//            intent.putParcelableArrayListExtra("listSearch", searchResults);
            startActivity(intent);
            updateListView(text);

//            reference.child(userId).child("Search").push().setValue(text);
            DatabaseReference searchRef = reference.child(userId).child("Search").push();
            searchRef.setValue(text);

//            // Lấy key của node vừa thêm vào
//            String key = searchRef.getKey();
//
//            // Di chuyển node mới được thêm vào vị trí đầu tiên
//            reference.child(userId).child("Search").child(key).setValue(null);
//            reference.child(userId).child("Search").child(key).setValue(text);
        }
    }

    private void getIntentExtra() {
        searchText = getIntent().getStringExtra("text");
        binding.edtSearch.setText(searchText);
    }


    private void addEvents() {
        binding.edtSearch.requestFocus();
        binding.imvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performSearch();
            }
        });
        binding.txtDeleteAll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Xoá tất cả các mục trong danh sách dữ liệu
                if (searchResults != null) {
                    searchResults.clear();
                    // Thông báo cho Adapter biết rằng dữ liệu đã thay đổi
                    adapter.notifyDataSetChanged();
                }
                reference.child(userId).child("Search").removeValue();
            }
        });
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Check if EditText is not empty
                if (s.length() > 0) {
                    // Set color filter to ic_search drawable
                    binding.imvSearch.setColorFilter(getResources().getColor(R.color.primary_500), PorterDuff.Mode.SRC_IN);
                } else {
                    // Set default color filter to ic_search drawable
                    binding.imvSearch.setColorFilter(null);
                }
            }
        });
        binding.lvSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String text = searchResults.get(i).getSearchResult().toString();
                binding.edtSearch.setText(text);
                performSearch();
            }
        });
        binding.imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}