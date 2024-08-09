package com.example.nasaimageviewer.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.nasaimageviewer.entities.Item;
import com.example.nasaimageviewer.repository.ItemRepository;
import java.util.List;

public class ItemViewModel extends AndroidViewModel {
    private ItemRepository itemRepository;
    private LiveData<List<Item>> allItems;

    public ItemViewModel(@NonNull Application application) {
        super(application);
        itemRepository = new ItemRepository(application);
        allItems = itemRepository.getAllItems();
    }

    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }

    public void insert(Item item) {
        itemRepository.insert(item);
    }

    public void delete(Item item) {
        itemRepository.delete(item);
    }
}
