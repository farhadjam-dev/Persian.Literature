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
    private List<String> favoritePoems; // لیست شعرهای علاقه‌مندی
    private TextView emptyTextView; // نمایش پیام وقتی لیست خالی است

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // تنظیم Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // اضافه کردن دکمه بازگشت

        // اتصال RecyclerView
        recyclerView = findViewById(R.id.favorites_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // اتصال TextView برای نمایش پیام خالی بودن لیست
        emptyTextView = findViewById(R.id.empty_text_view);

        // بارگذاری شعرهای علاقه‌مندی از SharedPreferences
        favoritePoems = loadFavoritePoems();

        // بررسی وضعیت لیست علاقه‌مندی‌ها
        if (favoritePoems.isEmpty()) {
            recyclerView.setVisibility(View.GONE); // مخفی کردن RecyclerView
            emptyTextView.setVisibility(View.VISIBLE); // نمایش پیام خالی بودن لیست
        } else {
            recyclerView.setVisibility(View.VISIBLE); // نمایش RecyclerView
            emptyTextView.setVisibility(View.GONE); // مخفی کردن پیام خالی بودن لیست
        }

        // تنظیم Adapter برای RecyclerView
        favoriteAdapter = new FavoriteAdapter(favoritePoems);
        recyclerView.setAdapter(favoriteAdapter);
    }

    // بارگذاری شعرهای علاقه‌مندی از SharedPreferences
    private List<String> loadFavoritePoems() {
        SharedPreferences sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE);
        Set<String> favoritesSet = sharedPreferences.getStringSet("favorites", new HashSet<>());
        return new ArrayList<>(new HashSet<>(favoritesSet)); // اطمینان از عدم تغییر مجموعه
    }
}