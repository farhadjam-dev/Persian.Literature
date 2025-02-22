package saadi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

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

public class BustanBab5ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BustanBab5Adapter bustanBab5Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bustan_bab5_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("باب پنجم بوستان");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<BustanBab5Poem> poemList = loadPoemsFromJson();
        bustanBab5Adapter = new BustanBab5Adapter(poemList, poem -> {
            Intent intent = new Intent(BustanBab5ListActivity.this, BustanBab5DetailActivity.class);
            intent.putExtra("bustanBab5Title", poem.getTitle());
            startActivity(intent);
        }, this);
        recyclerView.setAdapter(bustanBab5Adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bustanBab5Adapter.notifyDataSetChanged(); // به‌روزرسانی برای تغییرات علاقه‌مندی‌ها
    }

    private List<BustanBab5Poem> loadPoemsFromJson() {
        List<BustanBab5Poem> poemList = new ArrayList<>();
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.bustan_bab5);
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
                    String explanation = verseObject.optString("explanation", "");
                    verses.add(new Verse(text, explanation));
                }
                poemList.add(new BustanBab5Poem(title, verses));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return poemList;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}