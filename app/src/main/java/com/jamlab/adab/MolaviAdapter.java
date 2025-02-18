package com.jamlab.adab;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MolaviAdapter extends RecyclerView.Adapter<MolaviAdapter.MolaviViewHolder> {

    private List<MolaviItem> molaviItemList;

    public MolaviAdapter(List<MolaviItem> molaviItemList) {
        this.molaviItemList = molaviItemList;
    }

    @NonNull
    @Override
    public MolaviViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_molavi, parent, false);
        return new MolaviViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MolaviViewHolder holder, int position) {
        MolaviItem molaviItem = molaviItemList.get(position);
        holder.title.setText(molaviItem.getTitle());
        holder.icon.setImageResource(molaviItem.getIconResId());
    }

    @Override
    public int getItemCount() {
        return molaviItemList.size();
    }

    public static class MolaviViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;

        public MolaviViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
        }
    }
}