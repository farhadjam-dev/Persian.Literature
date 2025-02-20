package hafez;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jamlab.adab.R;
import java.util.List;

public class GhazalAdapter extends RecyclerView.Adapter<GhazalAdapter.GhazalViewHolder> {

    private final List<Ghazal> ghazalList;
    private final OnItemClickListener listener;

    public GhazalAdapter(List<Ghazal> ghazalList, OnItemClickListener listener) {
        this.ghazalList = ghazalList;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Ghazal ghazal);
    }

    @NonNull
    @Override
    public GhazalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ghazal, parent, false);
        return new GhazalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GhazalViewHolder holder, int position) {
        Ghazal ghazal = ghazalList.get(position);
        holder.title.setText(ghazal.getTitle());

        // نمایش مصرع اول غزل
        if (!ghazal.getVerses().isEmpty()) {
            String firstVerse = ghazal.getVerses().get(0).getText();
            // جدا کردن مصرع اول در صورت وجود "/" یا خط جدید
            String[] parts = firstVerse.split(" / |\n");
            holder.firstVerse.setText(parts[0]); // فقط مصرع اول
        } else {
            holder.firstVerse.setText("..."); // در صورت نبود بیت
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(ghazal));
    }

    @Override
    public int getItemCount() {
        return ghazalList.size();
    }

    public static class GhazalViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView firstVerse;

        public GhazalViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            firstVerse = itemView.findViewById(R.id.first_verse);
        }
    }
}