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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.TextUtilsCompat;
import androidx.viewpager2.widget.ViewPager2;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class GhasaidDetailActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private GhasaidPagerAdapter ghasaidAdapter;
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
    private ImageButton poemInfoButton; // دکمه جدید برای اطلاعات شعر
    private boolean isFavorite = false;
    private String ghasaidTitle;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isPlaying = false;
    private List<String> ghasaidTitles;
    private boolean isMenuExpanded = false;

    private static final Map<String, String> AUDIO_URLS = new HashMap<String, String>() {{
        put("قصیده شماره ۱ حافظ", "https://drive.google.com/uc?export=download&id=1mHBwA0PyA5EFFkCEHvyDn3v1elKo0ilI");
        put("قصیده شماره ۲ حافظ", "https://drive.google.com/uc?export=download&id=1Bjpe9yj4__LKbd0GZeuhblhXm7OBN5ht");
        put("قصیده شماره ۳ حافظ", "https://drive.google.com/uc?export=download&id=1QY0z-GGffRO_q8ynTxEEQpR_OlAnOios");
    }};

    private static final long MAX_CACHE_SIZE = 1000 * 1024 * 1024; // 1000 MB
    private static final long MENU_HIDE_DELAY = 3000; // 3 ثانیه

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghasaid_detail);

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
        poemInfoButton = findViewById(R.id.poem_info_button); // مقداردهی دکمه جدید

        // بارگذاری لیست قصاید و معکوس کردن آن
        ghasaidTitles = loadGhasaidTitlesFromJson();
        Collections.reverse(ghasaidTitles);
        ghasaidTitle = getIntent().getStringExtra("ghasaidTitle");
        if (ghasaidTitle == null || ghasaidTitle.isEmpty()) {
            ghasaidTitle = ghasaidTitles.isEmpty() ? "بدون عنوان" : ghasaidTitles.get(0);
        }
        int initialPosition = ghasaidTitles.indexOf(ghasaidTitle);
        if (initialPosition == -1) initialPosition = 0;

        // تنظیم ViewPager2
        ghasaidAdapter = new GhasaidPagerAdapter(this, ghasaidTitles);
        viewPager.setAdapter(ghasaidAdapter);
        viewPager.setCurrentItem(initialPosition, false);
        viewPager.setUserInputEnabled(true);

        toolbarTitle.setText(ghasaidTitle);

        // به‌روزرسانی عنوان و صوت هنگام تغییر صفحه
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                ghasaidTitle = ghasaidTitles.get(position);
                toolbarTitle.setText(ghasaidTitle);
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
                Toast.makeText(this, "این آخرین قصیده است", Toast.LENGTH_SHORT).show();
            }
        });

        prevButton.setOnClickListener(v -> {
            int nextPosition = viewPager.getCurrentItem() + 1;
            if (nextPosition < ghasaidTitles.size()) {
                viewPager.setCurrentItem(nextPosition);
            } else {
                Toast.makeText(this, "این اولین قصیده است", Toast.LENGTH_SHORT).show();
            }
        });

        // تنظیم دکمه علاقه‌مندی‌ها
        updateFavoriteButton();
        favoriteButton.setOnClickListener(v -> toggleFavorite());
        favoriteMenuButton.setOnClickListener(v -> toggleFavorite());

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

        // تنظیمات آیکون شناور و منوی گسترش‌پذیر
        fabSettings.setOnClickListener(v -> toggleMenu());

        copyButton.setOnClickListener(v -> copyText());
        shareButton.setOnClickListener(v -> shareText());
        poemInfoButton.setOnClickListener(v -> showPoemInfoDialog()); // رویداد کلیک دکمه اطلاعات شعر

        setupSeekBar();
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
        PoemDetails poemDetails = loadPoemDetails(ghasaidTitle);
        List<Verse> verses = poemDetails.getVerses();
        StringBuilder textToCopy = new StringBuilder(ghasaidTitle + "\n\n");
        for (Verse verse : verses) {
            textToCopy.append(verse.getText()).append("\n");
        }
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("قصیده", textToCopy.toString());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "متن قصیده کپی شد", Toast.LENGTH_SHORT).show();
    }

    private void shareText() {
        PoemDetails poemDetails = loadPoemDetails(ghasaidTitle);
        List<Verse> verses = poemDetails.getVerses();
        StringBuilder textToShare = new StringBuilder(ghasaidTitle + "\n\n");
        for (Verse verse : verses) {
            textToShare.append(verse.getText()).append("\n");
        }
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare.toString());
        startActivity(Intent.createChooser(shareIntent, "اشتراک قصیده"));
    }

    private void showPoemInfoDialog() {
        PoemDetails poemDetails = loadPoemDetails(ghasaidTitle);
        PoemInfo poemInfo = poemDetails.getPoemInfo();
        List<Verse> verses = poemDetails.getVerses();

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);

        // ساخت ویوی سفارشی برای عنوان
        TextView customTitle = new TextView(this);
        customTitle.setText("اطلاعات شعر");
        customTitle.setGravity(Gravity.CENTER);
        customTitle.setTextColor(getResources().getColor(R.color.textColor));
        customTitle.setTextSize(20);
        customTitle.setPadding(0, 20, 0, 20);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        titleParams.gravity = Gravity.CENTER;
        customTitle.setLayoutParams(titleParams);

        // تنظیم جهت متن بر اساس زبان
        boolean isRtl = TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_RTL;
        customTitle.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        customTitle.setTextDirection(isRtl ? View.TEXT_DIRECTION_RTL : View.TEXT_DIRECTION_LTR);

        builder.setCustomTitle(customTitle);

        StringBuilder message = new StringBuilder();
        if (poemInfo != null) {
            message.append("وزن: ").append(poemInfo.getMeter()).append("\n");
            message.append("قالب: ").append(poemInfo.getForm()).append("\n");
            int verseCount = verses.size();
            message.append("تعداد ابیات: ").append(toPersianNumber(verseCount));
        } else {
            message.append("اطلاعات شعر در دسترس نیست.");
        }

        builder.setMessage(message.toString());
        builder.setPositiveButton("بستن", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            if (positiveButton != null) {
                positiveButton.setTextColor(getResources().getColor(android.R.color.black));
                positiveButton.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            }
        });

        dialog.show();
    }

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

    private void updateFavoriteButton() {
        SharedPreferences sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE);
        Set<String> favorites = sharedPreferences.getStringSet("favorites", new HashSet<>());
        isFavorite = favorites.contains(ghasaidTitle);
        favoriteButton.setImageResource(isFavorite ? R.drawable.ic_star_filled : R.drawable.ic_star_outline);
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
            favorites.remove(ghasaidTitle);
            favoriteButton.setImageResource(R.drawable.ic_star_outline);
            favoriteMenuButton.setImageResource(R.drawable.ic_star_outline);
            Toast.makeText(this, "از علاقه‌مندی‌ها حذف شد", Toast.LENGTH_SHORT).show();
        } else {
            isFavorite = true;
            favorites.add(ghasaidTitle);
            favoriteButton.setImageResource(R.drawable.ic_star_filled);
            favoriteMenuButton.setImageResource(R.drawable.ic_star_filled);
            Toast.makeText(this, "به علاقه‌مندی‌ها اضافه شد", Toast.LENGTH_SHORT).show();
        }
        editor.putStringSet("favorites", favorites);
        editor.apply();
    }

    private void playAudio() {
        String audioUrl = AUDIO_URLS.get(ghasaidTitle);
        if (audioUrl == null) {
            Toast.makeText(this, "فایل صوتی برای این قصیده یافت نشد", Toast.LENGTH_SHORT).show();
            return;
        }

        File cacheFile = new File(getCacheDir(), ghasaidTitle.replace(" ", "_") + ".mp3");

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

        String audioUrl = AUDIO_URLS.get(ghasaidTitle);
        if (audioUrl != null) {
            File cacheFile = new File(getCacheDir(), ghasaidTitle.replace(" ", "_") + ".mp3");
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

    private List<String> loadGhasaidTitlesFromJson() {
        List<String> titles = new ArrayList<>();
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.hafez_ghasaid);
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
            Toast.makeText(this, "خطا در بارگذاری قصاید", Toast.LENGTH_SHORT).show();
        }
        return titles;
    }

    PoemDetails loadPoemDetails(String ghasaidTitle) {
        List<Verse> verseList = new ArrayList<>();
        PoemInfo poemInfo = null;
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.hafez_ghasaid);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("title");
                if (title.equals(ghasaidTitle)) {
                    JSONArray versesArray = jsonObject.getJSONArray("verses");
                    for (int j = 0; j < versesArray.length(); j++) {
                        JSONObject verseObject = versesArray.getJSONObject(j);
                        String text = verseObject.getString("text");
                        String explanation = verseObject.getString("explanation");
                        verseList.add(new Verse(text, explanation));
                    }
                    if (jsonObject.has("poem_info")) {
                        JSONObject poemInfoObject = jsonObject.getJSONObject("poem_info");
                        String meter = poemInfoObject.getString("meter");
                        String form = poemInfoObject.getString("form");
                        int verseCount = poemInfoObject.getInt("verse_count");
                        poemInfo = new PoemInfo(meter, form, verseCount);
                    }
                    break;
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return new PoemDetails(verseList, poemInfo);
    }
}