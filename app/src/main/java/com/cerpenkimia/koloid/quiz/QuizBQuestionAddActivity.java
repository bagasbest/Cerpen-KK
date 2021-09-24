package com.cerpenkimia.koloid.quiz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.cerpenkimia.koloid.R;
import com.cerpenkimia.koloid.databinding.ActivityQuizBquestionAddBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class QuizBQuestionAddActivity extends AppCompatActivity {

    private ActivityQuizBquestionAddBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBquestionAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // kembali ke halaman soal quiz B
        binding.imageButton.setOnClickListener(view -> onBackPressed());

        // simpan soal quiz B
        binding.saveBtn.setOnClickListener(view -> {
            // verifikasi kolom isian
            formVerification();
        });

    }

    private void formVerification() {
        String question = binding.questionEt.getText().toString();
        String answer = binding.answerEt.getText().toString().trim();
        String solution = binding.solutionEt.getText().toString().trim();

        if(question.isEmpty()) {
            Toast.makeText(QuizBQuestionAddActivity.this, "Soal Quiz tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (answer.isEmpty()) {
            Toast.makeText(QuizBQuestionAddActivity.this, "Jawaban tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (solution.isEmpty()) {
            Toast.makeText(QuizBQuestionAddActivity.this, "Pembahasan tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        // simpan soal ke firebase
        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        String questionId = String.valueOf(System.currentTimeMillis());

        Map<String, Object> questionMap = new HashMap<>();
        questionMap.put("questionId", questionId);
        questionMap.put("question", question);
        questionMap.put("answer", answer);
        questionMap.put("solution", solution);


        FirebaseFirestore
                .getInstance()
                .collection("quiz_b")
                .document(questionId)
                .set(questionMap)
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
                .setTitle("Gagal Menambahkan Soal Quiz B")
                .setMessage("Terdapat kesalahan ketika menambahkan soal quiz B, silahkan periksa koneksi internet anda, dan coba lagi nanti")
                .setIcon(R.drawable.ic_baseline_clear_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasil Menambahkan Soal Quiz B")
                .setMessage("soal quiz B akan segera terbit, anda dapat mengedit atau menghapus soal jika terdapat kesalahan")
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