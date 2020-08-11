package com.sidomafit.dosen.model.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sidomafit.dosen.model.SearchModel;

import java.util.ArrayList;
import java.util.List;

public class ViewModelSearch extends ViewModel {
    private MutableLiveData<List<SearchModel>> listkesalahan;

    public void getKeslahan(){
        DatabaseReference daftarkesalahanRef = FirebaseDatabase.getInstance().getReference();
        daftarkesalahanRef.child("DaftarKesalahan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<SearchModel> list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    SearchModel searchModel = snapshot.getValue(SearchModel.class);
                    list.add(searchModel);
                }
                listkesalahan.postValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void carKeslahan(final String kesalahan){
        DatabaseReference daftarkesalahanRef = FirebaseDatabase.getInstance().getReference();
        daftarkesalahanRef.child("DaftarKesalahan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<SearchModel> list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    SearchModel searchModel = snapshot.getValue(SearchModel.class);
                    if (searchModel.getKesalahan().toLowerCase().contains(kesalahan.toLowerCase())){
                        list.add(searchModel);
                    }
                }
                listkesalahan.postValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public MutableLiveData<List<SearchModel>> getListkesalahan(){
        if (listkesalahan == null){
            listkesalahan = new MutableLiveData<>();
            getKeslahan();
        }
        return listkesalahan;
    }
}
