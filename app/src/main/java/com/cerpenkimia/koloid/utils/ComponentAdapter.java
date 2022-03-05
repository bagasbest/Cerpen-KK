package com.cerpenkimia.koloid.utils;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.cerpenkimia.koloid.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ComponentAdapter extends RecyclerView.Adapter<ComponentAdapter.ViewHolder> {

    private final ArrayList<String> listData = new ArrayList<>();


    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<String> items) {
        listData.clear();
        listData.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ComponentAdapter.ViewHolder onCreateViewHolder (@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_component, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ComponentAdapter.ViewHolder holder, int position) {
        holder.bind(listData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView number, text;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            text = itemView.findViewById(R.id.text);
            linearLayout = itemView.findViewById(R.id.linear_layout);
        }

        public void bind(String dataModel, int position) {


            number.setText(String.valueOf(position+1));
            text.setText(dataModel);
        }
    }
}
