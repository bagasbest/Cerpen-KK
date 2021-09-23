package com.cerpenkimia.koloid.quiz.quiz_a_solution;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cerpenkimia.koloid.quiz.QuizAModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class QuizAViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<QuizAModel>> listQuiz = new MutableLiveData<>();
    final ArrayList<QuizAModel> quizAModelArrayList = new ArrayList<>();

    private static final String TAG = QuizAViewModel.class.getSimpleName();

    public void setListQuiz(String quiz) {
        quizAModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection(quiz)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
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

                                quizAModelArrayList.add(model);
                            }
                            listQuiz.postValue(quizAModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }


    public LiveData<ArrayList<QuizAModel>> getQuiz() {
        return listQuiz;
    }


}
