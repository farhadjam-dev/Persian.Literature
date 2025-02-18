package com.jamlab.adab;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PoemLoader {

    public static List<Poem> loadPoemsFromJson(InputStream inputStream) {
        List<Poem> poems = new ArrayList<>();

        try {
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, "UTF-8");

            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject poemObject = jsonArray.getJSONObject(i);
                String title = poemObject.getString("title");
                JSONArray versesArray = poemObject.getJSONArray("verses");
                StringBuilder poemText = new StringBuilder();
                for (int j = 0; j < versesArray.length(); j++) {
                    JSONObject verseObject = versesArray.getJSONObject(j);
                    poemText.append(verseObject.getString("text")).append("\n");
                }
                poems.add(new Poem(title, poemText.toString()));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return poems;
    }
}