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

import java.util.Arrays;
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
        String normalizedVerse = normalizeText(matchingVerse);
        String normalizedQuery = normalizeText(searchQuery);
        List<String> queryParts = Arrays.asList(normalizedQuery.split("\\s+")); // تقسیم عبارت جست‌وجو به بخش‌ها
        for (String part : queryParts) {
            int startIndex = normalizedVerse.indexOf(part);
            while (startIndex != -1) { // هایلایت همه رخدادها
                int endIndex = startIndex + part.length();
                spannableString.setSpan(
                        new BackgroundColorSpan(0xFFFFFF00), // رنگ زرد برای هایلایت
                        startIndex,
                        endIndex,
                        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                );
                startIndex = normalizedVerse.indexOf(part, endIndex); // جست‌وجوی رخداد بعدی
            }
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
        String normalizedPoemText = normalizeText(poemText);
        String normalizedQuery = normalizeText(query);
        List<String> queryParts = Arrays.asList(normalizedQuery.split("\\s+"));
        String[] verses = poemText.split("\n");
        String[] normalizedVerses = normalizedPoemText.split("\n");
        for (int i = 0; i < normalizedVerses.length; i++) {
            boolean matchesAll = true;
            for (String part : queryParts) {
                if (!normalizedVerses[i].contains(part)) {
                    matchesAll = false;
                    break;
                }
            }
            if (matchesAll) {
                return verses[i].trim(); // برگرداندن بیت اصلی با اعراب
            }
        }
        return verses[0]; // در صورت عدم یافتن، بیت اول
    }

    // تابع برای حذف اعراب از متن
    private String normalizeText(String text) {
        return text.replaceAll("[\\u064B-\\u065F]", ""); // حذف تمام اعراب یونی‌کد
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