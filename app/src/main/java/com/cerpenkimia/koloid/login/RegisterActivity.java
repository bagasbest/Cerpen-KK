package com.cerpenkimia.koloid.login;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;
import com.cerpenkimia.koloid.databinding.ActivityRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // kembali ke halaman login
        binding.backButton.setOnClickListener(view -> onBackPressed());

        // registrasi admin
        binding.registerBtn.setOnClickListener(view -> {
            // verifikasi form registrasi
            formVerification();
        });
    }

    private void formVerification() {
        String name = binding.nameEt.getText().toString().trim();
        String email = binding.emailEt.getText().toString().trim();
        String password = binding.passwordEt.getText().toString().trim();

        if(name.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Nama Lengkap tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        } else if (email.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Email tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Kata Sandi tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }


        // registrasikan admin
        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        FirebaseAuth
                .getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        saveDataToFirebase(email, name, mProgressDialog);
                    } else {
                        mProgressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Gagal registrasi admin!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveDataToFirebase(String email, String name, ProgressDialog mProgressDialog) {

        String uid = FirebaseAuth
                .getInstance()
                .getCurrentUser()
                .getUid();

        Map<String, Object> admin = new HashMap<>();
        admin.put("uid", uid);
        admin.put("name", name);
        admin.put("email", email);

        FirebaseFirestore
                .getInstance()
                .collection("admin")
                .document(uid)
                .set(admin)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        mProgressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Registrasi Berhasil", Toast.LENGTH_SHORT).show();
                    } else {
                        mProgressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Gagal registrasi admin!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}