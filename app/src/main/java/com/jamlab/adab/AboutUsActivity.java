package com.jamlab.adab;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        // تنظیم تولبار
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false); // فلش بازگشت غیرفعال
            getSupportActionBar().setDisplayShowTitleEnabled(false); // غیرفعال کردن عنوان پیش‌فرض ActionBar
        }

        // تنظیم عنوان در TextView داخل تولبار
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("درباره ما");

        // تنظیم لوگو
        ImageView appLogo = findViewById(R.id.app_logo);
        // appLogo.setImageResource(R.drawable.app_logo); // اگر نیاز است لوگو را تغییر دهید

        // تنظیم متن درباره ما
        TextView aboutText = findViewById(R.id.about_text);
        aboutText.setText("نرم‌افزار شعر و ادب پارسی با هدف ترویج فرهنگ و ادبیات غنی پارسی طراحی شده است. ما در این برنامه تلاش کرده‌ایم تا مجموعه‌ای از آثار ارزشمند شاعران بزرگ پارسی‌گو را در دسترس شما قرار دهیم.");

        // تنظیم آدرس ایمیل و قابلیت کپی
        TextView emailText = findViewById(R.id.email_text);
        emailText.setText("تماس با ما: jamshidpour.farhad@gmail.com");
        emailText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // کپی کردن ایمیل به کلیپ‌بورد
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                String email = "jamshidpour.farhad@gmail.com"; // فقط آدرس ایمیل بدون متن اضافی
                clipboard.setText(email);

                // نمایش توست
                Toast.makeText(AboutUsActivity.this, "ایمیل کپی شد", Toast.LENGTH_SHORT).show();
            }
        });
    }
}