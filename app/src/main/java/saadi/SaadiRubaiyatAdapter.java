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

public class SaadiRubaiyatAdapter extends RecyclerView.Adapter<SaadiRubaiyatAdapter.SaadiRubaiyatViewHolder> {

    private List<SaadiRubaiyat> rubaiyatList;
    private OnItemClickListener listener;
    private Context context;

    public SaadiRubaiyatAdapter(List<SaadiRubaiyat> rubaiyatList, OnItemClickListener listener, Context context) {
        this.rubaiyatList = rubaiyatList;
        this.listener = listener;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(SaadiRubaiyat rubaiyat);
    }

    @NonNull
    @Override
    public SaadiRubaiyatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ghazal, parent, false);
        return new SaadiRubaiyatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaadiRubaiyatViewHolder holder, int position) {
        SaadiRubaiyat rubaiyat = rubaiyatList.get(position);
        holder.title.setText(rubaiyat.getTitle());

        // نمایش مصرع اول رباعی
        if (!rubaiyat.getVerses().isEmpty()) {
            String[] lines = rubaiyat.getVerses().get(0).getText().split(" / ");
            holder.firstVerse.setText(lines.length > 0 ? lines[0] : rubaiyat.getVerses().get(0).getText());
        }

        // بررسی وضعیت علاقه‌مندی
        SharedPreferences sharedPreferences = context.getSharedPreferences("Favorites", Context.MODE_PRIVATE);
        Set<String> favorites = sharedPreferences.getStringSet("favorites", new HashSet<>());
        holder.favoriteIcon.setVisibility(favorites.contains(rubaiyat.getTitle()) ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(rubaiyat));
    }

    @Override
    public int getItemCount() {
        return rubaiyatList.size();
    }

    public static class SaadiRubaiyatViewHolder extends RecyclerView.ViewHolder {
        TextView title, firstVerse;
        ImageView favoriteIcon;

        public SaadiRubaiyatViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            firstVerse = itemView.findViewById(R.id.first_verse);
            favoriteIcon = itemView.findViewById(R.id.favorite_icon);
        }
    }
}