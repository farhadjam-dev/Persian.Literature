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

public class GhitaatDetailActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private GhitaatPagerAdapter ghitaatAdapter;
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
    private String ghitaatTitle;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isPlaying = false;
    private List<String> ghitaatTitles;
    private boolean isMenuExpanded = false;

    private static final Map<String, String> AUDIO_URLS = new HashMap<String, String>() {{
        //put("قطعه شماره ۱", "https://drive.google.com/uc?export=download&id=لینک_صوتی");
        put("قطعه شماره ۱ حافظ", "https://drive.google.com/uc?export=download&id=18q0t6Ws5p8EdCQlKxnwDYFMps62P-_bO");
        put("قطعه شماره ۲ حافظ", "https://drive.google.com/uc?export=download&id=1jZThH3c3i-ZAifiZiwIykXMs2V5X4kBn");
        put("قطعه شماره ۳ حافظ", "https://drive.google.com/uc?export=download&id=1ETyiDn3Uv0O9mL67e42tfGpraWm3_k1u");
        put("قطعه شماره ۴ حافظ", "https://drive.google.com/uc?export=download&id=1EFrqx5RKIJDnNR83cBs1y-41qez6jIXL");
        put("قطعه شماره ۵ حافظ", "https://drive.google.com/uc?export=download&id=1XEUoBhLoz_A6GUINbe0Z8faJMAvCp-t2");
        put("قطعه شماره ۶ حافظ", "https://drive.google.com/uc?export=download&id=15B2ilpgsQB6pdSuG3dfse1d8td2eTYlK");
        put("قطعه شماره ۷ حافظ", "https://drive.google.com/uc?export=download&id=1tBPV5BbajCgL0mhM_qQg41odhRv0gl-d");
        put("قطعه شماره ۸ حافظ", "https://drive.google.com/uc?export=download&id=1EEJmx_5RJL0p77FdgOQQSGe037yl6WJc");
        put("قطعه شماره ۹ حافظ", "https://drive.google.com/uc?export=download&id=1FptwS349LKerNac-a-jtuyBcRWx_vg4a");
        put("قطعه شماره ۱۰ حافظ", "https://drive.google.com/uc?export=download&id=1zZhv1YhyyOXer3fyg6GJ-a8WiNDZ2LBm");
        put("قطعه شماره ۱۱ حافظ", "https://drive.google.com/uc?export=download&id=1tQr2nk0tCFRTY7j84GfxN0XmGWn-GRby");
        put("قطعه شماره ۱۲ حافظ", "https://drive.google.com/uc?export=download&id=15_9-set_LqTKRusUpC-8zhs4BrlVD-5c");
        put("قطعه شماره ۱۳ حافظ", "https://drive.google.com/uc?export=download&id=149s5AvSA1UCJXNvFRrjAFyQ5p9DDrpGG");
        put("قطعه شماره ۱۴ حافظ", "https://drive.google.com/uc?export=download&id=1bedXdgsvlXfa5sTGMvJx5yd3m9SarKoN");
        put("قطعه شماره ۱۵ حافظ", "https://drive.google.com/uc?export=download&id=1NC7wAFFQVzqjp0UtF5kY1oXH2gJQZlvN");
        put("قطعه شماره ۱۶ حافظ", "https://drive.google.com/uc?export=download&id=13y0uegk8X-LZ2a4azT5Hqz1_k5r0qSMD");
        put("قطعه شماره ۱۷ حافظ", "https://drive.google.com/uc?export=download&id=1T8prqTqzA24xyhdr19lNJyMn0Capo0xG");
        put("قطعه شماره ۱۸ حافظ", "https://drive.google.com/uc?export=download&id=1C6jRjSfa6cgAX0L79h7ED2ZZBkj6mw8x");
        put("قطعه شماره ۱۹ حافظ", "https://drive.google.com/uc?export=download&id=1kuX5_wMKwPPxdLjnE7cp0AvdhlmVF8vS");
        put("قطعه شماره ۲۰ حافظ", "https://drive.google.com/uc?export=download&id=1JDCNM5mqMxWpv3muT-4NZcsVzF8RW4ze");
        put("قطعه شماره ۲۱ حافظ", "https://drive.google.com/uc?export=download&id=1W74r9cAfUpC5yoKvUosLbn7v0mpNVLV5");
        put("قطعه شماره ۲۲ حافظ", "https://drive.google.com/uc?export=download&id=1rz3LISt0r9__gKDDTB-o-vdTtODJdxHz");
        put("قطعه شماره ۲۳ حافظ", "https://drive.google.com/uc?export=download&id=1exUYd0OWY-EABgt5fnHHA-eYCT7h7bGx");
        put("قطعه شماره ۲۴ حافظ", "https://drive.google.com/uc?export=download&id=1f3Dv-YgOaOpXFw3m6Py_M0kxfhOgetc8");
        put("قطعه شماره ۲۵ حافظ", "https://drive.google.com/uc?export=download&id=1shHXcyk0NdGefxFAx3-EjSEK4LwDviUa");
        put("قطعه شماره ۲۶ حافظ", "https://drive.google.com/uc?export=download&id=1-IcTff1U720z4fDZrfCsAgGz3IcRhj9_");
        put("قطعه شماره ۲۷ حافظ", "https://drive.google.com/uc?export=download&id=1H0EJMMj0IOz4jm97nw6rRxtnbHAjNaeR");
        put("قطعه شماره ۲۸ حافظ", "https://drive.google.com/uc?export=download&id=1C7jZxX94LP1e6l0fIauPgVZEk8gJ9Fgp");
        put("قطعه شماره ۲۹ حافظ", "https://drive.google.com/uc?export=download&id=1KBiBEtmrZAloKNgC1FxEbd6vp5V2sX2O");
        put("قطعه شماره ۳۰ حافظ", "https://drive.google.com/uc?export=download&id=1UXX0i6Sk66JzVDGUCAFDKA4UrvZGhUGC");
        put("قطعه شماره ۳۱ حافظ", "https://drive.google.com/uc?export=download&id=1bkatVqMep77Kyn9ssUdqB1c6phYY2Xgu");
        put("قطعه شماره ۳۲ حافظ", "https://drive.google.com/uc?export=download&id=1yh7lh1bOov1P9A4mYBe8A6TGdcOK79RQ");
        put("قطعه شماره ۳۳ حافظ", "https://drive.google.com/uc?export=download&id=18DpwVrweC-I5OZVcZipquL2ECs1FsW49");
        put("قطعه شماره ۳۴ حافظ", "https://drive.google.com/uc?export=download&id=1wRyDGTZgH3vibCdFky9FmShNz50il8Oa");
    }};

    private static final long MAX_CACHE_SIZE = 1000 * 1024 * 1024; // 1000 MB
    private static final long MENU_HIDE_DELAY = 3000; // 3 ثانیه

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghitaat_detail);

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

        // بارگذاری لیست قطعات و معکوس کردن آن
        ghitaatTitles = loadGhitaatTitlesFromJson();
        Collections.reverse(ghitaatTitles);
        ghitaatTitle = getIntent().getStringExtra("ghitaatTitle");
        if (ghitaatTitle == null || ghitaatTitle.isEmpty()) {
            ghitaatTitle = ghitaatTitles.isEmpty() ? "بدون عنوان" : ghitaatTitles.get(0);
        }
        int initialPosition = ghitaatTitles.indexOf(ghitaatTitle);
        if (initialPosition == -1) initialPosition = 0;

        // تنظیم ViewPager2
        ghitaatAdapter = new GhitaatPagerAdapter(this, ghitaatTitles);
        viewPager.setAdapter(ghitaatAdapter);
        viewPager.setCurrentItem(initialPosition, false);
        viewPager.setUserInputEnabled(true);

        toolbarTitle.setText(ghitaatTitle);

        // به‌روزرسانی عنوان و صوت هنگام تغییر صفحه
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                ghitaatTitle = ghitaatTitles.get(position);
                toolbarTitle.setText(ghitaatTitle);
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
                Toast.makeText(this, "این آخرین قطعه است", Toast.LENGTH_SHORT).show();
            }
        });

        prevButton.setOnClickListener(v -> {
            int nextPosition = viewPager.getCurrentItem() + 1;
            if (nextPosition < ghitaatTitles.size()) {
                viewPager.setCurrentItem(nextPosition);
            } else {
                Toast.makeText(this, "این اولین قطعه است", Toast.LENGTH_SHORT).show();
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
        List<Verse> verses = loadVersesFromJson(ghitaatTitle);
        StringBuilder textToCopy = new StringBuilder(ghitaatTitle + "\n\n");
        for (Verse verse : verses) {
            textToCopy.append(verse.getText()).append("\n");
        }
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("قطعه", textToCopy.toString());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "متن قطعه کپی شد", Toast.LENGTH_SHORT).show();
    }

    private void shareText() {
        List<Verse> verses = loadVersesFromJson(ghitaatTitle);
        StringBuilder textToShare = new StringBuilder(ghitaatTitle + "\n\n");
        for (Verse verse : verses) {
            textToShare.append(verse.getText()).append("\n");
        }
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare.toString());
        startActivity(Intent.createChooser(shareIntent, "اشتراک قطعه"));
    }

    private void updateFavoriteButton() {
        SharedPreferences sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE);
        Set<String> favorites = sharedPreferences.getStringSet("favorites", new HashSet<>());
        isFavorite = favorites.contains(ghitaatTitle);
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
            favorites.remove(ghitaatTitle);
            favoriteButton.setImageResource(R.drawable.ic_star_outline);
            favoriteMenuButton.setImageResource(R.drawable.ic_star_outline);
            Toast.makeText(this, "از علاقه‌مندی‌ها حذف شد", Toast.LENGTH_SHORT).show();
        } else {
            isFavorite = true;
            favorites.add(ghitaatTitle);
            favoriteButton.setImageResource(R.drawable.ic_star_filled);
            favoriteMenuButton.setImageResource(R.drawable.ic_star_filled);
            Toast.makeText(this, "به علاقه‌مندی‌ها اضافه شد", Toast.LENGTH_SHORT).show();
        }
        editor.putStringSet("favorites", favorites);
        editor.apply();
    }

    private void playAudio() {
        String audioUrl = AUDIO_URLS.get(ghitaatTitle);
        if (audioUrl == null) {
            Toast.makeText(this, "فایل صوتی برای این قطعه یافت نشد", Toast.LENGTH_SHORT).show();
            return;
        }

        File cacheFile = new File(getCacheDir(), ghitaatTitle.replace(" ", "_") + ".mp3");

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

        String audioUrl = AUDIO_URLS.get(ghitaatTitle);
        if (audioUrl != null) {
            File cacheFile = new File(getCacheDir(), ghitaatTitle.replace(" ", "_") + ".mp3");
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

    private List<String> loadGhitaatTitlesFromJson() {
        List<String> titles = new ArrayList<>();
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.hafez_ghitaat);
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
            Toast.makeText(this, "خطا در بارگذاری قطعات", Toast.LENGTH_SHORT).show();
        }
        return titles;
    }

    public List<Verse> loadVersesFromJson(String ghitaatTitle) {
        List<Verse> verseList = new ArrayList<>();
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.hafez_ghitaat);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("title");
                if (title.equals(ghitaatTitle)) {
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