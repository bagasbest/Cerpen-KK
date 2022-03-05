package com.cerpenkimia.koloid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.cerpenkimia.koloid.cerpen.CerpenActivity;
import com.cerpenkimia.koloid.cerpen.CerpenDetailActivity;
import com.cerpenkimia.koloid.databinding.ActivityHomepageBinding;
import com.cerpenkimia.koloid.login.LoginActivity;
import com.cerpenkimia.koloid.pendahuluan.PreliminaryActivity;
import com.cerpenkimia.koloid.pengenalan_tokoh.PengenalanTokohActivity;
import com.cerpenkimia.koloid.petunjuk_penggunaan.GuideActivity;
import com.cerpenkimia.koloid.profil.ProfileActivity;
import com.cerpenkimia.koloid.quiz.QuizDashboardActivity;
import com.cerpenkimia.koloid.rangkuman.SummaryActivity;
import com.cerpenkimia.koloid.referensi.ReferencesActivity;
import com.google.firebase.auth.FirebaseAuth;

public class HomepageActivity extends AppCompatActivity {

    private ActivityHomepageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // cek apakah pengguna sudah login atau belum
        checkLoginState();

        Glide.with(this)
                .load(R.drawable.home_bg)
                .into(binding.homeBg);

        // ketika admin ingin login
        binding.login.setOnClickListener(view -> startActivity(new Intent(HomepageActivity.this, LoginActivity.class)));

        // ketika admin ingin logout
        binding.logout.setOnClickListener(view -> new AlertDialog.Builder(HomepageActivity.this)
                .setTitle("Konfirmasi Logout Admin")
                .setMessage("Apakah anda yakin ingin logout ?")
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setPositiveButton("YA", (dialogInterface, i) -> {
                    // hapus cerpen kimia
                    FirebaseAuth.getInstance().signOut();

                    binding.logout.setVisibility(View.INVISIBLE);
                    binding.login.setVisibility(View.VISIBLE);

                })
                .setNegativeButton("TIDAK", null)
                .show());

        // ke halaman Petunjuk penggunaan
        binding.view.setOnClickListener(view -> startActivity(new Intent(HomepageActivity.this, GuideActivity.class)));

        // ke halaman Pendahuluan
        binding.view2.setOnClickListener(view -> startActivity(new Intent(HomepageActivity.this, PreliminaryActivity.class)));

        // ke halaman pengenalan tokoh
        binding.view3.setOnClickListener(view -> startActivity(new Intent(HomepageActivity.this, PengenalanTokohActivity.class)));

        // ke halaman Cerpen Kimia Koloid
        binding.view4.setOnClickListener(view -> startActivity(new Intent(HomepageActivity.this, CerpenActivity.class)));

        // ke halaman quiz dahboard
        binding.view5.setOnClickListener(view -> startActivity(new Intent(HomepageActivity.this, QuizDashboardActivity.class)));

        // ke halaman rangkuman
        binding.view6.setOnClickListener(view -> startActivity(new Intent(HomepageActivity.this, SummaryActivity.class)));

        // ke halaman referensi
        binding.view7.setOnClickListener(view -> startActivity(new Intent(HomepageActivity.this, ReferencesActivity.class)));

        // ke halaman profil
        binding.view8.setOnClickListener(view -> startActivity(new Intent(HomepageActivity.this, ProfileActivity.class)));

    }

    private void checkLoginState() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
           binding.logout.setVisibility(View.VISIBLE);
        } else {
            binding.login.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}