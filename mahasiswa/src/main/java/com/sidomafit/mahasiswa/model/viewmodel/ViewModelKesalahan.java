package com.sidomafit.mahasiswa.model.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sidomafit.mahasiswa.model.Kesalahan;

import java.util.ArrayList;
import java.util.List;

public class ViewModelKesalahan extends ViewModel {
    private MutableLiveData<List<Kesalahan>> listkesalahan;

    private void getKeslahan(String nim){
        DatabaseReference daftarkesalahanRef = FirebaseDatabase.getInstance().getReference();
        daftarkesalahanRef.child("Kesalahan").orderByChild("id_mahasiswa").equalTo(nim).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Kesalahan> list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Kesalahan kesalahan = snapshot.getValue(Kesalahan.class);
                    list.add(kesalahan);
                }
                listkesalahan.postValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public MutableLiveData<List<Kesalahan>> getKesalahankesalahan(String nim){
        if (listkesalahan == null){
            listkesalahan = new MutableLiveData<>();
            getKeslahan(nim);
        }
        return listkesalahan;
    }
}
