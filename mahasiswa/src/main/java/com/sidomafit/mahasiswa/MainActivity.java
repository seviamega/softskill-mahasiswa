package com.sidomafit.mahasiswa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.sidomafit.mahasiswa.menubottom.KesalahanFragment;
import com.sidomafit.mahasiswa.menubottom.NotifikasiFragment;
import com.sidomafit.mahasiswa.menubottom.ProfilFragment;
import com.sidomafit.mahasiswa.model.Mahasiswa;
import com.sidomafit.mahasiswa.notification.Token;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static String USER_ID;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationView);

        final KesalahanFragment kesalahanFragment = new KesalahanFragment();
        final NotifikasiFragment notifikasiFragment = new NotifikasiFragment();
        final ProfilFragment profilFragment = new ProfilFragment();

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_kesalahan:
                        setFragment(kesalahanFragment);
                        break;
                    case R.id.nav_notifikasi:
                        setFragment(notifikasiFragment);
                        break;
                    case R.id.nav_profil:
                        setFragment(profilFragment);
                        break;
                }
                return true;
            }
        });

        if (savedInstanceState == null){
            setFragment(kesalahanFragment);
        }

        mAuth = FirebaseAuth.getInstance();
        chackStatusUser();


    }

    private void updateToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                String refereshToken = task.getResult().getToken();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
                Token token = new Token(refereshToken);
                reference.child(USER_ID).setValue(token);
            }
        });
    }

    private void chackStatusUser() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Tunggu sebentar...");
        dialog.show();
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
                            dialog.dismiss();
                            Log.d("CEKDATA", USER_ID);
                            updateToken();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_nav, fragment);
        transaction.commit();
    }
}
