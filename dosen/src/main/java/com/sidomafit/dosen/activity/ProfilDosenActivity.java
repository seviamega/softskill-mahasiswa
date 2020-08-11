package com.sidomafit.dosen.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sidomafit.dosen.loginpage.LoginActivity;
import com.sidomafit.dosen.model.Dosen;
import com.sidomafit.dosen.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sidomafit.dosen.MainActivity.USER_ID;

public class ProfilDosenActivity extends AppCompatActivity {

    private Button btnEdit, btnKeluar;
    private TextView tvNama, tvNip, tvEmail, tvNoTlp;
    private CircleImageView imgProfil;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_dosen);
        setTitle(R.string.title_profil_dosen);

        btnEdit = findViewById(R.id.btn_edit_profil);
        btnKeluar = findViewById(R.id.btn_keluar);
        tvNama = findViewById(R.id.tv_nama_profil_dosen);
        tvNip = findViewById(R.id.tv_nip_profil_dosen);
        tvEmail = findViewById(R.id.tv_email_profil_dosen);
        tvNoTlp = findViewById(R.id.tv_noTlp_profil_dosen);
        imgProfil = findViewById(R.id.img_profil_dosen);

        Log.d("CEKDATA", USER_ID);
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        tampilDataDosen();
        logoutUser();
        editProfilDosen();

    }

    private void editProfilDosen() {
        btnEdit.setOnClickListener(v -> startActivity(new Intent(ProfilDosenActivity.this, EditProfilDosenActivity.class)));
    }

    private void tampilDataDosen() {
        DatabaseReference dosenRef = FirebaseDatabase.getInstance().getReference();
        dosenRef.child("Dosen").child(USER_ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Dosen dosen = dataSnapshot.getValue(Dosen.class);
                tvNama.setText(dosen.getNama_dosen());
                tvEmail.setText(dosen.getEmail_dosen());
                tvNip.setText(dosen.getNip());
                tvNoTlp.setText(dosen.getNo_telepon());
                if (dosen.getFoto_dosen().equals("default")){
                    imgProfil.setImageResource(R.drawable.img_profile);
                }else {
                    Picasso.get().load(dosen.getFoto_dosen()).into(imgProfil);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void logoutUser() {
        btnKeluar.setOnClickListener(v -> {
            mAuth.signOut();
            mGoogleSignInClient.revokeAccess();
            mGoogleSignInClient.signOut();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
