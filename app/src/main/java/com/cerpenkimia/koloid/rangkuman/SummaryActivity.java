package com.cerpenkimia.koloid.rangkuman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cerpenkimia.koloid.databinding.ActivitySummaryBinding;
import com.cerpenkimia.koloid.utils.AddDataActivity;
import com.cerpenkimia.koloid.utils.ComponentAdapter;
import com.cerpenkimia.koloid.utils.DataViewModel;
import com.cerpenkimia.koloid.utils.EditDataActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class SummaryActivity extends AppCompatActivity {


    private ActivitySummaryBinding binding;
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
        binding = ActivitySummaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checkRole();



        binding.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SummaryActivity.this, AddDataActivity.class);
                intent.putExtra(AddDataActivity.EXTRA_ADD, "summary");
                startActivity(intent);
            }
        });

        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(data.size() == 0) {
                    Toast.makeText(SummaryActivity.this, "Maaf, tidak ada data yang dapat di edit", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(SummaryActivity.this, EditDataActivity.class);
                    intent.putExtra(EditDataActivity.EXTRA_DATA, data);
                    intent.putExtra(EditDataActivity.OPTION, "summary");
                    startActivity(intent);
                }
            }
        });
    }

    private void initRecyclerView() {
        binding.rvSummary.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ComponentAdapter();
        binding.rvSummary.setAdapter(adapter);
    }

    private void initViewModel() {
        DataViewModel viewModel = new ViewModelProvider(this).get(DataViewModel.class);

        viewModel.setListData("summary");
        viewModel.getListData().observe(this, summary -> {
            if (summary.size() > 0) {
                data.clear();
                data.addAll(summary.get(0).getData());
                adapter.setData(data);
            }
        });
    }

    private void checkRole() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            binding.add.setVisibility(View.VISIBLE);
            binding.edit.setVisibility(View.VISIBLE);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}