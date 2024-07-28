package com.group4.kidomtoystore.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.ActivityAddCardInforBinding;

import java.util.Calendar;

public class AddCardInforActivity extends AppCompatActivity {

    ActivityAddCardInforBinding binding;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCardInforBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }


}