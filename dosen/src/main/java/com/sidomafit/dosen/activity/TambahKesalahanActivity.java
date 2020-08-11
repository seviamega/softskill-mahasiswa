package com.sidomafit.dosen.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sidomafit.dosen.adapter.SearchAdapter;
import com.sidomafit.dosen.model.Dosen;
import com.sidomafit.dosen.model.Mahasiswa;
import com.sidomafit.dosen.model.viewmodel.ViewModelSearch;
import com.sidomafit.dosen.notifications.APIService;
import com.sidomafit.dosen.notifications.Client;
import com.sidomafit.dosen.notifications.Data;
import com.sidomafit.dosen.notifications.MyResponse;
import com.sidomafit.dosen.notifications.Sender;
import com.sidomafit.dosen.notifications.Token;
import com.sidomafit.dosen.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sidomafit.dosen.activity.DetailMahasiswaActivity.ID_MAHASISWA;
import static com.sidomafit.dosen.MainActivity.USER_ID;

public class TambahKesalahanActivity extends AppCompatActivity {

    private TextView tvNama, tvNim;
    private EditText etPoin, etAC;
    private CircleImageView imgProfile;
    private Button btnSimpan;
    private String userId;
    private int totalPoin;
    private String namaDosenLogin;
    private String namaPelangggar;
    private String dosen_wali;
    private String foto_mhs;
    private String IDKESALAHAN;
    private String statusKesalahan;

