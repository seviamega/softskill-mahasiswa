package com.sidomafit.dosen.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sidomafit.dosen.model.Kesalahan;
import com.sidomafit.dosen.R;
import com.sidomafit.dosen.model.viewmodel.SoftSkill;
import com.sidomafit.dosen.notifications.APIService;
import com.sidomafit.dosen.notifications.Client;
import com.sidomafit.dosen.notifications.Data;
import com.sidomafit.dosen.notifications.MyResponse;
import com.sidomafit.dosen.notifications.Sender;
import com.sidomafit.dosen.notifications.Token;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sidomafit.dosen.MainActivity.NamaDosen;
import static com.sidomafit.dosen.MainActivity.USER_ID;

public class DetailKesalahanActivity extends AppCompatActivity {
    public static final String EXTRA_KESALAHAN = "extra_kesalahan";

    private TextView tvDesk;
    public static String ID_KESALAHAN;
    public static String ID_MHS;
    private String imgURL;
    private String idPengirim;
    private String jenisSoftskill;
    private APIService apiService;

    private boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kesalahan);

        setTitle(R.string.title_detail_kesalahan);
        Kesalahan dataKesalahan = getIntent().getParcelableExtra(EXTRA_KESALAHAN);
        ID_KESALAHAN = dataKesalahan.getId_kesalahan();
        ID_MHS = dataKesalahan.getId_mahasiswa();
        imgURL = dataKesalahan.getPending_foto();
        idPengirim = dataKesalahan.getId_pengirim();
        Log.d("CEKDATA", ID_KESALAHAN);


        TextView tvNama = findViewById(R.id.tv_nama_detail_kesalahan);
        TextView tvNim = findViewById(R.id.tv_nim_detail_kesalahan);
        TextView tvWaktu = findViewById(R.id.tv_waktu_detail_kesalahan);
        TextView tvPoin = findViewById(R.id.tv_poin_detail_kesalahan);
        TextView tvTxtPoin = findViewById(R.id.textView6);
        TextView tvPengirim = findViewById(R.id.tv_pengirim_detail_kesalahan);
        tvDesk = findViewById(R.id.tv_desk_detail_kesalahan);
        CircleImageView imgProfil = findViewById(R.id.img_detail_kesalahan);
        ImageView imgPending = findViewById(R.id.img_pending);
        Button btnEdit = findViewById(R.id.btn_edit_detail_kesalahan);
        Button btnTerima = findViewById(R.id.btn_terima);
        Button btnTolak = findViewById(R.id.btn_tolak);

        String softSkill = dataKesalahan.getStatus();

        tvNama.setText(dataKesalahan.getNama_pelanggar());
        tvNim.setText(dataKesalahan.getId_mahasiswa());
        tvWaktu.setText(dataKesalahan.getWaktu_pengiriman());

        tvPoin.setText(dataKesalahan.getPoin_kesalahan());
        tvPengirim.setText(dataKesalahan.getNama_pengirim());

        apiService = Client.getClient("https://fcm.googleapis.com/", DetailKesalahanActivity.this).create(APIService.class);


        Log.d("CEKDATA", "Nim : " + idPengirim);
        Log.d("CEKDATA", "Nama Dosen : "+ NamaDosen);
        Log.d("CEKDATA", "NIP dosen : "+ USER_ID);





        if (dataKesalahan.getFoto_mhs().equals("default")){
            imgProfil.setImageResource(R.drawable.img_profile);
        }else {
            Picasso.get().load(dataKesalahan.getFoto_mhs()).into(imgProfil);
        }
        if (dataKesalahan.getPending_foto().equals("default")){
            imgPending.setVisibility(View.GONE);
            btnTolak.setVisibility(View.GONE);
            btnTerima.setVisibility(View.GONE);

            DatabaseReference skillRef = FirebaseDatabase.getInstance().getReference("SoftSkill");
            skillRef.child(softSkill).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    SoftSkill skill = dataSnapshot.getValue(SoftSkill.class);
                    jenisSoftskill = skill.getNama_softskill();
                    Log.d("CEKDATA", jenisSoftskill);

                    tvDesk.setText("Mahasiswa yang bernama " + dataKesalahan.getNama_pelanggar() +
                            " telah melakukan kesalahan "+
                            dataKesalahan.getDeskripsi_kesalahan().toUpperCase() +
                            " dan mendapatkan poin sebesar "+
                            dataKesalahan.getPoin_kesalahan()+
                            " poin. "+
                            "Bedasarkan kesalahan tesebut disarankan untuk memperbaiki soft skill "+
                            jenisSoftskill);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else {
            Picasso.get().load(dataKesalahan.getPending_foto()).into(imgPending);
            tvPoin.setVisibility(View.GONE);
            tvTxtPoin.setVisibility(View.GONE);
            tvNim.setText(idPengirim);
        }
        if (USER_ID.equals(dataKesalahan.getId_pengirim())){
            btnEdit.setVisibility(View.VISIBLE);
            btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(DetailKesalahanActivity.this, EditKesalahanActivity.class);
                finish();
                startActivity(intent);
            });
        }else {
            btnEdit.setVisibility(View.GONE);
        }

        if (dataKesalahan.getStatus().equals("terkirim")){
            btnTerima.setVisibility(View.VISIBLE);
            btnTolak.setVisibility(View.VISIBLE);
        }else if (dataKesalahan.getStatus().equals("direspon")){
            btnTerima.setVisibility(View.GONE);
            btnTolak.setVisibility(View.GONE);
        }

        btnTerima.setOnClickListener(v -> pilihTerima());
        btnTolak.setOnClickListener(v -> pilihTolak());


    }



    private void pilihTerima() {
        String kesalahan = "Foto profil anda diterima";
        DatabaseReference mahasiswaRef = FirebaseDatabase.getInstance().getReference("Mahasiswa");
        HashMap<String , Object> updateData = new HashMap<>();
        updateData.put("foto_mhs", imgURL);
        mahasiswaRef.child(idPengirim).updateChildren(updateData);

        DatabaseReference kesalahanRef = FirebaseDatabase.getInstance().getReference("Kesalahan");
        HashMap<String , Object> updateKesalahan = new HashMap<>();
        updateKesalahan.put("status", "direspon");
        kesalahanRef.child(ID_KESALAHAN).updateChildren(updateKesalahan);
        finish();
        sendNotificationMahasiswa(idPengirim,NamaDosen,kesalahan);
        notify = true;

    }
    private void pilihTolak() {
        String kesalahan = "Foto profil anda ditolak";
        DatabaseReference kesalahanRef = FirebaseDatabase.getInstance().getReference("Kesalahan");
        HashMap<String , Object> updateKesalahan = new HashMap<>();
        updateKesalahan.put("status", "direspon");
        kesalahanRef.child(ID_KESALAHAN).updateChildren(updateKesalahan);
        finish();
        sendNotificationMahasiswa(idPengirim,NamaDosen,kesalahan);
        notify = true;

    }



    private void sendNotificationMahasiswa(String reciver, final String username, final String message ){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(reciver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(USER_ID, R.drawable.icon_dsn,  message, NamaDosen,
                            idPengirim);

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNofirication(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1){
                                            Toast.makeText(DetailKesalahanActivity.this,R.string.gagal, Toast.LENGTH_SHORT).show();
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
