package com.jamlab.adab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private List<String> favoritePoems; // لیست شعرهای علاقه‌مندی

    public FavoriteAdapter(List<String> favoritePoems) {
        this.favoritePoems = favoritePoems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate کردن Layout برای هر آیتم
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_poem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // تنظیم داده‌ها برای هر آیتم
        String poem = favoritePoems.get(position);

        // جدا کردن عنوان و بخشی از شعر با استفاده از " - "
        if (poem.contains(" - ")) {
            String[] parts = poem.split(" - ");
            holder.poemTitle.setText(parts[0]); // عنوان شعر
            holder.poemExcerpt.setText(parts[1]); // بخشی از شعر
        } else {
            holder.poemTitle.setText(poem); // اگر فرمت درست نباشد، کل متن را به عنوان عنوان قرار دهید
            holder.poemExcerpt.setText("..."); // متن پیش‌فرض
        }
    }

    @Override
    public int getItemCount() {
        // تعداد آیتم‌ها در لیست
        return favoritePoems.size();
    }

    // کلاس ViewHolder برای نگهداری عناصر UI
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView poemTitle, poemExcerpt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // اتصال عناصر UI
            poemTitle = itemView.findViewById(R.id.poem_title);
            poemExcerpt = itemView.findViewById(R.id.poem_excerpt);
        }
    }
}