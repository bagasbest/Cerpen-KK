package com.cerpenkimia.koloid.cerpen;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cerpenkimia.koloid.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private final ArrayList<Uri> uriArrayList;
    private final ArrayList<String> image;
    private final String option;

    public ImageAdapter(ArrayList<Uri> uriArrayList, ArrayList<String> image, String option) {
        this.uriArrayList = uriArrayList;
        this.image = image;
        this.option = option;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        if(option.equals("add")) {
            holder.bind(uriArrayList.get(position), uriArrayList);
        } else {
            holder.bindImage(image.get(position), image);
        }
    }

    @Override
    public int getItemCount() {
        if(option.equals("add")) {
            return uriArrayList.size();
        } else {
            return image.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        FloatingActionButton delete;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            delete = itemView.findViewById(R.id.deleteBtn);
        }

        public void bind(Uri uri, ArrayList<Uri> uriArrayList) {
            Glide.with(itemView.getContext())
                    .load(uri)
                    .into(image);

            delete.setOnClickListener(view -> {
                uriArrayList.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                notifyItemRangeChanged(getAdapterPosition(), uriArrayList.size());
            });
        }

        public void bindImage(String dp, ArrayList<String> images) {
            Glide.with(itemView.getContext())
                    .load(dp)
                    .into(image);

            delete.setOnClickListener(view -> {
                images.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                notifyItemRangeChanged(getAdapterPosition(), images.size());
            });
        }
    }
}