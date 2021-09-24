package com.cerpenkimia.koloid.rangkuman;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cerpenkimia.koloid.R;
import com.cerpenkimia.koloid.databinding.ActivitySummaryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SummaryActivity extends AppCompatActivity {


    private ActivitySummaryBinding binding;
    private String summary = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySummaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ambil rangkuman dari database
        populateSummary();

        // cek apalah admin atau bukan
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            binding.editBtn.setVisibility(View.VISIBLE);
        }

        // kembali ke halaman pendahuluan
        binding.imageButton.setOnClickListener(view -> onBackPressed());

        // klik edit
        binding.editBtn.setOnClickListener(view -> {
            binding.summaryTv.setVisibility(View.GONE);
            binding.textInputLayout2.setVisibility(View.VISIBLE);
            binding.saveBtn.setVisibility(View.VISIBLE);

            binding.summaryEt.setText(summary);

        });

        // simpan indikator
        binding.saveBtn.setOnClickListener(view -> saveSummaryToDatabase());


    }


    private void saveSummaryToDatabase() {
        String summaryText = binding.summaryEt.getText().toString();

        if(summaryText.isEmpty()) {
            Toast.makeText(SummaryActivity.this, "Rangkuman Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        // simpan rangkuman ke database
        Map<String, Object> summaryMap = new HashMap<>();
        summaryMap.put("summary", summaryText);

        FirebaseFirestore
                .getInstance()
                .collection("summary")
                .document("summary")
                .set(summaryMap)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        summary = summaryText;
                        binding.textInputLayout2.setVisibility(View.GONE);
                        binding.saveBtn.setVisibility(View.GONE);
                        binding.summaryTv.setVisibility(View.VISIBLE);
                        binding.summaryTv.setText(summary);
                        mProgressDialog.dismiss();
                        showSuccessDialog();
                    } else {
                        mProgressDialog.dismiss();
                        showFailureDialog();
                    }
                });

    }

    @SuppressLint("SetTextI18n")
    private void populateSummary() {
        FirebaseFirestore
                .getInstance()
                .collection("summary")
                .document("summary")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()) {
                        summary = "" + documentSnapshot.get("summary");
                        binding.summaryTv.setText(summary);
                    }
                });
    }

    private void showFailureDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Gagal Memperbarui Rangkuman")
                .setMessage("Terdapat kesalahan ketika memperbarui rangkuman, silahkan periksa koneksi internet anda, dan coba lagi nanti")
                .setIcon(R.drawable.ic_baseline_clear_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasil Memperbarui Rangkuman")
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