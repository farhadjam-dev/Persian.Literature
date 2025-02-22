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

public class SaadiMalhaghatAdapter extends RecyclerView.Adapter<SaadiMalhaghatAdapter.SaadiMalhaghatViewHolder> {

    private List<SaadiMalhaghat> malhaghatList;
    private OnItemClickListener listener;
    private Context context;

    public SaadiMalhaghatAdapter(List<SaadiMalhaghat> malhaghatList, OnItemClickListener listener, Context context) {
        this.malhaghatList = malhaghatList;
        this.listener = listener;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(SaadiMalhaghat malhaghat);
    }

    @NonNull
    @Override
    public SaadiMalhaghatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ghazal, parent, false);
        return new SaadiMalhaghatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaadiMalhaghatViewHolder holder, int position) {
        SaadiMalhaghat malhaghat = malhaghatList.get(position);
        holder.title.setText(malhaghat.getTitle());

        // نمایش مصرع اول ملحقات
        if (!malhaghat.getVerses().isEmpty()) {
            String[] lines = malhaghat.getVerses().get(0).getText().split(" / ");
            holder.firstVerse.setText(lines.length > 0 ? lines[0] : malhaghat.getVerses().get(0).getText());
        }

        // بررسی وضعیت علاقه‌مندی
        SharedPreferences sharedPreferences = context.getSharedPreferences("Favorites", Context.MODE_PRIVATE);
        Set<String> favorites = sharedPreferences.getStringSet("favorites", new HashSet<>());
        holder.favoriteIcon.setVisibility(favorites.contains(malhaghat.getTitle()) ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(malhaghat));
    }

    @Override
    public int getItemCount() {
        return malhaghatList.size();
    }

    public static class SaadiMalhaghatViewHolder extends RecyclerView.ViewHolder {
        TextView title, firstVerse;
        ImageView favoriteIcon;

        public SaadiMalhaghatViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            firstVerse = itemView.findViewById(R.id.first_verse);
            favoriteIcon = itemView.findViewById(R.id.favorite_icon);
        }
    }
}