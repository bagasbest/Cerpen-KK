package com.cerpenkimia.koloid.pendahuluan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cerpenkimia.koloid.R;
import com.cerpenkimia.koloid.databinding.ActivityPreliminaryBinding;

public class PreliminaryActivity extends AppCompatActivity {

    private ActivityPreliminaryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreliminaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.view9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PreliminaryActivity.this, CoreCompetencyActivity.class));
            }
        });

        binding.view10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PreliminaryActivity.this, BasicCompetencyActivity.class));

            }
        });

        binding.view11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PreliminaryActivity.this, IndicatorActivity.class));
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}