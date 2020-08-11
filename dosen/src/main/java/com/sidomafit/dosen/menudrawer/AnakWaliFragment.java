package com.sidomafit.dosen.menudrawer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sidomafit.dosen.activity.DetailMahasiswaActivity;
import com.sidomafit.dosen.adapter.MahasiswaAdapter;
import com.sidomafit.dosen.MainActivity;
import com.sidomafit.dosen.model.viewmodel.ViewModelMahasiswa;
import com.sidomafit.dosen.R;

import java.util.Collections;

public class AnakWaliFragment extends Fragment {

    private MahasiswaAdapter adapter;
    private RecyclerView recyclerView;
    private TextView tvStatus;

    public AnakWaliFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_anak_wali, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.nav_anak_wali);

        recyclerView = view.findViewById(R.id.rv_anak_wali);
        tvStatus = view.findViewById(R.id.tv_status_anak_wali);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MahasiswaAdapter(getContext());
        recyclerView.setAdapter(adapter);


        ViewModelMahasiswa viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ViewModelMahasiswa.class);
        viewModel.getAllAnakWali().observe(this, list -> {
            adapter = new MahasiswaAdapter(getContext(), list, tvStatus);
            recyclerView.setAdapter(adapter);
            adapter.setList(list);

            Collections.sort(list, (o1, o2) -> o2.getTotal_poin() - o1.getTotal_poin());

            adapter.notifyDataSetChanged();

            adapter.setClickMahasiswa(mahasiswa -> {
                Intent intent = new Intent(getContext(), DetailMahasiswaActivity.class);
                intent.putExtra(DetailMahasiswaActivity.EXTRA_MAHASISWA, mahasiswa);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });
        });

    }
}
