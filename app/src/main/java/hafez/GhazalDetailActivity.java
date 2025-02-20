package hafez;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import com.jamlab.adab.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GhazalDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VerseAdapter verseAdapter;
    private ImageButton favoriteButton;
    private boolean isFavorite = false;
    private String ghazalTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghazal_detail);

        // تنظیم Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // اتصال RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // دریافت عنوان غزل از Intent
        ghazalTitle = getIntent().getStringExtra("ghazalTitle");
        if (ghazalTitle != null) {
            getSupportActionBar().setTitle(ghazalTitle); // تنظیم عنوان Toolbar
        }

        // بارگذاری ابیات غزل
        List<Verse> verseList = loadVersesFromJson(ghazalTitle);

        // تنظیم آداپتر
        verseAdapter = new VerseAdapter(verseList);
        recyclerView.setAdapter(verseAdapter);

        // اتصال دکمه علاقه‌مندی
        favoriteButton = findViewById(R.id.favorite_button);
        SharedPreferences sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE);
        Set<String> favorites = sharedPreferences.getStringSet("favorites", new HashSet<>());

        // بررسی وضعیت علاقه‌مندی
        if (favorites.contains(ghazalTitle)) {
            isFavorite = true;
            favoriteButton.setImageResource(R.drawable.ic_star_filled);
        } else {
            favoriteButton.setImageResource(R.drawable.ic_star_outline);
        }

        // مدیریت کلیک دکمه علاقه‌مندی
        favoriteButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (isFavorite) {
                isFavorite = false;
                favoriteButton.setImageResource(R.drawable.ic_star_outline);
                favorites.remove(ghazalTitle);
            } else {
                isFavorite = true;
                favoriteButton.setImageResource(R.drawable.ic_star_filled);
                favorites.add(ghazalTitle);
            }
            editor.putStringSet("favorites", favorites);
            editor.apply();
        });
    }

    private List<Verse> loadVersesFromJson(String ghazalTitle) {
        List<Verse> verseList = new ArrayList<>();
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
                if (title.equals(ghazalTitle)) {
                    JSONArray versesArray = jsonObject.getJSONArray("verses");
                    for (int j = 0; j < versesArray.length(); j++) {
                        JSONObject verseObject = versesArray.getJSONObject(j);
                        String text = verseObject.getString("text");
                        String explanation = verseObject.getString("explanation");
                        verseList.add(new Verse(text, explanation));
                    }
                    break;
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return verseList;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}