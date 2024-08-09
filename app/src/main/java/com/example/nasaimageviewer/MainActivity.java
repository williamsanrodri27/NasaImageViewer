package com.example.nasaimageviewer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nasaimageviewer.adapters.ItemAdapter;
import com.example.nasaimageviewer.entities.Item;
import com.example.nasaimageviewer.viewmodel.FetchImageTask;
import com.example.nasaimageviewer.viewmodel.ItemViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DatePicker datePicker;
    private Button buttonFetchImage;
    private TextView textViewDate;
    private ImageView imageView;
    private Button buttonSaveImage;
    private Button buttonViewSavedImages;
    private ProgressBar progressBar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private RecyclerView listViewItems;
    private EditText editTextNewItem;
    private Button buttonAddItem;

    private ItemAdapter itemAdapter;
    private ItemViewModel itemViewModel;
    private List<Item> itemList;

    private String primaryApiKey = "DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d";
    private String backupApiKey = "rU8MPPz2B2OjPW737df1cbKbqGFHPKoTR2XJ4Zfy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        datePicker = findViewById(R.id.datePicker);
        buttonFetchImage = findViewById(R.id.buttonFetchImage);
        textViewDate = findViewById(R.id.textViewDate);
        imageView = findViewById(R.id.imageView);
        buttonSaveImage = findViewById(R.id.buttonSaveImage);
        buttonViewSavedImages = findViewById(R.id.buttonViewSavedImages);
        progressBar = findViewById(R.id.progressBar);

        listViewItems = findViewById(R.id.listViewItems);
        editTextNewItem = findViewById(R.id.editTextNewItem);
        buttonAddItem = findViewById(R.id.buttonAddItem);

        String savedDate = getSavedDate();
        if (savedDate != null) {
            String[] dateParts = savedDate.split("-");
            int year = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]) - 1;
            int day = Integer.parseInt(dateParts[2]);
            datePicker.updateDate(year, month, day);
        }

        listViewItems.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new ItemAdapter(this, itemList, item -> {
            itemViewModel.delete(item);
            Toast.makeText(MainActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
        });
        listViewItems.setAdapter(itemAdapter);

        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        itemViewModel.getAllItems().observe(this, items -> {
            itemList = items;
            itemAdapter.setItems(items);
        });

        buttonFetchImage.setOnClickListener(v -> {
            int year = datePicker.getYear();
            int month = datePicker.getMonth() + 1;
            int day = datePicker.getDayOfMonth();
            String date = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month, day);
            saveSelectedDate(date);
            fetchImage(date);
        });

        buttonSaveImage.setOnClickListener(v -> {
            NasaImage nasaImage = new NasaImage(
                    textViewDate.getText().toString(),
                    imageView.getTag().toString(),
                    "your_hd_image_url"
            );
            saveImage(nasaImage);
        });

        buttonViewSavedImages.setOnClickListener(v -> {
            Intent intent = new Intent(this, SavedImagesActivity.class);
            startActivity(intent);
        });

        buttonAddItem.setOnClickListener(v -> addItem());

        TextView navHeaderTitle = navigationView.getHeaderView(0).findViewById(R.id.nav_header_title);
        TextView navHeaderVersion = navigationView.getHeaderView(0).findViewById(R.id.nav_header_version);

        navHeaderTitle.setText(getString(R.string.nav_header_title));
        navHeaderVersion.setText(getString(R.string.nav_header_version));

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackPressed();
            }
        });
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public TextView getTextViewDate() {
        return textViewDate;
    }

    public ImageView getImageView() {
        return imageView;
    }

    private void handleBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            selectedFragment = new HomeFragment();
        } else if (id == R.id.nav_saved_images) {
            startActivity(new Intent(this, SavedImagesActivity.class));
            return true;
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_help) {
            showHelpDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showHelpDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.action_help)
                .setMessage(R.string.help_message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    private void fetchImage(String date) {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        new FetchImageTask(this, primaryApiKey, backupApiKey).execute(date);
    }

    private void saveImage(NasaImage image) {
        List<NasaImage> savedImages = loadSavedImages();
        savedImages.add(image);
        getSharedPreferences("saved_images", MODE_PRIVATE)
                .edit()
                .putString("images", new Gson().toJson(savedImages))
                .apply();
        Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show();
    }

    private List<NasaImage> loadSavedImages() {
        String json = getSharedPreferences("saved_images", MODE_PRIVATE)
                .getString("images", "[]");
        return new Gson().fromJson(json, new TypeToken<List<NasaImage>>() {}.getType());
    }

    private void addItem() {
        String newItemName = editTextNewItem.getText().toString().trim();
        if (!newItemName.isEmpty()) {
            Item newItem = new Item(newItemName);
            itemViewModel.insert(newItem);
            editTextNewItem.setText("");
            Toast.makeText(this, "Item added", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveSelectedDate(String date) {
        SharedPreferences preferences = getSharedPreferences("app_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("selected_date", date);
        editor.apply();
    }

    private String getSavedDate() {
        SharedPreferences preferences = getSharedPreferences("app_preferences", MODE_PRIVATE);
        return preferences.getString("selected_date", null);
    }
}
