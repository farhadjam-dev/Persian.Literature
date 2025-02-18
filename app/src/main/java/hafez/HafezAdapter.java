package hafez;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jamlab.adab.R;

import java.util.List;

public class HafezAdapter extends RecyclerView.Adapter<HafezAdapter.HafezViewHolder> {

    private List<HafezItem> hafezItemList;
    private OnItemClickListener listener;

    public HafezAdapter(List<HafezItem> hafezItemList, OnItemClickListener listener) {
        this.hafezItemList = hafezItemList;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(HafezItem hafezItem);
    }

    @NonNull
    @Override
    public HafezViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hafez, parent, false);
        return new HafezViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HafezViewHolder holder, int position) {
        HafezItem hafezItem = hafezItemList.get(position);
        holder.title.setText(hafezItem.getTitle());
        holder.icon.setImageResource(hafezItem.getIconResId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(hafezItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hafezItemList.size();
    }

    public static class HafezViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;

        public HafezViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
        }
    }
}