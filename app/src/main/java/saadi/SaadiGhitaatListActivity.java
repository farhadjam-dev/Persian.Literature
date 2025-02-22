package saadi;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.jamlab.adab.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import hafez.Verse;

public class SaadiGhitaatListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SaadiGhitaatAdapter saadiGhitaatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saadi_ghitaat_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<SaadiGhitaat> ghitaatList = loadGhitaatFromJson();
        saadiGhitaatAdapter = new SaadiGhitaatAdapter(ghitaatList, ghitaat -> {
            Intent intent = new Intent(SaadiGhitaatListActivity.this, SaadiGhitaatDetailActivity.class);
            intent.putExtra("saadiGhitaatTitle", ghitaat.getTitle());
            startActivity(intent);
        }, this); // ارسال Context
        recyclerView.setAdapter(saadiGhitaatAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        saadiGhitaatAdapter.notifyDataSetChanged(); // به‌روزرسانی برای تغییرات علاقه‌مندی‌ها
    }

    private List<SaadiGhitaat> loadGhitaatFromJson() {
        List<SaadiGhitaat> ghitaatList = new ArrayList<>();
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.saadi_ghitaat);
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
                ghitaatList.add(new SaadiGhitaat(title, verses));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return ghitaatList;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}