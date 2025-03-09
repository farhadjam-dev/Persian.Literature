package search;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.jamlab.adab.Poem;
import com.jamlab.adab.R;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private List<Poem> searchResults;
    private OnItemClickListener onItemClickListener;
    private String searchQuery;

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

        holder.titleTextView.setText(poem.getTitle());

        String matchingVerse = findMatchingVerse(poem.getText(), searchQuery);

        // آماده‌سازی متن برای اعمال رنگ
        SpannableString spannableString = new SpannableString(matchingVerse);
        String normalizedVerse = normalizeText(matchingVerse);
        String normalizedQuery = normalizeText(searchQuery);
        Set<String> queryWords = new HashSet<>(Arrays.asList(normalizedQuery.split("\\s+")));

        // جدا کردن کلمات بیت
        String[] words = matchingVerse.split("\\s+");
        int currentPosition = 0;

        // بررسی هر کلمه و اعمال رنگ قرمز به کلمات مطابقت‌یافته
        for (String word : words) {
            String normalizedWord = normalizeText(word);
            if (queryWords.contains(normalizedWord)) {
                int startIndex = currentPosition;
                int endIndex = startIndex + word.length();
                spannableString.setSpan(
                        new ForegroundColorSpan(0xFFFF0000), // رنگ قرمز
                        startIndex,
                        endIndex,
                        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }
            currentPosition += word.length() + 1; // +1 برای فاصله
        }

        holder.verseTextView.setText(spannableString);
        holder.cardView.setOnClickListener(v -> onItemClickListener.onItemClick(poem));
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    private String findMatchingVerse(String poemText, String query) {
        String normalizedPoemText = normalizeText(poemText);
        String normalizedQuery = normalizeText(query);
        List<String> queryWords = Arrays.asList(normalizedQuery.split("\\s+"));
        String[] verses = poemText.split("\n");
        String[] normalizedVerses = normalizedPoemText.split("\n");

        for (int i = 0; i < normalizedVerses.length; i++) {
            boolean matchesAll = true;
            String[] verseWords = normalizedVerses[i].split("\\s+");
            for (String word : queryWords) {
                if (!Arrays.asList(verseWords).contains(word)) {
                    matchesAll = false;
                    break;
                }
            }
            if (matchesAll) {
                return verses[i].trim();
            }
        }
        return verses[0];
    }

    private String normalizeText(String text) {
        return text.replaceAll("[\\u064B-\\u065F]", ""); // حذف اعراب
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