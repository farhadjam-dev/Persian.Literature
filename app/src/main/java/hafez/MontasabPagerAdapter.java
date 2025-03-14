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

public class MontasabPagerAdapter extends RecyclerView.Adapter<MontasabPagerAdapter.MontasabViewHolder> {

    private Context context;
    private List<String> montasabTitles;

    public MontasabPagerAdapter(Context context, List<String> montasabTitles) {
        this.context = context;
        this.montasabTitles = montasabTitles;
    }

    @NonNull
    @Override
    public MontasabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.montasab_page_item, parent, false);
        return new MontasabViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MontasabViewHolder holder, int position) {
        String montasabTitle = montasabTitles.get(position);
        PoemDetails poemDetails = ((MontasabDetailActivity) context).loadPoemDetails(montasabTitle);
        List<Verse> verses = poemDetails.getVerses();
        VerseAdapter verseAdapter = new VerseAdapter(verses);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setAdapter(verseAdapter);
    }

    @Override
    public int getItemCount() {
        return montasabTitles.size();
    }

    static class MontasabViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        MontasabViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.verse_recycler_view);
        }
    }
}