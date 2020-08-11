package com.sidomafit.dosen.menudrawer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sidomafit.dosen.activity.KelasActivity;
import com.sidomafit.dosen.adapter.KelasAdapter;
import com.sidomafit.dosen.MainActivity;
import com.sidomafit.dosen.model.Kelas;
import com.sidomafit.dosen.R;

import java.util.ArrayList;

public class KelasFragment extends Fragment {

    private KelasAdapter adapter;
    private RecyclerView recyclerView;

    public KelasFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kelas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.nav_kelas);


        recyclerView = view.findViewById(R.id.rv_kelas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new KelasAdapter(getContext());

        tampilKelas();
    }

    private void tampilKelas() {
        DatabaseReference kelasRef = FirebaseDatabase.getInstance().getReference();
        kelasRef.child("DaftarKelas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Kelas> mList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Kelas kelas = snapshot.getValue(Kelas.class);
                    mList.add(kelas);
                    recyclerView.setAdapter(adapter);
                    adapter.setmList(mList);

                    adapter.setOnclickItemAdapter(kelas1 -> {
                        Intent intent = new Intent(getContext(), KelasActivity.class);
                        intent.putExtra(KelasActivity.EXTRA_KELAS, kelas1);
                        startActivity(intent);
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
