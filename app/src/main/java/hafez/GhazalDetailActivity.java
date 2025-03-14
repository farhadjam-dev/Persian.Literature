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
        put("غزل شماره ۶۱ حافظ", "https://drive.google.com/uc?export=download&id=1SULQa4jQtaz9TSIiuXFZlFxPo7y7tp_1");
        put("غزل شماره ۶۲ حافظ", "https://drive.google.com/uc?export=download&id=148x_GWT71BwKTZ3eR7jQ8Gc0IhUVvDBp");
        put("غزل شماره ۶۳ حافظ", "https://drive.google.com/uc?export=download&id=1h0EKfTRpG41IYCBfbOHvbRGQuRjphTgu");
        put("غزل شماره ۶۴ حافظ", "https://drive.google.com/uc?export=download&id=1Tlc_bzXwrA_eV8xS8fUqnkRbhH1o6_3j");
        put("غزل شماره ۶۵ حافظ", "https://drive.google.com/uc?export=download&id=1D4dOtiH2okTLLuC3_YYRvyJOKmYGN8VN");
        put("غزل شماره ۶۶ حافظ", "https://drive.google.com/uc?export=download&id=1mkYoI4f8neXJq77Ba8E6YqLOJiLoN4Uw");
        put("غزل شماره ۶۷ حافظ", "https://drive.google.com/uc?export=download&id=1rLcs_awVjG4ja0H1gi9X5B5CBiqbDYEs");
        put("غزل شماره ۶۸ حافظ", "https://drive.google.com/uc?export=download&id=1PW47W_EPziEqRIcsfeEsZi8thPVSE2m9");
        put("غزل شماره ۶۹ حافظ", "https://drive.google.com/uc?export=download&id=1rHKOcXU1fX33_rrGmtFp6hpruQuZkMd7");
        put("غزل شماره ۷۰ حافظ", "https://drive.google.com/uc?export=download&id=1SnxP8Xsvr7Y9b7ARBSIoF_e9noqceu0Z");
        put("غزل شماره ۷۱ حافظ", "https://drive.google.com/uc?export=download&id=1gw80aRDg5NKwzgVGiPyh_bfrZmgonq18");
        put("غزل شماره ۷۲ حافظ", "https://drive.google.com/uc?export=download&id=1SXHS0xWncqS-oIUVT9gjoTszdhocKYsd");
        put("غزل شماره ۷۳ حافظ", "https://drive.google.com/uc?export=download&id=1GtAVLh1p34mzz7VaImQkHlF0E2GK8EpK");
        put("غزل شماره ۷۴ حافظ", "https://drive.google.com/uc?export=download&id=1iP0IjoZEzBJq2wppvB1UsjVJcHIT6Rwl");
        put("غزل شماره ۷۵ حافظ", "https://drive.google.com/uc?export=download&id=1XE_XTK9jybtGHLczyS7b9sN1fuzAjxXG");
        put("غزل شماره ۷۶ حافظ", "https://drive.google.com/uc?export=download&id=1hxt2HA8fCcHqYGWw7myn8I-QkSSxh9uw");
        put("غزل شماره ۷۷ حافظ", "https://drive.google.com/uc?export=download&id=1_rEjVoHeFi8ih2HniUFVDuzS0vkOnS4T");
        put("غزل شماره ۷۸ حافظ", "https://drive.google.com/uc?export=download&id=1CRrSNZCnte-OrX3x1GT-GiVjBgbYhcEW");
        put("غزل شماره ۷۹ حافظ", "https://drive.google.com/uc?export=download&id=1e45GtXhQ4l94JZ0flRJwZcMjnzOKnu_G");
        put("غزل شماره ۸۰ حافظ", "https://drive.google.com/uc?export=download&id=1iebL4dltvIXPEx3uOqUTwYB0-Cdhk6FY");
        put("غزل شماره ۸۱ حافظ", "https://drive.google.com/uc?export=download&id=1voS_pkgBApKxqeD6pPOVNpiUFa_VDiby");
        put("غزل شماره ۸۲ حافظ", "https://drive.google.com/uc?export=download&id=1AC8Ctnl2HT18zHW3eONppe0M6t5qxGwt");
        put("غزل شماره ۸۳ حافظ", "https://drive.google.com/uc?export=download&id=1eVGpxkMSr-cIm4ugRR5NudvNR5BftPaq");
        put("غزل شماره ۸۴ حافظ", "https://drive.google.com/uc?export=download&id=1FYjm4GVw8GJvTIC3bzCIy_5lv6rB7Zrn");
        put("غزل شماره ۸۵ حافظ", "https://drive.google.com/uc?export=download&id=1cWzNTca1f3zI1oSxxKlZDBbEPaJ88u4Y");
        put("غزل شماره ۸۶ حافظ", "https://drive.google.com/uc?export=download&id=1bBzXyHFxKc0a81lafHLdL20_nUf0QQwj");
        put("غزل شماره ۸۷ حافظ", "https://drive.google.com/uc?export=download&id=17uNrhNBz0pgPUBUychGtOJvEKJzGNV94");
        put("غزل شماره ۸۸ حافظ", "https://drive.google.com/uc?export=download&id=1D-DkxT8OXjWz9e65XXTMGZ1yIDhMVO0L");
        put("غزل شماره ۸۹ حافظ", "https://drive.google.com/uc?export=download&id=1K4eHHmwTpkMSCbiAVGCkkuVnErGD4NPm");
        put("غزل شماره ۹۰ حافظ", "https://drive.google.com/uc?export=download&id=1G1ffVBKxR6Ak8D2O8Cz_qXb9d936-4LP");
        put("غزل شماره ۹۱ حافظ", "https://drive.google.com/uc?export=download&id=11NZgzpnJx01DF6-M3h1ZEaFqguNUVvmu");
        put("غزل شماره ۹۲ حافظ", "https://drive.google.com/uc?export=download&id=1OQNMTdYR2T77-WbF0ntCZxbAbcmNHR9z");
        put("غزل شماره ۹۳ حافظ", "https://drive.google.com/uc?export=download&id=11QO6BGM_qJWbYJRD_mJJtkUlzmkkC2T-");
        put("غزل شماره ۹۴ حافظ", "https://drive.google.com/uc?export=download&id=1N0Dx2ikmI3pIpzMSoBEy2RXEjnYsxrqY");
        put("غزل شماره ۹۵ حافظ", "https://drive.google.com/uc?export=download&id=1bLLdit2wIPTEqjB5QNmA5iDCyQ-I7JVg");
        put("غزل شماره ۹۶ حافظ", "https://drive.google.com/uc?export=download&id=1Tzbzyc8S9c3z330yEVTX4w88hUiLxr4j");
        put("غزل شماره ۹۷ حافظ", "https://drive.google.com/uc?export=download&id=1iT5u9ymNtcO89X0PuQzSrfLdM4uBKr2L");
        put("غزل شماره ۹۸ حافظ", "https://drive.google.com/uc?export=download&id=1Skr69mXLRBsy2GDjKG4AknUIspS_GWz0");
        put("غزل شماره ۹۹ حافظ", "https://drive.google.com/uc?export=download&id=12KSPTyDBnbA-bkUZ1AC2s9jZqiTDs-bU");
        put("غزل شماره ۱۰۰ حافظ", "https://drive.google.com/uc?export=download&id=1D8katJi6iI284JsoBlaY5W0fitlsGWYz");
        put("غزل شماره ۱۰۱ حافظ", "https://drive.google.com/uc?export=download&id=1tSMWjXC2tbOX9tZjYDc0UXWXhA2VQb7I");
        put("غزل شماره ۱۰۲ حافظ", "https://drive.google.com/uc?export=download&id=15FYDfAHA2EJmW2_ErCgBv9Fhz7CpEf5T");
        put("غزل شماره ۱۰۳ حافظ", "https://drive.google.com/uc?export=download&id=1A6j-lZ8ITHtkBRiCxay-nil8ciZMPbSH");
        put("غزل شماره ۱۰۴ حافظ", "https://drive.google.com/uc?export=download&id=1ib1UHiJAtbicAi_BhlLQQsC2F-TW6tIT");
        put("غزل شماره ۱۰۵ حافظ", "https://drive.google.com/uc?export=download&id=10rLZyzkHlTLRMH7mxahlwaIk-wmSdD3e");
        put("غزل شماره ۱۰۶ حافظ", "https://drive.google.com/uc?export=download&id=1jCNBpUHfs2gsWYoJcvRiAJ29Jhu_RTZf");
        put("غزل شماره ۱۰۷ حافظ", "https://drive.google.com/uc?export=download&id=1C3Zd5irSOFKAH3sA9nOGSfd150EdInz0");
        put("غزل شماره ۱۰۸ حافظ", "https://drive.google.com/uc?export=download&id=15GMS3tKvCDsOFr_SFBCgks6MX6lg4KPg");
        put("غزل شماره ۱۰۹ حافظ", "https://drive.google.com/uc?export=download&id=1w2HkAll8XtkxNTsXVdbo6466T0DYjSrz");
        put("غزل شماره ۱۱۰ حافظ", "https://drive.google.com/uc?export=download&id=1WsAcIWDt-vOPxwO6vIr4-TDCGNtTOaN_");
        put("غزل شماره ۱۱۱ حافظ", "https://drive.google.com/uc?export=download&id=12BvmV5jxLhpfnSbPk3hxInWPcHEfKZGu");
        put("غزل شماره ۱۱۲ حافظ", "https://drive.google.com/uc?export=download&id=1Bs9NP8dH9ZFqwxSFdfLEIpyqGCB8vVHa");
        put("غزل شماره ۱۱۳ حافظ", "https://drive.google.com/uc?export=download&id=1elK2h3tASiHZYjd2KTuPlUJK-gQ1L6BT");
        put("غزل شماره ۱۱۴ حافظ", "https://drive.google.com/uc?export=download&id=10zlWkwk3m8gU5qv2N1ACyryANFPZcdMY");
        put("غزل شماره ۱۱۵ حافظ", "https://drive.google.com/uc?export=download&id=1syuWP2pxZbNWkOzXKhz56Or64KdQf7ww");
        put("غزل شماره ۱۱۶ حافظ", "https://drive.google.com/uc?export=download&id=1mRjgqkbI7ArBmdKoB15blHhm484fRtFA");
        put("غزل شماره ۱۱۷ حافظ", "https://drive.google.com/uc?export=download&id=1e8zbvmTo8Lup7iiLQe0ZX_lAMmrZRYbR");
        put("غزل شماره ۱۱۸ حافظ", "https://drive.google.com/uc?export=download&id=1qRd-s_jYrsmdKdwc7kIjuDFb0zL9FCSf");
        put("غزل شماره ۱۱۹ حافظ", "https://drive.google.com/uc?export=download&id=1hnt_BPt4MFl1iB_TvigqgQ7NhTgtP0Q0");
        put("غزل شماره ۱۲۰ حافظ", "https://drive.google.com/uc?export=download&id=10kV2hsG218dSYDVqcD7wLqU6rm6DM6mE");
        put("غزل شماره ۱۲۱ حافظ", "https://drive.google.com/uc?export=download&id=1WSNYkq72ApxSdPINJ81Nmfq9oFMU4Yb3");
        put("غزل شماره ۱۲۲ حافظ", "https://drive.google.com/uc?export=download&id=1mbtELtpBBsT1E9sE8TSlex7B5tdqm2yP");
        put("غزل شماره ۱۲۳ حافظ", "https://drive.google.com/uc?export=download&id=1Yh5bQ0aEtDhHaTzZlCj9CY0DlY6b9TCj");
        put("غزل شماره ۱۲۴ حافظ", "https://drive.google.com/uc?export=download&id=1KEjWjb8IeOVbqatJtTWEEAA8EIrhjjGA");
        put("غزل شماره ۱۲۵ حافظ", "https://drive.google.com/uc?export=download&id=1pMOj_vV4ARyDNRgMC_obDC-34o35Mk2e");
        put("غزل شماره ۱۲۶ حافظ", "https://drive.google.com/uc?export=download&id=1rflCn4FtaYRGqngncmwqf9LxXMAwbmnz");
        put("غزل شماره ۱۲۷ حافظ", "https://drive.google.com/uc?export=download&id=1EuEent4lNtgAxQdQE77x5baWdpzIxdBA");
        put("غزل شماره ۱۲۸ حافظ", "https://drive.google.com/uc?export=download&id=1LSCtvwwUS1v5XfziQSD4f88NDP2nmVtt");
        put("غزل شماره ۱۲۹ حافظ", "https://drive.google.com/uc?export=download&id=1CINb6icMxY_5v4Aju7a9CrSVGn7MORId");
        put("غزل شماره ۱۳۰ حافظ", "https://drive.google.com/uc?export=download&id=1E9ONgtHldQE-T5m5c5BvHJ4xFkSrRTIC");
        put("غزل شماره ۱۳۱ حافظ", "https://drive.google.com/uc?export=download&id=1xuYBNzHF6Zp7T5n8cREcK4NQQHgIOOx2");
        put("غزل شماره ۱۳۲ حافظ", "https://drive.google.com/uc?export=download&id=1kzTuNA7s-P1Ir7JDGVPfIelJX6DDARGZ");
        put("غزل شماره ۱۳۳ حافظ", "https://drive.google.com/uc?export=download&id=1hqij6osFYVOrJ1ny6sLC32P3NsY49ES1");
        put("غزل شماره ۱۳۴ حافظ", "https://drive.google.com/uc?export=download&id=1vWHEgqw1pfPvCAcnT-sl6nK-gHWYePAR");
        put("غزل شماره ۱۳۵ حافظ", "https://drive.google.com/uc?export=download&id=1jrp_8_vAVm5PW4lJ0Cc-Jp31NNkXO-MH");
        put("غزل شماره ۱۳۶ حافظ", "https://drive.google.com/uc?export=download&id=1DbdT5XKsn37M8-_K6lRBvzZb_IQhVXD4");
        put("غزل شماره ۱۳۷ حافظ", "https://drive.google.com/uc?export=download&id=1wz3e_oaSGjtIDR1eQKtu-VUjPSCYbvTd");
        put("غزل شماره ۱۳۸ حافظ", "https://drive.google.com/uc?export=download&id=1smHvaueQj0xUIwJXvhToYKjHOttrCS4h");
        put("غزل شماره ۱۳۹ حافظ", "https://drive.google.com/uc?export=download&id=1P_I-7i6RPgWAlQZWx4cz0vXY6MFjnl5l");
        put("غزل شماره ۱۴۰ حافظ", "https://drive.google.com/uc?export=download&id=1qbBTjm16vVXzoCYhHcU8lHhr0tSJo-Hp");
        put("غزل شماره ۱۴۱ حافظ", "https://drive.google.com/uc?export=download&id=1cTRc47pTTvmHx9lB88a-FJm9sn6umI2c");
        put("غزل شماره ۱۴۲ حافظ", "https://drive.google.com/uc?export=download&id=1Rhm7tL72NGxjBGKzrr3jhL-rYHxCVr5D");
        put("غزل شماره ۱۴۳ حافظ", "https://drive.google.com/uc?export=download&id=10IUULCW9KkO8NG5RCTs-CX6iLCA5EiPP");
        put("غزل شماره ۱۴۴ حافظ", "https://drive.google.com/uc?export=download&id=1w3UOI0AaFr9OyTmvL6yQR_R3x4W_VXkx");
        put("غزل شماره ۱۴۵ حافظ", "https://drive.google.com/uc?export=download&id=1T42KPVTYHoVW8GzZVFMDD1-4jANmoOIG");
        put("غزل شماره ۱۴۶ حافظ", "https://drive.google.com/uc?export=download&id=1n7QG0wV83k5azv7gfDlTayYadk5C0czM");
        put("غزل شماره ۱۴۷ حافظ", "https://drive.google.com/uc?export=download&id=1HcZjr7KCSVjDDlayDIT6YcMS79fZiEMk");
        put("غزل شماره ۱۴۸ حافظ", "https://drive.google.com/uc?export=download&id=1Y-yoz96nemd2FhcqSDwfN2sBUzOb1qOo");
        put("غزل شماره ۱۴۹ حافظ", "https://drive.google.com/uc?export=download&id=1WfpDpBLPVDrT_1ly2cZ2L4An5X_U9AA1");
        put("غزل شماره ۱۵۰ حافظ", "https://drive.google.com/uc?export=download&id=1pnAaBkPci9yGsnMq6JPBBLwT6UMGuueH");
        put("غزل شماره ۱۵۱ حافظ", "https://drive.google.com/uc?export=download&id=1b_AAmLJnbJuC3Tt3JYcaIMORz611zWWs");
        put("غزل شماره ۱۵۲ حافظ", "https://drive.google.com/uc?export=download&id=1DI4351Rb0WKaV7Q7F8nqevlKnIDINxY8");
        put("غزل شماره ۱۵۳ حافظ", "https://drive.google.com/uc?export=download&id=1TayUpkFnkmx-dQ48LGmdOZBBU_QGDBSI");
        put("غزل شماره ۱۵۴ حافظ", "https://drive.google.com/uc?export=download&id=1rWwDc2uALsZ_nMZ8F_BiBab77zJJS-cw");
        put("غزل شماره ۱۵۵ حافظ", "https://drive.google.com/uc?export=download&id=1pOwt_QMatdqPAZRGIjIuA1nvMF3v5-vf");
        put("غزل شماره ۱۵۶ حافظ", "https://drive.google.com/uc?export=download&id=11Yfvy_HsCv9fCxYfROWWSMFMcVehg1t1");
        put("غزل شماره ۱۵۷ حافظ", "https://drive.google.com/uc?export=download&id=1R3WHiYRH7p39dzREmh_0TFUaWveTv2Q2");
        put("غزل شماره ۱۵۸ حافظ", "https://drive.google.com/uc?export=download&id=15cATqZO9GQDL7UDGM7dwzZQUi5sS42r6");
        put("غزل شماره ۱۵۹ حافظ", "https://drive.google.com/uc?export=download&id=1no9VT578RSysomR5fDWnS5WNVaBOHm0D");
        put("غزل شماره ۱۶۰ حافظ", "https://drive.google.com/uc?export=download&id=1RJyfJIhuMYYz0Y7BXI28zFe2cXbyfRhA");
        put("غزل شماره ۱۶۱ حافظ", "https://drive.google.com/uc?export=download&id=1uZB7yw9kCnSm4pQ7A3W-xA1XMSrZrqzG");
        put("غزل شماره ۱۶۲ حافظ", "https://drive.google.com/uc?export=download&id=1rTYJU9Y8oZMTXRxWqBIj-PIsmNH_ar5T");
        put("غزل شماره ۱۶۳ حافظ", "https://drive.google.com/uc?export=download&id=1WmPcXi4Fq0kb9ckA5YGeGVGNwkhUT6gi");
        put("غزل شماره ۱۶۴ حافظ", "https://drive.google.com/uc?export=download&id=1Pr6YyY0aojH_i7zcujVTSc7uzqRCWyyR");
        put("غزل شماره ۱۶۵ حافظ", "https://drive.google.com/uc?export=download&id=1kq3tgi0LIOJUmFXhz8PFheCHjwaq04nF");
        put("غزل شماره ۱۶۶ حافظ", "https://drive.google.com/uc?export=download&id=1LiKHv3SIlyj4dRQxnHgxasBuHGRA9FLX");
        put("غزل شماره ۱۶۷ حافظ", "https://drive.google.com/uc?export=download&id=1GLfbAus-kNclK4W_9S0d0QG9q32-e532");
        put("غزل شماره ۱۶۸ حافظ", "https://drive.google.com/uc?export=download&id=1pweJF7ypEx84FoO868Ge_YZ9luXq-A9L");
        put("غزل شماره ۱۶۹ حافظ", "https://drive.google.com/uc?export=download&id=1onQcg2LaxfWvNXbTap8mimMp_I0p3ach");
        put("غزل شماره ۱۷۰ حافظ", "https://drive.google.com/uc?export=download&id=1uaahoauJ3osaTkznjrmKfUqZ10wMSSVI");
        put("غزل شماره ۱۷۱ حافظ", "https://drive.google.com/uc?export=download&id=128KeBr344tV46TXF6I-_ELzSiRNjvWgr");
        put("غزل شماره ۱۷۲ حافظ", "https://drive.google.com/uc?export=download&id=1FFLPByXvrfDqjsBSj3iBEQzAGQxHG-mf");
        put("غزل شماره ۱۷۳ حافظ", "https://drive.google.com/uc?export=download&id=10sLhvGfomiVquV1tH8X6Mvn3NojQ5LYS");
        put("غزل شماره ۱۷۴ حافظ", "https://drive.google.com/uc?export=download&id=1LvnjfPclQdFQhpeDjosrSm0Ve0o_Kavy");
        put("غزل شماره ۱۷۵ حافظ", "https://drive.google.com/uc?export=download&id=1k7_OB5aiK1W-6P6RDeMFXlzaWSwsp1WN");
        put("غزل شماره ۱۷۶ حافظ", "https://drive.google.com/uc?export=download&id=1vjaGzqoA8bZYodVQjtoIp4oQiyyk47gb");
        put("غزل شماره ۱۷۷ حافظ", "https://drive.google.com/uc?export=download&id=1p-hGNIw2IDOYIK4jPg7nEJocnDBDgRhq");
        put("غزل شماره ۱۷۸ حافظ", "https://drive.google.com/uc?export=download&id=1dKkcxrfZSVc6wqWrY5RoOGuF0dmq9fi-");
        put("غزل شماره ۱۷۹ حافظ", "https://drive.google.com/uc?export=download&id=1BXBkcaGqntDB5YzVFYavBIhWTyh8s6xo");
        put("غزل شماره ۱۸۰ حافظ", "https://drive.google.com/uc?export=download&id=1G4Rqy2dg5gkVCfeosQ7-gjCLP4AmDDP3");
        put("غزل شماره ۱۸۱ حافظ", "https://drive.google.com/uc?export=download&id=1J7ZWLxhWsHe4PmZXytkWR1JkAMBcdU66");
        put("غزل شماره ۱۸۲ حافظ", "https://drive.google.com/uc?export=download&id=1gAlb3wJRp4x9wz4U2GuvFRwbZjYz_uEX");
        put("غزل شماره ۱۸۳ حافظ", "https://drive.google.com/uc?export=download&id=1o21sfzxE1ve4n3wpbkyW6wDjTiqvkHES");
        put("غزل شماره ۱۸۴ حافظ", "https://drive.google.com/uc?export=download&id=1tSOiaeESkGzx2l2GdY59NAaoQVjehCjK");
        put("غزل شماره ۱۸۵ حافظ", "https://drive.google.com/uc?export=download&id=1h63UD6ArJR__oZ_PywWhNt038JfDG7vY");
        put("غزل شماره ۱۸۶ حافظ", "https://drive.google.com/uc?export=download&id=10NDXZmbw8EXeyEtFqqNVENOEiIa1pi2N");
        put("غزل شماره ۱۸۷ حافظ", "https://drive.google.com/uc?export=download&id=19DdO8EW3Am_713s6b1JcygdSswDLB4r-");
        put("غزل شماره ۱۸۸ حافظ", "https://drive.google.com/uc?export=download&id=1xVzhIhhsfDIuGsPLdaifbXMMP24Htazi");
        put("غزل شماره ۱۸۹ حافظ", "https://drive.google.com/uc?export=download&id=1_T3DlojAQXngudZzlEOnn5KpxACoNUqw");
        put("غزل شماره ۱۹۰ حافظ", "https://drive.google.com/uc?export=download&id=1rY-axm09l6iosrT1A_eZpOc1sY4y1t8V");
        put("غزل شماره ۱۹۱ حافظ", "https://drive.google.com/uc?export=download&id=1AeoBOBbk412xU-8f0CTbFYxkcRrnz0At");
        put("غزل شماره ۱۹۲ حافظ", "https://drive.google.com/uc?export=download&id=1FwH8RwJx-RuR69KLR0I8Eowy5Y43mRnA");
        put("غزل شماره ۱۹۳ حافظ", "https://drive.google.com/uc?export=download&id=1Nz4-vuY9qrieQXzcSeE0ibFTkUyu5iU-");
        put("غزل شماره ۱۹۴ حافظ", "https://drive.google.com/uc?export=download&id=1V8KYfoWUPf8BTMxuE-1v4K3Tii_I9DoJ");
        put("غزل شماره ۱۹۵ حافظ", "https://drive.google.com/uc?export=download&id=1A-gZLIdDds_RsRifb9FT-_t82isqsrjg");
        put("غزل شماره ۱۹۶ حافظ", "https://drive.google.com/uc?export=download&id=1Ta430Oc8qUwLqqH6haEXu7kkTF_qu7XD");
        put("غزل شماره ۱۹۷ حافظ", "https://drive.google.com/uc?export=download&id=1RsOd-PQiVYjfCqVI4vEThQmojw7vrbGa");
        put("غزل شماره ۱۹۸ حافظ", "https://drive.google.com/uc?export=download&id=1Rs8Q4pR52HVmeEvKU7q1vgjWYM-e7CPU");
        put("غزل شماره ۱۹۹ حافظ", "https://drive.google.com/uc?export=download&id=1abRyKcnQx8_vQeUQ8MdcH5k61ZlaAbOd");
        put("غزل شماره ۲۰۰ حافظ", "https://drive.google.com/uc?export=download&id=1nDtvUl_ZlO5HuXKk4Kicn7Bo3Jny__lB");
        put("غزل شماره ۲۰۱ حافظ", "https://drive.google.com/uc?export=download&id=1gAr49O0mOHmquzSv99sayo54ahyANQAj");
        put("غزل شماره ۲۰۲ حافظ", "https://drive.google.com/uc?export=download&id=1SmZa20azJ97aAUo3qaNfFBTRdCghcLhj");
        put("غزل شماره ۲۰۳ حافظ", "https://drive.google.com/uc?export=download&id=1AtMV-yWP39D9h2rNichsEsRsmkByUkiR");
        put("غزل شماره ۲۰۴ حافظ", "https://drive.google.com/uc?export=download&id=1kGMRzAzGbqlKd-fi_Tu9cbgZY1vX6IcP");
        put("غزل شماره ۲۰۵ حافظ", "https://drive.google.com/uc?export=download&id=1_uMKq2yv9FJoexHiG-9hYPkwHKx1Lwt2");
        put("غزل شماره ۲۰۶ حافظ", "https://drive.google.com/uc?export=download&id=1-ddIbQeJe0pMAiBGQeb5G89dbMUA3H3Y");
        put("غزل شماره ۲۰۷ حافظ", "https://drive.google.com/uc?export=download&id=1moE7DcvN2svr1ra3s5Wr86rzJ6GG8Iz4");
        put("غزل شماره ۲۰۸ حافظ", "https://drive.google.com/uc?export=download&id=1isN4eRAtIUBTq1VQxqRyhe5Nt1j6P-h9");
        put("غزل شماره ۲۰۹ حافظ", "https://drive.google.com/uc?export=download&id=1z-eC-zxlkY0Ep5rFLZLrMAJhYj7-i1Ip");
        put("غزل شماره ۲۱۰ حافظ", "https://drive.google.com/uc?export=download&id=1AvwcBI9ZJ6sdENN3MLHh9xV_ex3nlLkv");
        put("غزل شماره ۲۱۱ حافظ", "https://drive.google.com/uc?export=download&id=1WvKXAEKk3gAYySw_s8rIdRUCslRAQEnC");
        put("غزل شماره ۲۱۲ حافظ", "https://drive.google.com/uc?export=download&id=14PzCM7zc93vrIjmE15_tDPqmZZMw2tfV");
        put("غزل شماره ۲۱۳ حافظ", "https://drive.google.com/uc?export=download&id=1ljNMisnLb57W7gyUb1ttXp9gGJ__Md6B");
        put("غزل شماره ۲۱۴ حافظ", "https://drive.google.com/uc?export=download&id=1rLxj4quFvECGimAcaWFcKByID058x5Er");
        put("غزل شماره ۲۱۵ حافظ", "https://drive.google.com/uc?export=download&id=1nyhWu6OSENWn4LgZFU6BUE8hPiCl2GJG");
        put("غزل شماره ۲۱۶ حافظ", "https://drive.google.com/uc?export=download&id=1kUk66AbWB5QjWRaeiyhU9ZPORcVOXjNG");
        put("غزل شماره ۲۱۷ حافظ", "https://drive.google.com/uc?export=download&id=15kGJrDf_bNgvtWjBc3iw3vluFh7lil6S");
        put("غزل شماره ۲۱۸ حافظ", "https://drive.google.com/uc?export=download&id=1tO8c-osGagK2asZRF6w9QcRbBA76pfzx");
        put("غزل شماره ۲۱۹ حافظ", "https://drive.google.com/uc?export=download&id=1pmAKRIn_H-EW2EGloOtuqRANquPoMwsi");
        put("غزل شماره ۲۲۰ حافظ", "https://drive.google.com/uc?export=download&id=1y0pryShHo-4Cm35-mD01HaaXtEA-aWce");
        put("غزل شماره ۲۲۱ حافظ", "https://drive.google.com/uc?export=download&id=1DXaIW4PQ1Z10zAkaah2IjjuThNocsd0x");
        put("غزل شماره ۲۲۲ حافظ", "https://drive.google.com/uc?export=download&id=1CKLuvWumvF1ArxXTxyswzg-pn3PPWmde");
        put("غزل شماره ۲۲۳ حافظ", "https://drive.google.com/uc?export=download&id=10X9rovGYCOAhWhxGEvzVq5Q-MTMfnmzR");
        put("غزل شماره ۲۲۴ حافظ", "https://drive.google.com/uc?export=download&id=1mrh-FzU-yAr1wl7D73neAeDhxuBdAda9");
        put("غزل شماره ۲۲۵ حافظ", "https://drive.google.com/uc?export=download&id=1C7VJ8-lbEmxSieMVFJkUktd2I-DGeLKp");
        put("غزل شماره ۲۲۶ حافظ", "https://drive.google.com/uc?export=download&id=11I33l50N8MzE0hnGznthdYkACnscICeQ");
        put("غزل شماره ۲۲۷ حافظ", "https://drive.google.com/uc?export=download&id=10g87jdLpnOya-qc884Zt0hsCZkDYEcm1");
        put("غزل شماره ۲۲۸ حافظ", "https://drive.google.com/uc?export=download&id=14uyzevosHWQAKMpbd5RjMrIdyaSy2dnn");
        put("غزل شماره ۲۲۹ حافظ", "https://drive.google.com/uc?export=download&id=1VfiJkG9gx3c8mX-LCtrP0qvCmqt3LT0R");
        put("غزل شماره ۲۳۰ حافظ", "https://drive.google.com/uc?export=download&id=1i736MC4Ruk3nr2VTwjYI4XYl3rM-7VSc");
        put("غزل شماره ۲۳۱ حافظ", "https://drive.google.com/uc?export=download&id=1jp7ZuWPilxmpNeZW3e5bN0xtgzJWETGL");
        put("غزل شماره ۲۳۲ حافظ", "https://drive.google.com/uc?export=download&id=1Et482vjkfQ5J-R9HB5LXID6Tpcz5xZye");
        put("غزل شماره ۲۳۳ حافظ", "https://drive.google.com/uc?export=download&id=1ogX1--pqN36WG7PKVbfXDrLa6iUujA3G");
        put("غزل شماره ۲۳۴ حافظ", "https://drive.google.com/uc?export=download&id=14c42G6wtWTKnit1cDEmXFGADFJyLhADA");
        put("غزل شماره ۲۳۵ حافظ", "https://drive.google.com/uc?export=download&id=1BG7dkfc3XpHiTSe60RqC6m9wZjIVidNc");
        put("غزل شماره ۲۳۶ حافظ", "https://drive.google.com/uc?export=download&id=11SDKKpbYadEGaUSgyXUOByFU8q5yIA2f");
        put("غزل شماره ۲۳۷ حافظ", "https://drive.google.com/uc?export=download&id=1UWZMM2IOe--IAo0snWnK0Oy1VYAtvtKQ");
        put("غزل شماره ۲۳۸ حافظ", "https://drive.google.com/uc?export=download&id=1jLtbmCH0ltZKeJaFWJEBG6Q4eckYrLj6");
        put("غزل شماره ۲۳۹ حافظ", "https://drive.google.com/uc?export=download&id=1_DfvANphodsgDGVRo-RUMAuVVclNiThb");
        put("غزل شماره ۲۴۰ حافظ", "https://drive.google.com/uc?export=download&id=1-M_IkQfaC5ntondkB23W_jDHyEdOl-07");
        put("غزل شماره ۲۴۱ حافظ", "https://drive.google.com/uc?export=download&id=1nQB8gXxdl1cB5U_YuKORaoZgDUsmkPL3");
        put("غزل شماره ۲۴۲ حافظ", "https://drive.google.com/uc?export=download&id=1FGS4uxh6KsTbzYSphfnzgmikc9YFAF_Y");
        put("غزل شماره ۲۴۳ حافظ", "https://drive.google.com/uc?export=download&id=1q5XGo4Gi8_hV1N_Tn9JhOqTQhlXxigVD");
        put("غزل شماره ۲۴۴ حافظ", "https://drive.google.com/uc?export=download&id=19f_bB4V-0Tc1iiia68qwPm2huevYoadm");
        put("غزل شماره ۲۴۵ حافظ", "https://drive.google.com/uc?export=download&id=1Ju0MoOgmxDjD3RlERauRwsvJGcqk_KbH");
        put("غزل شماره ۲۴۶ حافظ", "https://drive.google.com/uc?export=download&id=1UrSs0BHQc6baw7_McyCPTKQ9cP_ZnpWJ");
        put("غزل شماره ۲۴۷ حافظ", "https://drive.google.com/uc?export=download&id=1dFpswmLeGVuJFUfLa3QLBkLQhYQvmTa5");
        put("غزل شماره ۲۴۸ حافظ", "https://drive.google.com/uc?export=download&id=1Le70FqFfcsmBKInhAifXO4N-a-1T7dx3");
        put("غزل شماره ۲۴۹ حافظ", "https://drive.google.com/uc?export=download&id=1CXpK9eEAXzi6An2X1lnUG048yZ9_mGRP");
        put("غزل شماره ۲۵۰ حافظ", "https://drive.google.com/uc?export=download&id=17VSr28UYYGHDsKIa5xNtOhVCYSUcJUqE");
        put("غزل شماره ۲۵۱ حافظ", "https://drive.google.com/uc?export=download&id=1TrR7jEYvSuhy5uAPRaJIlBN03EvQfhNj");
        put("غزل شماره ۲۵۲ حافظ", "https://drive.google.com/uc?export=download&id=1_MapyfQQvqKPQItwc250dZl3vSHB2IUf");
        put("غزل شماره ۲۵۳ حافظ", "https://drive.google.com/uc?export=download&id=1GpFn2buUV_iRGxcPykSJY0DTrju5HOyL");
        put("غزل شماره ۲۵۴ حافظ", "https://drive.google.com/uc?export=download&id=1_XJyCtAx1zatJyPQ2-8_O-9Wv5HgRGHT");
        put("غزل شماره ۲۵۵ حافظ", "https://drive.google.com/uc?export=download&id=18K5GGha9cI8RYy7ACyxlal7d05L1K8VC");
        put("غزل شماره ۲۵۶ حافظ", "https://drive.google.com/uc?export=download&id=1ln8Je7uSVvm4kZZtb9dbZYy4sxkGUD0J");
        put("غزل شماره ۲۵۷ حافظ", "https://drive.google.com/uc?export=download&id=1t2X8xhozABLzwc2FbAoRjqrbaPuR5ZDG");
        put("غزل شماره ۲۵۸ حافظ", "https://drive.google.com/uc?export=download&id=1Ve153dGnBiP9_bWMh11xK2pYOEVBK2H3");
        put("غزل شماره ۲۵۹ حافظ", "https://drive.google.com/uc?export=download&id=1sfrcEEEihpB-aL7EZ4hgUSFH59ZW1KMb");
        put("غزل شماره ۲۶۰ حافظ", "https://drive.google.com/uc?export=download&id=1SE0t5pNAjEHSEfmDCVQJ3bCMhD5ojmFp");
        put("غزل شماره ۲۶۱ حافظ", "https://drive.google.com/uc?export=download&id=1XqT-j9bbdmKQl1jx7jzI71DuInW5moSA");
        put("غزل شماره ۲۶۲ حافظ", "https://drive.google.com/uc?export=download&id=12xubJz09RccPJ2Lbk_HC_axhFqhvcp9d");
        put("غزل شماره ۲۶۳ حافظ", "https://drive.google.com/uc?export=download&id=1PHCXCm59gPTLRu0aUCvEWxvqj8LIZpPG");
        put("غزل شماره ۲۶۴ حافظ", "https://drive.google.com/uc?export=download&id=1VW2ycJJym64-qOgflXm9tOlMxu-Tg6O6");
        put("غزل شماره ۲۶۵ حافظ", "https://drive.google.com/uc?export=download&id=1qRkAYhvKQrKLtjBIbsEuUZTdocn6c59E");
        put("غزل شماره ۲۶۶ حافظ", "https://drive.google.com/uc?export=download&id=1eAkoUJ369te37vpDPnxhll1cUGSeTxVB");
        put("غزل شماره ۲۶۷ حافظ", "https://drive.google.com/uc?export=download&id=13yTyBEOslQjur0MnFEXnVRaVt0ea0rgx");
        put("غزل شماره ۲۶۸ حافظ", "https://drive.google.com/uc?export=download&id=1M57a4FS5xcum9GskcGwKQEv0XT4djqCI");
        put("غزل شماره ۲۶۹ حافظ", "https://drive.google.com/uc?export=download&id=1R2iN5zI7-IsPLRG3f6C_9zOfn2tjvg0f");
        put("غزل شماره ۲۷۰ حافظ", "https://drive.google.com/uc?export=download&id=1Lbt2F968W89O9NzIGGsWO4UGxPJE3ey9");
        put("غزل شماره ۲۷۱ حافظ", "https://drive.google.com/uc?export=download&id=1nLgpggzr4ZRhWFwp7-2vzOZte4FED2zu");
        put("غزل شماره ۲۷۲ حافظ", "https://drive.google.com/uc?export=download&id=10CfpPTmq5mya1ddJ1HFnHx9aZ9kJNRwc");
        put("غزل شماره ۲۷۳ حافظ", "https://drive.google.com/uc?export=download&id=1uLjvBRgG3d1QE0__ZQTIIHIewkb_onbf");
        put("غزل شماره ۲۷۴ حافظ", "https://drive.google.com/uc?export=download&id=1JGEDQbbpA107unJydnYraap5wlUKW7mB");
        put("غزل شماره ۲۷۵ حافظ", "https://drive.google.com/uc?export=download&id=14TCCnhMOaEBSCNAIKZlvrHUy-R6nD1e4");
        put("غزل شماره ۲۷۶ حافظ", "https://drive.google.com/uc?export=download&id=1GWDVd7cwvPu6qDs2ArLmd2xydxKIANUB");
        put("غزل شماره ۲۷۷ حافظ", "https://drive.google.com/uc?export=download&id=1_KFciBOf8PcvGjym1k8YKVs-dbL2LbIb");
        put("غزل شماره ۲۷۸ حافظ", "https://drive.google.com/uc?export=download&id=1CUKqBcXGPkwkKo87Q0YBFWtezPeZYrWV");
        put("غزل شماره ۲۷۹ حافظ", "https://drive.google.com/uc?export=download&id=1DdXMGoh6FqFtBRZgbi2Ka8fiBt-rGqhK");
        put("غزل شماره ۲۸۰ حافظ", "https://drive.google.com/uc?export=download&id=1v9IpTVA_cvnF3WgtxDBXNV09Yco_BIPB");
        put("غزل شماره ۲۸۱ حافظ", "https://drive.google.com/uc?export=download&id=1ocegKEYv7H2bKbLXaya3vkrrrCwyCZjk");
        put("غزل شماره ۲۸۲ حافظ", "https://drive.google.com/uc?export=download&id=1ip9K-r-TxiT04n3fFi0Hy4oGSzilcOH_");
        put("غزل شماره ۲۸۳ حافظ", "https://drive.google.com/uc?export=download&id=1G2gldvw90HqKtoqM9ESQD1EA1IS4mg-t");
        put("غزل شماره ۲۸۴ حافظ", "https://drive.google.com/uc?export=download&id=1pNNFsYltWR6VApTojLEuCRAr2rKvHbDO");
        put("غزل شماره ۲۸۵ حافظ", "https://drive.google.com/uc?export=download&id=1bHaI-JlyanLW2rQXA_Q5nuKGKYNCAjCB");
        put("غزل شماره ۲۸۶ حافظ", "https://drive.google.com/uc?export=download&id=1oroAUU4AvI9X-eo7lFHH6NgIQIaKakHx");
        put("غزل شماره ۲۸۷ حافظ", "https://drive.google.com/uc?export=download&id=1R_ekyh2SvyhG3TQk2xgIL2wYYHigqIDQ");
        put("غزل شماره ۲۸۸ حافظ", "https://drive.google.com/uc?export=download&id=1cO3LNOZQq6GeyTnQaCvnubpQjir_iiXD");
        put("غزل شماره ۲۸۹ حافظ", "https://drive.google.com/uc?export=download&id=1HUCs5xfTnajWibeKOSlyUA7n5hV2R3V7");
        put("غزل شماره ۲۹۰ حافظ", "https://drive.google.com/uc?export=download&id=1hLGyfLftrSvxUe4jlujOQTjPvhXndpSe");
        put("غزل شماره ۲۹۱ حافظ", "https://drive.google.com/uc?export=download&id=10qJAk-IvTj2uUAB0dgScGMYf_pa4sexR");
        put("غزل شماره ۲۹۲ حافظ", "https://drive.google.com/uc?export=download&id=1gxvjP_DtdLK19R2j1248K1Wc3sWCp_lm");
        put("غزل شماره ۲۹۳ حافظ", "https://drive.google.com/uc?export=download&id=1SxY0pKyMg8FC-1MUZ-clh3SQWbKax4UR");
        put("غزل شماره ۲۹۴ حافظ", "https://drive.google.com/uc?export=download&id=1S-07XBI3PvrD3UvNu75yoKM4CUQgn2-i");
        put("غزل شماره ۲۹۵ حافظ", "https://drive.google.com/uc?export=download&id=1KjiXHEVqcoVlMVFwUQWXHPPPp3_sfVVm");
        put("غزل شماره ۲۹۶ حافظ", "https://drive.google.com/uc?export=download&id=1TIexWeo0Iy7h7oy3yOGRcqodS5Ly3kKj");
        put("غزل شماره ۲۹۷ حافظ", "https://drive.google.com/uc?export=download&id=1RWHoTmdVBzqjX6slY7X9ycq4Bzej00SJ");
        put("غزل شماره ۲۹۸ حافظ", "https://drive.google.com/uc?export=download&id=1PFGBPlwdjnqqhMFL-_gYPy8n8_0SGPAr");
        put("غزل شماره ۲۹۹ حافظ", "https://drive.google.com/uc?export=download&id=1miT2BF6_mY1Ut6j9M0r-qVNJcFPK2lxt");
        put("غزل شماره ۳۰۰ حافظ", "https://drive.google.com/uc?export=download&id=1NNTFnfxe7_FHLAhYoXo-BDd6NWg4LQGx");
        put("غزل شماره ۳۰۱ حافظ", "https://drive.google.com/uc?export=download&id=1uPRVjuLiJipUZr05Y1hbgDMI_jLR_obj");
        put("غزل شماره ۳۰۲ حافظ", "https://drive.google.com/uc?export=download&id=1oQbm4E0my_MbCqRmiVL1Z0DAX7cdi0AY");
        put("غزل شماره ۳۰۳ حافظ", "https://drive.google.com/uc?export=download&id=1gHwgUumpmMl2G_sDAd6TE7ffacT3k_EQ");
        put("غزل شماره ۳۰۴ حافظ", "https://drive.google.com/uc?export=download&id=15aKcgyfZIgnFKS2VI92YBEDBOT_w6lEW");
        put("غزل شماره ۳۰۵ حافظ", "https://drive.google.com/uc?export=download&id=1-BQD8oDRLK67g0HjlD603RFrzlIdq_Vn");
        put("غزل شماره ۳۰۶ حافظ", "https://drive.google.com/uc?export=download&id=1F4vjJ9xqpTJ4BsGbvGsMBko-HdT2PPot");
        put("غزل شماره ۳۰۷ حافظ", "https://drive.google.com/uc?export=download&id=1CikmZQZhnZJkaqfpgrtsEcUNw--DQNlE");
        put("غزل شماره ۳۰۸ حافظ", "https://drive.google.com/uc?export=download&id=1vnpCpW5nE4b0rha2A58jVlF5eKHcRk9d");
        put("غزل شماره ۳۰۹ حافظ", "https://drive.google.com/uc?export=download&id=1MZBrYaGhiWFmZSnvaZV0aYIxtNk365Wn");
        put("غزل شماره ۳۱۰ حافظ", "https://drive.google.com/uc?export=download&id=1VpeONyxyHY3INhe3q8vvJNvs35Lx_KuY");
        put("غزل شماره ۳۱۱ حافظ", "https://drive.google.com/uc?export=download&id=1KjBaIyHIUbNv2am980Yw4Ae2pG8WTXwt");
        put("غزل شماره ۳۱۲ حافظ", "https://drive.google.com/uc?export=download&id=1qpqdoQ82WItSkrlSVgOC5kf2x0WKtlg4");
        put("غزل شماره ۳۱۳ حافظ", "https://drive.google.com/uc?export=download&id=1JNDD6RzBexKV1xfr9K4F9CtKFLjqXzP3");
        put("غزل شماره ۳۱۴ حافظ", "https://drive.google.com/uc?export=download&id=18T8_w9RH1kJvE-qvH-nVsCNvw3SjRvT2");
        put("غزل شماره ۳۱۵ حافظ", "https://drive.google.com/uc?export=download&id=1dCgKCXYw2qV1fvgprDbWuK6vKp8i5Xw-");
        put("غزل شماره ۳۱۶ حافظ", "https://drive.google.com/uc?export=download&id=18yrwBG9yqvfPIUAlQzpdp_EseTOkw5XI");
        put("غزل شماره ۳۱۷ حافظ", "https://drive.google.com/uc?export=download&id=1eg_AB9lMRx5iB8mk0ZvSAPPuyc8honUy");
        put("غزل شماره ۳۱۸ حافظ", "https://drive.google.com/uc?export=download&id=16Gjbr8MESHvVSLavyD7gAOMQslBULhpY");
        put("غزل شماره ۳۱۹ حافظ", "https://drive.google.com/uc?export=download&id=1Au-TxIy9S8dTDBLHkdpniOm2u6KZ9Jat");
        put("غزل شماره ۳۲۰ حافظ", "https://drive.google.com/uc?export=download&id=1JmZPvR824Xx3FBd79gO4G-1oxySEebUS");
        put("غزل شماره ۳۲۱ حافظ", "https://drive.google.com/uc?export=download&id=16nOb-8HGa89GVxWoCCTWcHhrMWOVauNy");
        put("غزل شماره ۳۲۲ حافظ", "https://drive.google.com/uc?export=download&id=11jc_BVRr-9o8ZcDqVKy3U29No0CUGp3F");
        put("غزل شماره ۳۲۳ حافظ", "https://drive.google.com/uc?export=download&id=13ODvcqgqI4eAwZB-imHFbIanbBeigQdu");
        put("غزل شماره ۳۲۴ حافظ", "https://drive.google.com/uc?export=download&id=1tXuybxc7mWR60MaNBycQ08drF1iOl-jw");
        put("غزل شماره ۳۲۵ حافظ", "https://drive.google.com/uc?export=download&id=1TksBUjPNtYEyKIrmsG7rqJ8o4hHSeizi");
        put("غزل شماره ۳۲۶ حافظ", "https://drive.google.com/uc?export=download&id=15lIveRL0gJN-F4LVTIrnDc5cstOVFoaV");
        put("غزل شماره ۳۲۷ حافظ", "https://drive.google.com/uc?export=download&id=1fkC61z2_LJn-HOct4BLONcp183yx3COO");
        put("غزل شماره ۳۲۸ حافظ", "https://drive.google.com/uc?export=download&id=1X7zYhbc7eCqfWRG3L2No44LOvOxIOLPf");
        put("غزل شماره ۳۲۹ حافظ", "https://drive.google.com/uc?export=download&id=1jGBbb6P_AMCJrI9zpReYHlj90MX6Zqen");
        put("غزل شماره ۳۳۰ حافظ", "https://drive.google.com/uc?export=download&id=1sK-sa3u3NQCiCPm4PF-9-1zaRju3Mjsk");
        put("غزل شماره ۳۳۱ حافظ", "https://drive.google.com/uc?export=download&id=1DvcgVbs1PfzT--jRCwv2l9CzD2cqYrXz");
        put("غزل شماره ۳۳۲ حافظ", "https://drive.google.com/uc?export=download&id=1CgEwUv5IlDWTZyUQ0OwAOQ8T2jLyjguJ");
        put("غزل شماره ۳۳۳ حافظ", "https://drive.google.com/uc?export=download&id=1n7XD3viSSGskUD3PIEMWDScw3UT3uogI");
        put("غزل شماره ۳۳۴ حافظ", "https://drive.google.com/uc?export=download&id=1fPDp74xKeO4m1DJV5izq4WYT8s7mJ6VG");
        put("غزل شماره ۳۳۵ حافظ", "https://drive.google.com/uc?export=download&id=1q6dQtpW6GKV1rXPcTqks_QjVcpG1lUTH");
        put("غزل شماره ۳۳۶ حافظ", "https://drive.google.com/uc?export=download&id=1w_vZy2BoCinpcottokx5y55aqyycoCzQ");
        put("غزل شماره ۳۳۷ حافظ", "https://drive.google.com/uc?export=download&id=18hnI1sQev3x1KYhkBY6Ef-SGEG0LmXqD");
        put("غزل شماره ۳۳۸ حافظ", "https://drive.google.com/uc?export=download&id=1A3K5KcmCbnkKI8nKNfmopnUmm_pp6pw3");
        put("غزل شماره ۳۳۹ حافظ", "https://drive.google.com/uc?export=download&id=11lvbGfdGwHlaHdXTbMC-RNCMNIN3XKXe");
        put("غزل شماره ۳۴۰ حافظ", "https://drive.google.com/uc?export=download&id=1LbMUI0icp8ZqanOnpwFtn5lYT-EeXNX-");
        put("غزل شماره ۳۴۱ حافظ", "https://drive.google.com/uc?export=download&id=1Y3xSBFDyjhnJT7bkpQt3jm90FzhVaEyb");
        put("غزل شماره ۳۴۲ حافظ", "https://drive.google.com/uc?export=download&id=1KYG7DjlrHqaPHN6GaECjs29LeLG9HFt1");
        put("غزل شماره ۳۴۳ حافظ", "https://drive.google.com/uc?export=download&id=16cVJwpvftHplKF6Sl_S0RAh_s9TDfHFt");
        put("غزل شماره ۳۴۴ حافظ", "https://drive.google.com/uc?export=download&id=1lNaOn10wylRZDohYtRzjenVenSo1kn7a");
        put("غزل شماره ۳۴۵ حافظ", "https://drive.google.com/uc?export=download&id=1UayRht0iJeN67P5CkEbwziM1oMSf5k_G");
        put("غزل شماره ۳۴۶ حافظ", "https://drive.google.com/uc?export=download&id=1hcIL_td0NnheqLFaCCznPS5eBCzPWja9");
        put("غزل شماره ۳۴۷ حافظ", "https://drive.google.com/uc?export=download&id=15yU7jGm4Z4kYCrObDZIwFrQVQunRYVW9");
        put("غزل شماره ۳۴۸ حافظ", "https://drive.google.com/uc?export=download&id=1MFJNZjyE_l2_QYcTeXSp2NwjJx0-NpAd");
        put("غزل شماره ۳۴۹ حافظ", "https://drive.google.com/uc?export=download&id=1L3onVC8GbnEK_K0G6tuF8NsDN6y195dz");
        put("غزل شماره ۳۵۰ حافظ", "https://drive.google.com/uc?export=download&id=1uGsCp0mWrzkm-lQ1xaF5NvxpfS0AxNIC");
        put("غزل شماره ۳۵۱ حافظ", "https://drive.google.com/uc?export=download&id=1R3cHhYAdmFjtaa5V1pnS8w6M7jFaH2Ug");
        put("غزل شماره ۳۵۲ حافظ", "https://drive.google.com/uc?export=download&id=1LbyYVDvgKiP_NU0IErVNlRJIYJerCnW7");
        put("غزل شماره ۳۵۳ حافظ", "https://drive.google.com/uc?export=download&id=1b04lxKx3HtJ3FBZkCr1tNF37dj5CyoUM");
        put("غزل شماره ۳۵۴ حافظ", "https://drive.google.com/uc?export=download&id=1fPoYb3iT7upzYnEfg0KB0rv_Zjq_GT0Y");
        put("غزل شماره ۳۵۵ حافظ", "https://drive.google.com/uc?export=download&id=1SjP26vwpop5Za5gYf6cQ04z8Fcb95YjH");
        put("غزل شماره ۳۵۶ حافظ", "https://drive.google.com/uc?export=download&id=1-bFj6BJUgWIvKO3hf8f2uNdQ36aRemm3");
        put("غزل شماره ۳۵۷ حافظ", "https://drive.google.com/uc?export=download&id=10KXtJEIlHx-3Nvtyb7Bb2HQHDuVsXNgD");
        put("غزل شماره ۳۵۸ حافظ", "https://drive.google.com/uc?export=download&id=1DY6LH__6Od5jsP0aLvqCBBh10V6YapmZ");
        put("غزل شماره ۳۵۹ حافظ", "https://drive.google.com/uc?export=download&id=1_3s9lXX1mSSCqWA7JT8V8zA0fVuhOM4c");
        put("غزل شماره ۳۶۰ حافظ", "https://drive.google.com/uc?export=download&id=1v7B5dixUSX03tx6xX4b_BUll_jxV-bOA");
        put("غزل شماره ۳۶۱ حافظ", "https://drive.google.com/uc?export=download&id=1L-7ZYWnHbK1jyvji2QySQubGEeWbgMRA");
        put("غزل شماره ۳۶۲ حافظ", "https://drive.google.com/uc?export=download&id=1Z3EfNmjeqTQu9tW6okFR9cOXaYzO7ZW9");
        put("غزل شماره ۳۶۳ حافظ", "https://drive.google.com/uc?export=download&id=1gMlcG4m_GEUa6B8o1rNFLa3XbOUOAidG");
        put("غزل شماره ۳۶۴ حافظ", "https://drive.google.com/uc?export=download&id=1TWPbrDUo3Fn8HJUU5taxu49GCmVzEIJU");
        put("غزل شماره ۳۶۵ حافظ", "https://drive.google.com/uc?export=download&id=1Hzk7f9hnGMd9mytEHUoGCcaDDx6ZwNbE");
        put("غزل شماره ۳۶۶ حافظ", "https://drive.google.com/uc?export=download&id=1FajFTvfpS57NP5wgJNTtF6ecG2eyOCbZ");
        put("غزل شماره ۳۶۷ حافظ", "https://drive.google.com/uc?export=download&id=1kbkOg4Z0SsU8kxcvS0H1fHIqwdTo7roI");
        put("غزل شماره ۳۶۸ حافظ", "https://drive.google.com/uc?export=download&id=1mupwBzdJgNZDbpM0uWOMO5eVUxlZ4H29");
        put("غزل شماره ۳۶۹ حافظ", "https://drive.google.com/uc?export=download&id=1w-u5P4dp04Ouuw_UjKv0DYD3PrWZT2_J");
        put("غزل شماره ۳۷۰ حافظ", "https://drive.google.com/uc?export=download&id=1sSuBnq8oy4U1rsVAQNG2erXSlayyFQPk");
        put("غزل شماره ۳۷۱ حافظ", "https://drive.google.com/uc?export=download&id=1gjAM3awpiXaImWfbtbGFvazi9tCVWkh-");
        put("غزل شماره ۳۷۲ حافظ", "https://drive.google.com/uc?export=download&id=1DPI8muuwmJc0BXEZ5uuW6guevyCXdZxA");
        put("غزل شماره ۳۷۳ حافظ", "https://drive.google.com/uc?export=download&id=1hH6gv2ay-KtWsb6ltahNCs6uxOI-sOId");
        put("غزل شماره ۳۷۴ حافظ", "https://drive.google.com/uc?export=download&id=1FA2rzTLIf45j4zvUstZ-hv-VNYrA-aN3");
        put("غزل شماره ۳۷۵ حافظ", "https://drive.google.com/uc?export=download&id=1TCatgkdwwh1uvRWUvE-UbE3wYTiW3Lmb");
        put("غزل شماره ۳۷۶ حافظ", "https://drive.google.com/uc?export=download&id=1MgyLuNXXzC4CJ5g36iiLiEx1-gRw3pkm");
        put("غزل شماره ۳۷۷ حافظ", "https://drive.google.com/uc?export=download&id=1T_rthwV9rI0LLFA6QTqsxW3D8k-kdq6j");
        put("غزل شماره ۳۷۸ حافظ", "https://drive.google.com/uc?export=download&id=1Cb9CprBOqdY5KGBGW5x6b85NGhzRDY_a");
        put("غزل شماره ۳۷۹ حافظ", "https://drive.google.com/uc?export=download&id=1EHz1geG5MMeXu4Ul0qiLhZ3_LUETi4Nv");
        put("غزل شماره ۳۸۰ حافظ", "https://drive.google.com/uc?export=download&id=1I9pTY8U1KHeZnIq90GCr2h4rSr-AQfz0");
        put("غزل شماره ۳۸۱ حافظ", "https://drive.google.com/uc?export=download&id=1eHikX0Zur9D7nvHCgJbqUXwwYFc6j-Hf");
        put("غزل شماره ۳۸۲ حافظ", "https://drive.google.com/uc?export=download&id=1A7bnlv1d_NGcv0XG_vAMt6l3rnxnssyY");
        put("غزل شماره ۳۸۳ حافظ", "https://drive.google.com/uc?export=download&id=1RIpGaRyNQEoIKli_-5S49D7DjhYJ1l-Y");
        put("غزل شماره ۳۸۴ حافظ", "https://drive.google.com/uc?export=download&id=1_PFNw6wcLhjKtNkzQluM_D23oT8eN82J");
        put("غزل شماره ۳۸۵ حافظ", "https://drive.google.com/uc?export=download&id=1wk3u929T51EkYwrlNUMpzFhBkYCivxcR");
        put("غزل شماره ۳۸۶ حافظ", "https://drive.google.com/uc?export=download&id=1UNeJ2u7G7XeuPyXozKMVojHSZd46g8Ac");
        put("غزل شماره ۳۸۷ حافظ", "https://drive.google.com/uc?export=download&id=10Ts6j1eY1UVaPqDsA-iDqJrA8xNuRN04");
        put("غزل شماره ۳۸۸ حافظ", "https://drive.google.com/uc?export=download&id=1mh_NX5CZMx7eqFNEt9UZmmQvZTC5l09N");
        put("غزل شماره ۳۸۹ حافظ", "https://drive.google.com/uc?export=download&id=196GjUb9KcGU5XY4fG_olQuywdIG9YVWJ");
        put("غزل شماره ۳۹۰ حافظ", "https://drive.google.com/uc?export=download&id=1m5-OiIhTs6pWvYc944MQSjZunkAoaD8Q");
        put("غزل شماره ۳۹۱ حافظ", "https://drive.google.com/uc?export=download&id=1pzWs9Fi7b-cRpVDmk_DRga9FVpeHvYaO");
        put("غزل شماره ۳۹۲ حافظ", "https://drive.google.com/uc?export=download&id=1jJ0i36tMxc0bm6a1sCMVEJ1yqKGnENLZ");
        put("غزل شماره ۳۹۳ حافظ", "https://drive.google.com/uc?export=download&id=1bawu3z-jGu2bVnCzvb9FaDjGyQH2PNCm");
        put("غزل شماره ۳۹۴ حافظ", "https://drive.google.com/uc?export=download&id=1bOEdsyqnniJoFYCt5FSXc3eV1kF_zRdf");
        put("غزل شماره ۳۹۵ حافظ", "https://drive.google.com/uc?export=download&id=1vHZuSEVeLd2WdGlbvIGAEn1ahhc_i-cy");
        put("غزل شماره ۳۹۶ حافظ", "https://drive.google.com/uc?export=download&id=1p0uWmYHCS83OJEul9I7C-nlI2JDf6owH");
        put("غزل شماره ۳۹۷ حافظ", "https://drive.google.com/uc?export=download&id=1jKIwFG6bbxHp37lrZ2U-w2ayzTxIlojg");
        put("غزل شماره ۳۹۸ حافظ", "https://drive.google.com/uc?export=download&id=18Hrc-A41eHAcl6ihlLRxIC92t12ED46-");
        put("غزل شماره ۳۹۹ حافظ", "https://drive.google.com/uc?export=download&id=1i-3hAO6V1ZIimrXbXWMOL5-19qHZIJl6");
        put("غزل شماره ۴۰۰ حافظ", "https://drive.google.com/uc?export=download&id=1FEVMuAqmyWA8fZsAySkeK7rcODYNoTka");
        put("غزل شماره ۴۰۱ حافظ", "https://drive.google.com/uc?export=download&id=1rU8NkrCca_hevI0GpkJcPqPoJKjch7iC");
        put("غزل شماره ۴۰۲ حافظ", "https://drive.google.com/uc?export=download&id=1MS5c6H7ZFRSksBSp81Y98zsHRF9QxKgb");
        put("غزل شماره ۴۰۳ حافظ", "https://drive.google.com/uc?export=download&id=1UM3dfjnVxZ3VJnPEboNM3zsTO5UCf0PK");
        put("غزل شماره ۴۰۴ حافظ", "https://drive.google.com/uc?export=download&id=1kYoARGv4xn69nHJJ8IncxVWFOAL34luk");
        put("غزل شماره ۴۰۵ حافظ", "https://drive.google.com/uc?export=download&id=1r3kZbzuDq1ZVNqVcd-Q1qSgZTpaE5upR");
        put("غزل شماره ۴۰۶ حافظ", "https://drive.google.com/uc?export=download&id=1xBGSzIZIB3cYZzI3rfmn-wGZaJsDJysz");
        put("غزل شماره ۴۰۷ حافظ", "https://drive.google.com/uc?export=download&id=1t3l8R0DTORYGizUvbwQQuz5ZaGGWbKNC");
        put("غزل شماره ۴۰۸ حافظ", "https://drive.google.com/uc?export=download&id=1vraV_L94VzZT1FbVYPxwlp58roCIv0dE");
        put("غزل شماره ۴۰۹ حافظ", "https://drive.google.com/uc?export=download&id=1IEGVPzWhUACwD43JdGvVon8jkhtto0uF");
        put("غزل شماره ۴۱۰ حافظ", "https://drive.google.com/uc?export=download&id=1bqCNM8e4LCNjpXcibDIcMAoJVtVG_vqO");
        put("غزل شماره ۴۱۱ حافظ", "https://drive.google.com/uc?export=download&id=1Vp0Bvm_-8rn6Um6fNLP5XwihcjwsJf-w");
        put("غزل شماره ۴۱۲ حافظ", "https://drive.google.com/uc?export=download&id=1a6c16g5nbDR9stNEHFap2zJRVivL-8Pw");
        put("غزل شماره ۴۱۳ حافظ", "https://drive.google.com/uc?export=download&id=1CkQSb7vXuU16Oy-KNISqW1O52Nl3OVmG");
        put("غزل شماره ۴۱۴ حافظ", "https://drive.google.com/uc?export=download&id=1xdGAEAzJsQYw_FP2MXbsXvs0ZhT_SGnf");
        put("غزل شماره ۴۱۵ حافظ", "https://drive.google.com/uc?export=download&id=1NbfXCJF2DIyHQHe4pwEbP7uZ-4SaGe9w");
        put("غزل شماره ۴۱۶ حافظ", "https://drive.google.com/uc?export=download&id=1E8U512Quvmi-_BD8tH3UNl1oY-1BqI73");
        put("غزل شماره ۴۱۷ حافظ", "https://drive.google.com/uc?export=download&id=16UBOCQZzqURbWOiWcUskA1auz_9szZCO");
        put("غزل شماره ۴۱۸ حافظ", "https://drive.google.com/uc?export=download&id=1_-DbN6P4BvBbsHV9geziBqel2I6oIPj_");
        put("غزل شماره ۴۱۹ حافظ", "https://drive.google.com/uc?export=download&id=1BU4VutBtSwjBVkbA67v09lhsmGDC46wx");
        put("غزل شماره ۴۲۰ حافظ", "https://drive.google.com/uc?export=download&id=1l-8q1THmZLyjUTzLDWwthiLdYhNhWDLw");
        put("غزل شماره ۴۲۱ حافظ", "https://drive.google.com/uc?export=download&id=11lBdCFU1XJceZ0UTFEZyKRTpIl7erBfy");
        put("غزل شماره ۴۲۲ حافظ", "https://drive.google.com/uc?export=download&id=1GZR5AyZ6r2ZF26Rf4aPrP7w1pnKxdbxH");
        put("غزل شماره ۴۲۳ حافظ", "https://drive.google.com/uc?export=download&id=1g1o9Ikr6pUxTBE2Ex7eHEEckPK5jSxKV");
        put("غزل شماره ۴۲۴ حافظ", "https://drive.google.com/uc?export=download&id=1gRUwstshDKc2Qr36KzK4sjoOoe_1SSTn");
        put("غزل شماره ۴۲۵ حافظ", "https://drive.google.com/uc?export=download&id=1XiCBETGhKpjMl6F1ptop_aXd1N17LTHu");
        put("غزل شماره ۴۲۶ حافظ", "https://drive.google.com/uc?export=download&id=1TC3fx-Z6r3A2JwFz2tiG_B_gzd5D9A3S");
        put("غزل شماره ۴۲۷ حافظ", "https://drive.google.com/uc?export=download&id=19i4_msf2xZG63oDv9xryVvBP2vJNFnFl");
        put("غزل شماره ۴۲۸ حافظ", "https://drive.google.com/uc?export=download&id=12gCTmcdiEwkyQUhDBuhTlS-wVgBBiwlQ");
        put("غزل شماره ۴۲۹ حافظ", "https://drive.google.com/uc?export=download&id=1VXfTwqj_lbEg9B8gJSKgD_Mmdq2upl0I");
        put("غزل شماره ۴۳۰ حافظ", "https://drive.google.com/uc?export=download&id=1nE6OiZg6_JmKt-DD-HLdzMmNCu_IMQtK");
        put("غزل شماره ۴۳۱ حافظ", "https://drive.google.com/uc?export=download&id=1nQwHiqdo0PrJnrdqi9c2js8CrykUGxF3");
        put("غزل شماره ۴۳۲ حافظ", "https://drive.google.com/uc?export=download&id=1deeOueG6gPf8S70q-agvwxYPMHL0Lzfy");
        put("غزل شماره ۴۳۳ حافظ", "https://drive.google.com/uc?export=download&id=1S3R1k4EJUC6_G1vpyRKsjOu2lKUCOHIh");
        put("غزل شماره ۴۳۴ حافظ", "https://drive.google.com/uc?export=download&id=1ZrQLQ9727pf6G4Fq1STpgMpthQA_8wMG");
        put("غزل شماره ۴۳۵ حافظ", "https://drive.google.com/uc?export=download&id=1I_TvkKtMDse4gIQXulOS-eBRpcBgzoT-");
        put("غزل شماره ۴۳۶ حافظ", "https://drive.google.com/uc?export=download&id=1S0BCe2lPdiSaHYxY9CwsSpGyFdaN5-g1");
        put("غزل شماره ۴۳۷ حافظ", "https://drive.google.com/uc?export=download&id=1-sxDZjkn6DAqMy5D6-pQF0cAIDUpyKZF");
        put("غزل شماره ۴۳۸ حافظ", "https://drive.google.com/uc?export=download&id=10YVN_vxAzFL-fmhUR0FuJ3ZzKXejeDL_");
        put("غزل شماره ۴۳۹ حافظ", "https://drive.google.com/uc?export=download&id=1rtBxvNID4xxubk0U9VA1uxYAEEfbVTpt");
        put("غزل شماره ۴۴۰ حافظ", "https://drive.google.com/uc?export=download&id=1U5w4jiKJM1wiczCb5SZoe-N0AqvFWeow");
        put("غزل شماره ۴۴۱ حافظ", "https://drive.google.com/uc?export=download&id=1vIKOTYy8tI42FsEjoFbm2C-14ge93cA5");
        put("غزل شماره ۴۴۲ حافظ", "https://drive.google.com/uc?export=download&id=1gDqpzYFjZuHY0X83xH61beyTxqvE9rC7");
        put("غزل شماره ۴۴۳ حافظ", "https://drive.google.com/uc?export=download&id=1C6CFSPfJ_lcdjEcrHtbmGTSXQqYEn2fB");
        put("غزل شماره ۴۴۴ حافظ", "https://drive.google.com/uc?export=download&id=19L5uid0qLq9QVtRf19banOY9jI3f4NHm");
        put("غزل شماره ۴۴۵ حافظ", "https://drive.google.com/uc?export=download&id=1SIQfP91q3ER8teYFXGf3oKlPF1Pd7pWM");
        put("غزل شماره ۴۴۶ حافظ", "https://drive.google.com/uc?export=download&id=1cCprKefeo4jl9I5aLsPc507kpx_GAVKt");
        put("غزل شماره ۴۴۷ حافظ", "https://drive.google.com/uc?export=download&id=1m5AmC7FeXbn77fTWnCgAzmaf9NqIEns3");
        put("غزل شماره ۴۴۸ حافظ", "https://drive.google.com/uc?export=download&id=1rcQhYcMrxKQtggkpsIcPSML83JVUcCh2");
        put("غزل شماره ۴۴۹ حافظ", "https://drive.google.com/uc?export=download&id=1osv8XulKjUZen2xxBaKDVzva2ztHVVdb");
        put("غزل شماره ۴۵۰ حافظ", "https://drive.google.com/uc?export=download&id=1lz0mgMkvqSijZwiIDqCVueJsBztpL4Er");
        put("غزل شماره ۴۵۱ حافظ", "https://drive.google.com/uc?export=download&id=1--QJZAnMxtFFhibvKb4IVwFSh40VgtXz");
        put("غزل شماره ۴۵۲ حافظ", "https://drive.google.com/uc?export=download&id=1RVCu1hhQJGcMZDOLdbE5mslfmhkzR8Rd");
        put("غزل شماره ۴۵۳ حافظ", "https://drive.google.com/uc?export=download&id=16zUHuiKtCG3-68Ko8P5YkgYs_WSd9sAc");
        put("غزل شماره ۴۵۴ حافظ", "https://drive.google.com/uc?export=download&id=1nCH7q9V4EdQY52Em0pilt4PstbjXLiI3");
        put("غزل شماره ۴۵۵ حافظ", "https://drive.google.com/uc?export=download&id=1AoXIt8lf2rBsqh_P17zFL6ooQZiAIMeQ");
        put("غزل شماره ۴۵۶ حافظ", "https://drive.google.com/uc?export=download&id=1U3qZupVd1rgukD-NxgeG5WbNkdkNroFk");
        put("غزل شماره ۴۵۷ حافظ", "https://drive.google.com/uc?export=download&id=17qcqGcx7I5R2sWlowe5DAbQE0sbR6eSv");
        put("غزل شماره ۴۵۸ حافظ", "https://drive.google.com/uc?export=download&id=1oS6pbwsiX_nMouKKGUImVZYJYhhhEI6y");
        put("غزل شماره ۴۵۹ حافظ", "https://drive.google.com/uc?export=download&id=1DtnAGnGkaU46n1ZRG-QsumG8D1icoC-Y");
        put("غزل شماره ۴۶۰ حافظ", "https://drive.google.com/uc?export=download&id=1Vr5aTItgvcl6Hr0p_uCiQF34yEKDlB44");
        put("غزل شماره ۴۶۱ حافظ", "https://drive.google.com/uc?export=download&id=1tTK6F_wPPafIhw3QUatFOg6RNLwx9-rR");
        put("غزل شماره ۴۶۲ حافظ", "https://drive.google.com/uc?export=download&id=1_ILTH98paA1L9vUlVo8A-AvvM_rNpX2N");
        put("غزل شماره ۴۶۳ حافظ", "https://drive.google.com/uc?export=download&id=1GPQKc9HMGiOOKkKmAM6Qq7ElwSyTIkHH");
        put("غزل شماره ۴۶۴ حافظ", "https://drive.google.com/uc?export=download&id=1oxJWK3H_2-731tmqvjqzKwZqams9pI0U");
        put("غزل شماره ۴۶۵ حافظ", "https://drive.google.com/uc?export=download&id=1pNKtMqUCi8uCEqmDF7Tr8y9V8v2CLyjn");
        put("غزل شماره ۴۶۶ حافظ", "https://drive.google.com/uc?export=download&id=1q2op2d578T5EYp7pTaCuZOqDw4bZIV6x");
        put("غزل شماره ۴۶۷ حافظ", "https://drive.google.com/uc?export=download&id=1tGPwZapTx5Fwv5cO2xsKJyuyeaG8rWNg");
        put("غزل شماره ۴۶۸ حافظ", "https://drive.google.com/uc?export=download&id=1BQuM8lw79oqJtXPS6GedKr3WQT2zPBDo");
        put("غزل شماره ۴۶۹ حافظ", "https://drive.google.com/uc?export=download&id=1_Ed-cCOYv3KDdhGzG97PmFLYkf1Eg2wv");
        put("غزل شماره ۴۷۰ حافظ", "https://drive.google.com/uc?export=download&id=1PT8QW4i9L6MFVTIVXEx3RIkVMakUCKYJ");
        put("غزل شماره ۴۷۱ حافظ", "https://drive.google.com/uc?export=download&id=1o-IuZZ-oRseBxl1kJJzqby9csixXsgPz");
        put("غزل شماره ۴۷۲ حافظ", "https://drive.google.com/uc?export=download&id=1XbCv0IoOZX288p-vpuhOF_MyGRNgnPC5");
        put("غزل شماره ۴۷۳ حافظ", "https://drive.google.com/uc?export=download&id=1JkAjK1Hc87Wodau2Z0Z-2vYq7YFNgRsw");
        put("غزل شماره ۴۷۴ حافظ", "https://drive.google.com/uc?export=download&id=1hxuOt9z9I3zW9sC8fAD-6H829oUT5hFs");
        put("غزل شماره ۴۷۵ حافظ", "https://drive.google.com/uc?export=download&id=1JXmMsw0IYPKEPjvcJFF0lbhdQenSufJF");
        put("غزل شماره ۴۷۶ حافظ", "https://drive.google.com/uc?export=download&id=1v6kinBaq5-gvkd-kLgGKQQtEBvBd5ubP");
        put("غزل شماره ۴۷۷ حافظ", "https://drive.google.com/uc?export=download&id=1ssqrEmTw1rZGIaHrm27V6FwLX5qF8iRU");
        put("غزل شماره ۴۷۸ حافظ", "https://drive.google.com/uc?export=download&id=18ivodFBkD4Dy_qkiktkqqKNuTvsEZuRq");
        put("غزل شماره ۴۷۹ حافظ", "https://drive.google.com/uc?export=download&id=1YvzFedBPOPUnjmK83Gor3CZo9CE0W_lQ");
        put("غزل شماره ۴۸۰ حافظ", "https://drive.google.com/uc?export=download&id=1daeUHMxYeh_hnRx14nTaVlX4Xy6ze2BH");
        put("غزل شماره ۴۸۱ حافظ", "https://drive.google.com/uc?export=download&id=1g4zrmIzRgiOr__qrrXY-ziBhklSzV7Ca");
        put("غزل شماره ۴۸۲ حافظ", "https://drive.google.com/uc?export=download&id=1WDWEqsSQc9_T8o4WXg-HeHRILgyjM5xM");
        put("غزل شماره ۴۸۳ حافظ", "https://drive.google.com/uc?export=download&id=1pZv8S6I3U1s3ArruCMm7Hbcz2BKfBefd");
        put("غزل شماره ۴۸۴ حافظ", "https://drive.google.com/uc?export=download&id=1TsXDVIaI0uwt56sN1aZArZTtJYpqGudr");
        put("غزل شماره ۴۸۵ حافظ", "https://drive.google.com/uc?export=download&id=1u83XvzZHbP0CYVnnr8BQtY9gH3YgQvzO");
        put("غزل شماره ۴۸۶ حافظ", "https://drive.google.com/uc?export=download&id=1OrajsbPvm_QFPd9lwMJm5uitFQziqb26");
        put("غزل شماره ۴۸۷ حافظ", "https://drive.google.com/uc?export=download&id=1w46TPo7_E3WtSZbcYaqAa9dB9cOAETwd");
        put("غزل شماره ۴۸۸ حافظ", "https://drive.google.com/uc?export=download&id=1ad3wN8YRCWRBlpNPvlAWsUWngQ5nX6yi");
        put("غزل شماره ۴۸۹ حافظ", "https://drive.google.com/uc?export=download&id=1JuUrosMBCU1ahNAkcLxSCv1HieSiU1pX");
        put("غزل شماره ۴۹۰ حافظ", "https://drive.google.com/uc?export=download&id=1vBlJwOLSXN8MYbHqw_ADLPAaP0e-9l6E");
        put("غزل شماره ۴۹۱ حافظ", "https://drive.google.com/uc?export=download&id=1V6XYokbO41iSXgNi9s_MtLR5RMbd5BGZ");
        put("غزل شماره ۴۹۲ حافظ", "https://drive.google.com/uc?export=download&id=1he89tFrcp-DI77wzJICeCbo1Ewf8-POV");
        put("غزل شماره ۴۹۳ حافظ", "https://drive.google.com/uc?export=download&id=1wR7CB-38j2a3GO4uCF97CAgVNTiwriHJ");
        put("غزل شماره ۴۹۴ حافظ", "https://drive.google.com/uc?export=download&id=15FIfCTfJRzBPLElpjKj6Ivu460ueyi1w");
        put("غزل شماره ۴۹۵ حافظ", "https://drive.google.com/uc?export=download&id=1n1aWrlGlnaPE49uuFLgDbBCces_5_iuX");
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