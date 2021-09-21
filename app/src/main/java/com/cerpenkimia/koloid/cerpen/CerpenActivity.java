package com.cerpenkimia.koloid.cerpen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.cerpenkimia.koloid.databinding.ActivityCerpenBinding;
import com.google.firebase.auth.FirebaseAuth;

public class CerpenActivity extends AppCompatActivity {

    private ActivityCerpenBinding binding;
    private CerpenAdapter adapter;


    @Override
    protected void onResume() {
        super.onResume();
        initRecylerView();
        initViewModel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCerpenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // cek apakah admin atau bukan
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            binding.cerpenAddBtn.setVisibility(View.VISIBLE);
        }

        // kembali ke halaman sebelumnya
        binding.imageButton.setOnClickListener(view -> onBackPressed());


        // klik ikon + untuk menambahkan cerpen baru
        binding.cerpenAddBtn.setOnClickListener(view -> startActivity(new Intent(CerpenActivity.this, CerpenAddActivity.class)));

    }

    private void initRecylerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CerpenAdapter();
        binding.recyclerView.setAdapter(adapter);
    }

    private void initViewModel() {
        // tampilkan daftar cerpen kimia koloid
        CerpenViewModel viewModel = new ViewModelProvider(this).get(CerpenViewModel.class);

        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.setListCerpen();
        viewModel.getListCerpen().observe(this, cerpen -> {
            if (cerpen.size() > 0) {
                binding.noData.setVisibility(View.GONE);
                adapter.setData(cerpen);
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