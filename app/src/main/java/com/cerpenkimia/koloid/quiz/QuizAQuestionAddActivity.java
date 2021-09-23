package com.cerpenkimia.koloid.quiz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;
import com.cerpenkimia.koloid.R;
import com.cerpenkimia.koloid.databinding.ActivityQuizAquestionAddBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class QuizAQuestionAddActivity extends AppCompatActivity {

    private ActivityQuizAquestionAddBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizAquestionAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // kembali ke halaman soal quiz A
        binding.imageButton.setOnClickListener(view -> onBackPressed());

        // simpan soal quiz A
        binding.saveBtn.setOnClickListener(view -> {
            // verifikasi kolom isian
            formVerification();
        });

    }

    private void formVerification() {
        String question = binding.questionEt.getText().toString();
        String a = binding.aEt.getText().toString().trim();
        String b = binding.bEt.getText().toString().trim();
        String c = binding.cEt.getText().toString().trim();
        String d = binding.dEt.getText().toString().trim();
        String e = binding.eEt.getText().toString().trim();
        String answer = binding.answerEt.getText().toString().trim();
        String solution = binding.solutionEt.getText().toString().trim();

        if(question.isEmpty()) {
            Toast.makeText(QuizAQuestionAddActivity.this, "Soal Quiz tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (a.isEmpty()) {
            Toast.makeText(QuizAQuestionAddActivity.this, "Pilihan A tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (b.isEmpty()) {
            Toast.makeText(QuizAQuestionAddActivity.this, "Pilihan B tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (c.isEmpty()) {
            Toast.makeText(QuizAQuestionAddActivity.this, "Pilihan C tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (d.isEmpty()) {
            Toast.makeText(QuizAQuestionAddActivity.this, "Pilihan D tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (e.isEmpty()) {
            Toast.makeText(QuizAQuestionAddActivity.this, "Pilihan E tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (answer.isEmpty()) {
            Toast.makeText(QuizAQuestionAddActivity.this, "Jawaban tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (solution.isEmpty()) {
            Toast.makeText(QuizAQuestionAddActivity.this, "Pembahasan tidak boleh kosong", Toast.LENGTH_SHORT).show();
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
        questionMap.put("a", a);
        questionMap.put("b", b);
        questionMap.put("c", c);
        questionMap.put("d", d);
        questionMap.put("e", e);
        questionMap.put("answer", answer);
        questionMap.put("solution", solution);


        FirebaseFirestore
                .getInstance()
                .collection("quiz_a")
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
                .setTitle("Gagal Menambahkan Soal Quiz A")
                .setMessage("Terdapat kesalahan ketika menambahkan soal quiz A, silahkan periksa koneksi internet anda, dan coba lagi nanti")
                .setIcon(R.drawable.ic_baseline_clear_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasil Menambahkan Soal Quiz A")
                .setMessage("soal quiz A akan segera terbit, anda dapat mengedit atau menghapus soal jika terdapat kesalahan")
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