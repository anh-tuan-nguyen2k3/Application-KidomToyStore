package com.group4.kidomtoystore.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.group4.kidomtoystore.Adapters.BannerHomeAdapter;
import com.group4.kidomtoystore.Adapters.CategoryAdapterRecycle;
import com.group4.kidomtoystore.Adapters.ProductAdapter;
import com.group4.kidomtoystore.Adapters.ProductSaleAdapter;
import com.group4.kidomtoystore.Models.BannerHome;
import com.group4.kidomtoystore.Models.Category;
import com.group4.kidomtoystore.Models.Product;
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.ActivityHomePageFullBinding;

import java.util.ArrayList;


public class HomePageFull extends BaseActivity {

    ActivityHomePageFullBinding binding;
    CategoryAdapterRecycle adapter;
    ArrayList<Category> categoryList;
    ArrayList<BannerHome> bannerLists;
    BannerHomeAdapter adapter2;
    ProductAdapter adapterproduct;
    ProductSaleAdapter adaptersale;
    private static final int AUTO_SCROLL_DELAY = 2000; // Độ trễ giữa các lần chuyển đổi banner (ms)
    private int currentPage, currentPage1 = 0;
    private Handler handler, handler1;
    private Runnable runnable, runnable1;
    BottomNavigationView bottomNavigationView;
    ArrayList<Product> listRcm;
    private boolean isLoading = false;

    int k = 0;


    @Override
    protected void onStart() {
        super.onStart();
        handler.postDelayed(runnable, AUTO_SCROLL_DELAY);
        handler1.postDelayed(runnable1, AUTO_SCROLL_DELAY);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
        handler1.removeCallbacks(runnable1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePageFullBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        binding.bottomMenu.setSelectedItemId(R.id.mnHome);
        binding.bottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.mnHome  && !(getApplicationContext() instanceof HomePageFull)){
                    startActivity(new Intent(getApplicationContext(), HomePageFull.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if (id == R.id.mnSearch  && !(getApplicationContext() instanceof SearchTypeActivity)){
                    startActivity(new Intent(getApplicationContext(), SearchTypeActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if (id == R.id.mnCart  && !(getApplicationContext() instanceof CartActivity)){
                    startActivity(new Intent(getApplicationContext(), CartActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if (id == R.id.mnOrder  && !(getApplicationContext() instanceof OrderManagementActivity)){
                    startActivity(new Intent(getApplicationContext(), OrderManagementActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if (id == R.id.mnProfile  && !(getApplicationContext() instanceof AccountSettingActivity)){
                    startActivity(new Intent(getApplicationContext(), AccountSettingActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });



        initData();
        loadData();
        loadLayoutRecycleView();
        initProductRcm();
        initProductSale();
        addEvents();
        handler = new Handler();
        handler1 = new Handler();


    }

    private void addEvents() {
        binding.edtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Intent intent = new Intent(HomePageFull.this, SearchTypeActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    // Bỏ focus khỏi EditText sau khi chuyển trang
                    view.clearFocus();
                }
            }
        });

        binding.imvIconNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageFull.this, NotificationActivity.class);
                startActivity(intent);
            }
        });
        binding.txtRcmAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageFull.this, CategoryActivity.class);
                intent.putExtra("rcmProduct", "Đề xuất");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                HomePageFull.this.startActivity(intent);
            }
        });
    }

    private void initProductSale() {
        DatabaseReference myRef = database.getReference("Product");
        binding.progressBar.setVisibility(View.VISIBLE);
        ArrayList<Product> list = new ArrayList<>();
        Query query = myRef.orderByChild("flashsale").equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue: snapshot.getChildren()){
                        list.add(issue.getValue(Product.class));
                    }
                    if (list.size() > 0) {
                        binding.rvFlashSaleProduct.setLayoutManager(new LinearLayoutManager(HomePageFull.this, LinearLayoutManager.HORIZONTAL, false));
                        adaptersale = new ProductSaleAdapter(list,getResources());
                        binding.rvFlashSaleProduct.setAdapter(adaptersale);
                        adaptersale.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Product product) {
                                Intent intent = new Intent(HomePageFull.this, ProductDetailActivity.class);
                                intent.putExtra("productId", product.getId());
                                startActivity(intent);
                            }
                        });


                        runnable1 = new Runnable() {
                            public void run() {
                                if (currentPage1 == adaptersale.getItemCount()) {
                                    currentPage1 = 0;
                                }
                                binding.rvFlashSaleProduct.smoothScrollToPosition(currentPage1++);
                                handler1.postDelayed(runnable1, AUTO_SCROLL_DELAY);
                            }
                        };
                        handler1.postDelayed(runnable1, AUTO_SCROLL_DELAY);
                    }
                    binding.progressbarFlashsale.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


