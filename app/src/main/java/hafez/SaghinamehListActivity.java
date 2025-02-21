package hafez;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.jamlab.adab.R;

public class SaghinamehListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // به جای تنظیم محتوا، مستقیم به صفحه جزییات هدایت می‌شه
        Intent intent = new Intent(SaghinamehListActivity.this, SaghinamehDetailActivity.class);
        intent.putExtra("saghinamehTitle", "ساقی‌نامه حافظ"); // عنوان ثابت برای ساقی‌نامه
        startActivity(intent);
        finish(); // این اکتیویتی بسته می‌شه تا کاربر بهش برنگرده
    }
}