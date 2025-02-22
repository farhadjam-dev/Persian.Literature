package saadi;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.jamlab.adab.R;

public class SaadiTarjibandListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saadi_tarjiband_list);

        // مستقیم به صفحه جزییات هدایت می‌شود
        Intent intent = new Intent(SaadiTarjibandListActivity.this, SaadiTarjibandDetailActivity.class);
        intent.putExtra("saadiTarjibandTitle", "ترجیع‌بند سعدی"); // عنوان ثابت
        startActivity(intent);
        finish(); // این اکتیویتی بسته می‌شود
    }
}