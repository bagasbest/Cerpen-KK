package com.cerpenkimia.koloid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.bumptech.glide.Glide;
import com.cerpenkimia.koloid.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Glide.with(this)
                .load(R.drawable.splash_screen)
                .into(binding.splash);

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            // ke halaman login setelah 4 detik
            startActivity(new Intent(MainActivity.this, HomepageActivity.class));
            finish();
        }, 4000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}