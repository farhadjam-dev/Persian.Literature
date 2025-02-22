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

public class BustanDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BustanAdapter bustanAdapter;
    private EditText searchField;
    private ImageButton searchButton;
    private ImageButton favoriteButton;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bustan_detail);

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
        toolbarTitle.setText("بوستان سعدی");

        // مدیریت کلیک دکمه علاقه‌مندی‌ها
        favoriteButton.setOnClickListener(v -> {
            Intent intent = new Intent(BustanDetailActivity.this, FavoritesActivity.class);
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

        // ایجاد لیست بخش‌های بوستان سعدی
        List<BustanItem> bustanItemList = new ArrayList<>();
        bustanItemList.add(new BustanItem("نیایش خدا بوستان", R.drawable.ic_niyayesh));
        bustanItemList.add(new BustanItem("باب اول بوستان", R.drawable.ic_bab1));
        bustanItemList.add(new BustanItem("باب دوم بوستان", R.drawable.ic_bab2));
        bustanItemList.add(new BustanItem("باب سوم بوستان", R.drawable.ic_bab3));
        bustanItemList.add(new BustanItem("باب چهارم بوستان", R.drawable.ic_bab4));
        bustanItemList.add(new BustanItem("باب پنجم بوستان", R.drawable.ic_bab5));
        bustanItemList.add(new BustanItem("باب ششم بوستان", R.drawable.ic_bab6));
        bustanItemList.add(new BustanItem("باب هفتم بوستان", R.drawable.ic_bab7));
        bustanItemList.add(new BustanItem("باب هشتم بوستان", R.drawable.ic_bab8));
        bustanItemList.add(new BustanItem("باب نهم بوستان", R.drawable.ic_bab9));
        bustanItemList.add(new BustanItem("باب دهم بوستان", R.drawable.ic_bab10));

        // ایجاد Adapter و تنظیم listener برای کلیک روی آیتم‌ها
        bustanAdapter = new BustanAdapter(bustanItemList, bustanItem -> {
            Intent intent;
            switch (bustanItem.getTitle()) {
                case "نیایش خدا بوستان":
                    intent = new Intent(BustanDetailActivity.this, BustanNiyayeshListActivity.class);
                    break;
                case "باب اول بوستان":
                    intent = new Intent(BustanDetailActivity.this, BustanBab1ListActivity.class);
                    break;
                case "باب دوم بوستان":
                    intent = new Intent(BustanDetailActivity.this, BustanBab2ListActivity.class);
                    break;
                case "باب سوم بوستان":
                    intent = new Intent(BustanDetailActivity.this, BustanBab3ListActivity.class);
                    break;
                case "باب چهارم بوستان":
                    intent = new Intent(BustanDetailActivity.this, BustanBab4ListActivity.class);
                    break;
                case "باب پنجم بوستان":
                    intent = new Intent(BustanDetailActivity.this, BustanBab5ListActivity.class);
                    break;
                case "باب ششم بوستان":
                    intent = new Intent(BustanDetailActivity.this, BustanBab6ListActivity.class);
                    break;
                case "باب هفتم بوستان":
                    intent = new Intent(BustanDetailActivity.this, BustanBab7ListActivity.class);
                    break;
                case "باب هشتم بوستان":
                    intent = new Intent(BustanDetailActivity.this, BustanBab8ListActivity.class);
                    break;
                case "باب نهم بوستان":
                    intent = new Intent(BustanDetailActivity.this, BustanBab9ListActivity.class);
                    break;
                case "باب دهم بوستان":
                    intent = new Intent(BustanDetailActivity.this, BustanBab10ListActivity.class);
                    break;
                default:
                    return;
            }
            startActivity(intent);
        });

        recyclerView.setAdapter(bustanAdapter);
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
            Intent intent = new Intent(BustanDetailActivity.this, SearchResultsActivity.class);
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