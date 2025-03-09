package favorits;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jamlab.adab.Poem;
import com.jamlab.adab.PoemLoader;
import com.jamlab.adab.R;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FavoriteAdapter favoriteAdapter;
    private List<Poem> favoritePoems; // لیست اشعار علاقه‌مندی
    private TextView emptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // تنظیم Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // اتصال RecyclerView
        recyclerView = findViewById(R.id.favorites_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // اتصال TextView برای پیام خالی بودن
        emptyTextView = findViewById(R.id.empty_text_view);

        // بارگذاری اشعار علاقه‌مندی
        favoritePoems = loadFavoritePoems();

        // تنظیم آداپتر
        favoriteAdapter = new FavoriteAdapter(this, favoritePoems);
        recyclerView.setAdapter(favoriteAdapter);

        // بررسی وضعیت لیست
        updateFavoritesVisibility();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // به‌روزرسانی لیست علاقه‌مندی‌ها هنگام بازگشت به این اکتیویتی
        favoritePoems = loadFavoritePoems();
        favoriteAdapter.updateFavorites(favoritePoems);
        updateFavoritesVisibility();
    }

    // به‌روزرسانی نمایش بر اساس وضعیت لیست
    private void updateFavoritesVisibility() {
        if (favoritePoems.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.GONE);
        }
    }

    // بارگذاری اشعار علاقه‌مندی
    private List<Poem> loadFavoritePoems() {
        // بارگذاری عناوین علاقه‌مندی‌ها از SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE);
        Set<String> favoritesSet = sharedPreferences.getStringSet("favorites", new HashSet<>());

        // بارگذاری همه اشعار از فایل‌های JSON
        List<Poem> allPoems = loadAllPoemsFromJson();
        List<Poem> favoritePoems = new ArrayList<>();

        // فیلتر کردن اشعار علاقه‌مندی
        for (Poem poem : allPoems) {
            if (favoritesSet.contains(poem.getTitle())) {
                favoritePoems.add(poem);
            }
        }
        return favoritePoems;
    }

    // بارگذاری همه اشعار از فایل‌های JSON
    private List<Poem> loadAllPoemsFromJson() {
        List<Poem> allPoems = new ArrayList<>();
        int[] rawFiles = {
                R.raw.hafez_ghazals,
                R.raw.hafez_rubaiyat,
                R.raw.hafez_ghitaat,
                R.raw.hafez_ghasaid,
                R.raw.hafez_masnavi,
                R.raw.hafez_saghinameh,
                R.raw.hafez_montasab
                // اگر فایل‌های دیگری (مثل سعدی یا مولوی) دارید، اینجا اضافه کنید
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
        return allPoems;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}