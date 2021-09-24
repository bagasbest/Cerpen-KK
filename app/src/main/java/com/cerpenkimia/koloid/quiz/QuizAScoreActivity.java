package com.cerpenkimia.koloid.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.cerpenkimia.koloid.HomepageActivity;
import com.cerpenkimia.koloid.databinding.ActivityQuizAscoreBinding;
import com.cerpenkimia.koloid.quiz.quiz_a_solution.QuizASolutionActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class QuizAScoreActivity extends AppCompatActivity {

    public static final String CORRECT = "correct" ;
    public static final String FAILURE = "failure";
    public static final String TOTAL = "size";
    public static final String TYPE = "type";
    private ActivityQuizAscoreBinding binding;

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizAscoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.correctAns.setText("Jawaban Benar: " + getIntent().getIntExtra(CORRECT, 0));
        binding.failAns.setText("Jawaban Salah: " + getIntent().getIntExtra(FAILURE, 0));

        // hitung total skor 1 - 100
        double questionTotal = (Double.parseDouble(String.valueOf(getIntent().getIntExtra(CORRECT, 0))) / Double.parseDouble(String.valueOf(getIntent().getIntExtra(TOTAL, 0)))) * 100;
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        binding.score.setText(decimalFormat.format(questionTotal));

        // ke halaman solusi Quiz A / B
        binding.button.setOnClickListener(view -> {
            Intent intent = new Intent(QuizAScoreActivity.this, QuizASolutionActivity.class);
            intent.putExtra(QuizASolutionActivity.EXTRA_SOL, getIntent().getStringExtra(TYPE));
            startActivity(intent);
        });

        // kembali ke homepage
        binding.finishBtn.setOnClickListener(view -> {
            String name = binding.nameEt.getText().toString().trim();
            if(name.isEmpty()) {
                Toast.makeText(QuizAScoreActivity.this, "Nama Lengkap anda tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }


            if(getIntent().getStringExtra(TYPE).equals("A")) {
                saveScore(questionTotal, name, "score_quiz_a");
            } else {
                saveScore(questionTotal, name, "score_quiz_b");
            }
        });
    }

    private void saveScore(double scores, String name, String collection) {


        // simpan skor ke firebase
        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        String scoreId = String.valueOf(System.currentTimeMillis());

        Map<String, Object> score = new HashMap<>();
        score.put("scoreId", scoreId);
        score.put("score", scores);
        score.put("name", name);
        score.put("nameTemp", name.toLowerCase());

        FirebaseFirestore
                .getInstance()
                .collection(collection)
                .document(scoreId)
                .set(score)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            mProgressDialog.dismiss();
                            Toast.makeText(QuizAScoreActivity.this, "Berhasil menyimpan skor", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(QuizAScoreActivity.this, HomepageActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            mProgressDialog.dismiss();
                            Toast.makeText(QuizAScoreActivity.this, "Gagal menyimpan skor", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}