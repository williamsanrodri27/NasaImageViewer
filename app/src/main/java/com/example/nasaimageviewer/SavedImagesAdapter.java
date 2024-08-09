package com.example.nasaimageviewer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class SavedImagesAdapter extends ArrayAdapter<NasaImage> {

    private Context context;
    private List<NasaImage> savedImages;
    private SavedImagesActivity activity;

    public SavedImagesAdapter(SavedImagesActivity activity, List<NasaImage> images) {
        super(activity, 0, images);
        this.context = activity;
        this.savedImages = images;
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_with_delete, parent, false);
        }

        NasaImage image = getItem(position);

        TextView textViewItemDate = convertView.findViewById(R.id.textViewItemDate);
        Button buttonDeleteItem = convertView.findViewById(R.id.buttonDeleteItem);

        textViewItemDate.setText(image.getDate());

        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(activity, ImageDetailActivity.class);
            intent.putExtra("imageDate", image.getDate());
            intent.putExtra("imageUrl", image.getUrl());
            intent.putExtra("hdUrl", image.getHdurl()); // Pass HD URL if available
            activity.startActivity(intent);
        });

        buttonDeleteItem.setOnClickListener(v -> showDeleteConfirmationDialog(position));

        return convertView;
    }

    /**
     * Shows a confirmation dialog to delete an image.
     * @param position The position of the item to be deleted.
     */
    private void showDeleteConfirmationDialog(int position) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Image")
                .setMessage("Are you sure you want to delete this image?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> deleteImage(position))
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    /**
     * Deletes an image from the list and notifies the adapter.
     * @param position The position of the image to be deleted.
     */
    private void deleteImage(int position) {
        savedImages.remove(position);
        activity.saveImages();
        notifyDataSetChanged();
    }
}
