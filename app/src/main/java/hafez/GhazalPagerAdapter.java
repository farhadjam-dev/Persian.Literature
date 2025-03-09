package hafez;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jamlab.adab.R;

import java.util.List;

public class GhazalPagerAdapter extends RecyclerView.Adapter<GhazalPagerAdapter.GhazalViewHolder> {

    private Context context;
    private List<String> ghazalTitles;

    public GhazalPagerAdapter(Context context, List<String> ghazalTitles) {
        this.context = context;
        this.ghazalTitles = ghazalTitles;
    }

    @NonNull
    @Override
    public GhazalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ghazal_page_item, parent, false);
        return new GhazalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GhazalViewHolder holder, int position) {
        String ghazalTitle = ghazalTitles.get(position);
        List<Verse> verses = ((GhazalDetailActivity) context).loadVersesFromJson(ghazalTitle);
        VerseAdapter verseAdapter = new VerseAdapter(verses);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setAdapter(verseAdapter);
    }

    @Override
    public int getItemCount() {
        return ghazalTitles.size();
    }

    static class GhazalViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        GhazalViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.verse_recycler_view);
        }
    }
}