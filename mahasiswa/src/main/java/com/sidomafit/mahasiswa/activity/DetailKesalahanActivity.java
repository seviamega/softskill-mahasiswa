package com.sidomafit.mahasiswa.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sidomafit.mahasiswa.model.Kesalahan;
import com.sidomafit.mahasiswa.R;
import com.sidomafit.mahasiswa.model.viewmodel.SoftSkill;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailKesalahanActivity extends AppCompatActivity {

    public static final String EXTRA_KESALAHAN = "extra_kesalahan";
    private TextView tvDesk;
    private String jenisSoftskill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kesalahan);

        final Kesalahan dataKesalahan = getIntent().getParcelableExtra(EXTRA_KESALAHAN);
        setTitle(R.string.title_detail_kesalahan);

        TextView tvNama = findViewById(R.id.tv_nama_detail_mhs);
        TextView tvNim = findViewById(R.id.tv_nim_detail_mhs);
        TextView tvWaktu = findViewById(R.id.tv_waktu_detail_mhs);
        tvDesk = findViewById(R.id.tv_desk_detail_mhs);
        TextView tvPengirim = findViewById(R.id.tv_pengirim_detail_mhs);
        TextView tvPoin = findViewById(R.id.tv_poin_detail_mhs);
        CircleImageView imgProfil = findViewById(R.id.img_detail_mhs);

        tvNama.setText(dataKesalahan.getNama_pelanggar());
        tvNim.setText(dataKesalahan.getId_mahasiswa());
        tvWaktu.setText(dataKesalahan.getWaktu_pengiriman());
        tvPengirim.setText(dataKesalahan.getNama_pengirim());
        tvPoin.setText(String.valueOf(dataKesalahan.getPoin_kesalahan()));
        if (dataKesalahan.getFoto_mhs().equals("default")){
            imgProfil.setImageResource(R.drawable.img_profile);
        }else {
            Picasso.get().load(dataKesalahan.getFoto_mhs()).into(imgProfil);
        }

        String softSkill = dataKesalahan.getStatus();

        DatabaseReference skillRef = FirebaseDatabase.getInstance().getReference("SoftSkill");
        skillRef.child(softSkill).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SoftSkill skill = dataSnapshot.getValue(SoftSkill.class);
                jenisSoftskill = skill.getNama_softskill();
                Log.d("CEKDATA", jenisSoftskill);

                tvDesk.setText("Haloo "+ dataKesalahan.getNama_pelanggar() +" , " +
                        "anda telah melakukan kesalahan "+dataKesalahan.getDeskripsi_kesalahan().toUpperCase() +
                        " dan mendapatkan poin sebesar " + dataKesalahan.getPoin_kesalahan() +" poin. "+
                        "Perbaiki soft skill " + jenisSoftskill);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
