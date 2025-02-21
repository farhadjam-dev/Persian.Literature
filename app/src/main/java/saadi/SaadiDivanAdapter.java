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

public class SaadiDivanAdapter extends RecyclerView.Adapter<SaadiDivanAdapter.SaadiDivanViewHolder> {

    private List<SaadiDivanItem> saadiDivanItemList;
    private OnItemClickListener listener;

    public SaadiDivanAdapter(List<SaadiDivanItem> saadiDivanItemList, OnItemClickListener listener) {
        this.saadiDivanItemList = saadiDivanItemList;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(SaadiDivanItem saadiDivanItem);
    }

    @NonNull
    @Override
    public SaadiDivanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saadi_divan, parent, false);
        return new SaadiDivanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaadiDivanViewHolder holder, int position) {
        SaadiDivanItem saadiDivanItem = saadiDivanItemList.get(position);
        holder.title.setText(saadiDivanItem.getTitle());
        holder.icon.setImageResource(saadiDivanItem.getIconResId());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(saadiDivanItem));
    }

    @Override
    public int getItemCount() {
        return saadiDivanItemList.size();
    }

    public static class SaadiDivanViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;

        public SaadiDivanViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
        }
    }
}