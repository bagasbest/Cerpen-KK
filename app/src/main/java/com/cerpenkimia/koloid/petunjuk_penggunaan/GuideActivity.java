package com.cerpenkimia.koloid.petunjuk_penggunaan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.cerpenkimia.koloid.databinding.ActivityGuideBinding;
import com.cerpenkimia.koloid.rangkuman.SummaryActivity;
import com.cerpenkimia.koloid.utils.AddDataActivity;
import com.cerpenkimia.koloid.utils.ComponentAdapter;
import com.cerpenkimia.koloid.utils.DataViewModel;
import com.cerpenkimia.koloid.utils.EditDataActivity;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;

public class GuideActivity extends AppCompatActivity {

    private ActivityGuideBinding binding;
    private ComponentAdapter adapter;
    private final ArrayList<String> data = new ArrayList<>();
    @Override
    protected void onResume() {
        super.onResume();
        initRecyclerView();
        initViewModel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuideBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuideActivity.this, AddDataActivity.class);
                intent.putExtra(AddDataActivity.EXTRA_ADD, "guide");
                startActivity(intent);
            }
        });

        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(data.size() == 0) {
                    Toast.makeText(GuideActivity.this, "Maaf, tidak ada data yang dapat di edit", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(GuideActivity.this, EditDataActivity.class);
                    intent.putExtra(EditDataActivity.EXTRA_DATA, data);
                    intent.putExtra(EditDataActivity.OPTION, "guide");
                    startActivity(intent);
                }
            }
        });

    }

    private void initRecyclerView() {
        binding.rvGuide.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ComponentAdapter();
        binding.rvGuide.setAdapter(adapter);
    }

    private void initViewModel() {
        // tampilkan daftar cerpen kimia koloid
        DataViewModel viewModel = new ViewModelProvider(this).get(DataViewModel.class);

        viewModel.setListData("guide");
        viewModel.getListData().observe(this, guide -> {
            if (guide.size() > 0) {
                data.clear();
                data.addAll(guide.get(0).getData());
                adapter.setData(data);
            }
            checkRole();
        });
    }

    private void checkRole() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            if(data.size() == 0) {
                binding.add.setVisibility(View.VISIBLE);
            } else {
                binding.edit.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}