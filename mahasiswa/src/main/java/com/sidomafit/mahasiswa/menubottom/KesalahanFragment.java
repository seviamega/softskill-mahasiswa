package com.sidomafit.mahasiswa.menubottom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sidomafit.mahasiswa.activity.DetailKesalahanActivity;
import com.sidomafit.mahasiswa.adapter.KesalahanAdapter;
import com.sidomafit.mahasiswa.MainActivity;
import com.sidomafit.mahasiswa.model.Kesalahan;
import com.sidomafit.mahasiswa.model.Mahasiswa;
import com.sidomafit.mahasiswa.R;
import com.sidomafit.mahasiswa.model.viewmodel.ViewModelKesalahan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.sidomafit.mahasiswa.MainActivity.USER_ID;

public class KesalahanFragment extends Fragment {


    private RecyclerView recyclerView;
    private TextView tvTotal;
    private TextView tvStatus;
    private KesalahanAdapter adapter;
    private ViewModelKesalahan viewModel;
    private FirebaseAuth mAuth;
    private String nim;

    public KesalahanFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kesalahan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.kesalahan);

        tvTotal = view.findViewById(R.id.tv_total_poin);
        TextView tvPersen = view.findViewById(R.id.tv_persen_poin);
        tvStatus = view.findViewById(R.id.tv_status_detail_mahasiswa);
        recyclerView = view.findViewById(R.id.rv_list_kelasalahan);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAuth = FirebaseAuth.getInstance();
        tampiltotalpoin();
    }

    private void tampiltotalpoin() {

        DatabaseReference mhsRef = FirebaseDatabase.getInstance().getReference();
        FirebaseUser mUser = mAuth.getCurrentUser();
        assert mUser != null;
        String emailLogin = mUser.getEmail();

        mhsRef.child("Mahasiswa").orderByChild("email_mhs").equalTo(emailLogin)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<Mahasiswa> listMahasiswa = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Mahasiswa mahasiswa = snapshot.getValue(Mahasiswa.class);
                            listMahasiswa.add(mahasiswa);
                            USER_ID = mahasiswa.getNim();
                            Log.d("CEKDATA", USER_ID);
                            tvTotal.setText(String.valueOf(mahasiswa.getTotal_poin()));
                            nim = mahasiswa.getNim();
                            tampliKesalahan();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ViewModelKesalahan.class);
    }

    private void tampliKesalahan() {

        viewModel.getKesalahankesalahan(USER_ID).observe(this, new Observer<List<Kesalahan>>() {
            @Override
            public void onChanged(List<Kesalahan> kesalahans) {
                adapter = new KesalahanAdapter(getContext(),  kesalahans, tvStatus);
                recyclerView.setAdapter(adapter);
                adapter.setList(kesalahans);

                Collections.sort(kesalahans, new Comparator<Kesalahan>() {
                    @Override
                    public int compare(Kesalahan o1, Kesalahan o2) {
                        return o2.getWaktu_pengiriman().compareTo(o1.getWaktu_pengiriman());
                    }
                });

                adapter.setClickKesalahan(new KesalahanAdapter.onClickKesalahan() {
                    @Override
                    public void clickKesalahan(Kesalahan kesalahan) {
                        Intent intent = new Intent(getContext(), DetailKesalahanActivity.class);
                        intent.putExtra(DetailKesalahanActivity.EXTRA_KESALAHAN, kesalahan);
                        startActivity(intent);
                    }
                });
            }
        });

    }



}
