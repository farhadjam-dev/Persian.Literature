package search;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.jamlab.adab.Poem;
import com.jamlab.adab.R;
import hafez.GhazalDetailActivity;
import hafez.RubaiyatDetailActivity;
import hafez.GhitaatDetailActivity;
import hafez.GhasaidDetailActivity;
import hafez.MasnaviDetailActivity;
import hafez.SaghinamehDetailActivity;
import hafez.MontasabDetailActivity;

import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchResultAdapter searchResultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        recyclerView = findViewById(R.id.search_results_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Poem> searchResults = getIntent().getParcelableArrayListExtra("search_results");
        String searchQuery = getIntent().getStringExtra("search_query");

        searchResultAdapter = new SearchResultAdapter(searchResults, searchQuery, poem -> {
            Intent intent;
            String title = poem.getTitle().toLowerCase();
            if (title.contains("غزل")) {
                intent = new Intent(SearchResultsActivity.this, GhazalDetailActivity.class);
                intent.putExtra("ghazalTitle", poem.getTitle());
            } else if (title.contains("رباعی")) {
                intent = new Intent(SearchResultsActivity.this, RubaiyatDetailActivity.class);
                intent.putExtra("rubaiyatTitle", poem.getTitle());
            } else if (title.contains("قطعه")) {
                intent = new Intent(SearchResultsActivity.this, GhitaatDetailActivity.class);
                intent.putExtra("ghitaatTitle", poem.getTitle());
            } else if (title.contains("قصیده")) {
                intent = new Intent(SearchResultsActivity.this, GhasaidDetailActivity.class);
                intent.putExtra("ghasaidTitle", poem.getTitle());
            } else if (title.contains("مثنوی")) {
                intent = new Intent(SearchResultsActivity.this, MasnaviDetailActivity.class);
                intent.putExtra("masnaviTitle", poem.getTitle());
            } else if (title.contains("ساقی‌نامه")) {
                intent = new Intent(SearchResultsActivity.this, SaghinamehDetailActivity.class);
                intent.putExtra("saghinamehTitle", poem.getTitle());
            } else if (title.contains("منتسب")) {
                intent = new Intent(SearchResultsActivity.this, MontasabDetailActivity.class);
                intent.putExtra("montasabTitle", poem.getTitle());
            } else {
                intent = new Intent(SearchResultsActivity.this, GhazalDetailActivity.class);
                intent.putExtra("ghazalTitle", poem.getTitle());
            }
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