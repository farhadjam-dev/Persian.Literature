package com.jamlab.adab;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SaadiActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SaadiAdapter saadiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saadi);

        // تنظیم Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // اضافه کردن دکمه بازگشت

        // اتصال RecyclerView به layout
        recyclerView = findViewById(R.id.recyclerView);

        // تنظیم LayoutManager برای RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ایجاد لیست بخش‌های مربوط به سعدی
        List<SaadiItem> saadiItemList = new ArrayList<>();
        saadiItemList.add(new SaadiItem("زندگی‌نامه", R.drawable.ic_bio));
        saadiItemList.add(new SaadiItem("غزلیات", R.drawable.ic_ghazal));
        saadiItemList.add(new SaadiItem("قصاید", R.drawable.ic_ghaside));

        // ایجاد Adapter و تنظیم آن برای RecyclerView
        saadiAdapter = new SaadiAdapter(saadiItemList);
        recyclerView.setAdapter(saadiAdapter);
    }
}
