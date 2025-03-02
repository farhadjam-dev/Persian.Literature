package com.jamlab.adab;




import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PoetAdapter extends RecyclerView.Adapter<PoetAdapter.PoetViewHolder> {

    // لیست داده‌ها (شاعران)
    private List<Poet> poetList;

    // اینترفیس برای مدیریت کلیک‌ها
    private OnItemClickListener listener;

    // Constructor برای Adapter
    public PoetAdapter(List<Poet> poetList, OnItemClickListener listener) {
        this.poetList = poetList;
        this.listener = listener;
    }

    // اینترفیس برای مدیریت کلیک‌ها
    public interface OnItemClickListener {
        void onItemClick(Poet poet);
    }

    // ایجاد ViewHolder
    @NonNull
    @Override
    public PoetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // اتصال layout مربوط به هر آیتم (item_poet.xml)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poet, parent, false);
        return new PoetViewHolder(view);
    }

    // پر کردن داده‌ها در ViewHolder
    @Override
    public void onBindViewHolder(@NonNull PoetViewHolder holder, int position) {
        // دریافت داده‌های مربوط به شاعر فعلی
        Poet poet = poetList.get(position);

        // تنظیم نام شاعر
       // holder.poetName.setText(poet.getName());

        // تنظیم تصویر شاعر
        holder.poetImage.setImageResource(poet.getImageResId());

        // اضافه کردن Listener برای کلیک روی هر آیتم
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // فراخوانی متد onItemClick از اینترفیس
                listener.onItemClick(poet);
            }
        });
    }

    // تعداد آیتم‌ها در لیست
    @Override
    public int getItemCount() {
        return poetList.size();
    }

    // کلاس ViewHolder برای نگهداری viewهای هر آیتم
    public static class PoetViewHolder extends RecyclerView.ViewHolder {
        ImageView poetImage; // تصویر شاعر
       // TextView poetName;   // نام شاعر

        public PoetViewHolder(@NonNull View itemView) {
            super(itemView);

            // اتصال viewها به عناصر موجود در layout
            poetImage = itemView.findViewById(R.id.poet_image);
          //  poetName = itemView.findViewById(R.id.poet_name);
        }
    }
}