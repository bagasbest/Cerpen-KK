package com.cerpenkimia.koloid.quiz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.cerpenkimia.koloid.R;
import com.cerpenkimia.koloid.databinding.ActivityQuizAquestionEditBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class QuizAQuestionEditActivity extends AppCompatActivity {

    public static final String QUESTION = "question";
    public static final String A = "a";
    public static final String B = "b";
    public static final String C = "c";
    public static final String D = "d";
    public static final String E = "e";
    public static final String ANSWER = "ans";
    public static final String SOLUTION = "sol";
    public static final String QUESTION_ID = "qid";
    private ActivityQuizAquestionEditBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizAquestionEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.aEt.setText(getIntent().getStringExtra(A));
        binding.bEt.setText(getIntent().getStringExtra(B));
        binding.cEt.setText(getIntent().getStringExtra(C));
        binding.dEt.setText(getIntent().getStringExtra(D));
        binding.eEt.setText(getIntent().getStringExtra(E));
        binding.questionEt.setText(getIntent().getStringExtra(QUESTION));
        binding.answerEt.setText(getIntent().getStringExtra(ANSWER));
        binding.solutionEt.setText(getIntent().getStringExtra(SOLUTION));

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
            Toast.makeText(QuizAQuestionEditActivity.this, "Soal Quiz tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (a.isEmpty()) {
            Toast.makeText(QuizAQuestionEditActivity.this, "Pilihan A tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (b.isEmpty()) {
            Toast.makeText(QuizAQuestionEditActivity.this, "Pilihan B tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (c.isEmpty()) {
            Toast.makeText(QuizAQuestionEditActivity.this, "Pilihan C tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (d.isEmpty()) {
            Toast.makeText(QuizAQuestionEditActivity.this, "Pilihan D tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (e.isEmpty()) {
            Toast.makeText(QuizAQuestionEditActivity.this, "Pilihan E tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (answer.isEmpty()) {
            Toast.makeText(QuizAQuestionEditActivity.this, "Jawaban tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (solution.isEmpty()) {
            Toast.makeText(QuizAQuestionEditActivity.this, "Pembahasan tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        // simpan soal ke firebase
        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        Map<String, Object> questionMap = new HashMap<>();
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
                .setTitle("Gagal Mengedit Soal Quiz A")
                .setMessage("Terdapat kesalahan ketika mengedit soal quiz A, silahkan periksa koneksi internet anda, dan coba lagi nanti")
                .setIcon(R.drawable.ic_baseline_clear_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasil Mengedit Soal Quiz A")
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