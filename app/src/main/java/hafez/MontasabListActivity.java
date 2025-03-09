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

public class MontasabListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MontasabAdapter montasabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_montasab_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Montasab> montasabList = loadMontasabFromJson();
        montasabAdapter = new MontasabAdapter(montasabList, montasab -> {
            Intent intent = new Intent(MontasabListActivity.this, MontasabDetailActivity.class);
            intent.putExtra("montasabTitle", montasab.getTitle());
            startActivity(intent);
        }, this);
        recyclerView.setAdapter(montasabAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        montasabAdapter.notifyDataSetChanged();
    }

    private List<Montasab> loadMontasabFromJson() {
        List<Montasab> montasabList = new ArrayList<>();
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.hafez_montasab);
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
                montasabList.add(new Montasab(title, verses));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return montasabList;
    }
}