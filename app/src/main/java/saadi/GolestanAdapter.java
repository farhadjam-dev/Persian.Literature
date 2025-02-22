package saadi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jamlab.adab.R;
import java.util.List;

public class GolestanAdapter extends RecyclerView.Adapter<GolestanAdapter.GolestanViewHolder> {

    private List<GolestanItem> golestanItemList;
    private OnItemClickListener listener;

    public GolestanAdapter(List<GolestanItem> golestanItemList, OnItemClickListener listener) {
        this.golestanItemList = golestanItemList;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(GolestanItem golestanItem);
    }

    @NonNull
    @Override
    public GolestanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_golestan, parent, false);
        return new GolestanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GolestanViewHolder holder, int position) {
        GolestanItem golestanItem = golestanItemList.get(position);
        holder.title.setText(golestanItem.getTitle());
        holder.icon.setImageResource(golestanItem.getImageResId());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(golestanItem));
    }

    @Override
    public int getItemCount() {
        return golestanItemList.size();
    }

    public static class GolestanViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;

        public GolestanViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            icon = itemView.findViewById(R.id.icon);
        }
    }
}