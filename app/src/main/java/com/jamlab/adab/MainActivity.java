package com.jamlab.adab;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.AppCompatImageView;
import com.google.android.material.navigation.NavigationView;
import java.io.InputStream;
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
    private List<Poem> poems; // لیست اشعار با اطلاعات کامل

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // اتصال Toolbar به layout
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // حذف عنوان از Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false); // غیرفعال کردن نمایش عنوان
        }

        // اتصال عناصر UI
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        searchField = findViewById(R.id.search_field);
        searchIcon = findViewById(R.id.search_icon);
        favoriteIcon = findViewById(R.id.favorite_icon);
        menuIcon = findViewById(R.id.menu_icon);

        // مدیریت کلیک روی آیکون منوی کشویی
        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // مدیریت باز شدن صفحه اصلی وقتی منو بسته می‌شود
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {}

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {}

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                // عملیاتی که باید بعد از بستن منو انجام شود
            }

            @Override
            public void onDrawerStateChanged(int newState) {}
        });

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

        // مدیریت کلیک روی آیکون علاقه‌مندی‌ها
        favoriteIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });

        // تنظیم Adapter برای RecyclerView
        recyclerView.setAdapter(poetAdapter);

        // خواندن داده‌ها از فایل JSON
        InputStream inputStream = getResources().openRawResource(R.raw.hafez_ghazals);
        poems = PoemLoader.loadPoemsFromJson(inputStream);

        // مدیریت انتخاب گزینه‌های منوی کشویی
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            // بررسی گزینه انتخابی
            if (id == R.id.nav_favorites) {
                // انتقال به صفحه علاقه‌مندی‌ها
                Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START); // بستن منو بعد از انتخاب
            } else if (id == R.id.nav_settings) {
                // انتقال به صفحه تنظیمات
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START); // بستن منو بعد از انتخاب
            }

            return true; // نشان می‌دهد که گزینه پردازش شده است
        });

        // مدیریت کلیک روی آیکون جست‌وجو
        searchIcon.setOnClickListener(v -> {
            LinearLayout searchLayout = findViewById(R.id.search_layout);
            if (searchLayout.getVisibility() == View.GONE) {
                // نمایش فیلد جست‌جو با انیمیشن Slide-In
                Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in);
                searchLayout.setVisibility(View.VISIBLE);
                searchLayout.startAnimation(slideIn);

                // محو کردن آیکون‌های دیگر با انیمیشن Fade-Out
                Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        favoriteIcon.setVisibility(View.GONE);
                        menuIcon.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
                favoriteIcon.startAnimation(fadeOut);
                menuIcon.startAnimation(fadeOut);
            }
        });

        // مدیریت کلیک روی دکمه برگشت
        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            LinearLayout searchLayout = findViewById(R.id.search_layout);
            if (searchLayout.getVisibility() == View.VISIBLE) {
                // مخفی کردن فیلد جست‌جو با انیمیشن Slide-Out
                Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out);
                searchLayout.startAnimation(slideOut);
                searchLayout.setVisibility(View.GONE);

                // نمایش آیکون‌های دیگر با انیمیشن Fade-In
                Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
                favoriteIcon.setVisibility(View.VISIBLE);
                menuIcon.setVisibility(View.VISIBLE);
                favoriteIcon.startAnimation(fadeIn);
                menuIcon.startAnimation(fadeIn);

                // پاک کردن متن جست‌جو
                searchField.setText("");
            }
        });

        // مدیریت کلیک روی آیکون داخل فیلد جست‌جو
        searchField.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int padding = searchField.getPaddingLeft(); // فاصله از سمت چپ
                int iconWidth = searchField.getCompoundDrawables()[0].getBounds().width(); // عرض آیکون
                float touchX = event.getX(); // مختصات X کلیک
                if (touchX <= (padding + iconWidth)) {
                    performSearchAndNavigate();
                    return true;
                }
            }
            return false;
        });

        // مدیریت کلید Enter در کیبورد
        searchField.setOnEditorActionListener((v, actionId, event) -> {
            performSearchAndNavigate();
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // بررسی وضعیت فیلد جست‌جو و مخفی کردن آن در صورت نمایش
        LinearLayout searchLayout = findViewById(R.id.search_layout);
        if (searchLayout.getVisibility() == View.VISIBLE) {
            Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out);
            searchLayout.startAnimation(slideOut);
            searchLayout.setVisibility(View.GONE); // مخفی کردن فیلد جست‌جو

            // نمایش آیکون‌های دیگر با انیمیشن Fade-In
            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            favoriteIcon.setVisibility(View.VISIBLE);
            menuIcon.setVisibility(View.VISIBLE);
            favoriteIcon.startAnimation(fadeIn);
            menuIcon.startAnimation(fadeIn);

            // پاک کردن متن جست‌جو
            searchField.setText("");
        }
    }

    private void performSearchAndNavigate() {
        String query = searchField.getText().toString().trim();
        if (!query.isEmpty()) {
            List<Poem> searchResults = performSearch(poems, query);
            // انتقال نتایج جست‌وجو به صفحه جدید
            Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);
            intent.putParcelableArrayListExtra("search_results", new ArrayList<>(searchResults));
            startActivity(intent);
        }
    }

    private List<Poem> performSearch(List<Poem> poems, String query) {
        List<Poem> results = new ArrayList<>();
        for (Poem poem : poems) {
            if (poem.getText().contains(query)) {
                results.add(poem);
            }
        }
        return results;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}