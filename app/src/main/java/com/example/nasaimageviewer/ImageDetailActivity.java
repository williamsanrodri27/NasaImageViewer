package com.example.nasaimageviewer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

/**
 * Activity to display details of a selected image.
 */
public class ImageDetailActivity extends AppCompatActivity {

    private TextView textViewDetailDate;
    private ImageView imageViewDetail;
    private TextView textViewHdUrl;
    private TextView textViewTitle;
    private TextView textViewSection;
    private Button buttonOpenArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        textViewDetailDate = findViewById(R.id.textViewDetailDate);
        imageViewDetail = findViewById(R.id.imageViewDetail);
        textViewHdUrl = findViewById(R.id.textViewHdUrl);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewSection = findViewById(R.id.textViewSection);
        buttonOpenArticle = findViewById(R.id.buttonOpenArticle);

        String imageDate = getIntent().getStringExtra("imageDate");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String hdUrl = getIntent().getStringExtra("hdUrl");
        String title = getIntent().getStringExtra("title");
        String section = getIntent().getStringExtra("section");

        textViewDetailDate.setText(getString(R.string.date_text, imageDate));
        Picasso.get().load(imageUrl).into(imageViewDetail);

        textViewTitle.setText(title);
        textViewSection.setText(section);

        if (hdUrl != null && !hdUrl.isEmpty()) {
            textViewHdUrl.setText(hdUrl);
            textViewHdUrl.setOnClickListener(v -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(hdUrl));
                startActivity(browserIntent);
            });
        } else {
            textViewHdUrl.setText(R.string.no_hd_url_available);
        }

        buttonOpenArticle.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(hdUrl != null ? hdUrl : imageUrl));
            startActivity(browserIntent);
        });
    }
}

