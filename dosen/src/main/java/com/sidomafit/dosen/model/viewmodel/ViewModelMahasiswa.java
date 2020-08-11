package com.sidomafit.dosen.model.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sidomafit.dosen.model.Dosen;
import com.sidomafit.dosen.model.Mahasiswa;

import java.util.ArrayList;
import java.util.List;

public class ViewModelMahasiswa extends ViewModel {
    private MutableLiveData<List<Mahasiswa>> listMahasiswa;

    private void getAnakWali(){

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dosenRef = FirebaseDatabase.getInstance().getReference().child("Dosen");
        dosenRef.orderByChild("email_dosen").equalTo(currentUser.getEmail()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<Dosen> dosenList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Dosen dosen = snapshot.getValue(Dosen.class);
                    dosenList.add(dosen);
                    String kodeDosen = dosen.getKode_dosen();

                    DatabaseReference mahasiswaRef = FirebaseDatabase.getInstance().getReference().child("Mahasiswa");
                    mahasiswaRef.orderByChild("kode_dosen").equalTo(kodeDosen).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final List<Mahasiswa> mahasiswaList = new ArrayList<>();
                            for (DataSnapshot snapshotMhs : dataSnapshot.getChildren()){
                                Mahasiswa mahasiswa = snapshotMhs.getValue(Mahasiswa.class);
                                mahasiswaList.add(mahasiswa);
                            }
                            listMahasiswa.postValue(mahasiswaList);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getAllMahasiswa() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Mahasiswa").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<Mahasiswa> list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Mahasiswa mahasiswa = snapshot.getValue(Mahasiswa.class);
                    list.add(mahasiswa);
                }
                listMahasiswa.postValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void cariMahasiswa(final String name) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Mahasiswa").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<Mahasiswa> list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Mahasiswa mahasiswa = snapshot.getValue(Mahasiswa.class);
                    if (mahasiswa.getNama_mhs().toLowerCase().contains(name.toLowerCase()) ||
                            mahasiswa.getNim().toLowerCase().contains(name.toLowerCase())){
                        list.add(mahasiswa);
                    }
                }
                listMahasiswa.postValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public MutableLiveData<List<Mahasiswa>> getAllListMahasiswa() {

        if (listMahasiswa == null){
            listMahasiswa = new MutableLiveData<>();
            getAllMahasiswa();
        }
        return listMahasiswa;
    }



    public MutableLiveData<List<Mahasiswa>> getAllAnakWali() {

        if (listMahasiswa == null){
            listMahasiswa = new MutableLiveData<>();
            getAnakWali();
        }
        return listMahasiswa;
    }
}
