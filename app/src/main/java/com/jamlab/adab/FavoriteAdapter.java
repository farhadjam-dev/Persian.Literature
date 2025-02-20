package com.jamlab.adab;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import hafez.GhazalDetailActivity;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private List<String> favoritePoems; // لیست شعرهای علاقه‌مندی
    private Context context;

    public FavoriteAdapter(Context context, List<String> favoritePoems) {
        this.context = context;
        this.favoritePoems = favoritePoems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_poem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String poem = favoritePoems.get(position);

        // جدا کردن عنوان و بخشی از شعر با استفاده از " - "
        if (poem.contains(" - ")) {
            String[] parts = poem.split(" - ");
            holder.poemTitle.setText(parts[0]); // عنوان شعر
            holder.poemExcerpt.setText(parts[1]); // بخشی از شعر
        } else {
            holder.poemTitle.setText(poem);
            holder.poemExcerpt.setText("...");
        }

        // تنظیم کلیک روی آیتم
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, GhazalDetailActivity.class);
            intent.putExtra("ghazalTitle", holder.poemTitle.getText().toString());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return favoritePoems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView poemTitle, poemExcerpt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            poemTitle = itemView.findViewById(R.id.poem_title);
            poemExcerpt = itemView.findViewById(R.id.poem_excerpt);
        }
    }
}