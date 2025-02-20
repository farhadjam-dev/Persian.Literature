package favorits;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jamlab.adab.R;

import java.util.List;
import hafez.GhazalDetailActivity;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private List<Ghazal> favoriteGhazals; // لیست غزلیات علاقه‌مندی
    private Context context;

    public FavoriteAdapter(Context context, List<Ghazal> favoriteGhazals) {
        this.context = context;
        this.favoriteGhazals = favoriteGhazals;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_poem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ghazal ghazal = favoriteGhazals.get(position);

        // نمایش عنوان و بیت اول
        holder.poemTitle.setText(ghazal.getTitle());
        holder.poemExcerpt.setText(ghazal.getFirstVerse());

        // تنظیم کلیک روی آیتم
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, GhazalDetailActivity.class);
            intent.putExtra("ghazalTitle", ghazal.getTitle());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return favoriteGhazals.size();
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