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

public class MontasabAdapter extends RecyclerView.Adapter<MontasabAdapter.MontasabViewHolder> {

    private List<Montasab> montasabList;
    private OnItemClickListener listener;
    private Context context;

    public MontasabAdapter(List<Montasab> montasabList, OnItemClickListener listener, Context context) {
        this.montasabList = montasabList;
        this.listener = listener;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(Montasab montasab);
    }

    @NonNull
    @Override
    public MontasabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_montasab, parent, false);
        return new MontasabViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MontasabViewHolder holder, int position) {
        Montasab montasab = montasabList.get(position);
        holder.title.setText(montasab.getTitle());

        // نمایش مصرع اول شعر منتسب
        if (!montasab.getVerses().isEmpty()) {
            String[] lines = montasab.getVerses().get(0).getText().split(" / ");
            holder.firstVerse.setText(lines.length > 0 ? lines[0] : montasab.getVerses().get(0).getText());
        }

        // بررسی وضعیت علاقه‌مندی
        SharedPreferences sharedPreferences = context.getSharedPreferences("Favorites", Context.MODE_PRIVATE);
        Set<String> favorites = sharedPreferences.getStringSet("favorites", new HashSet<>());
        if (favorites.contains(montasab.getTitle())) {
            holder.favoriteIcon.setVisibility(View.VISIBLE);
        } else {
            holder.favoriteIcon.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(montasab));
    }

    @Override
    public int getItemCount() {
        return montasabList.size();
    }

    public static class MontasabViewHolder extends RecyclerView.ViewHolder {
        TextView title, firstVerse;
        ImageView favoriteIcon;

        public MontasabViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            firstVerse = itemView.findViewById(R.id.first_verse);
            favoriteIcon = itemView.findViewById(R.id.favorite_icon);
        }
    }
}