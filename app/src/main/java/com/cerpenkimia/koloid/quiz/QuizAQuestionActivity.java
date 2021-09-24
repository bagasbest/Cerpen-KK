package com.cerpenkimia.koloid.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.cerpenkimia.koloid.R;
import com.cerpenkimia.koloid.databinding.ActivityQuizAquestionBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class QuizAQuestionActivity extends AppCompatActivity {

    private ActivityQuizAquestionBinding binding;
    private static final String SHARED_PREFS = "sharedPrefs";
    private int questionSection = 0;
    private ArrayList<QuizAModel> questionList = new ArrayList<>();
    private String ans = "";

    @Override
    protected void onResume() {
        super.onResume();
        populateQuiz();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizAquestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // cek apakah yang login saat ini user atau admin
        checkRole();

        // pilihan jawaban pilihan ganda
        answerChoice();

        // kembali ke quiz dashboard
        binding.imageButton.setOnClickListener(view -> new AlertDialog.Builder(QuizAQuestionActivity.this)
                .setTitle("Konfirmasi Kembali")
                .setMessage("Apakah anda yakin ingin kembali ke Quiz Dashboard?\n\nJika YA, maka sistem akan mengakhiri Quiz ini!")
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setPositiveButton("YA", (dialogInterface, i) -> {

                    //hapus semua jawaban tersimpan
                    SharedPreferences delete = this.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                    delete.edit().clear().apply();

                    dialogInterface.dismiss();
                    onBackPressed();
                })
                .setNegativeButton("TIDAK", null)
                .show());

        // tambahkan quiz baru
        binding.addBtn.setOnClickListener(view -> startActivity(new Intent(QuizAQuestionActivity.this, QuizAQuestionAddActivity.class)));

        // pertanyaan sebelumnya
        binding.prevBtn.setOnClickListener(view -> {
            questionSection--;
            if(questionSection == 0) {
                if(questionList.size() == 2) {
                    binding.finishBtn.setVisibility(View.GONE);
                }
                binding.prevBtn.setVisibility(View.GONE);
                binding.textView13.setVisibility(View.GONE);
            } else if (questionSection == questionList.size() - 2) {
                binding.nextBtn.setVisibility(View.VISIBLE);
                binding.textView14.setVisibility(View.VISIBLE);
                binding.finishBtn.setVisibility(View.GONE);
            }
            SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            ans = sharedPreferences.getString(String.valueOf(questionSection), "");
            neutralizeAnswerChoiceBackgroundColor();
            setQuiz();

            // set background choice answer
            switch (ans) {
                case "A":
                    binding.a.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, android.R.color.holo_green_dark));
                    break;
                case "B":
                    binding.b.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, android.R.color.holo_green_dark));
                    break;
                case "C":
                    binding.c.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, android.R.color.holo_green_dark));
                    break;
                case "D":
                    binding.d.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, android.R.color.holo_green_dark));
                    break;
                case "E":
                    binding.e.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, android.R.color.holo_green_dark));
                    break;
            }
        });

        // pertanyaan berikutnya
        binding.nextBtn.setOnClickListener(view -> {
            // simpan jawaban ke dalam memori shared preferences
            SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(String.valueOf(questionSection), ans);
            editor.apply();

            // load pertanyaan selanjutnya
            questionSection++;
            if(questionSection == questionList.size() - 1) {
                binding.nextBtn.setVisibility(View.GONE);
                binding.textView14.setVisibility(View.GONE);
                binding.finishBtn.setVisibility(View.VISIBLE);
                binding.prevBtn.setVisibility(View.VISIBLE);
                binding.textView13.setVisibility(View.VISIBLE);
            } else if(questionSection == 1) {
                binding.prevBtn.setVisibility(View.VISIBLE);
                binding.textView13.setVisibility(View.VISIBLE);
            }

            ans = sharedPreferences.getString(String.valueOf(questionSection), "");
            neutralizeAnswerChoiceBackgroundColor();
            setQuiz();

            // set background choice answer
            switch (ans) {
                case "A":
                    binding.a.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, android.R.color.holo_green_dark));
                    break;
                case "B":
                    binding.b.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, android.R.color.holo_green_dark));
                    break;
                case "C":
                    binding.c.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, android.R.color.holo_green_dark));
                    break;
                case "D":
                    binding.d.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, android.R.color.holo_green_dark));
                    break;
                case "E":
                    binding.e.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, android.R.color.holo_green_dark));
                    break;
            }

        });

        // edit pertanyaan quiz A
        binding.editBtn.setOnClickListener(view -> {
            Intent intent = new Intent(QuizAQuestionActivity.this, QuizAQuestionEditActivity.class);
            intent.putExtra(QuizAQuestionEditActivity.QUESTION, questionList.get(questionSection).getQuestion());
            intent.putExtra(QuizAQuestionEditActivity.A, questionList.get(questionSection).getA());
            intent.putExtra(QuizAQuestionEditActivity.B, questionList.get(questionSection).getB());
            intent.putExtra(QuizAQuestionEditActivity.C, questionList.get(questionSection).getC());
            intent.putExtra(QuizAQuestionEditActivity.D, questionList.get(questionSection).getD());
            intent.putExtra(QuizAQuestionEditActivity.E, questionList.get(questionSection).getE());
            intent.putExtra(QuizAQuestionEditActivity.SOLUTION, questionList.get(questionSection).getSolution());
            intent.putExtra(QuizAQuestionEditActivity.ANSWER, questionList.get(questionSection).getAnswer());
            intent.putExtra(QuizAQuestionEditActivity.QUESTION_ID, questionList.get(questionSection).getQuestionId());
            startActivity(intent);
        });

        // delete pertanyaan quiz A
        binding.deleteBtn.setOnClickListener(view -> showConfirmDeleteQuestionDialog());


        // klik finish pada quiz A
        binding.finishBtn.setOnClickListener(view -> {

            // simpan jawaban soal terakhir
            SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(String.valueOf(questionSection), ans);
            editor.apply();

            new AlertDialog.Builder(QuizAQuestionActivity.this)
                    .setTitle("Konfirmasi Menyelesaikan Quiz A")
                    .setMessage("Apakah anda yakin ingin menyelesaikan Quiz A ?")
                    .setIcon(R.drawable.ic_baseline_warning_24)
                    .setPositiveButton("YA", (dialogInterface, ii) -> {

                        int correctAns = 0;
                        int failureAns = 0;

                        for(int i=0; i<questionList.size(); i++) {
                            if(sharedPreferences.getString(String.valueOf(i), "").equals(questionList.get(i).getAnswer())) {
                                correctAns ++;
                            } else {
                                failureAns ++;
                            }
                        }

                        //hapus semua jawaban tersimpan
                        SharedPreferences delete = this.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                        delete.edit().clear().apply();

                        // ke halaman skor
                        Intent intent = new Intent(QuizAQuestionActivity.this, QuizAScoreActivity.class);
                        intent.putExtra(QuizAScoreActivity.CORRECT, correctAns);
                        intent.putExtra(QuizAScoreActivity.FAILURE, failureAns);
                        intent.putExtra(QuizAScoreActivity.TOTAL, questionList.size());
                        intent.putExtra(QuizAScoreActivity.TYPE, "A");
                        startActivity(intent);

                    })
                    .setNegativeButton("TIDAK", null)
                    .show();
        });

    }

    private void showConfirmDeleteQuestionDialog() {
        new AlertDialog.Builder(QuizAQuestionActivity.this)
                .setTitle("Konfirmasi Menghapus Pertanyaan")
                .setMessage("Apakah anda yakin ingin menghapus pertanyaan ini ?")
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setPositiveButton("YA", (dialogInterface, i) -> {
                    FirebaseFirestore
                            .getInstance()
                            .collection("quiz_a")
                            .document(questionList.get(questionSection).getQuestionId())
                            .delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        showSuccessDialog();
                                    } else {
                                        showFailureDialog();
                                    }
                                    dialogInterface.dismiss();
                                }
                            });
                })
                .setNegativeButton("TIDAK", null)
                .show();
    }


    @SuppressLint("SetTextI18n")
    private void setQuiz() {
        if(questionList.size() > 0) {
            binding.questionSet.setVisibility(View.VISIBLE);
            binding.question.setText(questionList.get(questionSection).getQuestion());
            binding.a.setText("A. " + questionList.get(questionSection).getA());
            binding.b.setText("B. " + questionList.get(questionSection).getB());
            binding.c.setText("C. " + questionList.get(questionSection).getC());
            binding.d.setText("D. " + questionList.get(questionSection).getD());
            binding.e.setText("E. " + questionList.get(questionSection).getE());
            binding.noData.setVisibility(View.GONE);

            if(questionList.size() > 1 && questionSection < questionList.size() - 1) {
                binding.nextBtn.setVisibility(View.VISIBLE);
                binding.textView14.setVisibility(View.VISIBLE);
            } else if (questionList.size() == 1) {
                binding.nextBtn.setVisibility(View.GONE);
                binding.textView14.setVisibility(View.GONE);
                binding.finishBtn.setVisibility(View.VISIBLE);
            }

        } else {
            binding.noData.setVisibility(View.VISIBLE);
            binding.nextBtn.setVisibility(View.GONE);
            binding.textView14.setVisibility(View.GONE);
            binding.questionSet.setVisibility(View.GONE);
            binding.finishBtn.setVisibility(View.GONE);
            binding.prevBtn.setVisibility(View.GONE);
            binding.textView13.setVisibility(View.GONE);
        }
    }

    private void checkRole() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            binding.addBtn.setVisibility(View.VISIBLE);
            binding.editBtn.setVisibility(View.VISIBLE);
            binding.deleteBtn.setVisibility(View.VISIBLE);
        }
    }


    private void populateQuiz() {
        binding.progressBar.setVisibility(View.VISIBLE);
        questionList.clear();

        FirebaseFirestore
                .getInstance()
                .collection("quiz_a")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        for(QueryDocumentSnapshot document: task.getResult()) {
                            QuizAModel model = new QuizAModel();
                            model.setA("" + document.get("a"));
                            model.setAnswer("" + document.get("answer"));
                            model.setB("" + document.get("b"));
                            model.setC("" + document.get("c"));
                            model.setD("" + document.get("d"));
                            model.setE("" + document.get("e"));
                            model.setQuestion("" + document.get("question"));
                            model.setQuestionId("" + document.get("questionId"));
                            model.setSolution("" + document.get("solution"));

                            questionList.add(model);
                        }

                        // set quiz jika ada
                        setQuiz();
                        binding.progressBar.setVisibility(View.GONE);
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.noData.setVisibility(View.VISIBLE);
                    }
                });
    }


    private void answerChoice() {
        binding.a.setOnClickListener(view -> {
            ans = "A";
            binding.a.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, android.R.color.holo_green_dark));
            binding.b.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, R.color.yellow));
            binding.c.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, R.color.yellow));
            binding.d.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, R.color.yellow));
            binding.e.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, R.color.yellow));

        });

        binding.b.setOnClickListener(view -> {
            ans = "B";
            binding.a.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, R.color.yellow));
            binding.b.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, android.R.color.holo_green_dark));
            binding.c.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, R.color.yellow));
            binding.d.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, R.color.yellow));
            binding.e.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, R.color.yellow));            });

        binding.c.setOnClickListener(view -> {
            ans = "C";
            binding.a.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, R.color.yellow));
            binding.b.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, R.color.yellow));
            binding.c.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, android.R.color.holo_green_dark));
            binding.d.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, R.color.yellow));
            binding.e.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, R.color.yellow));            });

        binding.d.setOnClickListener(view -> {
            ans = "D";
            binding.a.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, R.color.yellow));
            binding.b.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, R.color.yellow));
            binding.c.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, R.color.yellow));
            binding.d.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, android.R.color.holo_green_dark));
            binding.e.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, R.color.yellow));            });

        binding.e.setOnClickListener(view -> {
            ans = "E";
            binding.a.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, R.color.yellow));
            binding.b.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, R.color.yellow));
            binding.c.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, R.color.yellow));
            binding.d.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, R.color.yellow));
            binding.e.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, android.R.color.holo_green_dark));
        });
    }

    private void neutralizeAnswerChoiceBackgroundColor() {
        binding.a.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, R.color.yellow));
        binding.b.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, R.color.yellow));
        binding.c.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, R.color.yellow));
        binding.d.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, R.color.yellow));
        binding.e.setBackgroundTintList(ContextCompat.getColorStateList(QuizAQuestionActivity.this, R.color.yellow));
    }

    private void showFailureDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Gagal Menghapus Pertanyaan")
                .setMessage("Gagal menghapus pertanyaan ini, silahkan periksa koneksi internet anda, dan coba lagi nanti")
                .setIcon(R.drawable.ic_baseline_clear_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasil Menghapus Pertanyaan")
                .setMessage("Sukses menghapus pertanyaan ini")
                .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    questionSection = 0;
                    binding.prevBtn.setVisibility(View.GONE);
                    binding.textView13.setVisibility(View.GONE);
                    populateQuiz();
                })
                .show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}