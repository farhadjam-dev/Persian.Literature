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