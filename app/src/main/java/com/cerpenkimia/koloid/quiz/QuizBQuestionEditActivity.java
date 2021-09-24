package com.cerpenkimia.koloid.quiz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.cerpenkimia.koloid.R;
import com.cerpenkimia.koloid.databinding.ActivityQuizBquestionEditBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class QuizBQuestionEditActivity extends AppCompatActivity {

    public static final String QUESTION = "question";
    public static final String SOLUTION = "sol";
    public static final String ANSWER = "ans";
    public static final String QUESTION_ID = "qid";
    private ActivityQuizBquestionEditBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBquestionEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.questionEt.setText(getIntent().getStringExtra(QUESTION));
        binding.answerEt.setText(getIntent().getStringExtra(ANSWER));
        binding.solutionEt.setText(getIntent().getStringExtra(SOLUTION));

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
            Toast.makeText(QuizBQuestionEditActivity.this, "Soal Quiz tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (answer.isEmpty()) {
            Toast.makeText(QuizBQuestionEditActivity.this, "Jawaban tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (solution.isEmpty()) {
            Toast.makeText(QuizBQuestionEditActivity.this, "Pembahasan tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        // simpan soal ke firebase
        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        Map<String, Object> questionMap = new HashMap<>();
        questionMap.put("question", question);
        questionMap.put("answer", answer);
        questionMap.put("solution", solution);


        FirebaseFirestore
                .getInstance()
                .collection("quiz_b")
                .document(getIntent().getStringExtra(QUESTION_ID))
                .update(questionMap)
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
                .setTitle("Gagal Mengedit Soal Quiz B")
                .setMessage("Terdapat kesalahan ketika mengedit soal quiz B, silahkan periksa koneksi internet anda, dan coba lagi nanti")
                .setIcon(R.drawable.ic_baseline_clear_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasil Mengedit Soal Quiz B")
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