//    private void initProductRcm() {
//        DatabaseReference myRef = database.getReference("Product");
//        binding.progressBar.setVisibility(View.VISIBLE);
//        listRcm = new ArrayList<>();
//        Query query = myRef.orderByChild("recommend").equalTo(true);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    for (DataSnapshot issue: snapshot.getChildren()){
//                        listRcm.add(issue.getValue(Product.class));
//                    }
//
//                    if (listRcm.size()>0) {
//                        binding.rvRecommendProduct.setLayoutManager(new GridLayoutManager(HomePageFull.this,2));
//                        adapterproduct = new ProductAdapter(listRcm);
//                        adapterproduct.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(Product product) {
//                                Intent intent = new Intent(HomePageFull.this, ProductDetailActivity.class);
//                                intent.putExtra("productId", product.getId());
//                                startActivity(intent);
//
//                            }
//                        });
//                        binding.rvRecommendProduct.setAdapter(adapterproduct);
//                        adapterproduct.notifyDataSetChanged();
//
//                    }
//                    binding.progressBar.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
private void initProductRcm() {
    DatabaseReference myRef = database.getReference("Product");
//    binding.progressBar.setVisibility(View.VISIBLE);
    listRcm = new ArrayList<>();
    Query query = myRef.orderByChild("recommend").equalTo(true);
    query.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                for (DataSnapshot issue : snapshot.getChildren()) {
                        listRcm.add(issue.getValue(Product.class));
                    }
                if (listRcm.size()>0) {
                    k =6;
                    ArrayList<Product> firstSixProducts = new ArrayList<>();
                    for (int i = 0; i < Math.min(6, listRcm.size()); i++) {
                        firstSixProducts.add(listRcm.get(i));
                    }

                    binding.rvRecommendProduct.setLayoutManager(new GridLayoutManager(HomePageFull.this, 2));
                    adapterproduct = new ProductAdapter(firstSixProducts);

                    adapterproduct.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Product product) {
                            Intent intent = new Intent(HomePageFull.this, ProductDetailActivity.class);
                            intent.putExtra("productId", product.getId());
                            startActivity(intent);

                        }
                    });
                    binding.rvRecommendProduct.setAdapter(adapterproduct);
                    adapterproduct.notifyDataSetChanged();

                }
                binding.progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
    binding.rvRecommendProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            // Kiểm tra xem RecyclerView đã đang ở cuối danh sách hay không
            GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

            // Nếu RecyclerView đã đến cuối danh sách và không có dữ liệu nào đang được tải
            if (lastVisibleItemPosition == totalItemCount - 1 && !isLoading) {
                // Gọi phương thức loadMoreData() để tải thêm dữ liệu
                loadMoreData();
            }
        }
    });
}
    private void loadMoreData() {
        isLoading = true; // Đánh dấu rằng dữ liệu đang được tải
        // Hiển thị ProgressBar hoặc thông báo khác để cho người dùng biết rằng dữ liệu đang được tải
        binding.progressBarload.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int currentSize = adapterproduct.getItemCount();
                int nextLimit = currentSize + 6;
                int count = 0;
                for (int i = currentSize; i < nextLimit && k < listRcm.size(); i++, k++) {
                    adapterproduct.addItem(listRcm.get(k));
                    count++;
                }
                adapterproduct.notifyDataSetChanged();
                isLoading = false;
                // Ẩn ProgressBar sau khi tải dữ liệu
                binding.progressBarload.setVisibility(View.GONE);
                // Nếu không có dữ liệu để tải thêm, bạn có thể hiển thị một thông báo để cho người dùng biết
                // Ví dụ: if (count == 0) showNoMoreDataMessage();
            }
        }, 3000); // Thời gian giả lập tải dữ liệu, thay đổi tùy theo nhu cầu của bạn
    }


    private void loadLayoutRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        binding.rvCategory.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        binding.rvBanner.setLayoutManager(layoutManager2);

        // Khởi tạo Handler và Runnable

        runnable = new Runnable() {
            public void run() {
                if (currentPage == bannerLists.size()) {
                    currentPage = 0;
                }
                binding.rvBanner.smoothScrollToPosition(currentPage++);
                handler.postDelayed(runnable, AUTO_SCROLL_DELAY);
            }
        };
//        handler.postDelayed(runnable, AUTO_SCROLL_DELAY);
    }
    private void initData() {
        categoryList = new ArrayList<>();
        categoryList.add(new Category(R.drawable.img_category_bupbe,"Búp bê"));
        categoryList.add(new Category(R.drawable.img_category_hoatrang,"Hoá trang"));
        categoryList.add(new Category(R.drawable.img_category_mythuat,"Mỹ thuật"));
        categoryList.add(new Category(R.drawable.img_category_ngoaitroi,"Ngoài trời"));
        categoryList.add(new Category(R.drawable.img_category_thethao,"Thể thao"));
        categoryList.add(new Category(R.drawable.img_category_tritue,"Trí tuệ"));


        bannerLists = new ArrayList<>();
        bannerLists.add(new BannerHome(R.drawable.img_bannerhome4));
        bannerLists.add(new BannerHome(R.drawable.img_bannerhome5));
        bannerLists.add(new BannerHome(R.drawable.img_banner));
        bannerLists.add(new BannerHome(R.drawable.img_banner_6));


    }

    private void loadData() {
        adapter = new CategoryAdapterRecycle(getApplicationContext(), categoryList);
        binding.rvCategory.setAdapter(adapter);

        adapter2 = new BannerHomeAdapter(getApplicationContext(), bannerLists);
        binding.rvBanner.setAdapter(adapter2);
    }


}