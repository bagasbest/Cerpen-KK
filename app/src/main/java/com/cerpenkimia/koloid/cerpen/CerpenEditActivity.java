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
import com.cerpenkimia.koloid.databinding.ActivityCerpenEditBinding;
import com.cerpenkimia.koloid.utils.EditDataActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CerpenEditActivity extends AppCompatActivity {

    public static final String EXTRA_CERPEN = "cerpen";
    private ActivityCerpenEditBinding binding;
    private ImageAdapter adapter;
    private CerpenModel model;
    private final ArrayList<String> image = new ArrayList<>();
    private static final int READ_PERMISSION = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCerpenEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        model = getIntent().getParcelableExtra(EXTRA_CERPEN);
        binding.titleEt.setText(model.getTitle());
        binding.descriptionEt.setText(model.getDescription());

        Log.e("tgf", String.valueOf(model.getDp().size()));

        image.addAll(model.getDp());

        checkImagePermission();
        initRecyclerView();

        // kembali ke halaman detail
        binding.imageButton.setOnClickListener(view -> onBackPressed());

        // perbarui cerpen
        binding.addCerpenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // pastikan tidak ada kolom yang kosong
                if(image.size() == 0) {
                    Toast.makeText(CerpenEditActivity.this, "Minimal 1 gambar terupdate", Toast.LENGTH_SHORT).show();
                } else {
                    formVerification();
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
        if(ContextCompat.checkSelfPermission(CerpenEditActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CerpenEditActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION);
        }
    }

    private void initRecyclerView() {
        adapter = new ImageAdapter(null, image, "edit");
        binding.rvImage.setLayoutManager(new LinearLayoutManager(this));
        binding.rvImage.setAdapter(adapter);
    }

    private void formVerification() {
        String title = binding.titleEt.getText().toString().trim();
        String description = binding.descriptionEt.getText().toString();

        if(title.isEmpty()) {
            Toast.makeText(CerpenEditActivity.this, "Judul Cerpen Kimia tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (description.isEmpty()) {
            Toast.makeText(CerpenEditActivity.this, "Isi Cerpen Kimia tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        // simpan cerpen kimia ke firebase
        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        Map<String, Object> cerpen = new HashMap<>();
        cerpen.put("title", title);
        cerpen.put("description", description);
        cerpen.put("dp", image);


        FirebaseFirestore
                .getInstance()
                .collection("cerpen")
                .document(model.getCerpenId())
                .update(cerpen)
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
                .setTitle("Gagal Memperbarui Cerpen Kimia")
                .setMessage("Terdapat kesalahan ketika memperbarui Cerpen Kimia, silahkan periksa koneksi internet anda, dan coba lagi nanti")
                .setIcon(R.drawable.ic_baseline_clear_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasil Memperbarui Cerpen Kimia")
                .setMessage("Cerpen Kimia sudah diperbarui, anda dapat mengedit atau menghapus kamera jika terdapat kesalahan")
                .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    Intent gotoScreenVar = new Intent(CerpenEditActivity.this, CerpenActivity.class);
                    gotoScreenVar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(gotoScreenVar);
                })
                .show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if(data.getClipData() != null) {
                int x = data.getClipData().getItemCount();

                for(int i=0; i<x; i++) {
                    uploadCerpenImage(data.getClipData().getItemAt(i).getUri());
                }
            } else {
                Uri imageUri = data.getData();
                uploadCerpenImage(imageUri);
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void uploadCerpenImage(Uri data) {
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
                                    image.add(uri.toString());
                                    adapter.notifyDataSetChanged();
                                })
                                .addOnFailureListener(e -> {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(CerpenEditActivity.this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show();
                                    Log.d("imageDp: ", e.toString());
                                }))
                .addOnFailureListener(e -> {
                    mProgressDialog.dismiss();
                    Toast.makeText(CerpenEditActivity.this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show();
                    Log.d("imageDp: ", e.toString());
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}