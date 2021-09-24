package com.cerpenkimia.koloid.quiz.score;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import com.cerpenkimia.koloid.databinding.ActivityScoreBinding;
public class ScoreActivity extends AppCompatActivity {

    public static final String EXTRA_SCORE = "SCORE";
    private ActivityScoreBinding binding;
    private ScoreAdapter adapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(getIntent().getStringExtra(EXTRA_SCORE).equals("A")) {
            binding.title.setText("Skor Quiz A");
        } else {
            binding.title.setText("Skor Quiz B");
        }

        initRecyclerView();
        initViewModel("all");


        // cari nama pengguna
        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().isEmpty()) {
                    initRecyclerView();
                    initViewModel(editable.toString());
                } else {
                    initRecyclerView();
                    initViewModel("all");
                }
            }
        });

        binding.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void initRecyclerView() {
        binding.rvScore.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ScoreAdapter();
        binding.rvScore.setAdapter(adapter);
    }

    private void initViewModel(String query) {
        ScoreViewModel viewModel = new ViewModelProvider(this).get(ScoreViewModel.class);

        binding.progressBar.setVisibility(View.VISIBLE);
        if(getIntent().getStringExtra(EXTRA_SCORE).equals("A")) {
            if(query.equals("all")){
                viewModel.setListScore("score_quiz_a");
            } else {
                viewModel.setListScoreByName("score_quiz_a", query);
            }
        } else {
            if(query.equals("all")){
                viewModel.setListScore("score_quiz_b");
            } else {
                viewModel.setListScoreByName("score_quiz_b", query);
            }
        }

        viewModel.getQuiz().observe(this, quiz -> {
            if (quiz.size() > 0) {
                binding.noData.setVisibility(View.GONE);
                adapter.setData(quiz);
            } else {
                binding.noData.setVisibility(View.VISIBLE);
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