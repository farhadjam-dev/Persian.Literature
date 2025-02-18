package hafez;




import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jamlab.adab.R;

import java.util.ArrayList;
import java.util.List;

public class HafezActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HafezAdapter hafezAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hafez);

        // اتصال RecyclerView به layout
        recyclerView = findViewById(R.id.recyclerView);

        // تنظیم LayoutManager برای RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ایجاد لیست بخش‌های مربوط به حافظ
        List<HafezItem> hafezItemList = new ArrayList<>();
        hafezItemList.add(new HafezItem("زندگی‌نامه", R.drawable.ic_bio));
        hafezItemList.add(new HafezItem("غزلیات", R.drawable.ic_ghazal));
        hafezItemList.add(new HafezItem("قصاید", R.drawable.ic_ghaside));

        // ایجاد Adapter و تنظیم listener برای کلیک روی آیتم‌ها
        hafezAdapter = new HafezAdapter(hafezItemList, new HafezAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(HafezItem hafezItem) {

                if (hafezItem.getTitle().equals("زندگی‌نامه")) {
                    Intent intent = new Intent(HafezActivity.this, HafezBioActivity.class);
                    startActivity(intent);
                }

                if (hafezItem.getTitle().equals("غزلیات")) {
                    Intent intent = new Intent(HafezActivity.this, GhazalListActivity.class);
                    startActivity(intent);

                }
            }
        });

        // تنظیم Adapter برای RecyclerView
        recyclerView.setAdapter(hafezAdapter);
    }
}