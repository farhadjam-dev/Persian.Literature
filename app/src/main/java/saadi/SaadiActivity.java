package saadi;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.jamlab.adab.Poem;
import com.jamlab.adab.PoemLoader;
import com.jamlab.adab.R;
import favorits.FavoritesActivity;
import search.SearchResultsActivity;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SaadiActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SaadiAdapter saadiAdapter;
    private EditText searchField;
    private ImageButton searchButton;
    private ImageButton favoriteButton;
    private TextView toolbarTitle;
    private List<Poem> allPoems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saadi);

        // تنظیم Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // اتصال عناصر UI
        favoriteButton = findViewById(R.id.favorite_button);
        toolbarTitle = findViewById(R.id.toolbar_title);
        searchButton = findViewById(R.id.search_button);
        searchField = findViewById(R.id.search_field);

        // مدیریت کلیک دکمه علاقه‌مندی‌ها
        favoriteButton.setOnClickListener(v -> {
            Intent intent = new Intent(SaadiActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });

        // مدیریت دکمه جست‌وجو و کشویی
        searchButton.setOnClickListener(v -> {
            if (searchField.getVisibility() == View.GONE) {
                // باز کردن فیلد جست‌وجو
                Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
                searchField.setVisibility(View.VISIBLE);
                searchField.startAnimation(slideIn);
                searchButton.setImageResource(R.drawable.ic_close);
                Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
                favoriteButton.startAnimation(fadeOut);
                toolbarTitle.startAnimation(fadeOut);
                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation) {
                        favoriteButton.setVisibility(View.GONE);
                        toolbarTitle.setVisibility(View.GONE);
                    }
                    @Override public void onAnimationRepeat(Animation animation) {}
                });
            } else {
                // بررسی محتوای فیلد جست‌وجو
                String query = searchField.getText().toString().trim();
                if (query.isEmpty()) {
                    // بستن فیلد جست‌وجو بدون انجام جست‌وجو
                    closeSearchField();
                } else {
                    // انجام جست‌وجو
                    performSearchAndNavigate();
                }
            }
        });

        // مدیریت کلیک روی آیکون جست‌وجو در فیلد
        searchField.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int padding = searchField.getPaddingLeft();
                int iconWidth = searchField.getCompoundDrawables()[0].getBounds().width();
                float touchX = event.getX();
                if (touchX <= (padding + iconWidth)) {
                    performSearchAndNavigate();
                    return true;
                }
            }
            return false;
        });

        // مدیریت کلید Enter در کیبورد
        searchField.setOnEditorActionListener((v, actionId, event) -> {
            performSearchAndNavigate();
            return true;
        });

        // اتصال RecyclerView به layout
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ایجاد لیست بخش‌های مربوط به سعدی
        List<SaadiItem> saadiItemList = new ArrayList<>();
        saadiItemList.add(new SaadiItem("دیوان اشعار", R.drawable.ic_divan));
        saadiItemList.add(new SaadiItem("بوستان", R.drawable.ic_bustan));
        saadiItemList.add(new SaadiItem("گلستان", R.drawable.ic_gulistan));
        saadiItemList.add(new SaadiItem("مواعظ", R.drawable.ic_mavaez));
        saadiItemList.add(new SaadiItem("رسائل نثر", R.drawable.ic_rasael));
        saadiItemList.add(new SaadiItem("مجالس پنجگانه", R.drawable.ic_majales));

        // ایجاد Adapter و تنظیم listener برای کلیک روی آیتم‌ها
        saadiAdapter = new SaadiAdapter(saadiItemList, saadiItem -> {
            Intent intent;
            switch (saadiItem.getTitle()) {
                case "دیوان اشعار": intent = new Intent(SaadiActivity.this, DivanListActivity.class); break;
                case "بوستان":
                    intent = new Intent(SaadiActivity.this, BustanDetailActivity.class);
                    intent.putExtra("title", "بوستان سعدی");
                    break;
                case "گلستان":
                    intent = new Intent(SaadiActivity.this, GulistanDetailActivity.class);
                    intent.putExtra("title", "گلستان سعدی");
                    break;
                case "مواعظ": intent = new Intent(SaadiActivity.this, MavaezListActivity.class); break;
                case "رسائل نثر":
                    intent = new Intent(SaadiActivity.this, RasaelDetailActivity.class);
                    intent.putExtra("title", "رسائل نثر سعدی");
                    break;
                case "مجالس پنجگانه":
                    intent = new Intent(SaadiActivity.this, MajalesDetailActivity.class);
                    intent.putExtra("title", "مجالس پنجگانه سعدی");
                    break;
                default: return;
            }
            startActivity(intent);
        });

        recyclerView.setAdapter(saadiAdapter);

        // بارگذاری همه اشعار از فایل‌های JSON
        loadAllPoems();
    }

    // متد برای بستن فیلد جست‌وجو
    private void closeSearchField() {
        Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
        searchField.startAnimation(slideOut);
        searchField.setVisibility(View.GONE);
        searchButton.setImageResource(R.drawable.ic_search);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        favoriteButton.setVisibility(View.VISIBLE);
        toolbarTitle.setVisibility(View.VISIBLE);
        favoriteButton.startAnimation(fadeIn);
        toolbarTitle.startAnimation(fadeIn);
        searchField.setText("");
    }

    // بارگذاری همه اشعار از فایل‌های JSON
    private void loadAllPoems() {
        allPoems = new ArrayList<>();
        int[] rawFiles = {
                // R.raw.saadi_divan,
                // R.raw.saadi_bustan,
                // R.raw.saadi_gulistan,
                // R.raw.saadi_mavaez,
                // R.raw.saadi_rasael,
                // R.raw.saadi_majales
        };

        for (int resId : rawFiles) {
            try {
                InputStream inputStream = getResources().openRawResource(resId);
                List<Poem> poems = PoemLoader.loadPoemsFromJson(inputStream);
                allPoems.addAll(poems);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // متد جست‌وجو و انتقال به SearchResultsActivity
    private void performSearchAndNavigate() {
        String query = searchField.getText().toString().trim();
        if (!query.isEmpty()) {
            List<Poem> searchResults = performSearch(allPoems, query);
            Intent intent = new Intent(SaadiActivity.this, SearchResultsActivity.class);
            intent.putParcelableArrayListExtra("search_results", new ArrayList<>(searchResults));
            intent.putExtra("search_query", query);
            startActivity(intent);
        }
        // همیشه فیلد رو ببند، چه جست‌وجو انجام بشه چه نه
        closeSearchField();
    }

    // متد جست‌وجو در لیست اشعار
    private List<Poem> performSearch(List<Poem> poems, String query) {
        List<Poem> results = new ArrayList<>();
        String normalizedQuery = normalizeText(query);
        List<String> queryParts = Arrays.asList(normalizedQuery.split("\\s+"));

        for (Poem poem : poems) {
            String normalizedPoemText = normalizeText(poem.getText());
            boolean matchesAll = true;
            for (String part : queryParts) {
                if (!normalizedPoemText.contains(part)) {
                    matchesAll = false;
                    break;
                }
            }
            if (matchesAll) {
                results.add(poem);
            }
        }
        return results;
    }

    // تابع برای حذف اعراب از متن
    private String normalizeText(String text) {
        return text.replaceAll("[\\u064B-\\u065F]", "");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}