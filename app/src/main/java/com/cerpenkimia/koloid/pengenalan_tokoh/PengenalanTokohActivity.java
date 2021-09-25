package com.cerpenkimia.koloid.pengenalan_tokoh;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cerpenkimia.koloid.R;
import com.cerpenkimia.koloid.databinding.ActivityPengenalanTokohBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PengenalanTokohActivity extends AppCompatActivity {

    private ActivityPengenalanTokohBinding binding;
    private String pengenalanTokoh = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPengenalanTokohBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ambil pengenalan tokoh dari database
        populatePengenalanTokoh();

        // cek apakah admin atau bukan
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            binding.editBtn.setVisibility(View.VISIBLE);
        }

        // kembali ke halaman utama
        binding.imageButton.setOnClickListener(view -> onBackPressed());

        // klik edit
        binding.editBtn.setOnClickListener(view -> {
            binding.pengenalanTokohTv.setVisibility(View.GONE);
            binding.textInputLayout2.setVisibility(View.VISIBLE);
            binding.saveBtn.setVisibility(View.VISIBLE);

            binding.pengenalanTokohEt.setText(pengenalanTokoh);

        });

        // simpan pengenalan tokoh
        binding.saveBtn.setOnClickListener(view -> savePengenalanTokohToDatabase());
    }

    private void savePengenalanTokohToDatabase() {
        String tokohText = binding.pengenalanTokohEt.getText().toString();

        if(tokohText.isEmpty()) {
            Toast.makeText(PengenalanTokohActivity.this, "Pengenalan Tokoh Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        // simpan pengenalan tokoh ke database
        Map<String, Object> ptMap = new HashMap<>();
        ptMap.put("tokoh", tokohText);

        FirebaseFirestore
                .getInstance()
                .collection("tokoh")
                .document("tokoh")
                .set(ptMap)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        pengenalanTokoh = tokohText;
                        binding.textInputLayout2.setVisibility(View.GONE);
                        binding.saveBtn.setVisibility(View.GONE);
                        binding.pengenalanTokohTv.setVisibility(View.VISIBLE);
                        binding.pengenalanTokohTv.setText(pengenalanTokoh);
                        mProgressDialog.dismiss();
                        showSuccessDialog();
                    } else {
                        mProgressDialog.dismiss();
                        showFailureDialog();
                    }
                });

    }

    @SuppressLint("SetTextI18n")
    private void populatePengenalanTokoh() {
        FirebaseFirestore
                .getInstance()
                .collection("tokoh")
                .document("tokoh")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()) {
                        pengenalanTokoh = "" + documentSnapshot.get("tokoh");
                        binding.pengenalanTokohTv.setText(pengenalanTokoh);
                    }
                });
    }

    private void showFailureDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Gagal Memperbarui Pengenalan Tokoh")
                .setMessage("Terdapat kesalahan ketika memperbarui pengenalan tokoh, silahkan periksa koneksi internet anda, dan coba lagi nanti")
                .setIcon(R.drawable.ic_baseline_clear_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasil Memperbarui Pengenalan Tokoh")
                .setMessage("Sukses")
                .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}