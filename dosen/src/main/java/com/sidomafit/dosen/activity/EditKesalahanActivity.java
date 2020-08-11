package com.sidomafit.dosen.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sidomafit.dosen.adapter.SearchAdapter;
import com.sidomafit.dosen.model.Kesalahan;
import com.sidomafit.dosen.model.Mahasiswa;
import com.sidomafit.dosen.model.viewmodel.ViewModelSearch;
import com.sidomafit.dosen.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sidomafit.dosen.activity.DetailKesalahanActivity.ID_KESALAHAN;
import static com.sidomafit.dosen.activity.DetailKesalahanActivity.ID_MHS;

public class EditKesalahanActivity extends AppCompatActivity {
    private TextView tvNama, tvNim;
    private EditText etPoin, etAC;
    private CircleImageView imgProfile;

    private int poinKesalahan;
    private int total_poin;

    private String idKesalahan;

    private RecyclerView recyclerView;
    private ViewModelSearch viewModel;
    private SearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kesalahan);

        setTitle(R.string.title_edit_kesalahan);

        tvNama = findViewById(R.id.tv_nama_tambah_kesalahan);
        tvNim = findViewById(R.id.tv_nim_tambah_kesalahan);
        etPoin = findViewById(R.id.et_poin_tambah_kesalahan);
        imgProfile = findViewById(R.id.img_tambah_kesalahan);
        Button btnSimpan = findViewById(R.id.btn_simpan_tambah_kesalahan);
        etAC = findViewById(R.id.et_ac);

        recyclerView = findViewById(R.id.rv_edit_kesalahan);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(EditKesalahanActivity.this));
        adapter = new SearchAdapter(EditKesalahanActivity.this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        btnSimpan.setOnClickListener(v -> simpanKesalahan());

        tampilDataMhs();
        setPencarian();
        openPencarian();

        tampilKesalahan();
    }

    private void tampilDataMhs() {
        DatabaseReference dataMhsRef = FirebaseDatabase.getInstance().getReference("Mahasiswa").child(ID_MHS);
        dataMhsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Mahasiswa mahasiswa = dataSnapshot.getValue(Mahasiswa.class);
                total_poin = Integer.parseInt(String.valueOf(mahasiswa.getTotal_poin()));
                Log.d("CEKDATA", "Total Poin : " + total_poin);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void tampilKesalahan() {
        DatabaseReference kesalahanRef = FirebaseDatabase.getInstance().getReference();
        kesalahanRef.child("Kesalahan").child(ID_KESALAHAN).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Kesalahan kesalahan = dataSnapshot.getValue(Kesalahan.class);
                tvNama.setText(kesalahan.getNama_pelanggar());
                tvNim.setText(kesalahan.getId_mahasiswa());
                idKesalahan = kesalahan.getId_kesalahan();
                Log.d("CEKDATA", idKesalahan);
                if (kesalahan.getFoto_mhs().equals("default")){
                    imgProfile.setImageResource(R.drawable.img_profile);
                }else {
                    Picasso.get().load(kesalahan.getFoto_mhs()).into(imgProfile);
                }
                etAC.setText(kesalahan.getDeskripsi_kesalahan());
                etPoin.setText(kesalahan.getPoin_kesalahan());

                poinKesalahan = Integer.parseInt(kesalahan.getPoin_kesalahan());
                Log.d("CEKDATA", "Jumlah Poin : " + poinKesalahan);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void hapusKesalahan() {
        Log.d("CEKDATA", "Hasil Akhir : "+ (total_poin + poinKesalahan));

        DatabaseReference hapusRef = FirebaseDatabase.getInstance().getReference();
        hapusRef.child("Kesalahan").child(idKesalahan).removeValue();

        DatabaseReference mahasiswaRef = FirebaseDatabase.getInstance().getReference("Mahasiswa");
        HashMap<String , Object> data = new HashMap<>();
        data.put("total_poin", total_poin+poinKesalahan);
        mahasiswaRef.child(ID_MHS).updateChildren(data);
    }


    private void setPencarian() {
        etAC.addTextChangedListener(new TextWatcher() {
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

    private void filterData(String kesalahan) {
        if (kesalahan.length()>0){
            viewModel.carKeslahan(kesalahan);
            recyclerView.setVisibility(View.VISIBLE);
        }else {
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void openPencarian() {
        recyclerView.setVisibility(View.GONE);
        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ViewModelSearch.class);
        viewModel.getKeslahan();
        viewModel.getListkesalahan().observe(this, searchModels -> {
            adapter = new SearchAdapter(EditKesalahanActivity.this);
            recyclerView.setAdapter(adapter);
            adapter.setList(searchModels);

            adapter.setOnClickDaftarKesalahan(posotion -> {
                etPoin.setText(posotion.getPoin_kesalahan());
                etAC.setText(posotion.getKesalahan());
//                        ID_KESALAHAN = posotion.getId_kesalahan();
                recyclerView.setVisibility(View.GONE);
            });
        });
    }




    private void simpanKesalahan() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.wait));
        dialog.show();

        String kesalahan = etAC.getText().toString();
        String poin = etPoin.getText().toString();
        int tambahPoin = Integer.parseInt(poin);

        Log.d("CEKDATA", "Hasil Akhir : " + (total_poin + poinKesalahan - tambahPoin));
        Log.d("CEKDATA", "Hasil Akhir Pengurangan : " + total_poin);

        if (TextUtils.isEmpty(kesalahan) || TextUtils.isEmpty(poin)){
            Toast.makeText(EditKesalahanActivity.this, R.string.data_tidak_kosong, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }else {
            DatabaseReference catatanRef = FirebaseDatabase.getInstance().getReference("Kesalahan");
            HashMap<String , Object> addCatatan = new HashMap<>();
            addCatatan.put("deskripsi_kesalahan", kesalahan);
            addCatatan.put("poin_kesalahan", poin);
            catatanRef.child(ID_KESALAHAN).updateChildren(addCatatan).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(EditKesalahanActivity.this, R.string.berhasil_memperbarui, Toast.LENGTH_SHORT).show();
                    finish();
                    dialog.dismiss();
                }
            });

            DatabaseReference mahasiswaRef = FirebaseDatabase.getInstance().getReference("Mahasiswa");
            HashMap<String , Object> data = new HashMap<>();
            data.put("total_poin", total_poin+poinKesalahan-tambahPoin);
            mahasiswaRef.child(ID_MHS).updateChildren(data);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hapus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.hapus) {
            if (item.getItemId() == R.id.hapus) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(EditKesalahanActivity.this);
                builder.setTitle(R.string.hapus_salaah);
                builder.setMessage(R.string.yakin_menghapus);
                builder.setIcon(R.drawable.logo_apk);
                builder.setPositiveButton(R.string.hapus, (dialogInterface, i) -> {
                    Toast.makeText(EditKesalahanActivity.this, R.string.berhasil_hapus_kesalahan, Toast.LENGTH_SHORT).show();
                    hapusKesalahan();
                    finish();
                });

                builder.setNegativeButton(R.string.batal, (dialogInterface, i) -> dialogInterface.cancel());

                builder.create().show();

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
