package com.example.nasaimageviewer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nasaimageviewer.R;
import com.example.nasaimageviewer.entities.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private final Context context;
    private List<Item> itemList = new ArrayList<>();
    private final OnItemDeleteClickListener deleteClickListener;

    public ItemAdapter(Context context, List<Item> itemList, OnItemDeleteClickListener deleteClickListener) {
        this.context = context;
        this.itemList = (itemList != null) ? itemList : new ArrayList<>();
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.itemNameTextView.setText(item.getName());
        holder.deleteButton.setOnClickListener(v -> deleteClickListener.onItemDeleteClick(item));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItems(List<Item> items) {
        this.itemList = items;
       // notifyDataSetChanged();
    }

    public interface OnItemDeleteClickListener {
        void onItemDeleteClick(Item item);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView;
        Button deleteButton;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.item_name);
            deleteButton = itemView.findViewById(R.id.button_delete);
        }
    }
}
