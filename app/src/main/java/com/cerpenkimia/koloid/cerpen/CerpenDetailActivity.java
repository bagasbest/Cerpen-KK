package com.cerpenkimia.koloid.cerpen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import com.bumptech.glide.Glide;
import com.cerpenkimia.koloid.R;
import com.cerpenkimia.koloid.databinding.ActivityCerpenDetailBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class CerpenDetailActivity extends AppCompatActivity {

    public static final String EXTRA_CERPEN = "cerpen";
    private ActivityCerpenDetailBinding binding;
    private CerpenModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCerpenDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ambil data cerpen dan set data cerpen ke dalam view xml
        model = getIntent().getParcelableExtra(EXTRA_CERPEN);
        Glide.with(this)
                .load(model.getDp())
                .into(binding.dp);

        binding.title.setText(model.getTitle());
        binding.description.setText(model.getDescription());


        // kembali ke halaman sebelumnya
        binding.imageButton.setOnClickListener(view -> onBackPressed());


        // edit cerpen
        binding.editBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, CerpenEditActivity.class);
            intent.putExtra(CerpenEditActivity.EXTRA_CERPEN, model);
            startActivity(intent);
        });

        // delete cerpen
        binding.deleteBtn.setOnClickListener(view -> new AlertDialog.Builder(CerpenDetailActivity.this)
                .setTitle("Konfirmasi Hapus Cerpen Kimia")
                .setMessage("Apakah anda yakin ingin menghapus cerpen kimia ini ?")
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setPositiveButton("YA", (dialogInterface, i) -> {
                    // hapus cerpen kimia
                    deleteCerpen();
                })
                .setNegativeButton("TIDAK", null)
                .show());

    }

    private void deleteCerpen() {

        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        FirebaseFirestore
                .getInstance()
                .collection("cerpen")
                .document(model.getCerpenId())
                .delete()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        mProgressDialog.dismiss();
                        showSuccessDeleteCerpenDialog();
                    } else {
                        mProgressDialog.dismiss();
                        showFailureDeleteCerpenDialog();
                    }
                });
    }

    private void showFailureDeleteCerpenDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Gagal Menghapus Cerpen Kimia")
                .setMessage("Terdapat kesalahan ketika menghapus Cerpen Kimia, silahkan periksa koneksi internet anda, dan coba lagi nanti")
                .setIcon(R.drawable.ic_baseline_clear_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    private void showSuccessDeleteCerpenDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasil Menghapus Cerpen Kimia")
                .setMessage("Cerpen Kimia ini berhasil di hapus!")
                .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    onBackPressed();
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}