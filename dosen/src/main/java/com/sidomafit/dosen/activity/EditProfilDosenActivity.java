package com.sidomafit.dosen.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sidomafit.dosen.model.Dosen;
import com.sidomafit.dosen.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sidomafit.dosen.MainActivity.USER_ID;

public class EditProfilDosenActivity extends AppCompatActivity {

    private CircleImageView imgFoto;
    private EditText etTlp;
    private Button btnSimpan;
    private ImageView imgProfil;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil_dosen);
        setTitle(R.string.title_edit_profil);

        imgProfil = findViewById(R.id.img_edit_profil);
        imgFoto = findViewById(R.id.img_edit_foto);
        etTlp = findViewById(R.id.et_nohp_edit_profile);
        btnSimpan = findViewById(R.id.btn_simpan_edit_profile);

        mStorageRef = FirebaseStorage.getInstance().getReference("images");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Dosen");

        tampilDataDosen();
        openGalery();
        saveData();
    }

    private void tampilDataDosen() {
        DatabaseReference dosenRef = FirebaseDatabase.getInstance().getReference();
        dosenRef.child("Dosen").child(USER_ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Dosen dosen = dataSnapshot.getValue(Dosen.class);
                etTlp.setText(dosen.getNo_telepon());
                if (dosen.getFoto_dosen().equals("default")){
                    imgFoto.setImageResource(R.drawable.img_profile);
                }else {
                    Picasso.get().load(dosen.getFoto_dosen()).into(imgFoto);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void openGalery() {
        imgProfil.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() !=null){
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(imgFoto);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void updateProfile(){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.wait));
        dialog.show();

        final String noTlp = etTlp.getText().toString();
        if (TextUtils.isEmpty(noTlp)){
            Toast.makeText(this, R.string.nomor_tidak_kosong, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }else {
            if (mImageUri != null){
                final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() +
                        "." + getFileExtension(mImageUri));
                fileReference.putFile(mImageUri).addOnSuccessListener(taskSnapshot -> {
                    if (taskSnapshot.getMetadata() != null){
                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                        result.addOnSuccessListener(uri -> {
                            String imgUrl = uri.toString();
                            HashMap<String , Object> updateData = new HashMap<>();
                            updateData.put("foto_dosen", imgUrl);
                            updateData.put("no_telepon", noTlp);
                            mDatabaseRef.child(USER_ID).updateChildren(updateData).addOnCompleteListener(task -> {
                                if (task.isSuccessful()){
                                    startActivity(new Intent(EditProfilDosenActivity.this, ProfilDosenActivity.class));
                                    finish();
                                    Toast.makeText(EditProfilDosenActivity.this, R.string.berhasil_memperbarui, Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }else {
                                    Toast.makeText(EditProfilDosenActivity.this, R.string.gagal_memperbarui, Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });
                        });
                    }
                });

            }else {
                HashMap<String , Object> updateData = new HashMap<>();
                updateData.put("no_telepon", noTlp);
                mDatabaseRef.child(USER_ID).updateChildren(updateData).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        startActivity(new Intent(EditProfilDosenActivity.this, ProfilDosenActivity.class));
                        finish();
                    }else {
                        Toast.makeText(EditProfilDosenActivity.this, R.string.gagal_memperbarui, Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.dismiss();
            }
        }
    }

    private void saveData() {
        btnSimpan.setOnClickListener(v -> updateProfile());
    }
}
