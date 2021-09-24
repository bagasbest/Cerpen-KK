package com.cerpenkimia.koloid.quiz.score;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ScoreViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<ScoreModel>> listScore = new MutableLiveData<>();
    final ArrayList<ScoreModel> scoreModelArrayList = new ArrayList<>();

    private static final String TAG = ScoreViewModel.class.getSimpleName();

    public void setListScore(String score) {
        scoreModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection(score)
                    .orderBy("score", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ScoreModel model = new ScoreModel();

                                model.setName("" + document.get("name"));
                                model.setScore("" + document.get("score"));

                                scoreModelArrayList.add(model);
                            }
                            listScore.postValue(scoreModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void setListScoreByName(String score, String name) {
        scoreModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection(score)
                    .whereGreaterThanOrEqualTo("nameTemp", name)
                    .whereLessThanOrEqualTo("nameTemp", name + '\uf8ff')
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ScoreModel model = new ScoreModel();

                                model.setName("" + document.get("name"));
                                model.setScore("" + document.get("score"));

                                scoreModelArrayList.add(model);
                            }
                            listScore.postValue(scoreModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }


    public LiveData<ArrayList<ScoreModel>> getQuiz() {
        return listScore;
    }

}
