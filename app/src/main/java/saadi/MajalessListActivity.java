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

public class MajalessListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GolestanBab1Adapter majalessAdapter; // از آداپتر باب اول استفاده می‌کنیم چون ساختار یکسانه

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_majaless_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("مجالس پنجگانه");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<GolestanBab1Hekayat> majalessList = loadMajalessFromJson();
        majalessAdapter = new GolestanBab1Adapter(majalessList, majlas -> {
            Intent intent = new Intent(MajalessListActivity.this, MajalessDetailActivity.class);
            intent.putExtra("majlasTitle", majlas.getTitle());
            startActivity(intent);
        }, this);
        recyclerView.setAdapter(majalessAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        majalessAdapter.notifyDataSetChanged(); // به‌روزرسانی برای تغییرات علاقه‌مندی‌ها
    }

    private List<GolestanBab1Hekayat> loadMajalessFromJson() {
        List<GolestanBab1Hekayat> majalessList = new ArrayList<>();
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.majaless);
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
                majalessList.add(new GolestanBab1Hekayat(title, firstProse));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return majalessList;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}