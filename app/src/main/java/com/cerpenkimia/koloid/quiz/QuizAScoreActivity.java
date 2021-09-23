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
import java.util.HashMap;
import java.util.Map;

public class QuizAScoreActivity extends AppCompatActivity {

    public static final String CORRECT = "correct" ;
    public static final String FAILURE = "failure";
    public static final String TOTAL = "size";
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
        binding.score.setText(String.valueOf(questionTotal));

        // ke halaman solusi Quiz A
        binding.button.setOnClickListener(view -> startActivity(new Intent(QuizAScoreActivity.this, QuizASolutionActivity.class)));

        // kembali ke homepage
        binding.finishBtn.setOnClickListener(view -> {
            saveScore(questionTotal);
        });
    }

    private void saveScore(double scores) {


        // simpan skor ke firebase
        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        String scoreId = String.valueOf(System.currentTimeMillis());

        Map<String, Object> score = new HashMap<>();
        score.put("scoreId", scoreId);
        score.put("score", scores);

        FirebaseFirestore
                .getInstance()
                .collection("score_quiz_a")
                .document(scoreId)
                .set(score)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            mProgressDialog.dismiss();
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