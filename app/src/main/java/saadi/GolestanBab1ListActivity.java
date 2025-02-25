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

public class GolestanBab1ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GolestanBab1Adapter golestanBab1Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_golestan_bab1_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("باب اول گلستان");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<GolestanBab1Hekayat> hekayatList = loadHekayatsFromJson();
        golestanBab1Adapter = new GolestanBab1Adapter(hekayatList, hekayat -> {
            Intent intent = new Intent(GolestanBab1ListActivity.this, GolestanBab1DetailActivity.class);
            intent.putExtra("hekayatTitle", hekayat.getTitle());
            startActivity(intent);
        }, this);
        recyclerView.setAdapter(golestanBab1Adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        golestanBab1Adapter.notifyDataSetChanged(); // به‌روزرسانی برای تغییرات علاقه‌مندی‌ها
    }

    private List<GolestanBab1Hekayat> loadHekayatsFromJson() {
        List<GolestanBab1Hekayat> hekayatList = new ArrayList<>();
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.golestan_bab1);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("title");
                JSONArray proseArray = jsonObject.getJSONArray("prose");
                String firstProse = proseArray.getString(0); // فقط جمله اول نثر
                hekayatList.add(new GolestanBab1Hekayat(title, firstProse));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return hekayatList;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}