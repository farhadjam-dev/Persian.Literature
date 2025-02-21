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

public class DivanListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SaadiDivanAdapter saadiDivanAdapter;
    private EditText searchField;
    private ImageButton searchButton;
    private ImageButton favoriteButton;
    private TextView toolbarTitle;
    private List<Poem> allPoems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_divan_list);

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
            Intent intent = new Intent(DivanListActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });

        // مدیریت دکمه جست‌وجو و کشویی
        searchButton.setOnClickListener(v -> {
            if (searchField.getVisibility() == View.GONE) {
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
                String query = searchField.getText().toString().trim();
                if (query.isEmpty()) {
                    closeSearchField();
                } else {
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

        // ایجاد لیست بخش‌های دیوان اشعار سعدی
        List<SaadiDivanItem> saadiDivanItemList = new ArrayList<>();
        saadiDivanItemList.add(new SaadiDivanItem("غزلیات سعدی", R.drawable.ic_ghazal));
        saadiDivanItemList.add(new SaadiDivanItem("رباعیات سعدی", R.drawable.ic_rubai));
        saadiDivanItemList.add(new SaadiDivanItem("قطعات سعدی", R.drawable.ic_ghita));
        saadiDivanItemList.add(new SaadiDivanItem("ملحقات", R.drawable.ic_malhaghat));
        saadiDivanItemList.add(new SaadiDivanItem("مفردات سعدی", R.drawable.ic_mofradat));
        saadiDivanItemList.add(new SaadiDivanItem("ترجیع‌بند سعدی", R.drawable.ic_tarjiband));

        // ایجاد Adapter و تنظیم listener برای کلیک روی آیتم‌ها
        saadiDivanAdapter = new SaadiDivanAdapter(saadiDivanItemList, saadiDivanItem -> {
            Intent intent;
            switch (saadiDivanItem.getTitle()) {
                case "غزلیات سعدی":
                    intent = new Intent(DivanListActivity.this, SaadiGhazalListActivity.class);
                    break;
                case "رباعیات سعدی":
                    intent = new Intent(DivanListActivity.this, SaadiRubaiyatListActivity.class);
                    break;
                case "قطعات سعدی":
                    intent = new Intent(DivanListActivity.this, SaadiGhitaatListActivity.class);
                    break;
                case "ملحقات":
                    intent = new Intent(DivanListActivity.this, SaadiMalhaghatListActivity.class);
                    break;

                case "ترجیع‌بند سعدی":
                    intent = new Intent(DivanListActivity.this, SaadiTarjibandListActivity.class);
                    break;
                default:
                    return;
            }
            startActivity(intent);
        });

        recyclerView.setAdapter(saadiDivanAdapter);

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
              //  R.raw.saadi_ghazals,
              //  R.raw.saadi_rubaiyat,
              //  R.raw.saadi_ghitaat,
              //  R.raw.saadi_malhaghat,
               // R.raw.saadi_mofradat,
               // R.raw.saadi_tarjiband
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
            Intent intent = new Intent(DivanListActivity.this, SearchResultsActivity.class);
            intent.putParcelableArrayListExtra("search_results", new ArrayList<>(searchResults));
            intent.putExtra("search_query", query);
            startActivity(intent);
        }
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