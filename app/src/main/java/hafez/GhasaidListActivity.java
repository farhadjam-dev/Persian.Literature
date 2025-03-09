package hafez;

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

public class GhasaidListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GhasaidAdapter ghasaidAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghasaid_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Ghasaid> ghasaidList = loadGhasaidFromJson();
        ghasaidAdapter = new GhasaidAdapter(ghasaidList, ghasaid -> {
            Intent intent = new Intent(GhasaidListActivity.this, GhasaidDetailActivity.class);
            intent.putExtra("ghasaidTitle", ghasaid.getTitle());
            startActivity(intent);
        }, this);
        recyclerView.setAdapter(ghasaidAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ghasaidAdapter.notifyDataSetChanged();
    }

    private List<Ghasaid> loadGhasaidFromJson() {
        List<Ghasaid> ghasaidList = new ArrayList<>();
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.hafez_ghasaid);
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
                ghasaidList.add(new Ghasaid(title, verses));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return ghasaidList;
    }
}