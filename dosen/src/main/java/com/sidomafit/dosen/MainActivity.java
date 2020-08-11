package com.sidomafit.dosen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sidomafit.dosen.activity.ProfilDosenActivity;
import com.sidomafit.dosen.menudrawer.AnakWaliFragment;
import com.sidomafit.dosen.menudrawer.BantuanFragment;
import com.sidomafit.dosen.menudrawer.KelasFragment;
import com.sidomafit.dosen.menudrawer.LaporanFragment;
import com.sidomafit.dosen.menudrawer.NotifikasiFragment;
import com.sidomafit.dosen.menudrawer.PencarianFragment;
import com.sidomafit.dosen.menudrawer.TentangFragment;
import com.sidomafit.dosen.model.Dosen;
import com.sidomafit.dosen.notifications.Token;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    public static String EXTRA_ID = "extra_id";

    public static String USER_ID;
    public static String NamaDosen;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private CircleImageView imgHeader;
    private TextView tvNamaHeader, tvEmailHeader;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        drawerLayout = findViewById(R.id.layout_drawer);
        NavigationView navigationView = findViewById(R.id.nav_view);
        tvNamaHeader = navigationView.getHeaderView(0).findViewById(R.id.nama_header);
        imgHeader = navigationView.getHeaderView(0).findViewById(R.id.img_header);
        tvEmailHeader = navigationView.getHeaderView(0).findViewById(R.id.email_header);
        LinearLayout linearLayout = navigationView.getHeaderView(0).findViewById(R.id.layout_header);

        linearLayout.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ProfilDosenActivity.class)));

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.buka, R.string.tutup);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (getSupportActionBar() !=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final AnakWaliFragment anakWaliFragment = new AnakWaliFragment();
        final KelasFragment kelasFragment = new KelasFragment();
        final PencarianFragment pencarianFragment = new PencarianFragment();
        final NotifikasiFragment notifikasiFragment = new NotifikasiFragment();
        final LaporanFragment laporanFragment = new LaporanFragment();
        final TentangFragment tentangFragment = new TentangFragment();
        final BantuanFragment bantuanFragment = new BantuanFragment();

        if (savedInstanceState == null){
            setFregment(anakWaliFragment);
            navigationView.setCheckedItem(R.id.nev_anakwali);
        }




        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.nev_anakwali:
                    setFregment(anakWaliFragment);
                    break;
                case R.id.nav_kelas:
                    setFregment(kelasFragment);
                    break;
                case R.id.nav_pencarian:
                    setFregment(pencarianFragment);
                    break;
                case R.id.nav_notifikasi:
                    setFregment(notifikasiFragment);
                    break;
                case R.id.nav_laporan_saya:
                    setFregment(laporanFragment);
                    break;
                case R.id.nav_tentang:
                    setFregment(tentangFragment);
                    break;
                case R.id.nav_batuan:
                    setFregment(bantuanFragment);
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        mAuth = FirebaseAuth.getInstance();
        chackStatusUser();


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("NotificationMessage")) {

                toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.buka, R.string.tutup);
                drawerLayout.addDrawerListener(toggle);
                toggle.syncState();
                if (getSupportActionBar() !=null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }



                navigationView.setNavigationItemSelectedListener(menuItem -> {
                    switch (menuItem.getItemId()){
                        case R.id.nev_anakwali:
                            setFregment(anakWaliFragment);
                            break;
                        case R.id.nav_kelas:
                            setFregment(kelasFragment);
                            break;
                        case R.id.nav_pencarian:
                            setFregment(pencarianFragment);
                            break;
                        case R.id.nav_notifikasi:
                            setFregment(notifikasiFragment);
                            break;
                        case R.id.nav_laporan_saya:
                            setFregment(laporanFragment);
                            break;
                        case R.id.nav_tentang:
                            setFregment(tentangFragment);
                            break;
                        case R.id.nav_batuan:
                            setFregment(bantuanFragment);
                            break;
                    }
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                });

                setFregment(notifikasiFragment);
                navigationView.setCheckedItem(R.id.nav_notifikasi);


            }
        }


    }

        private void updateToken () {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String refereshToken = task.getResult().getToken();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
                            Token token = new Token(refereshToken);
                            reference.child(USER_ID).setValue(token);
                        }
                    });
        }

        private void chackStatusUser () {
            DatabaseReference dosenRef = FirebaseDatabase.getInstance().getReference();

            FirebaseUser mUser = mAuth.getCurrentUser();
            assert mUser != null;
            String emailLogin = mUser.getEmail();

            Log.d("CEKDATA", emailLogin);

            dosenRef.child("Dosen").orderByChild("email_dosen").equalTo(emailLogin)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<Dosen> listDosen = new ArrayList<>();
                            for (DataSnapshot snapshotDosen : dataSnapshot.getChildren()) {
                                Dosen dosen = snapshotDosen.getValue(Dosen.class);
                                listDosen.add(dosen);
                                USER_ID = dosen.getKode_dosen();
                                NamaDosen = dosen.getNama_dosen();
                                tvEmailHeader.setText(dosen.getEmail_dosen());
                                tvNamaHeader.setText(dosen.getNama_dosen());
                                if (dosen.getFoto_dosen().equals("default")) {
                                    imgHeader.setImageResource(R.drawable.img_profile);
                                } else {
                                    Picasso.get().load(dosen.getFoto_dosen()).into(imgHeader);
                                }

                                updateToken();
                                Log.d("CEKDATA", dosen.getStatus());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

        }

    private void setFregment(Fragment fregment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fregment);
        fragmentTransaction.commit();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

