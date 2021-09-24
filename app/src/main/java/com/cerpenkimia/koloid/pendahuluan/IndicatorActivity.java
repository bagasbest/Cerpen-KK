package com.cerpenkimia.koloid.pendahuluan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.cerpenkimia.koloid.R;
import com.cerpenkimia.koloid.databinding.ActivityIndicatorBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class IndicatorActivity extends AppCompatActivity {

    private ActivityIndicatorBinding binding;
    private String indicator = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIndicatorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ambil indikator dari database
        populateIndicator();

        // cek apalah admin atau bukan
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            binding.editBtn.setVisibility(View.VISIBLE);
        }

        // kembali ke halaman pendahuluan
        binding.imageButton.setOnClickListener(view -> onBackPressed());

        // klik edit
        binding.editBtn.setOnClickListener(view -> {
            binding.indicatorTv.setVisibility(View.GONE);
            binding.textInputLayout2.setVisibility(View.VISIBLE);
            binding.saveBtn.setVisibility(View.VISIBLE);

            binding.indicatorEt.setText(indicator);

        });

        // simpan indikator
        binding.saveBtn.setOnClickListener(view -> saveIndicatorToDatabase());

    }

    private void saveIndicatorToDatabase() {
        String indicatorText = binding.indicatorEt.getText().toString();

        if(indicatorText.isEmpty()) {
            Toast.makeText(IndicatorActivity.this, "Indikator Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        // simpan indikator ke database
        Map<String, Object> indicatorMap = new HashMap<>();
        indicatorMap.put("indicator", indicatorText);

        FirebaseFirestore
                .getInstance()
                .collection("indicator")
                .document("indicator")
                .set(indicatorMap)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        indicator = indicatorText;
                        binding.textInputLayout2.setVisibility(View.GONE);
                        binding.saveBtn.setVisibility(View.GONE);
                        binding.indicatorTv.setVisibility(View.VISIBLE);
                        binding.indicatorTv.setText(indicator);
                        mProgressDialog.dismiss();
                        showSuccessDialog();
                    } else {
                        mProgressDialog.dismiss();
                        showFailureDialog();
                    }
                });

    }

    @SuppressLint("SetTextI18n")
    private void populateIndicator() {
        FirebaseFirestore
                .getInstance()
                .collection("indicator")
                .document("indicator")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()) {
                        indicator = "" + documentSnapshot.get("indicator");
                        binding.indicatorTv.setText(indicator);
                    }
                });
    }

    private void showFailureDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Gagal Memperbarui Indikator")
                .setMessage("Terdapat kesalahan ketika memperbarui indikator, silahkan periksa koneksi internet anda, dan coba lagi nanti")
                .setIcon(R.drawable.ic_baseline_clear_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasil Memperbarui Indikator")
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