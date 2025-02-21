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

public class GhasaidAdapter extends RecyclerView.Adapter<GhasaidAdapter.GhasaidViewHolder> {

    private List<Ghasaid> ghasaidList;
    private OnItemClickListener listener;
    private Context context;

    public GhasaidAdapter(List<Ghasaid> ghasaidList, OnItemClickListener listener, Context context) {
        this.ghasaidList = ghasaidList;
        this.listener = listener;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(Ghasaid ghasaid);
    }

    @NonNull
    @Override
    public GhasaidViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ghasaid, parent, false);
        return new GhasaidViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GhasaidViewHolder holder, int position) {
        Ghasaid ghasaid = ghasaidList.get(position);
        holder.title.setText(ghasaid.getTitle());

        // نمایش مصرع اول قصیده
        if (!ghasaid.getVerses().isEmpty()) {
            String[] lines = ghasaid.getVerses().get(0).getText().split(" / ");
            holder.firstVerse.setText(lines.length > 0 ? lines[0] : ghasaid.getVerses().get(0).getText());
        }

        // بررسی وضعیت علاقه‌مندی
        SharedPreferences sharedPreferences = context.getSharedPreferences("Favorites", Context.MODE_PRIVATE);
        Set<String> favorites = sharedPreferences.getStringSet("favorites", new HashSet<>());
        if (favorites.contains(ghasaid.getTitle())) {
            holder.favoriteIcon.setVisibility(View.VISIBLE);
        } else {
            holder.favoriteIcon.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(ghasaid));
    }

    @Override
    public int getItemCount() {
        return ghasaidList.size();
    }

    public static class GhasaidViewHolder extends RecyclerView.ViewHolder {
        TextView title, firstVerse;
        ImageView favoriteIcon;

        public GhasaidViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            firstVerse = itemView.findViewById(R.id.first_verse);
            favoriteIcon = itemView.findViewById(R.id.favorite_icon);
        }
    }
}