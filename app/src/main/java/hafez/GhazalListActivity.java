package hafez;



import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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

public class GhazalListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GhazalAdapter ghazalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghazal_list);

        // اتصال RecyclerView به layout
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // خواندن فایل JSON و ایجاد لیست غزلیات
        List<Ghazal> ghazalList = loadGhazalsFromJson();

        // ایجاد Adapter و تنظیم listener برای کلیک روی آیتم‌ها
        ghazalAdapter = new GhazalAdapter(ghazalList, new GhazalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Ghazal ghazal) {
                Intent intent = new Intent(GhazalListActivity.this, GhazalDetailActivity.class);
                intent.putExtra("ghazalTitle", ghazal.getTitle());
                startActivity(intent);
            }
        });

        // تنظیم Adapter برای RecyclerView
        recyclerView.setAdapter(ghazalAdapter);
    }

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
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return ghazalList;
    }
}
