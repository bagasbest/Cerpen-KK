package com.cerpenkimia.koloid.profil;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.cerpenkimia.koloid.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private ProfileModel model = new ProfileModel();

    @Override
    protected void onResume() {
        super.onResume();
        // load profil
        populateProfile();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // cek apakah admin atau bukan
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            binding.editBtn.setVisibility(View.VISIBLE);
        }

        // kembali ke homepage
        binding.imageButton.setOnClickListener(view -> onBackPressed());

        // edit profil
        binding.editBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, ProfileEditActivity.class);
            intent.putExtra(ProfileEditActivity.EXTRA_PROFILE, model);
            startActivity(intent);
        });

    }

    @SuppressLint("SetTextI18n")
    private void populateProfile() {
        FirebaseFirestore
                .getInstance()
                .collection("profile")
                .document("profile")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()) {
                        model.setAvatar("" + documentSnapshot.get("avatar"));
                        model.setLecturer1("" + documentSnapshot.get("lecturer1"));
                        model.setLecturer2("" + documentSnapshot.get("lecturer2"));
                        model.setName("" + documentSnapshot.get("name"));
                        model.setNim("" + documentSnapshot.get("nim"));


                        Glide.with(ProfileActivity.this)
                                .load(model.getAvatar())
                                .into(binding.avatar);

                        binding.textView21.setText(model.getLecturer1());
                        binding.textView22.setText(model.getLecturer2());
                        binding.textView23.setText("NIM. " + model.getNim());
                        binding.textView25.setText(model.getName());

                    } else {
                        model.setAvatar("");
                        model.setLecturer1("");
                        model.setLecturer2("");
                        model.setName("");
                        model.setNim("");
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}