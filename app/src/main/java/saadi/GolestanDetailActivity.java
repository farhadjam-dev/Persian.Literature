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
import com.jamlab.adab.R;
import favorits.FavoritesActivity;
import search.SearchResultsActivity;
import java.util.ArrayList;
import java.util.List;

public class GolestanDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GolestanAdapter golestanAdapter;
    private EditText searchField;
    private ImageButton searchButton;
    private ImageButton favoriteButton;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_golestan_detail);

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
        toolbarTitle.setText("گلستان سعدی");

        // مدیریت کلیک دکمه علاقه‌مندی‌ها
        favoriteButton.setOnClickListener(v -> {
            Intent intent = new Intent(GolestanDetailActivity.this, FavoritesActivity.class);
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
                    performSearchAndNavigate(query);
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
                    performSearchAndNavigate(searchField.getText().toString().trim());
                    return true;
                }
            }
            return false;
        });

        // مدیریت کلید Enter در کیبورد
        searchField.setOnEditorActionListener((v, actionId, event) -> {
            performSearchAndNavigate(searchField.getText().toString().trim());
            return true;
        });

        // اتصال RecyclerView به layout
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ایجاد لیست بخش‌های گلستان سعدی
        List<GolestanItem> golestanItemList = new ArrayList<>();
        golestanItemList.add(new GolestanItem("دیباچه گلستان", R.drawable.ic_dibache));
        golestanItemList.add(new GolestanItem("باب اول گلستان", R.drawable.ic_bab1_golestan));
        golestanItemList.add(new GolestanItem("باب دوم گلستان", R.drawable.ic_bab2_golestan));
        golestanItemList.add(new GolestanItem("باب سوم گلستان", R.drawable.ic_bab3_golestan));
        golestanItemList.add(new GolestanItem("باب چهارم گلستان", R.drawable.ic_bab4_golestan));
        golestanItemList.add(new GolestanItem("باب پنجم گلستان", R.drawable.ic_bab5_golestan));
        golestanItemList.add(new GolestanItem("باب ششم گلستان", R.drawable.ic_bab6_golestan));
        golestanItemList.add(new GolestanItem("باب هفتم گلستان", R.drawable.ic_bab7_golestan));
        golestanItemList.add(new GolestanItem("باب هشتم گلستان", R.drawable.ic_bab8_golestan));

        // ایجاد Adapter و تنظیم listener برای کلیک روی آیتم‌ها
        golestanAdapter = new GolestanAdapter(golestanItemList, golestanItem -> {
            Intent intent;
            switch (golestanItem.getTitle()) {
                case "دیباچه گلستان":
                    intent = new Intent(GolestanDetailActivity.this, DibacheGolestanActivity.class);
                    break;
                case "باب اول گلستان":
                    intent = new Intent(GolestanDetailActivity.this, GolestanBab1Activity.class);
                    break;
                case "باب دوم گلستان":
                    intent = new Intent(GolestanDetailActivity.this, GolestanBab2Activity.class);
                    break;
                case "باب سوم گلستان":
                    intent = new Intent(GolestanDetailActivity.this, GolestanBab3Activity.class);
                    break;
                case "باب چهارم گلستان":
                    intent = new Intent(GolestanDetailActivity.this, GolestanBab4Activity.class);
                    break;
                case "باب پنجم گلستان":
                    intent = new Intent(GolestanDetailActivity.this, GolestanBab5Activity.class);
                    break;
                case "باب ششم گلستان":
                    intent = new Intent(GolestanDetailActivity.this, GolestanBab6Activity.class);
                    break;
                case "باب هفتم گلستان":
                    intent = new Intent(GolestanDetailActivity.this, GolestanBab7Activity.class);
                    break;
                case "باب هشتم گلستان":
                    intent = new Intent(GolestanDetailActivity.this, GolestanBab8Activity.class);
                    break;
                default:
                    return;
            }
            startActivity(intent);
        });

        recyclerView.setAdapter(golestanAdapter);
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

    // متد جست‌وجو و انتقال به SearchResultsActivity
    private void performSearchAndNavigate(String query) {
        if (!query.isEmpty()) {
            Intent intent = new Intent(GolestanDetailActivity.this, SearchResultsActivity.class);
            intent.putExtra("search_query", query);
            startActivity(intent);
        }
        closeSearchField();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}