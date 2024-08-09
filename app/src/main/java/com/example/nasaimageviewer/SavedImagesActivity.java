package com.example.nasaimageviewer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class SavedImagesActivity extends AppCompatActivity {

    private ListView listViewSavedImages;
    private List<NasaImage> savedImages;
    private SavedImagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_images);

        listViewSavedImages = findViewById(R.id.listViewSavedImages);
        savedImages = loadSavedImages();

        adapter = new SavedImagesAdapter(savedImages);
        listViewSavedImages.setAdapter(adapter);

        // Back button to return to the main activity
        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> finish()); // finish() returns to the previous activity
    }

    private List<NasaImage> loadSavedImages() {
        String json = getSharedPreferences("saved_images", MODE_PRIVATE)
                .getString("images", "[]");
        return new Gson().fromJson(json, new TypeToken<List<NasaImage>>() {}.getType());
    }

    public void saveImages() {
        String json = new Gson().toJson(savedImages);
        getSharedPreferences("saved_images", MODE_PRIVATE)
                .edit()
                .putString("images", json)
                .apply();
    }

    private class SavedImagesAdapter extends ArrayAdapter<NasaImage> {

        public SavedImagesAdapter(List<NasaImage> images) {
            super(SavedImagesActivity.this, 0, images);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_with_delete, parent, false);
            }

            NasaImage image = getItem(position);

            TextView textViewItemTitle = convertView.findViewById(R.id.textViewItemTitle);
            TextView textViewItemDate = convertView.findViewById(R.id.textViewItemDate);
            TextView textViewItemUrl = convertView.findViewById(R.id.textViewItemUrl);
            TextView textViewItemSection = convertView.findViewById(R.id.textViewItemSection);
            Button buttonDeleteItem = convertView.findViewById(R.id.buttonDeleteItem);

            textViewItemTitle.setText(image.getTitle());
            textViewItemDate.setText(image.getDate());
            textViewItemUrl.setText(image.getUrl());
            textViewItemSection.setText(image.getSection());

            convertView.setOnClickListener(v -> {
                Intent intent = new Intent(SavedImagesActivity.this, ImageDetailActivity.class);
                intent.putExtra("imageDate", image.getDate());
                intent.putExtra("imageUrl", image.getUrl());
                intent.putExtra("title", image.getTitle());
                intent.putExtra("section", image.getSection());
                startActivity(intent);
            });

            buttonDeleteItem.setOnClickListener(v -> showDeleteConfirmationDialog(position));

            return convertView;
        }
    }

    private void showDeleteConfirmationDialog(int position) {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Delete Image")
                .setMessage("Are you sure you want to delete this image?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> deleteImage(position))
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void deleteImage(int position) {
        savedImages.remove(position);
        saveImages(); // Save the updated list
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Image deleted", Toast.LENGTH_SHORT).show();
    }
}
