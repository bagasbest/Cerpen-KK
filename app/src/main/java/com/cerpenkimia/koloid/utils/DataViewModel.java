package com.cerpenkimia.koloid.utils;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DataViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<DataModel>> listData = new MutableLiveData<>();
    final ArrayList<DataModel> listItem = new ArrayList<>();

    private static final String TAG = DataViewModel.class.getSimpleName();

    public void setListData(String option) {
        listItem.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection(option)
                    .document("data")
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if(documentSnapshot.exists()) {
                            DataModel model = new DataModel();

                            model.setData((ArrayList<String>)  documentSnapshot.get("data"));

                            listItem.add(model);
                            listData.postValue(listItem);
                        } else {
                            Log.e(TAG, "Empty Data");
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }


    public LiveData<ArrayList<DataModel>> getListData() {
        return listData;
    }

}
