package search;

import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.jamlab.adab.Poem;
import com.jamlab.adab.R;
import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private List<Poem> searchResults;
    private OnItemClickListener onItemClickListener;
    private String searchQuery; // عبارت جست‌وجو

    public interface OnItemClickListener {
        void onItemClick(Poem poem);
    }

    public SearchResultAdapter(List<Poem> searchResults, String searchQuery, OnItemClickListener onItemClickListener) {
        this.searchResults = searchResults;
        this.searchQuery = searchQuery;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Poem poem = searchResults.get(position);

        // تنظیم عنوان غزل
        holder.titleTextView.setText(poem.getTitle());

        // پیدا کردن بیتی که شامل عبارت جست‌وجو است
        String matchingVerse = findMatchingVerse(poem.getText(), searchQuery);

        // هایلایت کردن عبارت جست‌وجو در بیت مرتبط
        SpannableString spannableString = new SpannableString(matchingVerse);
        int startIndex = matchingVerse.indexOf(searchQuery);
        if (startIndex != -1) { // اگر عبارت جست‌وجو در بیت پیدا شد
            int endIndex = startIndex + searchQuery.length();
            spannableString.setSpan(
                    new BackgroundColorSpan(0xFFFFFF00), // رنگ زرد برای هایلایت
                    startIndex,
                    endIndex,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
        holder.verseTextView.setText(spannableString);

        // تنظیم کلیک روی کارت
        holder.cardView.setOnClickListener(v -> onItemClickListener.onItemClick(poem));
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    // یافتن بیت مرتبط با عبارت جست‌وجو
    private String findMatchingVerse(String poemText, String query) {
        String[] verses = poemText.split("\n"); // فرض می‌کنیم بیت‌ها با خط جدید جدا شده‌اند
        for (String verse : verses) {
            if (verse.contains(query)) {
                return verse.trim();
            }
        }
        return poemText.split("\n")[0]; // در صورت عدم یافتن، بیت اول را برگردان
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView titleTextView;
        TextView verseTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            titleTextView = itemView.findViewById(R.id.poem_title);
            verseTextView = itemView.findViewById(R.id.poem_verse);
        }
    }
}