package com.group4.kidomtoystore.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.group4.kidomtoystore.Adapters.ProductAdapter;
import com.group4.kidomtoystore.Adapters.ProductSaleAdapter;
import com.group4.kidomtoystore.Models.Product;
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.ActivityCategoryBinding;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Pattern;

public class CategoryActivity extends BaseActivity {

    ActivityCategoryBinding binding;
    private RecyclerView.Adapter adatperListProduct;
    private String categoryName;
    private String searchText, rcmProduct;
    private boolean isSearch;
    ProductAdapter adapterProduct;
    ArrayList<Product> list;

    private enum SortOrder {
        DEFAULT, // Mặc định
        BEST_SELL, // Số lượng bán ra từ cao xuống thấp
    }
    private enum SortRate {
        DEFAULT,
        ASCENDING,
        DESCENDING
    }
    private enum SortPrice {
        DEFAULT,
        ASCENDING,
        DESCENDING
    }
    private SortOrder currentSortOrder = SortOrder.DEFAULT;
    private SortPrice currentSortPrice = SortPrice.DEFAULT;
    private SortRate currentSortRate = SortRate.DEFAULT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        getIntentExtra();
        initList();
        addEvents();
    }

    private void addEvents() {
        binding.imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.bestSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.size() > 0) {
                    if (currentSortOrder == SortOrder.BEST_SELL) {
                        // Nếu đang ở trạng thái sắp xếp theo số lượng bán ra, chuyển về trạng thái mặc định
                        currentSortOrder = SortOrder.DEFAULT;
                        // Reset lại giao diện và hiển thị danh sách sản phẩm theo trạng thái mặc định
                        resetSortUI();
                        Collections.shuffle(list);
                        adapterProduct.notifyDataSetChanged();
                    } else {
                        // Nếu đang ở trạng thái mặc định, chuyển sang sắp xếp theo số lượng bán ra
                        currentSortOrder = SortOrder.BEST_SELL;
                        // Thay đổi giao diện và hiển thị danh sách sản phẩm theo số lượng bán ra
                        sortProductListByBestSell();
                    }

                }

            }
        });
        binding.btnPriceDropList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.size() > 0) {
                    // Kiểm tra trạng thái hiện tại của sắp xếp và thay đổi nó
                    switch (currentSortPrice) {
                        case DEFAULT:
                            // Nếu là trạng thái mặc định, sắp xếp từ thấp đến cao
                            sortProductListAscending();
                            currentSortPrice = SortPrice.ASCENDING;
                            binding.btnPriceDropList.setBackgroundResource(R.drawable.input_field_chosen);
                            binding.imvArrow.setImageResource(R.drawable.ic_arrow_up_red);

                            break;
                        case ASCENDING:
                            // Nếu là sắp xếp từ thấp đến cao, sắp xếp từ cao đến thấp
                            sortProductListDescending();
                            currentSortPrice = SortPrice.DESCENDING;
                            binding.imvArrow.setImageResource(R.drawable.ic_arrow_down_red);

                            break;
                        case DESCENDING:
                            // Nếu là sắp xếp từ cao đến thấp, chuyển về trạng thái mặc định
                            sortProductListDefault();
                            currentSortPrice = SortPrice.DEFAULT;
                            binding.btnPriceDropList.setBackgroundResource(R.drawable.border_red);
                            binding.imvArrow.setImageResource(R.drawable.ic_arrow_swap);
                            break;
                    }
                }


            }
        });
        binding.btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.size() > 0) {
                    // Kiểm tra trạng thái hiện tại của sắp xếp và thay đổi nó
                    switch (currentSortRate) {
                        case DEFAULT:
                            // Nếu là trạng thái mặc định, sắp xếp từ thấp đến cao
                            sortProductRateAscending();
                            currentSortRate = SortRate.ASCENDING;
                            binding.btnRate.setBackgroundResource(R.drawable.input_field_chosen);
                            binding.imvArrowRate.setImageResource(R.drawable.ic_arrow_up_red);
                            break;
                        case ASCENDING:
                            // Nếu là sắp xếp từ thấp đến cao, sắp xếp từ cao đến thấp
                            sortProductRateDescending();
                            currentSortRate = SortRate.DESCENDING;
//                            binding.btnRate.setBackgroundResource(R.drawable.button_border_chosen_double);
                            binding.imvArrowRate.setImageResource(R.drawable.ic_arrow_down_red);

                            break;
                        case DESCENDING:
                            // Nếu là sắp xếp từ cao đến thấp, chuyển về trạng thái mặc định
                            sortProductRateDefault();
                            currentSortRate = SortRate.DEFAULT;
                            binding.btnRate.setBackgroundResource(R.drawable.border_red);
                            binding.imvArrowRate.setImageResource(R.drawable.ic_arrow_swap);
                            break;
                    }
                }
            }
        });
    }
    private void sortProductListAscending() {
        // Sắp xếp danh sách sản phẩm từ thấp đến cao (ví dụ: giá từ thấp đến cao)
        Collections.sort(list, new Comparator<Product>() {
            @Override
            public int compare(Product p1, Product p2) {
                // Lấy giá sau khi giảm giá của từng sản phẩm
                double discountedPrice1 = p1.getPrice() - (p1.getPrice() * (p1.getSale() / 100.0));
                double discountedPrice2 = p2.getPrice() - (p2.getPrice() * (p2.getSale() / 100.0));
                // So sánh giá sau khi giảm giá của hai sản phẩm
                return Double.compare(discountedPrice1, discountedPrice2);
            }
        });
        // Cập nhật Adapter với danh sách sản phẩm đã được sắp xếp
        adapterProduct.notifyDataSetChanged();
    }
    private void sortProductListDescending() {
        // Sắp xếp danh sách sản phẩm từ cao đến thấp (ví dụ: giá từ cao đến thấp)
        Collections.sort(list, new Comparator<Product>() {
            @Override
            public int compare(Product p1, Product p2) {
                double discountedPrice1 = p1.getPrice() - (p1.getPrice() * (p1.getSale() / 100.0));
                double discountedPrice2 = p2.getPrice() - (p2.getPrice() * (p2.getSale() / 100.0));
                // So sánh giá sau khi giảm giá của hai sản phẩm
                return Double.compare(discountedPrice2, discountedPrice1);
            }
        });
        // Cập nhật Adapter với danh sách sản phẩm đã được sắp xếp
        adapterProduct.notifyDataSetChanged();
    }
    private void sortProductListDefault() {
        Collections.shuffle(list);
        adapterProduct.notifyDataSetChanged();
    }
    private void sortProductRateAscending() {
        Collections.sort(list, new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                double rate1 = o1.getStar();
                double rate2 = o2.getStar();
                return Double.compare(rate1,rate2);
            }
        });
        adapterProduct.notifyDataSetChanged();
    }
    private void sortProductRateDescending() {
        Collections.sort(list, new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                double rate1 = o1.getStar();
                double rate2 = o2.getStar();
                return Double.compare(rate2,rate1);
            }
        });
        adapterProduct.notifyDataSetChanged();

    }
    private void sortProductRateDefault() {
        Collections.shuffle(list);
        adapterProduct.notifyDataSetChanged();
    }
    private void resetSortUI() {
        // Thay đổi lại background của LinearLayout bestSell và ảnh của ImageView imvIconStar về trạng thái mặc định
        binding.bestSell.setBackgroundResource(R.drawable.border_red);
        binding.imvIconStar.setImageResource(R.drawable.ic_star_blank);
    }
    private void sortProductListByBestSell() {
        // Sắp xếp danh sách sản phẩm theo số lượng bán ra từ cao xuống thấp
        Collections.sort(list, new Comparator<Product>() {
            @Override
            public int compare(Product p1, Product p2) {
                // So sánh số lượng bán ra
                return Integer.compare(p2.getStock(), p1.getStock());
            }
        });

        // Cập nhật Adapter với danh sách sản phẩm đã được sắp xếp
        adapterProduct.notifyDataSetChanged();
        // Thay đổi giao diện khi đang ở trạng thái sắp xếp theo số lượng bán ra
        binding.bestSell.setBackgroundResource(R.drawable.input_field_chosen);
        binding.imvIconStar.setImageResource(R.drawable.ic_star_full);
    }



    private void getIntentExtra() {
        categoryName=getIntent().getStringExtra("categoryName");
        searchText = getIntent().getStringExtra("text");
        isSearch = getIntent().getBooleanExtra("isSearch", false);
        rcmProduct = getIntent().getStringExtra("rcmProduct");
    }
    private void initList() {
        DatabaseReference myRef = database.getReference("Product");
        list = new ArrayList<>();

        Query query;

        if (isSearch) {
            query = myRef.orderByChild("name"); // Sắp xếp theo tên sản phẩm
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        for (DataSnapshot issue: snapshot.getChildren()){
                            Product product = issue.getValue(Product.class);
                            if (product != null && containsIgnoreCaseAndAccent(product.getName(), searchText)
                                    || containsIgnoreCaseAndAccent(product.getBrand(), searchText)) {
                                list.add(product);
                            }
                        }
                        if (list.size() > 0){
                            binding.rvProduct.setLayoutManager(new GridLayoutManager(CategoryActivity.this,2));
                            adapterProduct = new ProductAdapter(list);
                            binding.rvProduct.setAdapter(adapterProduct);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (rcmProduct != null && !rcmProduct.isEmpty()) {
            //Set tên của category
            binding.txtCategoryName.setText(rcmProduct);
            // Trường hợp load sản phẩm theo rcmProduct
            query = myRef.orderByChild("recommend").equalTo(true);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot issue : snapshot.getChildren()) {
                            list.add(issue.getValue(Product.class));
                        }
                        if (list.size() > 0) {
                            binding.rvProduct.setLayoutManager(new GridLayoutManager(CategoryActivity.this, 2));
                            adapterProduct = new ProductAdapter(list);
                            binding.rvProduct.setAdapter(adapterProduct);
                            adapterProduct.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(Product product) {
                                    Intent intent = new Intent(CategoryActivity.this, ProductDetailActivity.class);
                                    intent.putExtra("productId", product.getId());
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            //Set tên Category
            binding.txtCategoryName.setText(categoryName);
            query = myRef.orderByChild("category").equalTo(categoryName);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        for (DataSnapshot issue: snapshot.getChildren()){
                            list.add(issue.getValue(Product.class));
                        }
                        if (list.size() > 0) {
                            binding.rvProduct.setLayoutManager(new GridLayoutManager(CategoryActivity.this,2));
                            adapterProduct = new ProductAdapter(list);
                            binding.rvProduct.setAdapter(adapterProduct);
                            adapterProduct.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(Product product) {
                                    Intent intent = new Intent(CategoryActivity.this, ProductDetailActivity.class);
                                    intent.putExtra("productId", product.getId());
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    // Hàm kiểm tra xem một chuỗi có chứa một từ khóa (không phân biệt chữ hoa chữ thường và không phân biệt dấu) không
    private boolean containsIgnoreCaseAndAccent(String source, String keyword) {
        // Chuẩn hóa cả từ khóa và chuỗi dữ liệu
        String normalizedSource = removeAccent(source).toLowerCase();
        String normalizedKeyword = removeAccent(keyword).toLowerCase();
        return normalizedSource.contains(normalizedKeyword);
    }

    // Hàm loại bỏ dấu từ một chuỗi
    private String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        return temp.replaceAll("\\p{M}", "").toLowerCase();
    }


}