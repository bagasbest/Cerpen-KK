package com.cerpenkimia.koloid.referensi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cerpenkimia.koloid.R;
import com.cerpenkimia.koloid.databinding.ActivityReferencesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ReferencesActivity extends AppCompatActivity {

    private ActivityReferencesBinding binding;
    private String reference = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReferencesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ambil referensi dari database
        populateReference();

        // cek apakah admin atau bukan
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            binding.editBtn.setVisibility(View.VISIBLE);
        }

        // kembali ke halaman pendahuluan
        binding.imageButton.setOnClickListener(view -> onBackPressed());

        // klik edit
        binding.editBtn.setOnClickListener(view -> {
            binding.referenceTv.setVisibility(View.GONE);
            binding.textInputLayout2.setVisibility(View.VISIBLE);
            binding.saveBtn.setVisibility(View.VISIBLE);

            binding.referenceEt.setText(reference);

        });

        // simpan referensi
        binding.saveBtn.setOnClickListener(view -> saveReferenceToDatabase());
    }

    private void saveReferenceToDatabase() {
        String referenceText = binding.referenceEt.getText().toString();

        if(referenceText.isEmpty()) {
            Toast.makeText(ReferencesActivity.this, "Referensi Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        // simpan indikator ke database
        Map<String, Object> referenceMap = new HashMap<>();
        referenceMap.put("reference", referenceText);

        FirebaseFirestore
                .getInstance()
                .collection("reference")
                .document("reference")
                .set(referenceMap)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        reference = referenceText;
                        binding.textInputLayout2.setVisibility(View.GONE);
                        binding.saveBtn.setVisibility(View.GONE);
                        binding.referenceTv.setVisibility(View.VISIBLE);
                        binding.referenceTv.setText(reference);
                        mProgressDialog.dismiss();
                        showSuccessDialog();
                    } else {
                        mProgressDialog.dismiss();
                        showFailureDialog();
                    }
                });

    }

    @SuppressLint("SetTextI18n")
    private void populateReference() {
        FirebaseFirestore
                .getInstance()
                .collection("reference")
                .document("reference")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()) {
                        reference = "" + documentSnapshot.get("reference");
                        binding.referenceTv.setText(reference);
                    }
                });
    }

    private void showFailureDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Gagal Memperbarui Referensi")
                .setMessage("Terdapat kesalahan ketika memperbarui referensi, silahkan periksa koneksi internet anda, dan coba lagi nanti")
                .setIcon(R.drawable.ic_baseline_clear_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasil Memperbarui Referensi")
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