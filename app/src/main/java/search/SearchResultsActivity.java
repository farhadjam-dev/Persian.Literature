package search;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.jamlab.adab.Poem;
import com.jamlab.adab.R;
import java.util.List;
import hafez.GhazalDetailActivity;

public class SearchResultsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchResultAdapter searchResultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // تنظیم Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false); // حذف دکمه Back
            getSupportActionBar().setDisplayShowTitleEnabled(false); // حذف عنوان پیش‌فرض
        }

        recyclerView = findViewById(R.id.search_results_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // دریافت نتایج جست‌وجو و عبارت جست‌وجو از Intent
        List<Poem> searchResults = getIntent().getParcelableArrayListExtra("search_results");
        String searchQuery = getIntent().getStringExtra("search_query");

        // تنظیم آداپتر
        searchResultAdapter = new SearchResultAdapter(searchResults, searchQuery, poem -> {
            Intent intent = new Intent(SearchResultsActivity.this, GhazalDetailActivity.class);
            intent.putExtra("ghazalTitle", poem.getTitle()); // ارسال عنوان غزل
            startActivity(intent);
        });

        recyclerView.setAdapter(searchResultAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}