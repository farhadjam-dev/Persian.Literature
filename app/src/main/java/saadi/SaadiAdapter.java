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

public class SaadiAdapter extends RecyclerView.Adapter<SaadiAdapter.SaadiViewHolder> {

    private List<SaadiItem> saadiItemList;
    private OnItemClickListener listener;

    public SaadiAdapter(List<SaadiItem> saadiItemList, OnItemClickListener listener) {
        this.saadiItemList = saadiItemList;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(SaadiItem saadiItem);
    }

    @NonNull
    @Override
    public SaadiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saadi, parent, false);
        return new SaadiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaadiViewHolder holder, int position) {
        SaadiItem saadiItem = saadiItemList.get(position);
        holder.title.setText(saadiItem.getTitle());
        holder.icon.setImageResource(saadiItem.getIconResId());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(saadiItem));
    }

    @Override
    public int getItemCount() {
        return saadiItemList.size();
    }

    public static class SaadiViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;

        public SaadiViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
        }
    }
}