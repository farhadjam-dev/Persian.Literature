package hafez;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jamlab.adab.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GhazalDetailActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private GhazalPagerAdapter ghazalAdapter;
    private ImageButton favoriteButton;
    private ImageButton playPauseButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private SeekBar audioSeekBar;
    private ProgressBar downloadProgress;
    private TextView remainingTime;
    private TextView toolbarTitle;
    private FloatingActionButton fabSettings;
    private LinearLayout expandedMenu;
    private ImageButton copyButton;
    private ImageButton shareButton;
    private ImageButton favoriteMenuButton;
    private boolean isFavorite = false;
    private String ghazalTitle;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isPlaying = false;
    private List<String> ghazalTitles;
    private boolean isMenuExpanded = false;

    private static final Map<String, String> AUDIO_URLS = new HashMap<String, String>() {{
        put("غزل شماره ۱ حافظ", "https://drive.google.com/uc?export=download&id=1aGP2JQIOU0r0V_nupxacJBRC7W3IHBJl");
        put("غزل شماره ۲ حافظ", "https://drive.google.com/uc?export=download&id=1zjIUShd6E8TQeiR-6DjqA9d4KwvwnQSI");
        put("غزل شماره ۳ حافظ", "https://drive.google.com/uc?export=download&id=1c2qJJ4A-fEwScQ4HsBgswhfYXMoJgQyq");
        put("غزل شماره ۴ حافظ", "https://drive.google.com/uc?export=download&id=1mm3M3QUL1F6J5BTIHG-J3vvd1GAzkNND");
        put("غزل شماره ۵ حافظ", "https://drive.google.com/uc?export=download&id=1971bMFmX6LFZTjalRDDmDHiccOdLTskz");
        put("غزل شماره ۶ حافظ", "https://drive.google.com/uc?export=download&id=1zhShLFbK9QaqrJdPU0U1xVefdQZgoQej");
        put("غزل شماره ۷ حافظ", "https://drive.google.com/uc?export=download&id=1pRGYIZxnUIefexJxjuqA92-agl_Uw4Ng");
        put("غزل شماره ۸ حافظ", "https://drive.google.com/uc?export=download&id=1zj3MHiu1riih2sCv_erZrTuzWTiTkNjt");
        put("غزل شماره ۹ حافظ", "https://drive.google.com/uc?export=download&id=131qYDy8_reIjMOkKGgCAegAxERPshIxC");
        put("غزل شماره ۱۰ حافظ", "https://drive.google.com/uc?export=download&id=1ZEbhLWnTLwVugWhveJY5lgZyx4oZBooo");
        put("غزل شماره ۱۱ حافظ", "https://drive.google.com/uc?export=download&id=16IuFoQ4wfkrFVbC5ItlpXX1-_aBnPEJE");
        put("غزل شماره ۱۲ حافظ", "https://drive.google.com/uc?export=download&id=13-xZfepPbGTzCZAJA3-7oOTKfq1yqMpC");
        put("غزل شماره ۱۳ حافظ", "https://drive.google.com/uc?export=download&id=1xpvZANTl9OjhkVM6Ped0GRPLjhPuSpA2");
        put("غزل شماره ۱۴ حافظ", "https://drive.google.com/uc?export=download&id=1DK0xfv42gsnMK8tR4D8q3BZpdaPylNph");
        put("غزل شماره ۱۵ حافظ", "https://drive.google.com/uc?export=download&id=1yMJ_QQrpBjGzxKEQFNzeo9gm5nSkECgn");
        put("غزل شماره ۱۶ حافظ", "https://drive.google.com/uc?export=download&id=1lVbZiVsSXnQTczvP5bLkf5xfX-udXhFB");
        put("غزل شماره ۱۷ حافظ", "https://drive.google.com/uc?export=download&id=1DDVloA0qKyKhM4Fx0LMgzxueQzL4TQO9");
        put("غزل شماره ۱۸ حافظ", "https://drive.google.com/uc?export=download&id=1k8ruPHsJT56Qv2AzEsvMq5USzsNZVZbe");
        put("غزل شماره ۱۹ حافظ", "https://drive.google.com/uc?export=download&id=1rDjY0UpDM_KDgOCxKtAgKZs-rPU5-yY0");
        put("غزل شماره ۲۰ حافظ", "https://drive.google.com/uc?export=download&id=1be-gY2eBdEb5kzz94OHeJlx88yaM2F7u");
        put("غزل شماره ۲۱ حافظ", "https://drive.google.com/uc?export=download&id=1NJB52WMWPjpoIenPk1l8tjzWoMskU7oR");
        put("غزل شماره ۲۲ حافظ", "https://drive.google.com/uc?export=download&id=1NG93wnTWQHjOivCztWTK9g-zqlpWbS2k");
        put("غزل شماره ۲۳ حافظ", "https://drive.google.com/uc?export=download&id=1N4CFLxlW8X4soSilKqHX4RinND1pSi45");
        put("غزل شماره ۲۴ حافظ", "https://drive.google.com/uc?export=download&id=1iB0YY4XUinqixJ_gzR0XxLDv40k0lVoE");
        put("غزل شماره ۲۵ حافظ", "https://drive.google.com/uc?export=download&id=1fD-3_e9qG3jL-QWj5S9NCUJoNlJwtVyr");
        put("غزل شماره ۲۶ حافظ", "https://drive.google.com/uc?export=download&id=1nXAsfELteVaYKLZpnMXPCfrT2nO7MUp5");
        put("غزل شماره ۲۷ حافظ", "https://drive.google.com/uc?export=download&id=1b6NwzcwLvthhYn3vn10u6afv5TXPsDhK");
        put("غزل شماره ۲۸ حافظ", "https://drive.google.com/uc?export=download&id=1rTGRBHGP4EAKlWEqc5gT6M7OhrHOBoDS");
        put("غزل شماره ۲۹ حافظ", "https://drive.google.com/uc?export=download&id=1ttcqWC1KaW_DBq0QEABTDRpRAd1NlV5F");
        put("غزل شماره ۳۰ حافظ", "https://drive.google.com/uc?export=download&id=1eyCv6-qvmYnR7GMw0nHZCtADbOeeOdec");
        put("غزل شماره ۳۱ حافظ", "https://drive.google.com/uc?export=download&id=1XQelxf3dwfVONG3f6XKnoQbXMj8wca8t");
        put("غزل شماره ۳۲ حافظ", "https://drive.google.com/uc?export=download&id=15di3pLMksi9rUCzwsgPdn2RHjV5y-99p");
        put("غزل شماره ۳۳ حافظ", "https://drive.google.com/uc?export=download&id=1bZGsT6E36k9llSlmqX7Ob4EbVshUn2YI");
        put("غزل شماره ۳۴ حافظ", "https://drive.google.com/uc?export=download&id=1pGbmCESPRNhS7hUMJseXToRreWjFahoa");
        put("غزل شماره ۳۵ حافظ", "https://drive.google.com/uc?export=download&id=19NdcUEw-resVYCbWybe9HSlH8LZU7T7M");
        put("غزل شماره ۳۶ حافظ", "https://drive.google.com/uc?export=download&id=1CO6f5qVz3KWZvm4IU1rxAtxea_vr4CEC");
        put("غزل شماره ۳۷ حافظ", "https://drive.google.com/uc?export=download&id=1YhZICZqE4BIQTlVgCPnCTBOPPoFVyJAI");
        put("غزل شماره ۳۸ حافظ", "https://drive.google.com/uc?export=download&id=1mNrQwvVTOJoG6CUo2AxFL_h5EOV-H9kk");
        put("غزل شماره ۳۹ حافظ", "https://drive.google.com/uc?export=download&id=1EBVQtwFIPbP0oL6Yg3fFUgw2c6GhBEK5");
        put("غزل شماره ۴۰ حافظ", "https://drive.google.com/uc?export=download&id=14vuTjvJMJdQDzX68e0ebCfr_x64ormk_");
        put("غزل شماره ۴۱ حافظ", "https://drive.google.com/uc?export=download&id=1RTChuQEBK5b7WiE-zXIb3e0mAgDrXZHw");
        put("غزل شماره ۴۲ حافظ", "https://drive.google.com/uc?export=download&id=17ffy4lu7irzWCum-7jV92hKkKUNLxfMm");
        put("غزل شماره ۴۳ حافظ", "https://drive.google.com/uc?export=download&id=1fmSaW2ZPX9aWYRHNAyq5T59F_5QH7utZ");
        put("غزل شماره ۴۴ حافظ", "https://drive.google.com/uc?export=download&id=16noPlfOcBfZajh4nyCIlxZwQPrWKmr3P");
        put("غزل شماره ۴۵ حافظ", "https://drive.google.com/uc?export=download&id=1JOitcr3RV0W29QVTd-xeL0u6UPrG4hTA");
        put("غزل شماره ۴۶ حافظ", "https://drive.google.com/uc?export=download&id=194chcJI8ZdyQZ-qhzpgR3sWJUOhLSk4V");
        put("غزل شماره ۴۷ حافظ", "https://drive.google.com/uc?export=download&id=14-R2yhwNFi42f2sYnZUqVtXAy99R7GIG");
        put("غزل شماره ۴۸ حافظ", "https://drive.google.com/uc?export=download&id=13ybKKo40dFHGAqJ1rU4nKnVHuPB3X-Ol");
        put("غزل شماره ۴۹ حافظ", "https://drive.google.com/uc?export=download&id=1HytOkcPxRHJKGo6n9QtJiOb6-TvzMuHn");
        put("غزل شماره ۵۰ حافظ", "https://drive.google.com/uc?export=download&id=1bJ1nyXH1bIrJRm9LkhxQi1zrcEXdkOIs");
        put("غزل شماره ۵۱ حافظ", "https://drive.google.com/uc?export=download&id=1ovgFWilzK9ddFHl8qLFnPa-esnvseNdJ");
        put("غزل شماره ۵۲ حافظ", "https://drive.google.com/uc?export=download&id=1dfLk7aTUoC_qXmnJg7i8_lLVojEkr1lk");
        put("غزل شماره ۵۳ حافظ", "https://drive.google.com/uc?export=download&id=1WwfAGDZtLfzuetdGBTdvpixOnd9V0Bpn");
        put("غزل شماره ۵۴ حافظ", "https://drive.google.com/uc?export=download&id=1nBShs_HLq421bJsuxSXlcS5-LZ3qFKVq");
        put("غزل شماره ۵۵ حافظ", "https://drive.google.com/uc?export=download&id=15QdVX0sDldjJESKp02fU_jTRJyzZkvA7");
        put("غزل شماره ۵۶ حافظ", "https://drive.google.com/uc?export=download&id=14qqqhlRgN8ADMkOG_dhphutFCeL3tBQH");
        put("غزل شماره ۵۷ حافظ", "https://drive.google.com/uc?export=download&id=1YbxWsfIUd9P-oFwnzWvFGF05ABHtbaEX");
        put("غزل شماره ۵۸ حافظ", "https://drive.google.com/uc?export=download&id=1ExAXQrzOYuuoivsJnDhhs5UxIOxVPxdK");
        put("غزل شماره ۵۹ حافظ", "https://drive.google.com/uc?export=download&id=1yU5tdsa3SJzz8kXN9_-mGtJkdd_XEaNX");
        put("غزل شماره ۶۰ حافظ", "https://drive.google.com/uc?export=download&id=1XiTgfQTAjgZ4p7IcpjKR6whxnbubyWp7");
    }};

    private static final long MAX_CACHE_SIZE = 1000 * 1024 * 1024; // 1000 MB
    private static final long MENU_HIDE_DELAY = 3000; // 3 ثانیه

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghazal_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        toolbarTitle = findViewById(R.id.toolbar_title);
        favoriteButton = findViewById(R.id.favorite_button);
        playPauseButton = findViewById(R.id.play_pause_button);
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        audioSeekBar = findViewById(R.id.audio_seekbar);
        downloadProgress = findViewById(R.id.download_progress);
        remainingTime = findViewById(R.id.remaining_time);
        viewPager = findViewById(R.id.viewPager);
        fabSettings = findViewById(R.id.fab_settings);
        expandedMenu = findViewById(R.id.expanded_menu);
        copyButton = findViewById(R.id.copy_button);
        shareButton = findViewById(R.id.share_button);
        favoriteMenuButton = findViewById(R.id.favorite_menu_button);

        // بارگذاری لیست غزل‌ها و معکوس کردن آن
        ghazalTitles = loadGhazalTitlesFromJson();
        Collections.reverse(ghazalTitles);
        ghazalTitle = getIntent().getStringExtra("ghazalTitle");
        if (ghazalTitle == null || ghazalTitle.isEmpty()) {
            ghazalTitle = ghazalTitles.isEmpty() ? "بدون عنوان" : ghazalTitles.get(0);
        }
        int initialPosition = ghazalTitles.indexOf(ghazalTitle);
        if (initialPosition == -1) initialPosition = 0;

        // تنظیم ViewPager2
        ghazalAdapter = new GhazalPagerAdapter(this, ghazalTitles);
        viewPager.setAdapter(ghazalAdapter);
        viewPager.setCurrentItem(initialPosition, false);
        viewPager.setUserInputEnabled(true);

        toolbarTitle.setText(ghazalTitle);

        // به‌روزرسانی عنوان و صوت هنگام تغییر صفحه
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                ghazalTitle = ghazalTitles.get(position);
                toolbarTitle.setText(ghazalTitle);
                updateFavoriteButton();
                resetAndLoadAudio();
            }
        });

        // مدیریت دکمه‌های قبلی و بعدی
        nextButton.setOnClickListener(v -> {
            int prevPosition = viewPager.getCurrentItem() - 1;
            if (prevPosition >= 0) {
                viewPager.setCurrentItem(prevPosition);
            } else {
                Toast.makeText(this, "این آخرین غزل است", Toast.LENGTH_SHORT).show();
            }
        });

        prevButton.setOnClickListener(v -> {
            int nextPosition = viewPager.getCurrentItem() + 1;
            if (nextPosition < ghazalTitles.size()) {
                viewPager.setCurrentItem(nextPosition);
            } else {
                Toast.makeText(this, "این اولین غزل است", Toast.LENGTH_SHORT).show();
            }
        });

        // تنظیم دکمه علاقه‌مندی‌ها
        updateFavoriteButton();
        favoriteButton.setOnClickListener(v -> toggleFavorite());

        // تنظیم دکمه پخش/توقف
        playPauseButton.setOnClickListener(v -> {
            Animation playPauseAnim = AnimationUtils.loadAnimation(this, R.anim.play_pause_animation);
            playPauseButton.startAnimation(playPauseAnim);
            if (!isPlaying) {
                playAudio();
            } else {
                pauseAudio();
            }
        });

        setupSeekBar();

        // تنظیمات آیکون شناور و منوی گسترش‌پذیر
        fabSettings.setOnClickListener(v -> toggleMenu());

        copyButton.setOnClickListener(v -> copyText());
        shareButton.setOnClickListener(v -> shareText());
        favoriteMenuButton.setOnClickListener(v -> toggleFavorite());
    }

    private void toggleMenu() {
        Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);

        if (isMenuExpanded) {
            expandedMenu.startAnimation(slideOut);
            expandedMenu.setVisibility(View.GONE);
            fabSettings.setImageResource(R.drawable.ic_settings);
            fabSettings.setAlpha(0.5f);
            handler.removeCallbacksAndMessages(null);
        } else {
            expandedMenu.setVisibility(View.VISIBLE);
            expandedMenu.startAnimation(slideIn);
            fabSettings.setImageResource(R.drawable.ic_arrow_back);
            fabSettings.setAlpha(1.0f);
            handler.postDelayed(() -> {
                if (isMenuExpanded) {
                    toggleMenu();
                }
            }, MENU_HIDE_DELAY);
        }
        isMenuExpanded = !isMenuExpanded;
    }

    private void copyText() {
        List<Verse> verses = loadVersesFromJson(ghazalTitle);
        StringBuilder textToCopy = new StringBuilder(ghazalTitle + "\n\n");
        for (Verse verse : verses) {
            textToCopy.append(verse.getText()).append("\n");
        }
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("غزل", textToCopy.toString());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "متن غزل کپی شد", Toast.LENGTH_SHORT).show();
    }

    private void shareText() {
        List<Verse> verses = loadVersesFromJson(ghazalTitle);
        StringBuilder textToShare = new StringBuilder(ghazalTitle + "\n\n");
        for (Verse verse : verses) {
            textToShare.append(verse.getText()).append("\n");
        }
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare.toString());
        startActivity(Intent.createChooser(shareIntent, "اشتراک غزل"));
    }

    private void updateFavoriteButton() {
        SharedPreferences sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE);
        Set<String> favorites = sharedPreferences.getStringSet("favorites", new HashSet<>());
        isFavorite = favorites.contains(ghazalTitle);
        favoriteButton.setImageResource(isFavorite ? R.drawable.ic_star_filled : R.drawable.ic_star_outline);
        updateFavoriteMenuButton();
    }

    private void updateFavoriteMenuButton() {
        favoriteMenuButton.setImageResource(isFavorite ? R.drawable.ic_star_filled : R.drawable.ic_star_outline);
    }

    private void toggleFavorite() {
        Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        favoriteButton.startAnimation(scaleUp);

        SharedPreferences sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> favorites = new HashSet<>(sharedPreferences.getStringSet("favorites", new HashSet<>()));

        if (isFavorite) {
            isFavorite = false;
            favorites.remove(ghazalTitle);
            favoriteButton.setImageResource(R.drawable.ic_star_outline);
            favoriteMenuButton.setImageResource(R.drawable.ic_star_outline);
            Toast.makeText(this, "از علاقه‌مندی‌ها حذف شد", Toast.LENGTH_SHORT).show();
        } else {
            isFavorite = true;
            favorites.add(ghazalTitle);
            favoriteButton.setImageResource(R.drawable.ic_star_filled);
            favoriteMenuButton.setImageResource(R.drawable.ic_star_filled);
            Toast.makeText(this, "به علاقه‌مندی‌ها اضافه شد", Toast.LENGTH_SHORT).show();
        }
        editor.putStringSet("favorites", favorites);
        editor.apply();
    }

    private void playAudio() {
        String audioUrl = AUDIO_URLS.get(ghazalTitle);
        if (audioUrl == null) {
            Toast.makeText(this, "فایل صوتی برای این غزل یافت نشد", Toast.LENGTH_SHORT).show();
            return;
        }

        File cacheFile = new File(getCacheDir(), ghazalTitle.replace(" ", "_") + ".mp3");

        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            isPlaying = true;
            playPauseButton.setImageResource(R.drawable.ic_pause);
            updateSeekBar();
        } else if (cacheFile.exists()) {
            playFromCache(cacheFile);
        } else {
            manageCacheSpace();
            downloadAndPlay(audioUrl, cacheFile);
        }
    }

    private void playFromCache(File cacheFile) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(cacheFile.getAbsolutePath());
            mediaPlayer.setOnPreparedListener(mp -> {
                audioSeekBar.setMax(mp.getDuration());
                int remaining = mp.getDuration() - mp.getCurrentPosition();
                remainingTime.setText("-" + formatTime(remaining));
                mp.start();
                isPlaying = true;
                playPauseButton.setImageResource(R.drawable.ic_pause);
                updateSeekBar();
            });
            mediaPlayer.setOnCompletionListener(mp -> resetAndLoadAudio());
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "خطا در پخش از حافظه کش", Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadAndPlay(String audioUrl, File cacheFile) {
        Toast.makeText(this, "در حال دانلود صوت...", Toast.LENGTH_SHORT).show();
        downloadProgress.setVisibility(View.VISIBLE);
        playPauseButton.setEnabled(false);

        new Thread(() -> {
            try {
                URL url = new URL(audioUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    runOnUiThread(() -> {
                        downloadProgress.setVisibility(View.GONE);
                        playPauseButton.setEnabled(true);
                        try {
                            Toast.makeText(this, "خطا در دانلود: " + connection.getResponseCode(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    return;
                }

                InputStream input = connection.getInputStream();
                FileOutputStream output = new FileOutputStream(cacheFile);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                output.close();
                input.close();
                connection.disconnect();

                runOnUiThread(() -> {
                    downloadProgress.setVisibility(View.GONE);
                    playPauseButton.setEnabled(true);
                    playFromCache(cacheFile);
                });
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    downloadProgress.setVisibility(View.GONE);
                    playPauseButton.setEnabled(true);
                    Toast.makeText(this, "خطا در دانلود صوت", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void manageCacheSpace() {
        File cacheDir = getCacheDir();
        File[] files = cacheDir.listFiles();
        if (files == null) return;

        long totalSize = 0;
        for (File file : files) {
            totalSize += file.length();
        }

        if (totalSize >= MAX_CACHE_SIZE) {
            Arrays.sort(files, Comparator.comparingLong(File::lastModified));
            for (File file : files) {
                if (totalSize < MAX_CACHE_SIZE) break;
                long fileSize = file.length();
                if (file.delete()) {
                    totalSize -= fileSize;
                }
            }
        }
    }

    private void pauseAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPlaying = false;
            playPauseButton.setImageResource(R.drawable.ic_play);
            handler.removeCallbacksAndMessages(null);
        }
    }

    private void resetAndLoadAudio() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        isPlaying = false;
        playPauseButton.setImageResource(R.drawable.ic_play);
        audioSeekBar.setProgress(0);
        remainingTime.setText("-00:00");
        handler.removeCallbacksAndMessages(null);

        String audioUrl = AUDIO_URLS.get(ghazalTitle);
        if (audioUrl != null) {
            File cacheFile = new File(getCacheDir(), ghazalTitle.replace(" ", "_") + ".mp3");
            if (cacheFile.exists()) {
                try {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(cacheFile.getAbsolutePath());
                    mediaPlayer.setOnPreparedListener(mp -> {
                        audioSeekBar.setMax(mp.getDuration());
                        remainingTime.setText("-" + formatTime(mp.getDuration()));
                    });
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "خطا در بارگذاری صوت", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void setupSeekBar() {
        audioSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                    int remaining = mediaPlayer.getDuration() - progress;
                    remainingTime.setText("-" + formatTime(remaining));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void updateSeekBar() {
        if (mediaPlayer != null && isPlaying) {
            int currentPosition = mediaPlayer.getCurrentPosition();
            audioSeekBar.setProgress(currentPosition);
            int remaining = mediaPlayer.getDuration() - currentPosition;
            remainingTime.setText("-" + formatTime(remaining));
            handler.postDelayed(this::updateSeekBar, 1000);
        }
    }

    private String formatTime(int milliseconds) {
        int seconds = (milliseconds / 1000) % 60;
        int minutes = (milliseconds / (1000 * 60)) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacksAndMessages(null);
    }

    private List<String> loadGhazalTitlesFromJson() {
        List<String> titles = new ArrayList<>();
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.hafez_ghazals);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                titles.add(jsonObject.getString("title"));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "خطا در بارگذاری غزل‌ها", Toast.LENGTH_SHORT).show();
        }
        return titles;
    }

    public List<Verse> loadVersesFromJson(String ghazalTitle) {
        List<Verse> verseList = new ArrayList<>();
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.hafez_ghazals);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("title");
                if (title.equals(ghazalTitle)) {
                    JSONArray versesArray = jsonObject.getJSONArray("verses");
                    for (int j = 0; j < versesArray.length(); j++) {
                        JSONObject verseObject = versesArray.getJSONObject(j);
                        String text = verseObject.getString("text");
                        String explanation = verseObject.getString("explanation");
                        verseList.add(new Verse(text, explanation));
                    }
                    break;
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return verseList;
    }
}