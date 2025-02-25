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

public class MavaezGhaseedahListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MavaezGhaseedahAdapter mavaezGhaseedahAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mavaez_ghaseedah_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("مواعظ قصاید");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<MavaezGhaseedahPoem> poemList = loadPoemsFromJson();
        mavaezGhaseedahAdapter = new MavaezGhaseedahAdapter(poemList, poem -> {
            Intent intent = new Intent(MavaezGhaseedahListActivity.this, MavaezGhaseedahDetailActivity.class);
            intent.putExtra("mavaezGhaseedahTitle", poem.getTitle());
            startActivity(intent);
        }, this);
        recyclerView.setAdapter(mavaezGhaseedahAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mavaezGhaseedahAdapter.notifyDataSetChanged(); // به‌روزرسانی برای تغییرات علاقه‌مندی‌ها
    }

    private List<MavaezGhaseedahPoem> loadPoemsFromJson() {
        List<MavaezGhaseedahPoem> poemList = new ArrayList<>();
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.mavaez_ghaseedah);
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
                poemList.add(new MavaezGhaseedahPoem(title, verses));
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