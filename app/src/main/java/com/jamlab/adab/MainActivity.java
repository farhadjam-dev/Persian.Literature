package com.jamlab.adab;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.List;

import hafez.HafezActivity;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PoetAdapter poetAdapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private EditText searchField;
    private ImageView searchIcon, favoriteIcon, menuIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        // اتصال Toolbar به layout
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // اتصال عناصر UI
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        searchField = findViewById(R.id.search_field);
        searchIcon = findViewById(R.id.search_icon);
        favoriteIcon = findViewById(R.id.favorite_icon);
        menuIcon = findViewById(R.id.menu_icon);

        // تنظیم RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ایجاد لیست شاعران
        List<Poet> poetList = new ArrayList<>();
        poetList.add(new Poet("حافظ", R.drawable.hafez));
        poetList.add(new Poet("سعدی", R.drawable.saadi));
        poetList.add(new Poet("مولوی", R.drawable.molavi));

        // ایجاد Adapter و تنظیم listener برای کلیک روی آیتم‌ها
        poetAdapter = new PoetAdapter(poetList, new PoetAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Poet poet) {
                // بررسی نام شاعر و انتقال به صفحه مربوطه
                if (poet.getName().equals("حافظ")) {
                    Intent intent = new Intent(MainActivity.this, HafezActivity.class);
                    startActivity(intent);
                } else if (poet.getName().equals("سعدی")) {
                    Intent intent = new Intent(MainActivity.this, SaadiActivity.class);
                    startActivity(intent);
                } else if (poet.getName().equals("مولوی")) {
                    Intent intent = new Intent(MainActivity.this, MolaviActivity.class);
                    startActivity(intent);
                }
            }
        });

        // تنظیم Adapter برای RecyclerView
        recyclerView.setAdapter(poetAdapter);

        // مدیریت کلیک روی آیکون جستجو
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchField.getVisibility() == View.GONE) {
                    // نمایش فیلد جستجو و مخفی کردن آیکون‌های دیگر
                    searchField.setVisibility(View.VISIBLE);
                    favoriteIcon.setVisibility(View.GONE);
                    menuIcon.setVisibility(View.GONE);
                } else {
                    // مخفی کردن فیلد جستجو و نمایش آیکون‌های دیگر
                    searchField.setVisibility(View.GONE);
                    favoriteIcon.setVisibility(View.VISIBLE);
                    menuIcon.setVisibility(View.VISIBLE);
                }
            }
        });

        // مدیریت کلیک روی آیکون علاقه‌مندی‌ها
        favoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
                startActivity(intent);
            }
        });

        // مدیریت کلیک روی آیکون منوی کشویی
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(navigationView);
            }
        });

        // مدیریت کلیک روی آیتم‌های منوی کشویی
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    // بازگشت به صفحه اصلی
                } else if (id == R.id.nav_favorites) {
                    Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_settings) {
                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });
    }
}