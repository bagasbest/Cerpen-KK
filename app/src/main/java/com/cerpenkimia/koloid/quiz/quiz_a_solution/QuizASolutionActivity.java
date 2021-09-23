package com.cerpenkimia.koloid.quiz.quiz_a_solution;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.os.Bundle;
import android.view.View;
import com.cerpenkimia.koloid.databinding.ActivityQuizAsolutionBinding;

public class QuizASolutionActivity extends AppCompatActivity {


    private ActivityQuizAsolutionBinding binding;
    private QuizAAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizAsolutionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // kembali ke halaman skor
        binding.imageButton.setOnClickListener(view -> onBackPressed());

        // inisiasi solusi - solusi
        initRecyclerView();

        // ambil data solusi dari database
        initViewModel();

    }

    private void initRecyclerView() {
        binding.rvSolution.setLayoutManager(new LinearLayoutManager(this));
        adapter = new QuizAAdapter();
        binding.rvSolution.setAdapter(adapter);
    }

    private void initViewModel() {
        QuizAViewModel viewModel = new ViewModelProvider(this).get(QuizAViewModel.class);

        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.setListQuiz("quiz_a");
        viewModel.getQuiz().observe(this, quiz -> {
            if (quiz.size() > 0) {
                adapter.setData(quiz);
            }
            binding.progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}