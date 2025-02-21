package com.jamlab.adab;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.navigation.NavigationView;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import favorits.FavoritesActivity;
import hafez.HafezActivity;
import search.SearchResultsActivity;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PoetAdapter poetAdapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private EditText searchField;
    private ImageView searchIcon, favoriteIcon, menuIcon;
    private List<Poem> allPoems; // لیست کل اشعار از همه فایل‌ها
    private boolean doubleBackToExitPressedOnce = false;
    private static final int DOUBLE_BACK_DELAY = 2000;
    private boolean isSearchInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        searchField = findViewById(R.id.search_field);
        searchIcon = findViewById(R.id.search_icon);
        favoriteIcon = findViewById(R.id.favorite_icon);
        menuIcon = findViewById(R.id.menu_icon);

        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {}
            @Override public void onDrawerOpened(@NonNull View drawerView) {}
            @Override public void onDrawerClosed(@NonNull View drawerView) {}
            @Override public void onDrawerStateChanged(int newState) {}
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Poet> poetList = new ArrayList<>();
        poetList.add(new Poet("حافظ", R.drawable.hafez));
        poetList.add(new Poet("سعدی", R.drawable.saadi));
        poetList.add(new Poet("مولوی", R.drawable.molavi));

        poetAdapter = new PoetAdapter(poetList, poet -> {
            if (poet.getName().equals("حافظ")) {
                startActivity(new Intent(MainActivity.this, HafezActivity.class));
            } else if (poet.getName().equals("سعدی")) {
                startActivity(new Intent(MainActivity.this, SaadiActivity.class));
            } else if (poet.getName().equals("مولوی")) {
                startActivity(new Intent(MainActivity.this, MolaviActivity.class));
            }
        });

        favoriteIcon.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, FavoritesActivity.class)));
        recyclerView.setAdapter(poetAdapter);

        // بارگذاری همه اشعار از فایل‌های JSON
        loadAllPoems();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_favorites) {
                startActivity(new Intent(MainActivity.this, FavoritesActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (id == R.id.nav_settings) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            return true;
        });

        searchIcon.setOnClickListener(v -> {
            LinearLayout searchLayout = findViewById(R.id.search_layout);
            if (searchLayout.getVisibility() == View.GONE) {
                Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in);
                searchLayout.setVisibility(View.VISIBLE);
                searchLayout.startAnimation(slideIn);
                Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation) {
                        favoriteIcon.setVisibility(View.GONE);
                        menuIcon.setVisibility(View.GONE);
                    }
                    @Override public void onAnimationRepeat(Animation animation) {}
                });
                favoriteIcon.startAnimation(fadeOut);
                menuIcon.startAnimation(fadeOut);
            } else {
                performSearchAndNavigate();
            }
        });

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            LinearLayout searchLayout = findViewById(R.id.search_layout);
            if (searchLayout.getVisibility() == View.VISIBLE) {
                Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out);
                searchLayout.startAnimation(slideOut);
                searchLayout.setVisibility(View.GONE);
                Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
                favoriteIcon.setVisibility(View.VISIBLE);
                menuIcon.setVisibility(View.VISIBLE);
                favoriteIcon.startAnimation(fadeIn);
                menuIcon.startAnimation(fadeIn);
                searchField.setText("");
            }
        });

        searchField.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int padding = searchField.getPaddingLeft();
                int iconWidth = searchField.getCompoundDrawables()[0].getBounds().width();
                float touchX = event.getX();
                if (touchX <= (padding + iconWidth) && !isSearchInProgress) {
                    performSearchAndNavigate();
                    return true;
                }
            }
            return false;
        });

        searchField.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) || actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                if (!isSearchInProgress) {
                    performSearchAndNavigate();
                }
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LinearLayout searchLayout = findViewById(R.id.search_layout);
        if (searchLayout.getVisibility() == View.VISIBLE) {
            Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out);
            searchLayout.startAnimation(slideOut);
            searchLayout.setVisibility(View.GONE);
            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            favoriteIcon.setVisibility(View.VISIBLE);
            menuIcon.setVisibility(View.VISIBLE);
            favoriteIcon.startAnimation(fadeIn);
            menuIcon.startAnimation(fadeIn);
            searchField.setText("");
        }
    }

    // بارگذاری همه اشعار از فایل‌های JSON
    private void loadAllPoems() {
        allPoems = new ArrayList<>();
        int[] rawFiles = {
                R.raw.hafez_ghazals,
                R.raw.hafez_rubaiyat,
                R.raw.hafez_ghitaat,
                R.raw.hafez_ghasaid,
                R.raw.hafez_masnavi,
                R.raw.hafez_saghinameh,
                R.raw.hafez_montasab
                // اگه فایل‌های سعدی یا مولوی دارید، اینجا اضافه کنید
                // مثلاً: R.raw.saadi_ghazals, R.raw.molavi_masnavi
        };

        for (int resId : rawFiles) {
            try {
                InputStream inputStream = getResources().openRawResource(resId);
                List<Poem> poems = PoemLoader.loadPoemsFromJson(inputStream);
                allPoems.addAll(poems);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void performSearchAndNavigate() {
        if (isSearchInProgress) return;

        String query = searchField.getText().toString().trim();
        if (!query.isEmpty()) {
            isSearchInProgress = true;
            List<Poem> searchResults = performSearch(allPoems, query);
            Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);
            intent.putParcelableArrayListExtra("search_results", new ArrayList<>(searchResults));
            intent.putExtra("search_query", query);
            startActivity(intent);
            // ریست کردن قفل بعد از تأخیر کوتاه
            new Handler(Looper.getMainLooper()).postDelayed(() -> isSearchInProgress = false, 500);

            // مخفی کردن نوار جست‌وجو پس از جست‌وجو
            LinearLayout searchLayout = findViewById(R.id.search_layout);
            Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out);
            searchLayout.startAnimation(slideOut);
            searchLayout.setVisibility(View.GONE);
            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            favoriteIcon.setVisibility(View.VISIBLE);
            menuIcon.setVisibility(View.VISIBLE);
            favoriteIcon.startAnimation(fadeIn);
            menuIcon.startAnimation(fadeIn);
            searchField.setText("");
        }
    }

    private List<Poem> performSearch(List<Poem> poems, String query) {
        List<Poem> results = new ArrayList<>();
        String normalizedQuery = normalizeText(query);
        List<String> queryParts = Arrays.asList(normalizedQuery.split("\\s+")); // تقسیم با فاصله

        for (Poem poem : poems) {
            String normalizedPoemText = normalizeText(poem.getText());
            boolean matchesAll = true;
            for (String part : queryParts) {
                if (!normalizedPoemText.contains(part)) {
                    matchesAll = false;
                    break;
                }
            }
            if (matchesAll) {
                results.add(poem);
            }
        }
        return results;
    }

    private String normalizeText(String text) {
        return text.replaceAll("[\\u064B-\\u065F]", ""); // حذف تمام اعراب یونی‌کد
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "برای خروج دو بار ضربه بزنید", Toast.LENGTH_SHORT).show();
            new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false, DOUBLE_BACK_DELAY);
        }
    }
}