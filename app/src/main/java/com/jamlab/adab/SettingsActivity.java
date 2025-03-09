package com.jamlab.adab;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // تنظیم تولبار
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false); // فلش بازگشت غیرفعال
            getSupportActionBar().setDisplayShowTitleEnabled(false); // غیرفعال کردن عنوان پیش‌فرض
        }

        // تنظیم عنوان در TextView داخل تولبار
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        if (toolbarTitle != null) {
            toolbarTitle.setText("تنظیمات");
            toolbarTitle.setVisibility(View.VISIBLE);
        }

        // تنظیم پیام موقت
        TextView emptyTextView = findViewById(R.id.empty_text_view);
        emptyTextView.setText("به زودی این صفحه طراحی می‌گردد");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}