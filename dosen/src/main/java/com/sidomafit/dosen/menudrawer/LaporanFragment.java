package com.sidomafit.dosen.menudrawer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sidomafit.dosen.activity.DetailKesalahanActivity;
import com.sidomafit.dosen.adapter.LaporanAdapter;
import com.sidomafit.dosen.MainActivity;
import com.sidomafit.dosen.model.viewmodel.ViewModelKesalahan;
import com.sidomafit.dosen.R;

import java.util.Collections;

import static com.sidomafit.dosen.MainActivity.USER_ID;

public class LaporanFragment extends Fragment {
    private LaporanAdapter adapter;
    private RecyclerView recyclerView;
    private TextView tvStatus;

    public LaporanFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_laporan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.nav_laporan);

        tvStatus = view.findViewById(R.id.tv_status_laporan);
        recyclerView = view.findViewById(R.id.rv_laporan);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new LaporanAdapter(getContext());
        adapter.notifyDataSetChanged();


        tampilLaporan();
    }

    private void tampilLaporan() {

        ViewModelKesalahan viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ViewModelKesalahan.class);
        viewModel.getAllLaporan(USER_ID).observe(this, kesalahans -> {
            Log.d("CEKDATA", kesalahans.toString());
            adapter = new LaporanAdapter(getContext(), kesalahans, tvStatus);
            recyclerView.setAdapter(adapter);
            adapter.setList(kesalahans);
            Collections.sort(kesalahans, (o1, o2) -> o2.getWaktu_pengiriman().compareTo(o1.getWaktu_pengiriman()));

            adapter.setOnClickLaporan(kesalahan -> {
                Intent intent = new Intent(getContext(), DetailKesalahanActivity.class);
                intent.putExtra(DetailKesalahanActivity.EXTRA_KESALAHAN, kesalahan);
                startActivity(intent);
            });
        });
    }
}
