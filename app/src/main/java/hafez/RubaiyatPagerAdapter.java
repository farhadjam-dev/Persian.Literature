package hafez;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jamlab.adab.R;
import java.util.List;

public class RubaiyatPagerAdapter extends RecyclerView.Adapter<RubaiyatPagerAdapter.RubaiyatViewHolder> {

    private Context context;
    private List<String> rubaiyatTitles;

    public RubaiyatPagerAdapter(Context context, List<String> rubaiyatTitles) {
        this.context = context;
        this.rubaiyatTitles = rubaiyatTitles;
    }

    @NonNull
    @Override
    public RubaiyatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rubaiyat_page_item, parent, false);
        return new RubaiyatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RubaiyatViewHolder holder, int position) {
        String rubaiyatTitle = rubaiyatTitles.get(position);
        List<Verse> verses = ((RubaiyatDetailActivity) context).loadVersesFromJson(rubaiyatTitle);
        VerseAdapter verseAdapter = new VerseAdapter(verses);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setAdapter(verseAdapter);
    }

    @Override
    public int getItemCount() {
        return rubaiyatTitles.size();
    }

    static class RubaiyatViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        RubaiyatViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.verse_recycler_view);
        }
    }
}