package saadi;

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

public class SaadiGhazalAdapter extends RecyclerView.Adapter<SaadiGhazalAdapter.SaadiGhazalViewHolder> {

    private List<SaadiGhazal> ghazalList;
    private OnItemClickListener listener;
    private Context context;

    public SaadiGhazalAdapter(List<SaadiGhazal> ghazalList, OnItemClickListener listener, Context context) {
        this.ghazalList = ghazalList;
        this.listener = listener;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(SaadiGhazal ghazal);
    }

    @NonNull
    @Override
    public SaadiGhazalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ghazal, parent, false);
        return new SaadiGhazalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaadiGhazalViewHolder holder, int position) {
        SaadiGhazal ghazal = ghazalList.get(position);
        holder.title.setText(ghazal.getTitle());

        // نمایش مصرع اول غزل
        if (!ghazal.getVerses().isEmpty()) {
            String[] lines = ghazal.getVerses().get(0).getText().split(" / ");
            holder.firstVerse.setText(lines.length > 0 ? lines[0] : ghazal.getVerses().get(0).getText());
        }

        // بررسی وضعیت علاقه‌مندی
        SharedPreferences sharedPreferences = context.getSharedPreferences("Favorites", Context.MODE_PRIVATE);
        Set<String> favorites = sharedPreferences.getStringSet("favorites", new HashSet<>());
        holder.favoriteIcon.setVisibility(favorites.contains(ghazal.getTitle()) ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(ghazal));
    }

    @Override
    public int getItemCount() {
        return ghazalList.size();
    }

    public static class SaadiGhazalViewHolder extends RecyclerView.ViewHolder {
        TextView title, firstVerse;
        ImageView favoriteIcon;

        public SaadiGhazalViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            firstVerse = itemView.findViewById(R.id.first_verse);
            favoriteIcon = itemView.findViewById(R.id.favorite_icon);
        }
    }
}