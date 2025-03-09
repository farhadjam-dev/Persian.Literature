package favorits;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
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

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private List<Poem> favoritePoems; // لیست اشعار علاقه‌مندی
    private Context context;

    public FavoriteAdapter(Context context, List<Poem> favoritePoems) {
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
        Poem poem = favoritePoems.get(position);

        // نمایش عنوان و بیت اول
        holder.poemTitle.setText(poem.getTitle());
        String[] verses = poem.getText().split("\n");
        holder.poemExcerpt.setText(verses.length > 0 ? verses[0] : "...");

        // تنظیم کلیک روی آیتم
        holder.itemView.setOnClickListener(v -> {
            Intent intent;
            String title = poem.getTitle().toLowerCase();
            if (title.contains("غزل")) {
                intent = new Intent(context, GhazalDetailActivity.class);
                intent.putExtra("ghazalTitle", poem.getTitle());
            } else if (title.contains("رباعی")) {
                intent = new Intent(context, RubaiyatDetailActivity.class);
                intent.putExtra("rubaiyatTitle", poem.getTitle());
            } else if (title.contains("قطعه")) {
                intent = new Intent(context, GhitaatDetailActivity.class);
                intent.putExtra("ghitaatTitle", poem.getTitle());
            } else if (title.contains("قصیده")) {
                intent = new Intent(context, GhasaidDetailActivity.class);
                intent.putExtra("ghasaidTitle", poem.getTitle());
            } else if (title.contains("مثنوی")) {
                intent = new Intent(context, MasnaviDetailActivity.class);
                intent.putExtra("masnaviTitle", poem.getTitle());
            } else if (title.contains("ساقی‌نامه")) {
                intent = new Intent(context, SaghinamehDetailActivity.class);
                intent.putExtra("saghinamehTitle", poem.getTitle());
            } else if (title.contains("منتسب")) {
                intent = new Intent(context, MontasabDetailActivity.class);
                intent.putExtra("montasabTitle", poem.getTitle());
            } else {
                // پیش‌فرض: غزل
                intent = new Intent(context, GhazalDetailActivity.class);
                intent.putExtra("ghazalTitle", poem.getTitle());
            }
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return favoritePoems.size();
    }

    // متد برای به‌روزرسانی لیست علاقه‌مندی‌ها
    public void updateFavorites(List<Poem> newFavoritePoems) {
        this.favoritePoems = newFavoritePoems;
        notifyDataSetChanged();
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