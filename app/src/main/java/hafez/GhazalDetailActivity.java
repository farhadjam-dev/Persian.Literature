package hafez;

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
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
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
    private boolean isFavorite = false;
    private String ghazalTitle;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isPlaying = false;
    private List<String> ghazalTitles;

    private static final Map<String, String> AUDIO_URLS = new HashMap<String, String>() {{
        put("غزل شماره ۱ حافظ", "https://drive.google.com/uc?export=download&id=1aGP2JQIOU0r0V_nupxacJBRC7W3IHBJl");
        put("غزل شماره ۲ حافظ", "https://drive.google.com/uc?export=download&id=1zjIUShd6E8TQeiR-6DjqA9d4KwvwnQSI");
    }};

    private static final long MAX_CACHE_SIZE = 1000 * 1024 * 1024; // 1000 MB

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

        // بارگذاری لیست غزل‌ها و معکوس کردن آن
        ghazalTitles = loadGhazalTitlesFromJson();
        Collections.reverse(ghazalTitles); // معکوس کردن ترتیب غزل‌ها
        ghazalTitle = getIntent().getStringExtra("ghazalTitle");
        if (ghazalTitle == null || ghazalTitle.isEmpty()) {
            ghazalTitle = ghazalTitles.isEmpty() ? "بدون عنوان" : ghazalTitles.get(0); // مقدار پیش‌فرض
        }
        int initialPosition = ghazalTitles.indexOf(ghazalTitle);
        if (initialPosition == -1) {
            initialPosition = 0; // اگر غزل پیدا نشد، از اولین غزل شروع کن
        }

        // تنظیم ViewPager2
        ghazalAdapter = new GhazalPagerAdapter(this, ghazalTitles);
        viewPager.setAdapter(ghazalAdapter);
        viewPager.setCurrentItem(initialPosition, false);
        viewPager.setUserInputEnabled(true); // فعال کردن کشیدن

        // تنظیم عنوان اولیه
        toolbarTitle.setText(ghazalTitle);

        // به‌روزرسانی عنوان و صوت هنگام تغییر صفحه
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                ghazalTitle = ghazalTitles.get(position);
                toolbarTitle.setText(ghazalTitle); // به‌روزرسانی عنوان
                updateFavoriteButton();
                resetAndLoadAudio();
            }
        });

        // مدیریت کلیک روی دکمه‌های قبلی و بعدی
        nextButton.setOnClickListener(v -> { // دکمه بعدی (سمت چپ) → غزل قبلی
            int prevPosition = viewPager.getCurrentItem() - 1;
            if (prevPosition >= 0) {
                viewPager.setCurrentItem(prevPosition);
            } else {
                Toast.makeText(this, "این آخرین غزل است", Toast.LENGTH_SHORT).show();
            }
        });

        prevButton.setOnClickListener(v -> { // دکمه قبلی (سمت راست) → غزل بعدی
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
    }

    private void updateFavoriteButton() {
        SharedPreferences sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE);
        Set<String> favorites = sharedPreferences.getStringSet("favorites", new HashSet<>());
        isFavorite = favorites.contains(ghazalTitle);
        favoriteButton.setImageResource(isFavorite ? R.drawable.ic_star_filled : R.drawable.ic_star_outline);
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
            Toast.makeText(this, "از علاقه‌مندی‌ها حذف شد", Toast.LENGTH_SHORT).show();
        } else {
            isFavorite = true;
            favorites.add(ghazalTitle);
            favoriteButton.setImageResource(R.drawable.ic_star_filled);
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