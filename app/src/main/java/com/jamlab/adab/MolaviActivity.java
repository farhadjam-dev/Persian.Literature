package com.jamlab.adab;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MolaviActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MolaviAdapter molaviAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_molavi);

        // تنظیم Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // اضافه کردن دکمه بازگشت

        // اتصال RecyclerView به layout
        recyclerView = findViewById(R.id.recyclerView);

        // تنظیم LayoutManager برای RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ایجاد لیست بخش‌های مربوط به مولوی
        List<MolaviItem> molaviItemList = new ArrayList<>();
        molaviItemList.add(new MolaviItem("زندگی‌نامه", R.drawable.ic_bio));
        molaviItemList.add(new MolaviItem("غزلیات", R.drawable.ic_ghazal));
        molaviItemList.add(new MolaviItem("مثنوی", R.drawable.ic_masnavi));

        // ایجاد Adapter و تنظیم آن برای RecyclerView
        molaviAdapter = new MolaviAdapter(molaviItemList);
        recyclerView.setAdapter(molaviAdapter);
    }
}
