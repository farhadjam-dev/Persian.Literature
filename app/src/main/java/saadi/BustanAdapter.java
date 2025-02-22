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

public class BustanAdapter extends RecyclerView.Adapter<BustanAdapter.BustanViewHolder> {

    private List<BustanItem> bustanItemList;
    private OnItemClickListener listener;

    public BustanAdapter(List<BustanItem> bustanItemList, OnItemClickListener listener) {
        this.bustanItemList = bustanItemList;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(BustanItem bustanItem);
    }

    @NonNull
    @Override
    public BustanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bustan, parent, false);
        return new BustanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BustanViewHolder holder, int position) {
        BustanItem bustanItem = bustanItemList.get(position);
        holder.title.setText(bustanItem.getTitle());
        holder.icon.setImageResource(bustanItem.getImageResId());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(bustanItem));
    }

    @Override
    public int getItemCount() {
        return bustanItemList.size();
    }

    public static class BustanViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;

        public BustanViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            icon = itemView.findViewById(R.id.icon);
        }
    }
}