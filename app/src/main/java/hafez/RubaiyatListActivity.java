package hafez;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
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

public class RubaiyatListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RubaiyatAdapter rubaiyatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rubaiyat_list);

        // تنظیم Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // اتصال RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // بارگذاری رباعیات از JSON
        List<Rubaiyat> rubaiyatList = loadRubaiyatsFromJson();
        if (rubaiyatList.isEmpty()) {
            Toast.makeText(this, "هیچ رباعی‌ای یافت نشد", Toast.LENGTH_SHORT).show();
        }

        // تنظیم آداپتر
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
        rubaiyatAdapter.notifyDataSetChanged(); // به‌روزرسانی برای تغییرات علاقه‌مندی‌ها
    }

    // بارگذاری رباعیات از فایل JSON
    private List<Rubaiyat> loadRubaiyatsFromJson() {
        List<Rubaiyat> rubaiyatList = new ArrayList<>();
        try {
            // باز کردن فایل JSON از res/raw
            InputStream inputStream = getResources().openRawResource(R.raw.hafez_rubaiyat);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            int bytesRead = inputStream.read(buffer);
            inputStream.close();

            if (bytesRead <= 0) {
                Log.e("RubaiyatDebug", "فایل JSON خالی است");
                return rubaiyatList;
            }

            String json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.optString("title", "بدون عنوان");

                // بررسی وجود و نوع "verses"
                if (!jsonObject.has("verses") || jsonObject.isNull("verses")) {
                    Log.w("RubaiyatDebug", "کلید 'verses' برای " + title + " وجود ندارد");
                    continue;
                }

                Object versesObj = jsonObject.get("verses");
                if (!(versesObj instanceof JSONArray)) {
                    Log.w("RubaiyatDebug", "'verses' برای " + title + " آرایه نیست: " + versesObj.toString());
                    continue;
                }

                JSONArray versesArray = (JSONArray) versesObj;
                List<Verse> verses = new ArrayList<>();

                for (int j = 0; j < versesArray.length(); j++) {
                    JSONObject verseObject = versesArray.getJSONObject(j);
                    String text = verseObject.optString("text", "");
                    String explanation = verseObject.optString("explanation", "");
                    verses.add(new Verse(text, explanation));
                }

                rubaiyatList.add(new Rubaiyat(title, verses));
            }
        } catch (IOException e) {
            Log.e("RubaiyatDebug", "خطا در خواندن فایل JSON: " + e.getMessage());
            Toast.makeText(this, "خطا در بارگذاری رباعیات", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Log.e("RubaiyatDebug", "خطا در پردازش JSON: " + e.getMessage());
            Toast.makeText(this, "فرمت JSON رباعیات نامعتبر است", Toast.LENGTH_SHORT).show();
        }
        Log.d("RubaiyatDebug", "تعداد رباعیات بارگذاری‌شده: " + rubaiyatList.size());
        return rubaiyatList;
    }
}