package saadi;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.jamlab.adab.R;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GolestanBab8DetailActivity extends AppCompatActivity {

    private LinearLayout contentLayout;
    private ImageButton favoriteButton;
    private ImageButton playPauseButton;
    private SeekBar audioSeekBar;
    private ProgressBar downloadProgress;
    private TextView remainingTime;
    private boolean isFavorite = false;
    private String hekayatTitle;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isPlaying = false;

    private static final Map<String, String> AUDIO_URLS = new HashMap<String, String>() {{
        put("حکایت اول باب هشتم", "https://example.com/golestan_bab8_hekayat1.mp3");
    }};

    private static final long MAX_CACHE_SIZE = 1000 * 1024 * 1024; // 1000 MB

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_golestan_bab8_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        favoriteButton = findViewById(R.id.favorite_button);
        playPauseButton = findViewById(R.id.play_pause_button);
        audioSeekBar = findViewById(R.id.audio_seekbar);
        downloadProgress = findViewById(R.id.download_progress);
        remainingTime = findViewById(R.id.remaining_time);
        contentLayout = findViewById(R.id.content_layout);

        hekayatTitle = getIntent().getStringExtra("hekayatTitle");

        toolbarTitle.setText(hekayatTitle);

        // بارگذاری متن از JSON
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.golestan_bab8);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);
            JSONObject hekayatObject = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                if (obj.getString("title").equals(hekayatTitle)) {
                    hekayatObject = obj;
                    break;
                }
            }

            if (hekayatObject != null) {
                // نمایش نثر
                JSONArray proseArray = hekayatObject.getJSONArray("prose");
                JSONArray versesArray = hekayatObject.getJSONArray("verses");
                int verseIndex = 0;

                for (int i = 0; i < proseArray.length(); i++) {
                    String proseText = proseArray.getString(i);
                    TextView proseTextView = new TextView(this);
                    proseTextView.setText(proseText);
                    proseTextView.setTextSize(16);
                    proseTextView.setTextColor(getResources().getColor(R.color.textColor));
                    proseTextView.setPadding(0, 0, 0, 32); // فاصله زیر نثر
                    contentLayout.addView(proseTextView);

                    // اضافه کردن بیت مرتبط اگه وجود داشته باشه
                    if (verseIndex < versesArray.length() && i < proseArray.length() - 1) {
                        JSONObject verse = versesArray.getJSONObject(verseIndex);
                        TextView verseTextView = new TextView(this);
                        String verseText = verse.getString("verse1") + "\n" + verse.getString("verse2");
                        verseTextView.setText(verseText);
                        verseTextView.setTextSize(16);
                        verseTextView.setTextColor(getResources().getColor(R.color.textColor));
                        verseTextView.setGravity(Gravity.CENTER_HORIZONTAL); // وسط‌چین کردن بیت‌ها
                        verseTextView.setPadding(0, 16, 0, 32); // فاصله بالای بیت و زیر بیت
                        contentLayout.addView(verseTextView);
                        verseIndex++;
                    }
                }

                // اضافه کردن بیت‌های باقی‌مانده
                while (verseIndex < versesArray.length()) {
                    JSONObject verse = versesArray.getJSONObject(verseIndex);
                    TextView verseTextView = new TextView(this);
                    String verseText = verse.getString("verse1") + "\n" + verse.getString("verse2");
                    verseTextView.setText(verseText);
                    verseTextView.setTextSize(16);
                    verseTextView.setTextColor(getResources().getColor(R.color.textColor));
                    verseTextView.setGravity(Gravity.CENTER_HORIZONTAL); // وسط‌چین کردن بیت‌ها
                    verseTextView.setPadding(0, 16, 0, 32); // فاصله بالای بیت و زیر بیت
                    contentLayout.addView(verseTextView);
                    verseIndex++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE);
        Set<String> favorites = sharedPreferences.getStringSet("favorites", new HashSet<>());
        Set<String> mutableFavorites = new HashSet<>(favorites);

        isFavorite = mutableFavorites.contains(hekayatTitle);
        favoriteButton.setImageResource(isFavorite ? R.drawable.ic_star_filled : R.drawable.ic_star_outline);

        favoriteButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (isFavorite) {
                isFavorite = false;
                mutableFavorites.remove(hekayatTitle);
                favoriteButton.setImageResource(R.drawable.ic_star_outline);
                Toast.makeText(this, "از علاقه‌مندی‌ها حذف شد", Toast.LENGTH_SHORT).show();
            } else {
                isFavorite = true;
                mutableFavorites.add(hekayatTitle);
                favoriteButton.setImageResource(R.drawable.ic_star_filled);
                Toast.makeText(this, "به علاقه‌مندی‌ها اضافه شد", Toast.LENGTH_SHORT).show();
            }
            editor.putStringSet("favorites", mutableFavorites);
            editor.apply();
        });

        playPauseButton.setOnClickListener(v -> {
            if (!isPlaying) {
                playAudio();
            } else {
                pauseAudio();
            }
        });

        setupSeekBar();
    }

    private void playAudio() {
        String audioUrl = AUDIO_URLS.get(hekayatTitle);
        if (audioUrl == null) {
            Toast.makeText(this, "فایل صوتی برای این حکایت یافت نشد", Toast.LENGTH_SHORT).show();
            return;
        }

        File cacheFile = new File(getCacheDir(), hekayatTitle.replace(" ", "_") + ".mp3");

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
            mediaPlayer.setOnCompletionListener(mp -> resetAudio());
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

    private void resetAudio() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
            mediaPlayer.seekTo(0);
            isPlaying = false;
            playPauseButton.setImageResource(R.drawable.ic_play);
            audioSeekBar.setProgress(0);
            remainingTime.setText("-" + formatTime(mediaPlayer.getDuration()));
            handler.removeCallbacksAndMessages(null);
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}