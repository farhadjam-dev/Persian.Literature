package hafez;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jamlab.adab.R;

import java.util.List;

public class VerseAdapter extends RecyclerView.Adapter<VerseAdapter.VerseViewHolder> {

    private List<Verse> verseList;

    public VerseAdapter(List<Verse> verseList) {
        this.verseList = verseList;
    }

    @NonNull
    @Override
    public VerseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_verse, parent, false);
        return new VerseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VerseViewHolder holder, int position) {
        Verse verse = verseList.get(position);

        // تقسیم متن بیت به دو مصرع
        String[] lines = verse.getText().split("/");
        if (lines.length >= 2) {
            holder.line1.setText(lines[0].trim()); // مصرع اول
            holder.line2.setText(lines[1].trim()); // مصرع دوم
        } else {
            holder.line1.setText(verse.getText()); // اگر تقسیم نشد، کل متن در مصرع اول نمایش داده شود
            holder.line2.setText(""); // مصرع دوم خالی باشد
        }

        // تنظیم تفسیر بیت
        holder.explanation.setText(verse.getExplanation());

        // مدیریت کلیک روی کارت بیت
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.explanation.getVisibility() == View.GONE) {
                    holder.explanation.setVisibility(View.VISIBLE);
                } else {
                    holder.explanation.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return verseList.size();
    }

    public static class VerseViewHolder extends RecyclerView.ViewHolder {
        TextView line1; // مصرع اول
        TextView line2; // مصرع دوم
        TextView explanation; // تفسیر بیت

        public VerseViewHolder(@NonNull View itemView) {
            super(itemView);
            line1 = itemView.findViewById(R.id.line1);
            line2 = itemView.findViewById(R.id.line2);
            explanation = itemView.findViewById(R.id.explanation);
        }
    }
}