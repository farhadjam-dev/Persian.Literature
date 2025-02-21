package hafez;

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

public class HafezActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HafezAdapter hafezAdapter;
    private EditText searchField;
    private ImageButton searchButton;
    private ImageButton favoriteButton;
    private TextView toolbarTitle;
    private List<Poem> poems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hafez);

        // تنظیم Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // اتصال عناصر UI
        favoriteButton = findViewById(R.id.favorite_button);
        toolbarTitle = findViewById(R.id.toolbar_title);
        searchButton = findViewById(R.id.search_button);
        searchField = findViewById(R.id.search_field);

        // مدیریت کلیک دکمه علاقه‌مندی‌ها
        favoriteButton.setOnClickListener(v -> {
            Intent intent = new Intent(HafezActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });

        // مدیریت دکمه جست‌وجو و کشویی
        searchButton.setOnClickListener(v -> {
            if (searchField.getVisibility() == View.GONE) {
                // نمایش فیلد جست‌وجو از راست به چپ
                Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
                searchField.setVisibility(View.VISIBLE);
                searchField.startAnimation(slideIn);
                searchButton.setImageResource(R.drawable.ic_close);

                // محو کردن آیکون علاقه‌مندی‌ها و عنوان
                Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
                favoriteButton.startAnimation(fadeOut);
                toolbarTitle.startAnimation(fadeOut);
                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        favoriteButton.setVisibility(View.GONE);
                        toolbarTitle.setVisibility(View.GONE);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
            } else {
                performSearchAndNavigate();
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

        // ایجاد لیست بخش‌های مربوط به حافظ
        List<HafezItem> hafezItemList = new ArrayList<>();
        hafezItemList.add(new HafezItem("زندگی‌نامه", R.drawable.ic_bio));
        hafezItemList.add(new HafezItem("غزلیات", R.drawable.ic_ghazal));
        hafezItemList.add(new HafezItem("قصاید", R.drawable.ic_ghaside));

        // ایجاد Adapter و تنظیم listener برای کلیک روی آیتم‌ها
        hafezAdapter = new HafezAdapter(hafezItemList, new HafezAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(HafezItem hafezItem) {
                if (hafezItem.getTitle().equals("زندگی‌نامه")) {
                    Intent intent = new Intent(HafezActivity.this, HafezBioActivity.class);
                    startActivity(intent);
                }
                if (hafezItem.getTitle().equals("غزلیات")) {
                    Intent intent = new Intent(HafezActivity.this, GhazalListActivity.class);
                    startActivity(intent);
                }
            }
        });

        // تنظیم Adapter برای RecyclerView
        recyclerView.setAdapter(hafezAdapter);

        // بارگذاری لیست اشعار از JSON برای جست‌وجو
        InputStream inputStream = getResources().openRawResource(R.raw.hafez_ghazals);
        poems = PoemLoader.loadPoemsFromJson(inputStream);
    }

    // متد جست‌وجو و انتقال به SearchResultsActivity
    private void performSearchAndNavigate() {
        String query = searchField.getText().toString().trim();
        if (!query.isEmpty()) {
            List<Poem> searchResults = performSearch(poems, query);
            Intent intent = new Intent(HafezActivity.this, SearchResultsActivity.class);
            intent.putParcelableArrayListExtra("search_results", new ArrayList<>(searchResults));
            intent.putExtra("search_query", query);
            startActivity(intent);
            // مخفی کردن فیلد جست‌وجو پس از جست‌وجو
            Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
            searchField.startAnimation(slideOut);
            searchField.setVisibility(View.GONE);
            searchButton.setImageResource(R.drawable.ic_search);
            // نمایش دوباره آیکون‌ها
            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            favoriteButton.setVisibility(View.VISIBLE);
            toolbarTitle.setVisibility(View.VISIBLE);
            favoriteButton.startAnimation(fadeIn);
            toolbarTitle.startAnimation(fadeIn);
            searchField.setText("");
        }
    }

    // متد جست‌وجو در لیست اشعار
    private List<Poem> performSearch(List<Poem> poems, String query) {
        List<Poem> results = new ArrayList<>();
        // پاکسازی عبارت جست‌وجو از اعراب و تقسیم به بخش‌ها
        String normalizedQuery = normalizeText(query);
        List<String> queryParts = Arrays.asList(normalizedQuery.split("\\s+")); // تقسیم با فاصله

        for (Poem poem : poems) {
            String normalizedPoemText = normalizeText(poem.getText());
            boolean matchesAll = true;
            // چک کردن اینکه آیا همه بخش‌های عبارت جست‌وجو در متن هستن
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
        return text.replaceAll("[\\u064B-\\u065F]", ""); // حذف تمام اعراب یونی‌کد
    }
}