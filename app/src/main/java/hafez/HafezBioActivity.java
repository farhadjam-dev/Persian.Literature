package hafez;


import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.jamlab.adab.R;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;

public class HafezBioActivity extends AppCompatActivity {

    private TextView bioText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hafez_bio);

        // اتصال TextView به layout
        bioText = findViewById(R.id.bio_text);

        // خواندن فایل JSON و نمایش زندگی‌نامه
        String bio = loadJSONFromRaw();
        if (bio != null) {
            bioText.setText(bio);
        }
    }

    // خواندن فایل JSON از پوشه raw
    private String loadJSONFromRaw() {
        String json;
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.hafez_bio);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

            // تجزیه JSON و استخراج زندگی‌نامه
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.getString("bio");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}