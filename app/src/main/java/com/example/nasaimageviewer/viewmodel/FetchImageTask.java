package com.example.nasaimageviewer.viewmodel;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nasaimageviewer.MainActivity;
import com.example.nasaimageviewer.NasaImage;
import com.example.nasaimageviewer.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchImageTask extends AsyncTask<String, Void, NasaImage> {
    private final WeakReference<MainActivity> activityReference;
    private final String primaryApiKey;
    private final String backupApiKey;

    public FetchImageTask(MainActivity context, String primaryApiKey, String backupApiKey) {
        activityReference = new WeakReference<>(context);
        this.primaryApiKey = primaryApiKey;
        this.backupApiKey = backupApiKey;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        MainActivity activity = activityReference.get();
        if (activity == null || activity.isFinishing()) return;
        activity.getProgressBar().setVisibility(ProgressBar.VISIBLE);
    }

    @Override
    protected NasaImage doInBackground(String... params) {
        String date = params[0];
        NasaImage result = fetchImageWithApiKey(date, primaryApiKey);

        if (result == null) {
            Log.d("FetchImageTask", "Primary API key failed, trying backup key.");
            result = fetchImageWithApiKey(date, backupApiKey);
        }

        return result;
    }

    private NasaImage fetchImageWithApiKey(String date, String apiKey) {
        try {
            URL url = new URL("https://api.nasa.gov/planetary/apod?api_key=" + apiKey + "&date=" + date);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            Log.d("FetchImageTask", "Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                connection.disconnect();

                Log.d("FetchImageTask", "Response: " + content.toString());

                return new Gson().fromJson(content.toString(), NasaImage.class);
            } else {
                Log.d("FetchImageTask", "Error: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(NasaImage nasaImage) {
        super.onPostExecute(nasaImage);
        MainActivity activity = activityReference.get();
        if (activity == null || activity.isFinishing()) return;

        activity.getProgressBar().setVisibility(ProgressBar.GONE);
        if (nasaImage != null) {
            activity.getTextViewDate().setText(nasaImage.getDate());
            Picasso.get().load(nasaImage.getUrl()).into(activity.getImageView());
            activity.getImageView().setTag(nasaImage.getUrl());

            TextView hdUrlTextView = activity.findViewById(R.id.hdUrlTextView);
            if (nasaImage.getHdurl() != null && !nasaImage.getHdurl().isEmpty()) {
                hdUrlTextView.setText(nasaImage.getHdurl());
                hdUrlTextView.setOnClickListener(v -> {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(nasaImage.getHdurl()));
                    activity.startActivity(browserIntent);
                });
            }
        } else {
            Toast.makeText(activity, "Failed to fetch image", Toast.LENGTH_SHORT).show();
        }
    }
}
