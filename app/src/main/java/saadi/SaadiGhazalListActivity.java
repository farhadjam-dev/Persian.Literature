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

public class SaadiGhazalListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SaadiGhazalAdapter saadiGhazalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saadi_ghazal_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<SaadiGhazal> ghazalList = loadGhazalsFromJson();
        saadiGhazalAdapter = new SaadiGhazalAdapter(ghazalList, ghazal -> {
            Intent intent = new Intent(SaadiGhazalListActivity.this, SaadiGhazalDetailActivity.class);
            intent.putExtra("saadiGhazalTitle", ghazal.getTitle());
            startActivity(intent);
        }, this); // ارسال Context
        recyclerView.setAdapter(saadiGhazalAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        saadiGhazalAdapter.notifyDataSetChanged(); // به‌روزرسانی برای تغییرات علاقه‌مندی‌ها
    }

    private List<SaadiGhazal> loadGhazalsFromJson() {
        List<SaadiGhazal> ghazalList = new ArrayList<>();
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.saadi_ghazals);
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
                ghazalList.add(new SaadiGhazal(title, verses));
            }
        } catch (IOException | JSONException e) {
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