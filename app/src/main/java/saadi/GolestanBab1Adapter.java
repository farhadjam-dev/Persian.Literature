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

public class GolestanBab1Adapter extends RecyclerView.Adapter<GolestanBab1Adapter.GolestanBab1ViewHolder> {

    private List<GolestanBab1Hekayat> hekayatList;
    private OnItemClickListener listener;
    private Context context;

    public GolestanBab1Adapter(List<GolestanBab1Hekayat> hekayatList, OnItemClickListener listener, Context context) {
        this.hekayatList = hekayatList;
        this.listener = listener;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(GolestanBab1Hekayat hekayat);
    }

    @NonNull
    @Override
    public GolestanBab1ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_golestan_bab1, parent, false);
        return new GolestanBab1ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GolestanBab1ViewHolder holder, int position) {
        GolestanBab1Hekayat hekayat = hekayatList.get(position);
        holder.title.setText(hekayat.getTitle());

        // نمایش فقط جمله اول از نثر
        String firstSentence = hekayat.getContent().split("\\.")[0] + ".";
        holder.firstLine.setText(firstSentence);

        // بررسی وضعیت علاقه‌مندی
        SharedPreferences sharedPreferences = context.getSharedPreferences("Favorites", Context.MODE_PRIVATE);
        Set<String> favorites = sharedPreferences.getStringSet("favorites", new HashSet<>());
        holder.favoriteIcon.setVisibility(favorites.contains(hekayat.getTitle()) ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(hekayat));
    }

    @Override
    public int getItemCount() {
        return hekayatList.size();
    }

    public static class GolestanBab1ViewHolder extends RecyclerView.ViewHolder {
        TextView title, firstLine;
        ImageView favoriteIcon;

        public GolestanBab1ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            firstLine = itemView.findViewById(R.id.first_line);
            favoriteIcon = itemView.findViewById(R.id.favorite_icon);
        }
    }
}