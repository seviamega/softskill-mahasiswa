package com.sidomafit.dosen.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sidomafit.dosen.adapter.MahasiswaAdapter;
import com.sidomafit.dosen.model.Kelas;
import com.sidomafit.dosen.model.Mahasiswa;
import com.sidomafit.dosen.R;

import java.util.ArrayList;
import java.util.List;

public class KelasActivity extends AppCompatActivity {

    public static final String EXTRA_KELAS = "extra_kelas";
    private MahasiswaAdapter adapter;
    private RecyclerView recyclerView;
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelas);


        recyclerView = findViewById(R.id.rv_list_kelas);
        tvStatus = findViewById(R.id.tv_status_kelas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MahasiswaAdapter(this);
        recyclerView.setAdapter(adapter);

        Kelas kelas = getIntent().getParcelableExtra(EXTRA_KELAS);
        String ID_KELAS = kelas.getId();
        Log.d("CEKDATA", ID_KELAS);
        setTitle(kelas.getKelas());

        final ProgressDialog dialog = new ProgressDialog(KelasActivity.this);
        dialog.setMessage(getString(R.string.wait));
        dialog.show();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Mahasiswa");
        reference.orderByChild("kelas").equalTo(ID_KELAS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("CEKDATA", dataSnapshot.toString());
                if (dataSnapshot.getValue() == null){
                    dialog.dismiss();
                }else {
                    dialog.dismiss();
                }
                final List<Mahasiswa> list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Mahasiswa mahasiswa = snapshot.getValue(Mahasiswa.class);
                    list.add(mahasiswa);
                    adapter = new MahasiswaAdapter(KelasActivity.this, list, tvStatus);
                    recyclerView.setAdapter(adapter);
                    adapter.setList(list);
                    dialog.dismiss();

                    adapter.setClickMahasiswa(mahasiswa1 -> {
                        Intent intent = new Intent(KelasActivity.this, DetailMahasiswaActivity.class);
                        intent.putExtra(DetailMahasiswaActivity.EXTRA_MAHASISWA, mahasiswa1);
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
