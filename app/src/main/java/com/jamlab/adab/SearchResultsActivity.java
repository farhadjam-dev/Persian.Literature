package com.jamlab.adab;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchResultAdapter searchResultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        recyclerView = findViewById(R.id.search_results_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // دریافت نتایج جست‌وجو از MainActivity
        List<String> searchResults = getIntent().getStringArrayListExtra("search_results");

        searchResultAdapter = new SearchResultAdapter(searchResults);
        recyclerView.setAdapter(searchResultAdapter);
    }
}