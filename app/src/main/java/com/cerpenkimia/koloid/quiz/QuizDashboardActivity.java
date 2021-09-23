package com.cerpenkimia.koloid.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.cerpenkimia.koloid.R;
import com.cerpenkimia.koloid.databinding.ActivityQuizDashboardBinding;

public class QuizDashboardActivity extends AppCompatActivity {

    private ActivityQuizDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Glide.with(this)
                .load(R.drawable.a)
                .into(binding.roundedImageView5);

        Glide.with(this)
                .load(R.drawable.b)
                .into(binding.roundedImageView55);

        Glide.with(this)
                .load(R.drawable.score)
                .into(binding.roundedImageView77);

        Glide.with(this)
                .load(R.drawable.score)
                .into(binding.roundedImageView9);

        Glide.with(this)
                .load(R.drawable.a)
                .into(binding.roundedImageView6);

        Glide.with(this)
                .load(R.drawable.b)
                .into(binding.roundedImageView6);


        // kembali ke homepage
        binding.imageButton.setOnClickListener(view -> onBackPressed());

        binding.quizA.setOnClickListener(view -> startActivity(new Intent(QuizDashboardActivity.this, QuizAQuestionActivity.class)));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}