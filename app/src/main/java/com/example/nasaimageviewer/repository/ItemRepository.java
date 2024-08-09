package com.example.nasaimageviewer.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.nasaimageviewer.dao.ItemDao;
import com.example.nasaimageviewer.database.ItemDatabase;
import com.example.nasaimageviewer.entities.Item;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ItemRepository {
    private ItemDao itemDao;
    private LiveData<List<Item>> allItems;
    private static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(1);

    public ItemRepository(Application application) {
        ItemDatabase database = ItemDatabase.getInstance(application);
        itemDao = database.itemDao();
        allItems = itemDao.getAllItems();
    }

    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }

    public void insert(Item item) {
        databaseWriteExecutor.execute(() -> itemDao.insert(item));
    }

    public void delete(Item item) {
        databaseWriteExecutor.execute(() -> itemDao.delete(item));
    }
}
