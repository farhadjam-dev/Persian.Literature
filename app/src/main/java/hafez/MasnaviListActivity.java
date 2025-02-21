package hafez;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.jamlab.adab.R;

public class MasnaviListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // به جای تنظیم محتوا، مستقیم به صفحه جزییات هدایت می‌شه
        Intent intent = new Intent(MasnaviListActivity.this, MasnaviDetailActivity.class);
        intent.putExtra("masnaviTitle", "مثنوی حافظ"); // عنوان ثابت برای مثنوی
        startActivity(intent);
        finish(); // این اکتیویتی بسته می‌شه تا کاربر بهش برنگرده
    }
}