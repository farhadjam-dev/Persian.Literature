package com.jamlab.adab;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import android.content.SharedPreferences;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FavoriteAdapter favoriteAdapter;
    private List<String> favoritePoems;
    private TextView emptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // تنظیم Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // اتصال RecyclerView
        recyclerView = findViewById(R.id.favorites_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // اتصال TextView برای پیام خالی بودن
        emptyTextView = findViewById(R.id.empty_text_view);

        // بارگذاری شعرهای علاقه‌مندی
        favoritePoems = loadFavoritePoems();

        // بررسی وضعیت لیست
        if (favoritePoems.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.GONE);
        }

        // تنظیم آداپتر با ارسال Context
        favoriteAdapter = new FavoriteAdapter(this, favoritePoems);
        recyclerView.setAdapter(favoriteAdapter);
    }

    // بارگذاری شعرهای علاقه‌مندی از SharedPreferences
    private List<String> loadFavoritePoems() {
        SharedPreferences sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE);
        Set<String> favoritesSet = sharedPreferences.getStringSet("favorites", new HashSet<>());
        return new ArrayList<>(favoritesSet); // تبدیل به لیست
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // بستن اکتیویتی با کلیک روی دکمه بازگشت
        return true;
    }
}