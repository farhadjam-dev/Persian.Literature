package hafez;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx

        .recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.jamlab.adab.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RubaiyatListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RubaiyatAdapter rubaiyatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rubaiyat_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Rubaiyat> rubaiyatList = loadRubaiyatsFromJson();
        rubaiyatAdapter = new RubaiyatAdapter(rubaiyatList, rubaiyat -> {
            Intent intent = new Intent(RubaiyatListActivity.this, RubaiyatDetailActivity.class);
            intent.putExtra("rubaiyatTitle", rubaiyat.getTitle());
            startActivity(intent);
        }, this);
        recyclerView.setAdapter(rubaiyatAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        rubaiyatAdapter.notifyDataSetChanged();
    }

    private List<Rubaiyat> loadRubaiyatsFromJson() {
        List<Rubaiyat> rubaiyatList = new ArrayList<>();
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.hafez_rubaiyat);
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
                rubaiyatList.add(new Rubaiyat(title, verses));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return rubaiyatList;
    }
}