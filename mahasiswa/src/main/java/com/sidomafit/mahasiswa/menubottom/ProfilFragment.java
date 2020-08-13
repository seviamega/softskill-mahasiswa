package com.sidomafit.mahasiswa.menubottom;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sidomafit.mahasiswa.activity.EditProfilMahasiswaActivity;
import com.sidomafit.mahasiswa.activity.LoginActivity;
import com.sidomafit.mahasiswa.MainActivity;
import com.sidomafit.mahasiswa.model.Mahasiswa;
import com.sidomafit.mahasiswa.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sidomafit.mahasiswa.MainActivity.USER_ID;

public class ProfilFragment extends Fragment {

    private Button btnEdit, btnKeluar;
    private TextView tvNama, tvNip, tvEmail, tvNoTlp;
    private CircleImageView imgProfil;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    public ProfilFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.profil);

        btnEdit = view.findViewById(R.id.btn_edit_profil);
        btnKeluar = view.findViewById(R.id.btn_keluar);
        tvNama = view.findViewById(R.id.tv_nama_profil_dosen);
        tvNip = view.findViewById(R.id.tv_nip_profil_dosen);
        tvEmail = view.findViewById(R.id.tv_email_profil_dosen);
        tvNoTlp = view.findViewById(R.id.tv_noTlp_profil_dosen);
        imgProfil = view.findViewById(R.id.img_profil_dosen);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        tampildataProfil();
        logoutUser();
        editProfilMahasiswa();

    }

    private void tampildataProfil() {
        DatabaseReference mhsRef = FirebaseDatabase.getInstance().getReference();
        mhsRef.child("Mahasiswa").child(USER_ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Mahasiswa mahasiswa = dataSnapshot.getValue(Mahasiswa.class);
                tvNama.setText(mahasiswa.getNama_mhs());
                tvEmail.setText(mahasiswa.getEmail_mhs());
                tvNip.setText(mahasiswa.getNim());
                tvNoTlp.setText(mahasiswa.getNo_telepon());
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
    }

    private void editProfilMahasiswa() {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EditProfilMahasiswaActivity.class));
            }
        });
    }

    private void logoutUser() {
        btnKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                mGoogleSignInClient.revokeAccess();
                mGoogleSignInClient.signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });
    }
}
