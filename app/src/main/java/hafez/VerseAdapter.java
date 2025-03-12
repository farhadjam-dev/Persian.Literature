package hafez;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.jamlab.adab.R;
import java.util.ArrayList;
import java.util.List;

public class VerseAdapter extends RecyclerView.Adapter<VerseAdapter.VerseViewHolder> {

    private List<Verse> verseList;
    private List<Boolean> expandedStates; // لیست برای ذخیره وضعیت باز/بسته بودن هر آیتم

    public VerseAdapter(List<Verse> verseList) {
        this.verseList = verseList;
        // مقداردهی اولیه وضعیت بسته برای همه آیتم‌ها
        this.expandedStates = new ArrayList<>();
        for (int i = 0; i < verseList.size(); i++) {
            expandedStates.add(false);
        }
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

        // تنظیم شماره بیت به صورت فارسی (شروع از ۱)
        holder.verseNumber.setText("بیت " + toPersianNumber(position + 1));

        // تقسیم متن بیت به دو مصراع
        String[] lines = verse.getText().split("/");
        String line1Text = lines.length > 0 ? lines[0].trim() : "";
        String line2Text = lines.length > 1 ? lines[1].trim() : "";
        holder.line1.setText(line1Text); // مصرع اول
        holder.line2.setText(line2Text); // مصرع دوم

        // تنظیم تفسیر بیت
        holder.explanation.setText(verse.getExplanation());

        // تنظیم وضعیت اولیه تفسیر و نشانک بر اساس expandedStates
        boolean isExpanded = expandedStates.get(position);
        holder.explanation.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.expandIcon.setImageResource(isExpanded ? R.drawable.ic_expand_up : R.drawable.ic_expand_down);

        // مدیریت کلیک روی نشانک
        holder.expandIcon.setOnClickListener(v -> {
            expandedStates.set(position, !isExpanded);
            notifyItemChanged(position);
        });

        // اضافه کردن قابلیت دبل‌کلیک روی کارت ویو
        GestureDetector gestureDetector = new GestureDetector(holder.itemView.getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                // کپی کردن متن بیت (مصرع اول و دوم)
                String verseText = line1Text + "\n" + line2Text;
                ClipboardManager clipboard = (ClipboardManager) holder.itemView.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("بیت", verseText);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(holder.itemView.getContext(), "بیت کپی شد", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        holder.cardView.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true; // بازگشت true برای مصرف رویداد
        });
    }

    @Override
    public int getItemCount() {
        return verseList.size();
    }

    // تابع تبدیل اعداد لاتین به فارسی
    private String toPersianNumber(int number) {
        String[] persianNumbers = {"۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹"};
        StringBuilder result = new StringBuilder();
        String numberStr = String.valueOf(number);
        for (int i = 0; i < numberStr.length(); i++) {
            char digit = numberStr.charAt(i);
            result.append(persianNumbers[Character.getNumericValue(digit)]);
        }
        return result.toString();
    }

    public static class VerseViewHolder extends RecyclerView.ViewHolder {
        TextView verseNumber; // شماره بیت
        TextView line1; // مصرع اول
        TextView line2; // مصرع دوم
        TextView explanation; // تفسیر بیت
        ImageView expandIcon; // نشانک
        CardView cardView; // کارت ویو

        public VerseViewHolder(@NonNull View itemView) {
            super(itemView);
            verseNumber = itemView.findViewById(R.id.verse_number);
            line1 = itemView.findViewById(R.id.line1);
            line2 = itemView.findViewById(R.id.line2);
            explanation = itemView.findViewById(R.id.explanation);
            expandIcon = itemView.findViewById(R.id.expand_icon);
            cardView = (CardView) itemView; // اشاره به CardView ریشه
        }
    }
}