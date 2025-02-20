package hafez;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.jamlab.adab.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GhazalDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VerseAdapter verseAdapter;
    private ImageButton favoriteButton;
    private ImageButton playPauseButton;
    private SeekBar audioSeekBar;
    private ProgressBar downloadProgress;
    private boolean isFavorite = false;
    private String ghazalTitle;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isPlaying = false;
    private boolean isPrepared = false; // برای بررسی آماده بودن MediaPlayer

    // نگاشت عنوان غزل به لینک صوتی Google Drive
    private static final Map<String, String> AUDIO_URLS = new HashMap<String, String>() {{
        put("غزل شماره ۱", "https://drive.google.com/uc?export=download&id=1aGP2JQIOU0r0V_nupxacJBRC7W3IHBJl");
        // برای غزل‌های دیگر، FILE_ID واقعی را اضافه کنید
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghazal_detail);

        // تنظیم Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // پیدا کردن عناصر UI
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        favoriteButton = findViewById(R.id.favorite_button);
        playPauseButton = findViewById(R.id.play_pause_button);
        audioSeekBar = findViewById(R.id.audio_seekbar);
        downloadProgress = findViewById(R.id.download_progress);

        // اتصال RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // دریافت عنوان غزل از Intent
        ghazalTitle = getIntent().getStringExtra("ghazalTitle");
        if (ghazalTitle != null) {
            toolbarTitle.setText(ghazalTitle);
        }

        // بارگذاری ابیات غزل
        List<Verse> verseList = loadVersesFromJson(ghazalTitle);
        verseAdapter = new VerseAdapter(verseList);
        recyclerView.setAdapter(verseAdapter);

        // مدیریت علاقه‌مندی‌ها
        SharedPreferences sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE);
        Set<String> favorites = sharedPreferences.getStringSet("favorites", new HashSet<>());
        if (favorites.contains(ghazalTitle)) {
            isFavorite = true;
            favoriteButton.setImageResource(R.drawable.ic_star_filled);
        } else {
            favoriteButton.setImageResource(R.drawable.ic_star_outline);
        }

        favoriteButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (isFavorite) {
                isFavorite = false;
                favoriteButton.setImageResource(R.drawable.ic_star_outline);
                favorites.remove(ghazalTitle);
                Toast.makeText(this, "از علاقه‌مندی‌ها حذف شد", Toast.LENGTH_SHORT).show();
            } else {
                isFavorite = true;
                favoriteButton.setImageResource(R.drawable.ic_star_filled);
                favorites.add(ghazalTitle);
                Toast.makeText(this, "به علاقه‌مندی‌ها اضافه شد", Toast.LENGTH_SHORT).show();
            }
            editor.putStringSet("favorites", favorites);
            editor.apply();
        });

        // تنظیم دکمه Play/Pause
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
        String audioUrl = AUDIO_URLS.get(ghazalTitle);
        if (audioUrl == null) {
            Toast.makeText(this, "فایل صوتی برای این غزل یافت نشد", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mediaPlayer == null) {
            // نمایش Toast و انیمیشن دانلود فقط در بار اول
            Toast.makeText(this, "در حال دانلود صوت...", Toast.LENGTH_SHORT).show();
            downloadProgress.setVisibility(View.VISIBLE);
            playPauseButton.setEnabled(false);

            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(audioUrl);
                mediaPlayer.setOnPreparedListener(mp -> {
                    downloadProgress.setVisibility(View.GONE);
                    playPauseButton.setEnabled(true);
                    audioSeekBar.setMax(mp.getDuration());
                    mp.start();
                    isPlaying = true;
                    isPrepared = true; // صوت آماده پخش شده است
                    playPauseButton.setImageResource(R.drawable.ic_pause);
                    updateSeekBar();
                });
                mediaPlayer.setOnCompletionListener(mp -> {
                    stopAudio();
                });
                mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                    downloadProgress.setVisibility(View.GONE);
                    playPauseButton.setEnabled(true);
                    Toast.makeText(this, "خطا در دانلود یا پخش صوت", Toast.LENGTH_SHORT).show();
                    return true;
                });
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
                downloadProgress.setVisibility(View.GONE);
                playPauseButton.setEnabled(true);
                Toast.makeText(this, "خطا در دانلود صوت", Toast.LENGTH_SHORT).show();
            }
        } else if (!mediaPlayer.isPlaying() && isPrepared) {
            // ادامه پخش از موقعیت فعلی
            mediaPlayer.start();
            isPlaying = true;
            playPauseButton.setImageResource(R.drawable.ic_pause);
            updateSeekBar();
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

    private void stopAudio() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
            mediaPlayer.seekTo(0); // بازگشت به ابتدا
            isPlaying = false;
            playPauseButton.setImageResource(R.drawable.ic_play);
            audioSeekBar.setProgress(0);
            handler.removeCallbacksAndMessages(null);
        }
    }

    private void setupSeekBar() {
        audioSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
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
            audioSeekBar.setProgress(mediaPlayer.getCurrentPosition());
            handler.postDelayed(this::updateSeekBar, 1000); // به‌روزرسانی هر ثانیه
        }
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

    private List<Verse> loadVersesFromJson(String ghazalTitle) {
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