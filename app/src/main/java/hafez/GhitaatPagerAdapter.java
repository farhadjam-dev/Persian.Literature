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

public class GhitaatPagerAdapter extends RecyclerView.Adapter<GhitaatPagerAdapter.GhitaatViewHolder> {

    private Context context;
    private List<String> ghitaatTitles;

    public GhitaatPagerAdapter(Context context, List<String> ghitaatTitles) {
        this.context = context;
        this.ghitaatTitles = ghitaatTitles;
    }

    @NonNull
    @Override
    public GhitaatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ghitaat_page_item, parent, false);
        return new GhitaatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GhitaatViewHolder holder, int position) {
        String ghitaatTitle = ghitaatTitles.get(position);
        List<Verse> verses = ((GhitaatDetailActivity) context).loadVersesFromJson(ghitaatTitle);
        VerseAdapter verseAdapter = new VerseAdapter(verses);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setAdapter(verseAdapter);
    }

    @Override
    public int getItemCount() {
        return ghitaatTitles.size();
    }

    static class GhitaatViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        GhitaatViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.verse_recycler_view);
        }
    }
}