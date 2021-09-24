package com.cerpenkimia.koloid.quiz.quiz_a_solution;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cerpenkimia.koloid.R;
import com.cerpenkimia.koloid.quiz.QuizAModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class QuizAAdapter extends RecyclerView.Adapter<QuizAAdapter.ViewHolder> {

    private final ArrayList<QuizAModel> listQuiz = new ArrayList<>();

    private final String sol;
    public QuizAAdapter(String sol) {
        this.sol = sol;
    }

    public void setData(ArrayList<QuizAModel> items) {
        listQuiz.clear();
        listQuiz.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public QuizAAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_solution, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull QuizAAdapter.ViewHolder holder, int position) {
        holder.bind(listQuiz.get(position), sol);
    }

    @Override
    public int getItemCount() {
        return listQuiz.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView question, a, b, c, d, e, answer, solution;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            a = itemView.findViewById(R.id.a);
            b = itemView.findViewById(R.id.b);
            c = itemView.findViewById(R.id.c);
            d = itemView.findViewById(R.id.d);
            e = itemView.findViewById(R.id.e);
            answer = itemView.findViewById(R.id.answer);
            solution = itemView.findViewById(R.id.solution);
        }

        @SuppressLint("SetTextI18n")
        public void bind(QuizAModel model, String sol) {
            question.setText(model.getQuestion());

            if(sol.equals("A")) {
                a.setText("A. " + model.getA());
                b.setText("B. " + model.getA());
                c.setText("C. " + model.getA());
                d.setText("D. " + model.getA());
                e.setText("E. " + model.getA());
            } else {
                a.setVisibility(View.GONE);
                b.setVisibility(View.GONE);
                c.setVisibility(View.GONE);
                d.setVisibility(View.GONE);
                e.setVisibility(View.GONE);
            }

            answer.setText("Jawaban: " + model.getAnswer());
            solution.setText(model.getSolution());
        }
    }
}
