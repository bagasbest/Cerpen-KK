package com.cerpenkimia.koloid.cerpen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cerpenkimia.koloid.R;
import com.cerpenkimia.koloid.databinding.ActivityCerpenDetailBinding;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CerpenDetailActivity extends AppCompatActivity {

    public static final String EXTRA_CERPEN = "cerpen";
    private ActivityCerpenDetailBinding binding;
    private CerpenModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCerpenDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // cek apakah admin atau bukan
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            binding.editBtn.setVisibility(View.VISIBLE);
            binding.deleteBtn.setVisibility(View.VISIBLE);
        }

        // ambil data cerpen dan set data cerpen ke dalam view xml
        model = getIntent().getParcelableExtra(EXTRA_CERPEN);
        showOnboardingImage();

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

    private void showOnboardingImage() {
        final ArrayList<SlideModel> imageList = new ArrayList<>();// Create image list

        for (int i = 0; i < model.getDp().size(); i++) {
            imageList.add(new SlideModel(model.getDp().get(i), ScaleTypes.CENTER_CROP));
        }

        binding.dp.setImageList(imageList);
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
                    if (task.isSuccessful()) {
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