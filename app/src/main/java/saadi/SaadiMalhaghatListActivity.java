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

public class SaadiMalhaghatListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SaadiMalhaghatAdapter saadiMalhaghatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saadi_malhaghat_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<SaadiMalhaghat> malhaghatList = loadMalhaghatFromJson();
        saadiMalhaghatAdapter = new SaadiMalhaghatAdapter(malhaghatList, malhaghat -> {
            Intent intent = new Intent(SaadiMalhaghatListActivity.this, SaadiMalhaghatDetailActivity.class);
            intent.putExtra("saadiMalhaghatTitle", malhaghat.getTitle());
            startActivity(intent);
        }, this); // ارسال Context
        recyclerView.setAdapter(saadiMalhaghatAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        saadiMalhaghatAdapter.notifyDataSetChanged(); // به‌روزرسانی برای تغییرات علاقه‌مندی‌ها
    }

    private List<SaadiMalhaghat> loadMalhaghatFromJson() {
        List<SaadiMalhaghat> malhaghatList = new ArrayList<>();
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.saadi_malhaghat);
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
                malhaghatList.add(new SaadiMalhaghat(title, verses));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return malhaghatList;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}