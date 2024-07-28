package com.group4.kidomtoystore.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.group4.kidomtoystore.Adapters.BankAdapter;
import com.group4.kidomtoystore.Models.Bank;
import com.group4.kidomtoystore.Models.PaymentCard;
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.ActivityCardNewBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class CardNew extends AppCompatActivity {

    ActivityCardNewBinding binding;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    Intent intent;
    BankAdapter adapter;
    ArrayList<Bank> banks;
    String bankname;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCardNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Toolbar toolbar = findViewById(R.id.tbCardNew);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        initDataBank();
        loadDataBank();
        addEvents();

        binding.gvBanks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bank selectedBank = banks.get(position);

                bankname = selectedBank.getBankName();
                // Lấy vị trí ngân hàng đã chọn trước đó
                int previousSelectedBankPosition = adapter.getSelectedBankPosition();

                if (previousSelectedBankPosition == position) {
                    adapter.setSelectedBankPosition(-1);
                } else {
                    // Ngược lại, cập nhật trạng thái đã chọn của ngân hàng mới
                    adapter.setSelectedBankPosition(position);
                }
                adapter.notifyDataSetChanged();
            }
        });

        binding.edtCardHolder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!TextUtils.isEmpty(binding.edtCardHolder.getText().toString()) && !TextUtils.isEmpty(binding.edtCardNumber.getText().toString()) && !TextUtils.isEmpty(binding.edtCvv.getText().toString()) && !TextUtils.isEmpty(binding.edtExpireDate.getText().toString())) {
                    binding.btnSave.setBackground(ContextCompat.getDrawable(CardNew.this, R.drawable.button_available));
                }else{
                    binding.btnSave.setBackgroundResource(R.drawable.button_disable);
                }
            }
        });

        binding.edtCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!TextUtils.isEmpty(binding.edtCardNumber.getText().toString()) && !TextUtils.isEmpty(binding.edtCardNumber.getText().toString()) && !TextUtils.isEmpty(binding.edtCvv.getText().toString()) && !TextUtils.isEmpty(binding.edtExpireDate.getText().toString())) {
                    binding.btnSave.setBackground(ContextCompat.getDrawable(CardNew.this, R.drawable.button_available));
                }else{
                    binding.btnSave.setBackgroundResource(R.drawable.button_disable);
                }
            }
        });

        binding.edtExpireDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!TextUtils.isEmpty(binding.edtExpireDate.getText().toString()) && !TextUtils.isEmpty(binding.edtCardNumber.getText().toString()) && !TextUtils.isEmpty(binding.edtCvv.getText().toString()) && !TextUtils.isEmpty(binding.edtExpireDate.getText().toString())) {
                    binding.btnSave.setBackground(ContextCompat.getDrawable(CardNew.this, R.drawable.button_available));
                }else{
                    binding.btnSave.setBackgroundResource(R.drawable.button_disable);
                }
            }
        });

        binding.edtCvv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!TextUtils.isEmpty(binding.edtCardHolder.getText().toString()) && !TextUtils.isEmpty(binding.edtCardNumber.getText().toString()) && !TextUtils.isEmpty(binding.edtCvv.getText().toString()) && !TextUtils.isEmpty(binding.edtExpireDate.getText().toString())) {
                    binding.btnSave.setBackground(ContextCompat.getDrawable(CardNew.this, R.drawable.button_available));
                }else{
                    binding.btnSave.setBackgroundResource(R.drawable.button_disable);
                }
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardHolder = binding.edtCardHolder.getText().toString();
                String cardNumber = binding.edtCardNumber.getText().toString();
                String expireDate = binding.edtExpireDate.getText().toString();
                String cvv = binding.edtCvv.getText().toString();

                // Kiểm tra xem người dùng đã chọn ngân hàng từ GridView chưa
                if (adapter.getSelectedBankPosition() == -1) {
                    Toast.makeText(CardNew.this, "Vui lòng chọn một ngân hàng", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra xem các trường có đầy đủ không
                if (!TextUtils.isEmpty(cardHolder) && !TextUtils.isEmpty(cardNumber) && !TextUtils.isEmpty(expireDate) && !TextUtils.isEmpty(cvv)) {
                    // Tạo một đối tượng Address với các giá trị đã lấy từ các trường EditText
                    PaymentCard card = new PaymentCard(cardHolder, bankname,cardNumber, cvv, expireDate);

                    // Lưu địa chỉ vào cơ sở dữ liệu Firebase
                    reference = FirebaseDatabase.getInstance().getReference("Users");
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        String userID = user.getUid();
                        reference.child(userID).child("PaymentCards").push().setValue(card).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(CardNew.this, "Thêm thẻ thanh toán mới thành công!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CardNew.this, CardManagement.class);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CardNew.this, "Đã xảy ra lỗi. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // Xử lý khi không có người dùng đăng nhập
                    }

                } else {
                    Toast.makeText(CardNew.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addEvents() {
        binding.edtCardNumber.addTextChangedListener(new BankCardNumberFormattingTextWatcher(binding.edtCardNumber));
        binding.edtExpireDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMonthYearPicker(binding.edtExpireDate);
            }
        });
    }

    private void showMonthYearPicker(final TextInputEditText edtExpireDate) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String formattedDate = String.format(Locale.US, "%02d/%d", monthOfYear + 1, year % 100);
                edtExpireDate.setText(formattedDate);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(CardNew.this, dateSetListener, year, month, 1);
        datePickerDialog.show();
    }



    //cardnumber khi nhập 4 số tự động 1 dấu cách
    // Lớp TextWatcher cho số thẻ ngân hàng
    public static class BankCardNumberFormattingTextWatcher implements TextWatcher {
        private final TextInputEditText editText;

        public BankCardNumberFormattingTextWatcher(TextInputEditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}


        @Override
        public void afterTextChanged(Editable s) {
            editText.removeTextChangedListener(this);
            String originalText = s.toString();
            String formattedText = originalText.replaceAll("\\s", "").replaceAll("(\\d{4})(?=\\d)", "$1 "); // Thêm dấu cách sau mỗi 4 ký tự
            editText.setText(formattedText);
            editText.setSelection(formattedText.length()); // Di chuyển con trỏ về cuối chuỗi
            editText.addTextChangedListener(this);
        }
    }



    private void initDataBank() {
        banks = new ArrayList<>();
        banks.add(new Bank(R.drawable.ic_vietcombank,"Vietcombank"));
        banks.add(new Bank(R.drawable.ic_vietinbank,"Vietinbank"));
        banks.add(new Bank(R.drawable.ic_techcombank,"Techcombank"));
        banks.add(new Bank(R.drawable.ic_bidv, "BIDV"));
        banks.add(new Bank(R.drawable.ic_mbbank,"MBbank"));
        banks.add(new Bank(R.drawable.ic_tpbank,"TPbank"));
    }

    private void loadDataBank() {
        adapter = new BankAdapter(CardNew.this, R.layout.item_bank,banks);
        binding.gvBanks.setAdapter(adapter);
    }

}