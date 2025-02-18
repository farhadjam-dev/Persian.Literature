package com.jamlab.adab;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PoemDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poem_detail);

        TextView titleTextView = findViewById(R.id.poem_title);
        TextView textTextView = findViewById(R.id.poem_text);

        // دریافت شعر از Intent
        Poem poem = getIntent().getParcelableExtra("poem");

        // نمایش عنوان و متن شعر
        titleTextView.setText(poem.getTitle());
        textTextView.setText(poem.getText());
    }
}