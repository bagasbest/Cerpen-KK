package com.cerpenkimia.koloid.utils;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cerpenkimia.koloid.R;

import java.util.List;

public class AddDataAdapter extends RecyclerView.Adapter<AddDataAdapter.ViewHolder> {

    List<String> item;
    public AddDataAdapter(List<String> item) {
        this.item = item;
    }

    @NonNull
    @Override
    public AddDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddDataAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
       holder.bind(item.get(position), item);
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        EditText editText;
        ImageView delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            editText = itemView.findViewById(R.id.editText);
            delete = itemView.findViewById(R.id.deleteBtn);
        }

        public void bind(String s, List<String> item) {
            editText.setText(s);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    item.set(getAdapterPosition(), editable.toString());
                }
            });
            delete.setOnClickListener(view -> {
                item.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                notifyItemRangeChanged(getAdapterPosition(), item.size());
            });
        }
    }
}
