package com.sidomafit.dosen.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sidomafit.dosen.model.Mahasiswa;
import com.sidomafit.dosen.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sidomafit.dosen.activity.DetailMahasiswaActivity.ID_MAHASISWA;

public class ProfilMahasiswaActivity extends AppCompatActivity {

    private TextView tvNama, tvNim, tvKelas, tvEmail, tvNoTlp;
    private CircleImageView imgProfil;
    private ImageView imgEmail;
    private String nomor, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_mahasiswa);

        Log.d("CEKDATA", ID_MAHASISWA);

        tvNama = findViewById(R.id.tv_nama_profil_mahasiswa_detail);
        tvNim = findViewById(R.id.tv_nim_profil_mahasiswa_detail);
        tvKelas = findViewById(R.id.tv_kelas_profil_mahasiswa_detail);
        tvEmail = findViewById(R.id.tv_email_profil_mahasiswa_detail);
        tvNoTlp = findViewById(R.id.tv_notlp_profil_mahasiswa_detail);
        imgProfil = findViewById(R.id.img_profil_mahasiswa_detail);
        imgEmail = findViewById(R.id.img_email_mhs);
        ImageView imgTelp = findViewById(R.id.img_tlp);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Mahasiswa").child(ID_MAHASISWA);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("Mahasiswa", dataSnapshot.toString());
                Mahasiswa mahasiswa = dataSnapshot.getValue(Mahasiswa.class);
                tvNama.setText(mahasiswa.getNama_mhs());
                setTitle(mahasiswa.getNama_mhs());
                tvNim.setText(mahasiswa.getNim());
                tvKelas.setText("D3RPLA-"+mahasiswa.getKelas());
                tvEmail.setText(mahasiswa.getEmail_mhs());
                tvNoTlp.setText(mahasiswa.getNo_telepon());
                nomor = mahasiswa.getNo_telepon();
                email = mahasiswa.getEmail_mhs();
                if (mahasiswa.getFoto_mhs().equals("default")){
                    imgProfil.setImageResource(R.drawable.img_profile);
                }else {
                    Picasso.get().load(mahasiswa.getFoto_mhs()).into(imgProfil);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        imgTelp.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+nomor));
            startActivity(intent);
        });
        sendEmail(new String []{email});
    }

    private void sendEmail(String[] addresses) {
        imgEmail.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" +email));
            intent.putExtra(Intent.EXTRA_EMAIL, addresses);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        });
    }
}

