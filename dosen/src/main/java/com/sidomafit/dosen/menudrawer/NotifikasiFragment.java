package com.sidomafit.dosen.menudrawer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.sidomafit.dosen.activity.DetailKesalahanActivity;
import com.sidomafit.dosen.adapter.NotifikasiAdapter;
import com.sidomafit.dosen.MainActivity;
import com.sidomafit.dosen.model.Kesalahan;
import com.sidomafit.dosen.R;

import java.util.ArrayList;
import java.util.Collections;

import static com.sidomafit.dosen.MainActivity.USER_ID;

public class NotifikasiFragment extends Fragment {

    private NotifikasiAdapter adapter;
    private RecyclerView recyclerView;
    private TextView tvStatus;


    public NotifikasiFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifikasi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.nav_notif);

        recyclerView = view.findViewById(R.id.rv_notif);
        tvStatus = view.findViewById(R.id.tv_status_notifikasi);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new NotifikasiAdapter(getContext());

        tampilNotifikasi();
    }

    private void tampilNotifikasi() {
        DatabaseReference kesalahanRef = FirebaseDatabase.getInstance().getReference();
        kesalahanRef.child("Kesalahan").orderByChild("id_dosen_wali").equalTo(USER_ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Kesalahan> list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Kesalahan kesalahan = snapshot.getValue(Kesalahan.class);
                    if (!kesalahan.getId_pengirim().equals(USER_ID)){
                        list.add(kesalahan);
                    }
                    adapter = new NotifikasiAdapter(getContext(), list, tvStatus);
                    recyclerView.setAdapter(adapter);
                    adapter.setList(list);
                    Collections.sort(list, (o1, o2) -> o2.getWaktu_pengiriman().compareTo(o1.getWaktu_pengiriman()));

                    adapter.setClickNotifikasi(kesalahan1 -> {
                        Intent intent = new Intent(getContext(), DetailKesalahanActivity.class);
                        intent.putExtra(DetailKesalahanActivity.EXTRA_KESALAHAN, kesalahan1);
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
