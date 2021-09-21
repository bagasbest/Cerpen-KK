package com.cerpenkimia.koloid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cerpenkimia.koloid.cerpen.CerpenActivity;
import com.cerpenkimia.koloid.databinding.ActivityHomepageBinding;
import com.cerpenkimia.koloid.login.LoginActivity;
import com.cerpenkimia.koloid.pendahuluan.CoreCompetencyActivity;
import com.cerpenkimia.koloid.pendahuluan.PreliminaryActivity;
import com.cerpenkimia.koloid.petunjuk_penggunaan.GuideActivity;
import com.google.firebase.auth.FirebaseAuth;

public class HomepageActivity extends AppCompatActivity {

    private ActivityHomepageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // klik login, menuju ke halaman login
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // cek apakah pengguna sudah login atau belum
                checkLoginState();
            }
        });

        // ke halaman Petunjuk penggunaan
        binding.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomepageActivity.this, GuideActivity.class));
            }
        });

        // ke halaman Pendahuluan
        binding.view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomepageActivity.this, PreliminaryActivity.class));
            }
        });

        binding.view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomepageActivity.this, PreliminaryActivity.class));
            }
        });


        // ke halaman Cerpen Kimia Koloid
        binding.view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomepageActivity.this, CerpenActivity.class));
            }
        });


        binding.view5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomepageActivity.this, PreliminaryActivity.class));
            }
        });

        binding.view6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomepageActivity.this, PreliminaryActivity.class));
            }
        });

        binding.view7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomepageActivity.this, PreliminaryActivity.class));
            }
        });

        binding.view8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomepageActivity.this, PreliminaryActivity.class));
            }
        });

    }

    private void checkLoginState() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            new AlertDialog.Builder(this)
                    .setTitle("Anda Sudah Login")
                    .setMessage("Saat ini status anda adalah ADMIN")
                    .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
                    .setPositiveButton("OKE", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    })
                    .show();
        } else {
            startActivity(new Intent(HomepageActivity.this, LoginActivity.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}