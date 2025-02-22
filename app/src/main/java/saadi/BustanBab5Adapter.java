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

public class BustanBab5Adapter extends RecyclerView.Adapter<BustanBab5Adapter.BustanBab5ViewHolder> {

    private List<BustanBab5Poem> poemList;
    private OnItemClickListener listener;
    private Context context;

    public BustanBab5Adapter(List<BustanBab5Poem> poemList, OnItemClickListener listener, Context context) {
        this.poemList = poemList;
        this.listener = listener;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(BustanBab5Poem poem);
    }

    @NonNull
    @Override
    public BustanBab5ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bustan_bab5, parent, false);
        return new BustanBab5ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BustanBab5ViewHolder holder, int position) {
        BustanBab5Poem poem = poemList.get(position);
        holder.title.setText(poem.getTitle());

        // نمایش مصرع اول شعر
        if (!poem.getVerses().isEmpty()) {
            String[] lines = poem.getVerses().get(0).getText().split(" / ");
            holder.firstVerse.setText(lines.length > 0 ? lines[0] : poem.getVerses().get(0).getText());
        }

        // بررسی وضعیت علاقه‌مندی
        SharedPreferences sharedPreferences = context.getSharedPreferences("Favorites", Context.MODE_PRIVATE);
        Set<String> favorites = sharedPreferences.getStringSet("favorites", new HashSet<>());
        holder.favoriteIcon.setVisibility(favorites.contains(poem.getTitle()) ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(poem));
    }

    @Override
    public int getItemCount() {
        return poemList.size();
    }

    public static class BustanBab5ViewHolder extends RecyclerView.ViewHolder {
        TextView title, firstVerse;
        ImageView favoriteIcon;

        public BustanBab5ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            firstVerse = itemView.findViewById(R.id.first_verse);
            favoriteIcon = itemView.findViewById(R.id.favorite_icon);
        }
    }
}