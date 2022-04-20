package com.cerpenkimia.koloid.cerpen;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cerpenkimia.koloid.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CerpenAdapter extends RecyclerView.Adapter<CerpenAdapter.ViewHolder> {

    private final ArrayList<CerpenModel> listCerpen = new ArrayList<>();
    public void setData(ArrayList<CerpenModel> items) {
        listCerpen.clear();
        listCerpen.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cerpen, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.bind(listCerpen.get(position));
    }

    @Override
    public int getItemCount() {
        return listCerpen.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView dp;
        TextView title, description;
        CardView cv;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            dp = itemView.findViewById(R.id.dp);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            cv = itemView.findViewById(R.id.cv);
        }

        public void bind(CerpenModel model) {
            Glide.with(itemView.getContext())
                    .load(model.getDp().get(0))
                    .into(dp);

            title.setText(model.getTitle());
            description.setText(model.getDescription());

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), CerpenDetailActivity.class);
                    intent.putExtra(CerpenDetailActivity.EXTRA_CERPEN, model);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
