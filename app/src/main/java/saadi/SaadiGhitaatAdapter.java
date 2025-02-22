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

public class SaadiGhitaatAdapter extends RecyclerView.Adapter<SaadiGhitaatAdapter.SaadiGhitaatViewHolder> {

    private List<SaadiGhitaat> ghitaatList;
    private OnItemClickListener listener;
    private Context context;

    public SaadiGhitaatAdapter(List<SaadiGhitaat> ghitaatList, OnItemClickListener listener, Context context) {
        this.ghitaatList = ghitaatList;
        this.listener = listener;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(SaadiGhitaat ghitaat);
    }

    @NonNull
    @Override
    public SaadiGhitaatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ghazal, parent, false);
        return new SaadiGhitaatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaadiGhitaatViewHolder holder, int position) {
        SaadiGhitaat ghitaat = ghitaatList.get(position);
        holder.title.setText(ghitaat.getTitle());

        // نمایش مصرع اول قطعه
        if (!ghitaat.getVerses().isEmpty()) {
            String[] lines = ghitaat.getVerses().get(0).getText().split(" / ");
            holder.firstVerse.setText(lines.length > 0 ? lines[0] : ghitaat.getVerses().get(0).getText());
        }

        // بررسی وضعیت علاقه‌مندی
        SharedPreferences sharedPreferences = context.getSharedPreferences("Favorites", Context.MODE_PRIVATE);
        Set<String> favorites = sharedPreferences.getStringSet("favorites", new HashSet<>());
        holder.favoriteIcon.setVisibility(favorites.contains(ghitaat.getTitle()) ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(ghitaat));
    }

    @Override
    public int getItemCount() {
        return ghitaatList.size();
    }

    public static class SaadiGhitaatViewHolder extends RecyclerView.ViewHolder {
        TextView title, firstVerse;
        ImageView favoriteIcon;

        public SaadiGhitaatViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            firstVerse = itemView.findViewById(R.id.first_verse);
            favoriteIcon = itemView.findViewById(R.id.favorite_icon);
        }
    }
}