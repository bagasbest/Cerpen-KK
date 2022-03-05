package com.cerpenkimia.koloid.utils;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;
import com.cerpenkimia.koloid.R;
import com.cerpenkimia.koloid.databinding.ActivityEditDataBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditDataActivity extends AppCompatActivity {

    public static final String EXTRA_DATA = "data";
    public static final String OPTION = "option";
    private ActivityEditDataBinding binding;
    private AddDataAdapter adapter;
    private final ArrayList<String> myList = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myList.addAll(getIntent().getStringArrayListExtra(EXTRA_DATA));
        initRecyclerView();

        binding.imageButton.setOnClickListener(view -> onBackPressed());

        binding.add.setOnClickListener(view -> {
            myList.add("");
            adapter.notifyDataSetChanged();
        });

        binding.save.setOnClickListener(view -> {

            if(myList.size() == 0) {
                Toast.makeText(EditDataActivity.this, "Maaf data tidak boleh kosong, silahkan klik tombol + untuk menambahkan data", Toast.LENGTH_SHORT).show();
                return;
            }

            for(int i=0; i<myList.size(); i++)  {
                if(myList.get(i).isEmpty()) {
                    Toast.makeText(EditDataActivity.this, "Maaf poin ke " + (i+1) + " tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            ProgressDialog mProgressDialog = new ProgressDialog(this);

            mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            String option = getIntent().getStringExtra(OPTION);

            // simpan rangkuman ke database
            Map<String, Object> summaryMap = new HashMap<>();
            summaryMap.put("data", myList);

            FirebaseFirestore
                    .getInstance()
                    .collection(option)
                    .document("data")
                    .set(summaryMap)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            mProgressDialog.dismiss();
                            showSuccessDialog();
                        } else {
                            mProgressDialog.dismiss();
                            showFailureDialog();
                        }
                    });

        });

    }

    private void initRecyclerView() {
        adapter = new AddDataAdapter(myList);
        binding.rvEdit.setLayoutManager(new LinearLayoutManager(this));
        binding.rvEdit.setAdapter(adapter);
    }

    private void showFailureDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Gagal memperbarui data")
                .setMessage("Terdapat kesalahan ketika memperbarui data, silahkan periksa koneksi internet anda, dan coba lagi nanti")
                .setIcon(R.drawable.ic_baseline_clear_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasil memperbarui data")
                .setMessage("Sukses")
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