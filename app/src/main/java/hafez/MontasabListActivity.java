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

public class MontasabListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MontasabAdapter montasabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_montasab_list);

        // تنظیم Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // اتصال RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // بارگذاری اشعار منتسب از JSON
        List<Montasab> montasabList = loadMontasabFromJson();
        if (montasabList.isEmpty()) {
            Toast.makeText(this, "هیچ شعر منتسبی یافت نشد", Toast.LENGTH_SHORT).show();
        }

        // تنظیم آداپتر
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
        montasabAdapter.notifyDataSetChanged(); // به‌روزرسانی برای تغییرات علاقه‌مندی‌ها
    }

    // بارگذاری اشعار منتسب از فایل JSON
    private List<Montasab> loadMontasabFromJson() {
        List<Montasab> montasabList = new ArrayList<>();
        try {
            // باز کردن فایل JSON از res/raw
            InputStream inputStream = getResources().openRawResource(R.raw.hafez_montasab);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            int bytesRead = inputStream.read(buffer);
            inputStream.close();

            if (bytesRead <= 0) {
                Log.e("MontasabDebug", "فایل JSON خالی است");
                return montasabList;
            }

            String json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.optString("title", "بدون عنوان");

                // بررسی وجود و نوع "verses"
                if (!jsonObject.has("verses") || jsonObject.isNull("verses")) {
                    Log.w("MontasabDebug", "کلید 'verses' برای " + title + " وجود ندارد");
                    continue;
                }

                Object versesObj = jsonObject.get("verses");
                if (!(versesObj instanceof JSONArray)) {
                    Log.w("MontasabDebug", "'verses' برای " + title + " آرایه نیست: " + versesObj.toString());
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

                montasabList.add(new Montasab(title, verses));
            }
        } catch (IOException e) {
            Log.e("MontasabDebug", "خطا در خواندن فایل JSON: " + e.getMessage());
            Toast.makeText(this, "خطا در بارگذاری اشعار منتسب", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Log.e("MontasabDebug", "خطا در پردازش JSON: " + e.getMessage());
            Toast.makeText(this, "فرمت JSON اشعار منتسب نامعتبر است", Toast.LENGTH_SHORT).show();
        }
        Log.d("MontasabDebug", "تعداد اشعار منتسب بارگذاری‌شده: " + montasabList.size());
        return montasabList;
    }
}