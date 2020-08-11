package com.sidomafit.dosen.menudrawer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sidomafit.dosen.activity.DetailMahasiswaActivity;
import com.sidomafit.dosen.adapter.MahasiswaAdapter;
import com.sidomafit.dosen.MainActivity;
import com.sidomafit.dosen.model.viewmodel.ViewModelMahasiswa;
import com.sidomafit.dosen.R;

public class PencarianFragment extends Fragment {

    private MahasiswaAdapter adapter;
    private RecyclerView recyclerView;
    private ViewModelMahasiswa viewModel;
    private TextView tvStatus;
    private EditText etPencarian;

    public PencarianFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pencarian, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.nav_pencarian);

        etPencarian = view.findViewById(R.id.et_cari_pencarian);
        recyclerView = view.findViewById(R.id.rv_pencarian);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tvStatus = view.findViewById(R.id.tv_status_pencarian);
        adapter = new MahasiswaAdapter(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ViewModelMahasiswa.class);
        viewModel.getAllListMahasiswa().observe(this, list -> {
            adapter = new MahasiswaAdapter(getContext(), list, tvStatus);
            recyclerView.setAdapter(adapter);
            adapter.setList(list);

            adapter.setClickMahasiswa(mahasiswa -> {
                Intent intent = new Intent(getContext(), DetailMahasiswaActivity.class);
                intent.putExtra(DetailMahasiswaActivity.EXTRA_MAHASISWA, mahasiswa);
                startActivity(intent);
            });
        });

        setPencarian();
    }

    private void setPencarian(){
        etPencarian.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterData(s.toString());
            }
        });
    }

    private void filterData(String nama){
        if (nama.length()>0){
            viewModel.cariMahasiswa(nama);
        }else {
            viewModel.getAllListMahasiswa();
        }
    }
}
