package hafez;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jamlab.adab.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GhazalAdapter extends RecyclerView.Adapter<GhazalAdapter.GhazalViewHolder> {

    private final List<Ghazal> ghazalList;
    private final OnItemClickListener listener;
    private final Context context; // برای دسترسی به SharedPreferences

    public GhazalAdapter(List<Ghazal> ghazalList, OnItemClickListener listener, Context context) {
        this.ghazalList = ghazalList;
        this.listener = listener;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(Ghazal ghazal);
    }

    @NonNull
    @Override
    public GhazalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ghazal, parent, false);
        return new GhazalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GhazalViewHolder holder, int position) {
        Ghazal ghazal = ghazalList.get(position);
        holder.title.setText(ghazal.getTitle());

        // نمایش مصرع اول غزل
        if (!ghazal.getVerses().isEmpty()) {
            String firstVerse = ghazal.getVerses().get(0).getText();
            String[] parts = firstVerse.split(" / |\n");
            holder.firstVerse.setText(parts[0]);
        } else {
            holder.firstVerse.setText("...");
        }

        // بررسی وضعیت علاقه‌مندی و نمایش ستاره
        SharedPreferences sharedPreferences = context.getSharedPreferences("Favorites", Context.MODE_PRIVATE);
        Set<String> favorites = sharedPreferences.getStringSet("favorites", new HashSet<>());
        if (favorites.contains(ghazal.getTitle())) {
            holder.favoriteIcon.setVisibility(View.VISIBLE); // نمایش ستاره
        } else {
            holder.favoriteIcon.setVisibility(View.GONE); // مخفی کردن ستاره
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(ghazal));
    }

    @Override
    public int getItemCount() {
        return ghazalList.size();
    }

    public static class GhazalViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView firstVerse;
        ImageView favoriteIcon;

        public GhazalViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            firstVerse = itemView.findViewById(R.id.first_verse);
            favoriteIcon = itemView.findViewById(R.id.favorite_icon);
        }
    }
}