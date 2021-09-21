package com.cerpenkimia.koloid.cerpen;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

public class CerpenViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<CerpenModel>> listCerpen = new MutableLiveData<>();
    final ArrayList<CerpenModel> cerpenModelArrayList = new ArrayList<>();

    private static final String TAG = CerpenViewModel.class.getSimpleName();

    public void setListCerpen() {
        cerpenModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("cerpen")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CerpenModel model = new CerpenModel();

                                model.setCerpenId("" + document.get("cerpenId"));
                                model.setDescription("" + document.get("description"));
                                model.setDp("" + document.get("dp"));
                                model.setTitle("" + document.get("title"));

                                cerpenModelArrayList.add(model);
                            }
                            listCerpen.postValue(cerpenModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }


    public LiveData<ArrayList<CerpenModel>> getListCerpen() {
        return listCerpen;
    }


}