    private RecyclerView recyclerView;
    private ViewModelSearch viewModel;
    private SearchAdapter adapter;
    private APIService apiService;
    private boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kesalahan);
        setTitle(R.string.title_tambah_keselahan);

        Log.d("CEKDATA", ID_MAHASISWA);

        tvNama = findViewById(R.id.tv_nama_tambah_kesalahan);
        tvNim = findViewById(R.id.tv_nim_tambah_kesalahan);
        etPoin = findViewById(R.id.et_poin_tambah_kesalahan);
        imgProfile = findViewById(R.id.img_tambah_kesalahan);
        btnSimpan = findViewById(R.id.btn_simpan_tambah_kesalahan);
        etAC = findViewById(R.id.et_ac);

        recyclerView = findViewById(R.id.rv_search);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(TambahKesalahanActivity.this));
        adapter = new SearchAdapter(TambahKesalahanActivity.this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        tampilMahasiswa();
        tampilDosen();

        saveKesalahan();
        openPencarian();
        setPencarian();
        apiService = Client.getClient("https://fcm.googleapis.com/", TambahKesalahanActivity.this).create(APIService.class);
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
            adapter = new SearchAdapter(TambahKesalahanActivity.this);
            recyclerView.setAdapter(adapter);
            adapter.setList(searchModels);

            adapter.setOnClickDaftarKesalahan(posotion -> {
               etPoin.setText(posotion.getPoin_kesalahan());
               etAC.setText(posotion.getKesalahan());
               IDKESALAHAN = posotion.getId_kesalahan();
               statusKesalahan = posotion.getStatus();
               recyclerView.setVisibility(View.GONE);
            });
        });
    }


    private void tampilDosen() {
        DatabaseReference dosenRef = FirebaseDatabase.getInstance().getReference();
        dosenRef.child("Dosen").child(USER_ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Dosen dosen = dataSnapshot.getValue(Dosen.class);
                userId = dosen.getKode_dosen();
                namaDosenLogin = dosen.getNama_dosen();
                Log.d("CEKDATA", userId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void tampilMahasiswa() {
        final DatabaseReference intenRef = FirebaseDatabase.getInstance().getReference("Mahasiswa").child(ID_MAHASISWA);
        intenRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Mahasiswa mahasiswa = dataSnapshot.getValue(Mahasiswa.class);
                tvNama.setText(mahasiswa.getNama_mhs());
                tvNim.setText(mahasiswa.getNim());
                totalPoin = mahasiswa.getTotal_poin();
                foto_mhs = mahasiswa.getFoto_mhs();
                namaPelangggar = mahasiswa.getNama_mhs();
                dosen_wali = mahasiswa.getKode_dosen();
                if (mahasiswa.getFoto_mhs().equals("default")) {
                    imgProfile.setImageResource(R.drawable.img_profile);
                } else {
                    Picasso.get().load(mahasiswa.getFoto_mhs()).into(imgProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveKesalahan() {
        btnSimpan.setOnClickListener(v -> {
            getKesalahan();
            notify = true;
        });
    }

    private void getKesalahan(){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.wait));
        dialog.show();
        DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();

        String waktu = dateFormat.format(date);
        String kesalahan = etAC.getText().toString();
        String poin = etPoin.getText().toString();



        if (TextUtils.isEmpty(kesalahan) || TextUtils.isEmpty(poin)){
            Toast.makeText(TambahKesalahanActivity.this, R.string.data_tidak_kosong, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }else {
            int updatePoin = Integer.parseInt(String.valueOf(totalPoin));
            int tambahPoin = Integer.parseInt(poin);

            DatabaseReference catatanRef = FirebaseDatabase.getInstance().getReference("Kesalahan");
            String idKesalahan = catatanRef.push().getKey();
            HashMap<String , Object> addCatatan = new HashMap<>();
            addCatatan.put("id_kesalahan", idKesalahan);
            addCatatan.put("id_dosen_wali", dosen_wali);
            addCatatan.put("id_mahasiswa", ID_MAHASISWA);
            addCatatan.put("id_pengirim", userId);
            addCatatan.put("nama_pelanggar", namaPelangggar);
            addCatatan.put("nama_pengirim", namaDosenLogin);
            addCatatan.put("deskripsi_kesalahan", kesalahan);
            addCatatan.put("poin_kesalahan", poin);
            addCatatan.put("waktu_pengiriman", waktu);
            addCatatan.put("foto_mhs", foto_mhs);
            addCatatan.put("status", statusKesalahan);
            addCatatan.put("pending_foto", "default");
            assert idKesalahan != null;
            catatanRef.child(idKesalahan).setValue(addCatatan);

            DatabaseReference daftarkesalahanRef = FirebaseDatabase.getInstance().getReference("DaftarKesalahan");
            HashMap<String , Object> dftKesalaha = new HashMap<>();
            if (IDKESALAHAN == null){
                dftKesalaha.put("id_kesalahan", idKesalahan);
                dftKesalaha.put("kesalahan", kesalahan);
                dftKesalaha.put("poin_kesalahan", poin);

                daftarkesalahanRef.child(idKesalahan).updateChildren(dftKesalaha);
            }else {
                dftKesalaha.put("id_kesalahan", IDKESALAHAN);
                dftKesalaha.put("kesalahan", kesalahan);
                dftKesalaha.put("poin_kesalahan", poin);
                daftarkesalahanRef.child(IDKESALAHAN).updateChildren(dftKesalaha);
            }

            DatabaseReference mahasiswaRef = FirebaseDatabase.getInstance().getReference("Mahasiswa").child(ID_MAHASISWA);
            HashMap<String , Object> hashMap = new HashMap<>();
            hashMap.put("total_poin", updatePoin - tambahPoin);
            mahasiswaRef.updateChildren(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    finish();
                    Toast.makeText(TambahKesalahanActivity.this, R.string.berhasil_tambah_kesalahan, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            final String msg = kesalahan;

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Dosen").child(USER_ID);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Dosen dosen = dataSnapshot.getValue(Dosen.class);
                        sendNotification(dosen_wali, dosen.getNama_dosen(), msg);
                        sendNotificationMahasiswa(ID_MAHASISWA, namaPelangggar, msg);
                        notify = true;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
    }

    private void sendNotification(String reciver, final String username, final String message ){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(reciver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (!reciver.equals(USER_ID)){
                        Token token = snapshot.getValue(Token.class);
                        Data data = new Data(USER_ID, R.drawable.icon_dsn,  message, namaPelangggar,
                                dosen_wali);

                        Sender sender = new Sender(data, token.getToken());

                        apiService.sendNofirication(sender)
                                .enqueue(new Callback<MyResponse>() {
                                    @Override
                                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                        if (response.code() == 200){
                                            if (response.body().success != 1){
                                                Toast.makeText(TambahKesalahanActivity.this,R.string.gagal, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<MyResponse> call, Throwable t) {

                                    }
                                });
                    }else {
                        Token token = snapshot.getValue(Token.class);
                        Data data = new Data(USER_ID, R.drawable.icon_dsn,  message, namaPelangggar,
                                dosen_wali);

                        Sender sender = new Sender(data, "null");

                        apiService.sendNofirication(sender)
                                .enqueue(new Callback<MyResponse>() {
                                    @Override
                                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                        if (response.code() == 200){
                                            if (response.body().success != 1){
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<MyResponse> call, Throwable t) {

                                    }
                                });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendNotificationMahasiswa(String reciver, final String username, final String message ){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(reciver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(USER_ID, R.drawable.icon_dsn,  message, namaDosenLogin,
                            ID_MAHASISWA);

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNofirication(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1){
                                            Toast.makeText(TambahKesalahanActivity.this,R.string.gagal, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
