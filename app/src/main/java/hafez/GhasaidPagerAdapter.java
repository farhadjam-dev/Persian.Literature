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

public class GhasaidPagerAdapter extends RecyclerView.Adapter<GhasaidPagerAdapter.GhasaidViewHolder> {

    private Context context;
    private List<String> ghasaidTitles;

    public GhasaidPagerAdapter(Context context, List<String> ghasaidTitles) {
        this.context = context;
        this.ghasaidTitles = ghasaidTitles;
    }

    @NonNull
    @Override
    public GhasaidViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ghasaid_page_item, parent, false);
        return new GhasaidViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GhasaidViewHolder holder, int position) {
        String ghasaidTitle = ghasaidTitles.get(position);
        PoemDetails poemDetails = ((GhasaidDetailActivity) context).loadPoemDetails(ghasaidTitle);
        List<Verse> verses = poemDetails.getVerses();
        VerseAdapter verseAdapter = new VerseAdapter(verses);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setAdapter(verseAdapter);
    }

    @Override
    public int getItemCount() {
        return ghasaidTitles.size();
    }

    static class GhasaidViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        GhasaidViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.verse_recycler_view);
        }
    }
}