package com.cerpenkimia.koloid.quiz.score;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cerpenkimia.koloid.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolder> {

    private ArrayList<ScoreModel> listScore = new ArrayList<>();
    public void setData(ArrayList<ScoreModel> items) {
        listScore.clear();
        listScore.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.bind(listScore.get(position));
    }

    @Override
    public int getItemCount() {
        return listScore.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, score;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            score = itemView.findViewById(R.id.score);
        }

        public void bind(ScoreModel model) {
            name.setText("Nama: " + model.getName());
            score.setText("Skor: " + model.getScore());
        }
    }
}
