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

public class MavaezAdapter extends RecyclerView.Adapter<MavaezAdapter.MavaezViewHolder> {

    private List<MavaezItem> mavaezItemList;
    private OnItemClickListener listener;

    public MavaezAdapter(List<MavaezItem> mavaezItemList, OnItemClickListener listener) {
        this.mavaezItemList = mavaezItemList;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(MavaezItem mavaezItem);
    }

    @NonNull
    @Override
    public MavaezViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mavaez, parent, false);
        return new MavaezViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MavaezViewHolder holder, int position) {
        MavaezItem mavaezItem = mavaezItemList.get(position);
        holder.title.setText(mavaezItem.getTitle());
        holder.icon.setImageResource(mavaezItem.getImageResId());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(mavaezItem));
    }

    @Override
    public int getItemCount() {
        return mavaezItemList.size();
    }

    public static class MavaezViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;

        public MavaezViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            icon = itemView.findViewById(R.id.icon);
        }
    }
}