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

public class MavaezDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MavaezAdapter mavaezAdapter;
    private EditText searchField;
    private ImageButton searchButton;
    private ImageButton favoriteButton;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mavaez_detail);

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
        toolbarTitle.setText("مواعظ سعدی");

        // مدیریت کلیک دکمه علاقه‌مندی‌ها
        favoriteButton.setOnClickListener(v -> {
            Intent intent = new Intent(MavaezDetailActivity.this, FavoritesActivity.class);
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

        // ایجاد لیست بخش‌های مواعظ سعدی
        List<MavaezItem> mavaezItemList = new ArrayList<>();
        mavaezItemList.add(new MavaezItem("غزلیات", R.drawable.ic_ghazal_mavaez));
        mavaezItemList.add(new MavaezItem("قصاید", R.drawable.ic_qasaed_mavaez));
        mavaezItemList.add(new MavaezItem("مراثی", R.drawable.ic_marasie_mavaez));
        mavaezItemList.add(new MavaezItem("قطعات", R.drawable.ic_qetaat_mavaez));
        mavaezItemList.add(new MavaezItem("مثنویات", R.drawable.ic_masnaviat_mavaez));
        mavaezItemList.add(new MavaezItem("رباعیات", R.drawable.ic_rubaiyat_mavaez));
        mavaezItemList.add(new MavaezItem("قصاید و قطعات عربی", R.drawable.ic_arabic_mavaez));
        mavaezItemList.add(new MavaezItem("مفردات", R.drawable.ic_mofradat_mavaez));
        mavaezItemList.add(new MavaezItem("مثلثات", R.drawable.ic_mosalasat_mavaez));

        // ایجاد Adapter و تنظیم listener برای کلیک روی آیتم‌ها
        mavaezAdapter = new MavaezAdapter(mavaezItemList, mavaezItem -> {
            Intent intent;
            switch (mavaezItem.getTitle()) {
                case "غزلیات":
                    intent = new Intent(MavaezDetailActivity.this, MavaezGhazalListActivity.class);
                    break;
                case "قصاید":
                    intent = new Intent(MavaezDetailActivity.this, MavaezGhaseedahListActivity.class);
                    break;
                case "مراثی":
                    intent = new Intent(MavaezDetailActivity.this, MavaezMarasiListActivity.class);
                    break;
                case "قطعات":
                    intent = new Intent(MavaezDetailActivity.this, MavaezGhetaatListActivity.class);
                    break;
                case "مثنویات":
                    intent = new Intent(MavaezDetailActivity.this, MavaezMasnaviListActivity.class);
                    break;
                case "رباعیات":
                    intent = new Intent(MavaezDetailActivity.this, MavaezRobaiiatListActivity.class);
                    break;
                case "قصاید و قطعات عربی":
                    intent = new Intent(MavaezDetailActivity.this, MavaezArabicListActivity.class);
                    break;
                case "مفردات":
                    intent = new Intent(MavaezDetailActivity.this, MavaezMofradatListActivity.class);
                    break;
                case "مثلثات":
                    intent = new Intent(MavaezDetailActivity.this, MavaezMosalasatActivity.class);
                    break;
                default:
                    return;
            }
            startActivity(intent);
        });

        recyclerView.setAdapter(mavaezAdapter);
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
            Intent intent = new Intent(MavaezDetailActivity.this, SearchResultsActivity.class);
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