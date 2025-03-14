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

public class RubaiyatDetailActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private RubaiyatPagerAdapter rubaiyatAdapter;
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
    private ImageButton poemInfoButton;
    private boolean isFavorite = false;
    private String rubaiyatTitle;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isPlaying = false;
    private List<String> rubaiyatTitles;
    private boolean isMenuExpanded = false;

    private static final Map<String, String> AUDIO_URLS = new HashMap<String, String>() {{
        put("رباعی شماره ۱ حافظ", "https://drive.google.com/uc?export=download&id=1I0-uTMaLue_soo10cXKFMptPI1UCab4b");
        put("رباعی شماره ۲ حافظ", "https://drive.google.com/uc?export=download&id=1zi0kAzVPs2aKHiwTWGp3JgXn8INwt3ni");
        put("رباعی شماره ۳ حافظ", "https://drive.google.com/uc?export=download&id=1cuMY_-2TT5UOSEJ-gimU5z3eJuvJcCGn");
        put("رباعی شماره ۴ حافظ", "https://drive.google.com/uc?export=download&id=1F56ZN-E4SfI3di8zUDS9xWM47LLgKsw-");
        put("رباعی شماره ۵ حافظ", "https://drive.google.com/uc?export=download&id=1YEZYk6ou9fScHQt5drS8IVaFI_aojx75");
        put("رباعی شماره ۶ حافظ", "https://drive.google.com/uc?export=download&id=1u1e8Xt59vRG35O3ghgx_xr_qOE-IMfeV");
        put("رباعی شماره ۷ حافظ", "https://drive.google.com/uc?export=download&id=1-V7Qf4BOyHJzxmUZgzSV5066G0yxQ5bo");
        put("رباعی شماره ۸ حافظ", "https://drive.google.com/uc?export=download&id=18KufMA4fvVeWcvhOyEA3ioUTGAdwecu4");
        put("رباعی شماره ۹ حافظ", "https://drive.google.com/uc?export=download&id=1lUCMgPWFeYkzyPR5TmzWAXouosjMgVtx");
        put("رباعی شماره ۱۰ حافظ", "https://drive.google.com/uc?export=download&id=1bUtlbBkWHXuLa7Bwy_JGDDsfBjSXD5jL");
        put("رباعی شماره ۱۱ حافظ", "https://drive.google.com/uc?export=download&id=1CXpiVvnKNtFX0jZpk8aoFP9ZV9pBkhED");
        put("رباعی شماره ۱۲ حافظ", "https://drive.google.com/uc?export=download&id=1nH2R6QYRJiiXhe5LSGXOS2fWQ01yx9_W");
        put("رباعی شماره ۱۳ حافظ", "https://drive.google.com/uc?export=download&id=1RvzcFcbp2r3O7E5LsEHDgfsV9FEPrbKn");
        put("رباعی شماره ۱۴ حافظ", "https://drive.google.com/uc?export=download&id=1aYBmKbB4EbcfnfZkgyPzOiYjZAQcdspC");
        put("رباعی شماره ۱۵ حافظ", "https://drive.google.com/uc?export=download&id=1wGdmogfJIjE1H2WnoI3ZDkzbMt87d0IC");
        put("رباعی شماره ۱۶ حافظ", "https://drive.google.com/uc?export=download&id=1rBb5qWQiXaeWfsKlfVAQpmOtfwHDaYeW");
        put("رباعی شماره ۱۷ حافظ", "https://drive.google.com/uc?export=download&id=1lSqaEX5NJEbY0Y1iwje53iDllRty-P95");
        put("رباعی شماره ۱۸ حافظ", "https://drive.google.com/uc?export=download&id=148ConGyqGystSLT9eq0-rtafiIkx4T_J");
        put("رباعی شماره ۱۹ حافظ", "https://drive.google.com/uc?export=download&id=19HVVT27XZNWu_ZQP29soOTpu8-LCC2g8");
        put("رباعی شماره ۲۰ حافظ", "https://drive.google.com/uc?export=download&id=1GxhSY-Od3MmR9fPt948VC6qFB-zh4aqv");
        put("رباعی شماره ۲۱ حافظ", "https://drive.google.com/uc?export=download&id=1a2vFTOik0ti3qOpJW4NWS51cIpeogeNU");
        put("رباعی شماره ۲۲ حافظ", "https://drive.google.com/uc?export=download&id=1aTRx1_DO7BbkSp6T64RdsBkmGKVK9jEv");
        put("رباعی شماره ۲۳ حافظ", "https://drive.google.com/uc?export=download&id=1Z2vQ6N_vsL4wyQE9ldA7QbX2OvQvaE8G");
        put("رباعی شماره ۲۴ حافظ", "https://drive.google.com/uc?export=download&id=1y0hiCVo6St_sE5nDcO3EdjS7ij9PIxVg");
        put("رباعی شماره ۲۵ حافظ", "https://drive.google.com/uc?export=download&id=1fApmU0j39CyEqu78IrbKI6ZMnIGR3wPm");
        put("رباعی شماره ۲۶ حافظ", "https://drive.google.com/uc?export=download&id=1_MYGYinUS35UxSihrXbnJ4ME-EZgQINe");
        put("رباعی شماره ۲۷ حافظ", "https://drive.google.com/uc?export=download&id=1yehSUXGD_W4aM_M9Ih1EBfkAvPU8z35e");
        put("رباعی شماره ۲۸ حافظ", "https://drive.google.com/uc?export=download&id=1De1UwNB2iPWGydiL2iwLBGpXgmw9PLoU");
        put("رباعی شماره ۲۹ حافظ", "https://drive.google.com/uc?export=download&id=1wDMaw33mwUNhhHfJkig504752GuIS-Uk");
        put("رباعی شماره ۳۰ حافظ", "https://drive.google.com/uc?export=download&id=1LEEEYgtcdb9uhsnweJYJ2TpgM46YaVP5");
        put("رباعی شماره ۳۱ حافظ", "https://drive.google.com/uc?export=download&id=12SVCWxmYeuk0nT7_reLbFa7QGns7fPQN");
        put("رباعی شماره ۳۲ حافظ", "https://drive.google.com/uc?export=download&id=19meDCV7brjH-QW0YGsm5HkMbRa6mvId5");
        put("رباعی شماره ۳۳ حافظ", "https://drive.google.com/uc?export=download&id=1U8P5j7JKQfoZ3MRP7mfP4lPRSt8IKKbK");
        put("رباعی شماره ۳۴ حافظ", "https://drive.google.com/uc?export=download&id=1RNrGzlENSoYWuJpW1fuW4LIuqohLHWqr");
        put("رباعی شماره ۳۵ حافظ", "https://drive.google.com/uc?export=download&id=1-VZOqEa3x7owLZep7DVQCDLz0Fp6mDeY");
        put("رباعی شماره ۳۶ حافظ", "https://drive.google.com/uc?export=download&id=1drifU5NWt0wlTtb7mxUujU7_oqyvCESf");
        put("رباعی شماره ۳۷ حافظ", "https://drive.google.com/uc?export=download&id=1htgX5NkKrymvXY_0fT_ZQb6hvegeePR7");
        put("رباعی شماره ۳۸ حافظ", "https://drive.google.com/uc?export=download&id=1NwGi4gpFta9AG1qloVr-YOBxPF-6ZnOK");
        put("رباعی شماره ۳۹ حافظ", "https://drive.google.com/uc?export=download&id=1syqkbsTly14AkJCh0F7av8oe1enZRqb5");
        put("رباعی شماره ۴۰ حافظ", "https://drive.google.com/uc?export=download&id=1mJv_Uu8njDf7n54dxrDiMRkOhVVkMlVr");
        put("رباعی شماره ۴۱ حافظ", "https://drive.google.com/uc?export=download&id=1XDuJiCsa2d15mo77_Rw9SADsgxq5YGEg");
        put("رباعی شماره ۴۲ حافظ", "https://drive.google.com/uc?export=download&id=1dpxlJWURSFnHUwdENrdKdrAIvTyvTy6n");
    }};

    private static final long MAX_CACHE_SIZE = 1000 * 1024 * 1024; // 1000 MB
    private static final long MENU_HIDE_DELAY = 3000; // 3 ثانیه

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rubaiyat_detail);

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
        poemInfoButton = findViewById(R.id.poem_info_button);

        rubaiyatTitles = loadRubaiyatTitlesFromJson();
        Collections.reverse(rubaiyatTitles);
        rubaiyatTitle = getIntent().getStringExtra("rubaiyatTitle");
        if (rubaiyatTitle == null || rubaiyatTitle.isEmpty()) {
            rubaiyatTitle = rubaiyatTitles.isEmpty() ? "بدون عنوان" : rubaiyatTitles.get(0);
        }
        int initialPosition = rubaiyatTitles.indexOf(rubaiyatTitle);
        if (initialPosition == -1) initialPosition = 0;

        rubaiyatAdapter = new RubaiyatPagerAdapter(this, rubaiyatTitles);
        viewPager.setAdapter(rubaiyatAdapter);
        viewPager.setCurrentItem(initialPosition, false);
        viewPager.setUserInputEnabled(true);

        toolbarTitle.setText(rubaiyatTitle);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                rubaiyatTitle = rubaiyatTitles.get(position);
                toolbarTitle.setText(rubaiyatTitle);
                updateFavoriteButton();
                resetAndLoadAudio();
            }
        });

        nextButton.setOnClickListener(v -> {
            int prevPosition = viewPager.getCurrentItem() - 1;
            if (prevPosition >= 0) {
                viewPager.setCurrentItem(prevPosition);
            } else {
                Toast.makeText(this, "این آخرین رباعی است", Toast.LENGTH_SHORT).show();
            }
        });

        prevButton.setOnClickListener(v -> {
            int nextPosition = viewPager.getCurrentItem() + 1;
            if (nextPosition < rubaiyatTitles.size()) {
                viewPager.setCurrentItem(nextPosition);
            } else {
                Toast.makeText(this, "این اولین رباعی است", Toast.LENGTH_SHORT).show();
            }
        });

        updateFavoriteButton();
        favoriteButton.setOnClickListener(v -> toggleFavorite());
        favoriteMenuButton.setOnClickListener(v -> toggleFavorite());

        playPauseButton.setOnClickListener(v -> {
            Animation playPauseAnim = AnimationUtils.loadAnimation(this, R.anim.play_pause_animation);
            playPauseButton.startAnimation(playPauseAnim);
            if (!isPlaying) {
                playAudio();
            } else {
                pauseAudio();
            }
        });

        fabSettings.setOnClickListener(v -> toggleMenu());
        copyButton.setOnClickListener(v -> copyText());
        shareButton.setOnClickListener(v -> shareText());
        poemInfoButton.setOnClickListener(v -> showPoemInfoDialog());

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
        PoemDetails poemDetails = loadPoemDetails(rubaiyatTitle);
        List<Verse> verses = poemDetails.getVerses();
        StringBuilder textToCopy = new StringBuilder(rubaiyatTitle + "\n\n");
        for (Verse verse : verses) {
            textToCopy.append(verse.getText()).append("\n");
        }
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("رباعی", textToCopy.toString());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "متن رباعی کپی شد", Toast.LENGTH_SHORT).show();
    }

    private void shareText() {
        PoemDetails poemDetails = loadPoemDetails(rubaiyatTitle);
        List<Verse> verses = poemDetails.getVerses();
        StringBuilder textToShare = new StringBuilder(rubaiyatTitle + "\n\n");
        for (Verse verse : verses) {
            textToShare.append(verse.getText()).append("\n");
        }
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare.toString());
        startActivity(Intent.createChooser(shareIntent, "اشتراک رباعی"));
    }

    private void showPoemInfoDialog() {
        PoemDetails poemDetails = loadPoemDetails(rubaiyatTitle);
        PoemInfo poemInfo = poemDetails.getPoemInfo();
        List<Verse> verses = poemDetails.getVerses();

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);

        // ساخت ویوی سفارشی برای عنوان
        TextView customTitle = new TextView(this);
        customTitle.setText("اطلاعات شعر");
        customTitle.setGravity(Gravity.CENTER);
        customTitle.setTextColor(getResources().getColor(R.color.textColor));
        customTitle.setTextSize(20); // اندازه متن عنوان
        customTitle.setPadding(0, 20, 0, 20); // پدینگ برای فاصله بهتر
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        titleParams.gravity = Gravity.CENTER;
        customTitle.setLayoutParams(titleParams);

        // تنظیم جهت متن بر اساس زبان (برای جلوگیری از مشکلات RTL)
        boolean isRtl = TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_RTL;
        customTitle.setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // اجبار به LTR برای وسط‌چین شدن
        customTitle.setTextDirection(isRtl ? View.TEXT_DIRECTION_RTL : View.TEXT_DIRECTION_LTR);

        builder.setCustomTitle(customTitle);

        StringBuilder message = new StringBuilder();
        if (poemInfo != null) {
            message.append("وزن: ").append(poemInfo.getMeter()).append("\n");
            message.append("قالب: ").append(poemInfo.getForm()).append("\n");
            int verseCount = verses.size(); // هر Verse یک بیت کامل است
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
                positiveButton.setTextColor(getResources().getColor(android.R.color.black)); // رنگ تیره
                positiveButton.setGravity(Gravity.START | Gravity.CENTER_VERTICAL); // انتقال به چپ
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
        isFavorite = favorites.contains(rubaiyatTitle);
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
            favorites.remove(rubaiyatTitle);
            favoriteButton.setImageResource(R.drawable.ic_star_outline);
            favoriteMenuButton.setImageResource(R.drawable.ic_star_outline);
            Toast.makeText(this, "از علاقه‌مندی‌ها حذف شد", Toast.LENGTH_SHORT).show();
        } else {
            isFavorite = true;
            favorites.add(rubaiyatTitle);
            favoriteButton.setImageResource(R.drawable.ic_star_filled);
            favoriteMenuButton.setImageResource(R.drawable.ic_star_filled);
            Toast.makeText(this, "به علاقه‌مندی‌ها اضافه شد", Toast.LENGTH_SHORT).show();
        }
        editor.putStringSet("favorites", favorites);
        editor.apply();
    }

    private void playAudio() {
        String audioUrl = AUDIO_URLS.get(rubaiyatTitle);
        if (audioUrl == null) {
            Toast.makeText(this, "فایل صوتی برای این رباعی یافت نشد", Toast.LENGTH_SHORT).show();
            return;
        }

        File cacheFile = new File(getCacheDir(), rubaiyatTitle.replace(" ", "_") + ".mp3");

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

        String audioUrl = AUDIO_URLS.get(rubaiyatTitle);
        if (audioUrl != null) {
            File cacheFile = new File(getCacheDir(), rubaiyatTitle.replace(" ", "_") + ".mp3");
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

    private List<String> loadRubaiyatTitlesFromJson() {
        List<String> titles = new ArrayList<>();
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.hafez_rubaiyat);
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
            Toast.makeText(this, "خطا در بارگذاری رباعیات", Toast.LENGTH_SHORT).show();
        }
        return titles;
    }

    PoemDetails loadPoemDetails(String rubaiyatTitle) {
        List<Verse> verseList = new ArrayList<>();
        PoemInfo poemInfo = null;
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.hafez_rubaiyat);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("title");
                if (title.equals(rubaiyatTitle)) {
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