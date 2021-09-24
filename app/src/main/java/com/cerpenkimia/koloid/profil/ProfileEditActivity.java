package com.cerpenkimia.koloid.profil;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cerpenkimia.koloid.R;
import com.cerpenkimia.koloid.databinding.ActivityProfileEditBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class ProfileEditActivity extends AppCompatActivity {

    public static final String EXTRA_PROFILE = "model";
    private ActivityProfileEditBinding binding;
    private String dp;
    private static final int REQUEST_FROM_GALLERY = 1001;
    private ProfileModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        model = getIntent().getParcelableExtra(EXTRA_PROFILE);

        if (!model.getAvatar().isEmpty()) {
            Glide.with(this)
                    .load(model.getAvatar())
                    .into(binding.dp);
        }

        binding.nameEt.setText(model.getName());
        binding.nimEt.setText(model.getNim());
        binding.lecturer1Et.setText(model.getLecturer1());
        binding.lecturer2Et.setText(model.getLecturer2());

        // kembali ke halaman profil
        binding.imageButton.setOnClickListener(view -> onBackPressed());

        // klik tombol simpan profil
        binding.saveBtn.setOnClickListener(view -> {
            // verifikasi form edit profil
            formVerification();
        });


        // KLIK TAMBAH GAMBAR
        binding.imageHint.setOnClickListener(view -> ImagePicker.with(ProfileEditActivity.this)
                .galleryOnly()
                .compress(1024)
                .start(REQUEST_FROM_GALLERY));
    }

    private void formVerification() {
        String name = binding.nameEt.getText().toString().trim();
        String nim = binding.nimEt.getText().toString();
        String lecturer1 = binding.lecturer1Et.getText().toString().trim();
        String lecturer2 = binding.lecturer2Et.getText().toString().trim();


        if(name.isEmpty()) {
            Toast.makeText(ProfileEditActivity.this, "Nama Lengkap tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (nim.isEmpty()) {
            Toast.makeText(ProfileEditActivity.this, "NIM tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (lecturer1.isEmpty()) {
            Toast.makeText(ProfileEditActivity.this, "Dosen 1 tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (lecturer2.isEmpty()) {
            Toast.makeText(ProfileEditActivity.this, "Dosen 2 tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        // simpan profil ke firebase
        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        Map<String, Object> profile = new HashMap<>();
        profile.put("name", name);
        profile.put("nim", nim);
        profile.put("lecturer1", lecturer1);
        profile.put("lecturer2", lecturer2);
        if(dp != null) {
            profile.put("avatar", dp);
        }


        FirebaseFirestore
                .getInstance()
                .collection("profile")
                .document("profile")
                .update(profile)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        mProgressDialog.dismiss();
                        showSuccessDialog();
                    } else {
                        mProgressDialog.dismiss();
                        showFailureDialog();
                    }
                });
    }

    private void showFailureDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Gagal Memperbarui Profil")
                .setMessage("Terdapat kesalahan ketika memperbarui mrofil, silahkan periksa koneksi internet anda, dan coba lagi nanti")
                .setIcon(R.drawable.ic_baseline_clear_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasil Memperbarui Profil")
                .setMessage("Sukses")
                .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    onBackPressed();
                })
                .show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_FROM_GALLERY) {
                uploadCerpenImage(data.getData());
            }
        }
    }

    private void uploadCerpenImage(Uri data) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        String imageFileName = "profile/image_" + System.currentTimeMillis() + ".png";

        mStorageRef.child(imageFileName).putFile(data)
                .addOnSuccessListener(taskSnapshot ->
                        mStorageRef.child(imageFileName).getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    mProgressDialog.dismiss();
                                    dp = uri.toString();
                                    binding.imageHint.setVisibility(View.GONE);
                                    Glide
                                            .with(this)
                                            .load(dp)
                                            .into(binding.dp);
                                })
                                .addOnFailureListener(e -> {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(ProfileEditActivity.this, "Gagal mengunggah foto profil", Toast.LENGTH_SHORT).show();
                                    Log.d("imageDp: ", e.toString());
                                }))
                .addOnFailureListener(e -> {
                    mProgressDialog.dismiss();
                    Toast.makeText(ProfileEditActivity.this, "Gagal mengunggah foto profil", Toast.LENGTH_SHORT).show();
                    Log.d("imageDp: ", e.toString());
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}