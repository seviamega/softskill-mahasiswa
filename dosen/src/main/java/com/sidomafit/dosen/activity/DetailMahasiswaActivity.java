package com.sidomafit.dosen.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sidomafit.dosen.adapter.KesalahanAdapter;
import com.sidomafit.dosen.model.Mahasiswa;
import com.sidomafit.dosen.model.viewmodel.ViewModelKesalahan;
import com.sidomafit.dosen.R;

import java.util.Collections;

public class DetailMahasiswaActivity extends AppCompatActivity {

    public static final String EXTRA_MAHASISWA = "extra_mahasiswa";

    private RecyclerView recyclerView;
    private TextView tvTotal;
    private TextView tvStatus;
    private KesalahanAdapter adapter;
    private Button btnLihatProfil;
    private FloatingActionButton fabTambah;
    public static String ID_MAHASISWA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_mahasiswa);

        Mahasiswa mahasiswa = getIntent().getParcelableExtra(EXTRA_MAHASISWA);
        setTitle(mahasiswa.getNama_mhs());
        ID_MAHASISWA = mahasiswa.getNim();
        Log.d("CEKDATA", ID_MAHASISWA);

        tvTotal = findViewById(R.id.tv_total_poin);
        TextView tvPersen = findViewById(R.id.tv_persen_poin);
        tvStatus = findViewById(R.id.tv_status_detail_mahasiswa);
        btnLihatProfil = findViewById(R.id.btn_lihat_profil);
        fabTambah = findViewById(R.id.fab_tambah);
        recyclerView = findViewById(R.id.rv_list_kelasalahan);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




        lihatProfil();
        tambahKesalahan();
        tampliKesalahan();
        tampilDataMahasiswa();
    }

    private void tampilDataMahasiswa() {
        DatabaseReference mahasiswaRef = FirebaseDatabase.getInstance().getReference();
        mahasiswaRef.child("Mahasiswa").child(ID_MAHASISWA).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Mahasiswa mahasiswa = dataSnapshot.getValue(Mahasiswa.class);
                tvTotal.setText(String.valueOf(mahasiswa.getTotal_poin()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void tampliKesalahan() {
        ViewModelKesalahan viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ViewModelKesalahan.class);
        viewModel.getKesalahankesalahan(ID_MAHASISWA).observe(this, kesalahans -> {
            Log.d("CEKDATA", kesalahans.toString());
            adapter = new KesalahanAdapter(DetailMahasiswaActivity.this,  kesalahans, tvStatus);
            recyclerView.setAdapter(adapter);
            adapter.setList(kesalahans);

            Collections.sort(kesalahans, (o1, o2) -> o2.getWaktu_pengiriman().compareTo(o1.getWaktu_pengiriman()));

            adapter.setClickKesalahan(kesalahan1 -> {
                Intent intent = new Intent(DetailMahasiswaActivity.this, DetailKesalahanActivity.class);
                intent.putExtra(DetailKesalahanActivity.EXTRA_KESALAHAN, kesalahan1);
                startActivity(intent);
            });
        });
    }

    private void tambahKesalahan() {
        fabTambah.setOnClickListener(v -> {
            Intent intent = new Intent(DetailMahasiswaActivity.this, TambahKesalahanActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void lihatProfil() {
        btnLihatProfil.setOnClickListener(v -> startActivity(new Intent(DetailMahasiswaActivity.this, ProfilMahasiswaActivity.class)));
    }
}
