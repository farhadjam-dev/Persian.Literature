package com.jamlab.adab;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PoemLoader {

    public static List<String> loadPoemsFromJson(InputStream inputStream) {
        List<String> poems = new ArrayList<>();

        try {
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, "UTF-8");

            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject poemObject = jsonArray.getJSONObject(i);
                JSONArray versesArray = poemObject.getJSONArray("verses");
                for (int j = 0; j < versesArray.length(); j++) {
                    JSONObject verseObject = versesArray.getJSONObject(j);
                    poems.add(verseObject.getString("text"));
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return poems;
    }
}