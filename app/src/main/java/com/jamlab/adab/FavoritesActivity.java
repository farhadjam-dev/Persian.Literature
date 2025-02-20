package com.jamlab.adab;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import android.content.SharedPreferences;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FavoriteAdapter favoriteAdapter;
    private List<Ghazal> favoriteGhazals; // لیست غزلیات علاقه‌مندی
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

        // بارگذاری غزلیات علاقه‌مندی
        favoriteGhazals = loadFavoriteGhazals();

        // بررسی وضعیت لیست
        if (favoriteGhazals.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.GONE);
        }

        // تنظیم آداپتر
        favoriteAdapter = new FavoriteAdapter(this, favoriteGhazals);
        recyclerView.setAdapter(favoriteAdapter);
    }

    // بارگذاری غزلیات علاقه‌مندی
    private List<Ghazal> loadFavoriteGhazals() {
        // بارگذاری عناوین علاقه‌مندی‌ها از SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE);
        Set<String> favoritesSet = sharedPreferences.getStringSet("favorites", new HashSet<>());

        // بارگذاری همه غزلیات از JSON
        List<Ghazal> allGhazals = loadGhazalsFromJson();
        List<Ghazal> favoriteGhazals = new ArrayList<>();

        // فیلتر کردن غزلیات علاقه‌مندی
        for (Ghazal ghazal : allGhazals) {
            if (favoritesSet.contains(ghazal.getTitle())) {
                favoriteGhazals.add(ghazal);
            }
        }
        return favoriteGhazals;
    }

    // بارگذاری غزلیات از فایل JSON
    private List<Ghazal> loadGhazalsFromJson() {
        List<Ghazal> ghazalList = new ArrayList<>();
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.hafez_ghazals);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("title");
                JSONArray versesArray = jsonObject.getJSONArray("verses");
                List<Verse> verses = new ArrayList<>();
                for (int j = 0; j < versesArray.length(); j++) {
                    JSONObject verseObject = versesArray.getJSONObject(j);
                    String text = verseObject.getString("text");
                    String explanation = verseObject.getString("explanation");
                    verses.add(new Verse(text, explanation));
                }
                ghazalList.add(new Ghazal(title, verses));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ghazalList;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}