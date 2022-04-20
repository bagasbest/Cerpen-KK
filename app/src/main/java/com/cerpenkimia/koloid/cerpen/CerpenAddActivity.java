package com.cerpenkimia.koloid.cerpen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cerpenkimia.koloid.R;
import com.cerpenkimia.koloid.databinding.ActivityCerpenAddBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CerpenAddActivity extends AppCompatActivity {

    private ActivityCerpenAddBinding binding;
    private ArrayList<Uri> uri = new ArrayList<>();
    private ArrayList<String> dp = new ArrayList<>();
    private static final int READ_PERMISSION = 101;
    private ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCerpenAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initRecyclerView();

        checkImagePermission();

        // kembali ke halaman sebelumnya
        binding.imageButton.setOnClickListener(view -> onBackPressed());

        // klik tombol tambahkan cerpen baru
        binding.addCerpenBtn.setOnClickListener(view -> {
            // verifikasi form tambahkan cerpen baru

            if(uri.size() == 0) {
                Toast.makeText(this, "Minimal harus ada 1 gambar", Toast.LENGTH_SHORT).show();
            } else {
                for(int i=0; i<uri.size(); i++) {
                    if(i+1 == uri.size()) {
                        uploadCerpenImage(uri.get(i), true);
                    } else {
                        uploadCerpenImage(uri.get(i), false);
                    }
                }
            }
        });


      binding.add.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), 1);
          }
      });

    }


    private void checkImagePermission() {
        if(ContextCompat.checkSelfPermission(CerpenAddActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CerpenAddActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION);
        }
    }

    private void initRecyclerView() {
        adapter = new ImageAdapter(uri, null, "add");
        binding.rvImage.setLayoutManager(new LinearLayoutManager(this));
        binding.rvImage.setAdapter(adapter);
    }

    private void formVerification() {
        String title = binding.titleEt.getText().toString().trim();
        String description = binding.descriptionEt.getText().toString();

        if(title.isEmpty()) {
            Toast.makeText(CerpenAddActivity.this, "Judul Cerpen Kimia tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (description.isEmpty()) {
            Toast.makeText(CerpenAddActivity.this, "Isi Cerpen Kimia tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (dp.size() == 0) {
            Toast.makeText(CerpenAddActivity.this, "Minimal 1 gambar ditambahkan", Toast.LENGTH_SHORT).show();
            return;
        }

        // simpan cerpen kimia ke firebase
        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        String uid = String.valueOf(System.currentTimeMillis());

        Map<String, Object> cerpen = new HashMap<>();
        cerpen.put("cerpenId", uid);
        cerpen.put("title", title);
        cerpen.put("description", description);
        cerpen.put("dp", dp);


        FirebaseFirestore
                .getInstance()
                .collection("cerpen")
                .document(uid)
                .set(cerpen)
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
                .setTitle("Gagal Mengunggah Cerpen Kimia")
                .setMessage("Terdapat kesalahan ketika mengunggah Cerpen Kimia, silahkan periksa koneksi internet anda, dan coba lagi nanti")
                .setIcon(R.drawable.ic_baseline_clear_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasil Mengunggah Cerpen Kimia")
                .setMessage("Cerpen Kimia akan segera terbit, anda dapat mengedit atau menghapus cerpen jika terdapat kesalahan")
                .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    onBackPressed();
                })
                .show();
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if(requestCode == 1 && resultCode == Activity.RESULT_OK) {
           if(data.getClipData() != null) {
               int x = data.getClipData().getItemCount();

               for(int i=0; i<x; i++) {
                   uri.add(data.getClipData().getItemAt(i).getUri());
               }
           } else {
               Uri imageUri = data.getData();
               uri.add(imageUri);
           }
           adapter.notifyDataSetChanged();
       }
    }

    private void uploadCerpenImage(Uri data, boolean result) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        String imageFileName = "cerpen/image_" + System.currentTimeMillis() + ".png";

        mStorageRef.child(imageFileName).putFile(data)
                .addOnSuccessListener(taskSnapshot ->
                        mStorageRef.child(imageFileName).getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    mProgressDialog.dismiss();
                                    dp.add(uri.toString());

                                    if(result) {
                                        formVerification();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(CerpenAddActivity.this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show();
                                    Log.d("imageDp: ", e.toString());
                                }))
                .addOnFailureListener(e -> {
                    mProgressDialog.dismiss();
                    Toast.makeText(CerpenAddActivity.this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show();
                    Log.d("imageDp: ", e.toString());
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}