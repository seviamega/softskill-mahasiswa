package com.sidomafit.mahasiswa.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sidomafit.mahasiswa.model.Mahasiswa;
import com.sidomafit.mahasiswa.R;
import com.sidomafit.mahasiswa.notification.APIService;
import com.sidomafit.mahasiswa.notification.Client;
import com.sidomafit.mahasiswa.notification.Data;
import com.sidomafit.mahasiswa.notification.MyResponse;
import com.sidomafit.mahasiswa.notification.Sender;
import com.sidomafit.mahasiswa.notification.Token;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sidomafit.mahasiswa.MainActivity.USER_ID;

public class EditProfilMahasiswaActivity extends AppCompatActivity {

    private CircleImageView imgFoto;
    private EditText etTlp;
    private Button btnSimpan;
    private ImageView imgProfil;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private String kodeDosen;
    private String foto_mhs;
    private String namaDosenLogin;
    private APIService apiService;
    private boolean notify = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil_mahasiswa);
        setTitle(R.string.title_edit_profil);

        imgFoto = findViewById(R.id.img_edit_foto);
        imgProfil = findViewById(R.id.img_edit_profil);
        etTlp = findViewById(R.id.et_nohp_edit_profile);
        btnSimpan = findViewById(R.id.btn_simpan_edit_profile);

        mStorageRef = FirebaseStorage.getInstance().getReference("images");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Mahasiswa");

        tampilDataDosen();
        openGalery();
        saveData();

        apiService = Client.getClient("https://fcm.googleapis.com/", EditProfilMahasiswaActivity.this).create(APIService.class);
    }

    private void tampilDataDosen() {
        DatabaseReference dosenRef = FirebaseDatabase.getInstance().getReference();
        dosenRef.child("Mahasiswa").child(USER_ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Mahasiswa dosen = dataSnapshot.getValue(Mahasiswa.class);
                etTlp.setText(dosen.getNo_telepon());
                if (dosen.getFoto_mhs().equals("default")){
                    imgFoto.setImageResource(R.drawable.img_profile);
                }else {
                    Picasso.get().load(dosen.getFoto_mhs()).into(imgFoto);
                }
                kodeDosen = dosen.getKode_dosen();
                foto_mhs = dosen.getFoto_mhs();
                namaDosenLogin = dosen.getNama_mhs();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void openGalery() {
        imgProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
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

        DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        final String waktu = dateFormat.format(date);


        final String noTlp = etTlp.getText().toString();
        if (TextUtils.isEmpty(noTlp)){
            Toast.makeText(this, R.string.nomor_tidak_kosong, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }else {
            if (mImageUri != null){
                final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() +
                        "." + getFileExtension(mImageUri));
                fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (taskSnapshot.getMetadata() != null){
                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imgUrl = uri.toString();
                                    HashMap<String , Object> updateData = new HashMap<>();
                                    updateData.put("no_telepon", noTlp);

                                    mDatabaseRef.child(USER_ID).updateChildren(updateData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                finish();
                                                Toast.makeText(EditProfilMahasiswaActivity.this, R.string.berhasil_memperbarui, Toast.LENGTH_LONG).show();
                                                dialog.dismiss();
                                            }else {
                                                Toast.makeText(EditProfilMahasiswaActivity.this, R.string.gagal_memperbarui, Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }
                                    });

                                    DatabaseReference catatanRef = FirebaseDatabase.getInstance().getReference("Kesalahan");
                                    String idKesalahan = catatanRef.push().getKey();


                                    String kesalahan = "Mengganti foto profil...";
                                    HashMap<String , Object> addCatatan = new HashMap<>();
                                    addCatatan.put("id_kesalahan", idKesalahan);
                                    addCatatan.put("id_dosen_wali", kodeDosen);
                                    addCatatan.put("id_mahasiswa", "default");
                                    addCatatan.put("id_pengirim", USER_ID);
                                    addCatatan.put("pending_foto", imgUrl);
                                    addCatatan.put("nama_pelanggar", namaDosenLogin);
                                    addCatatan.put("nama_pengirim", namaDosenLogin);
                                    addCatatan.put("deskripsi_kesalahan", kesalahan);
                                    addCatatan.put("poin_kesalahan", "default");
                                    addCatatan.put("waktu_pengiriman", waktu);
                                    addCatatan.put("foto_mhs", foto_mhs);
                                    addCatatan.put("status", "terkirim");
                                    assert idKesalahan != null;
                                    catatanRef.child(idKesalahan).setValue(addCatatan);

                                    sendNotificationMahasiswa(kodeDosen, namaDosenLogin, kesalahan);
                                }
                            });
                        }
                    }
                });

            }else {
                HashMap<String , Object> updateData = new HashMap<>();
                updateData.put("no_telepon", noTlp);
                mDatabaseRef.child(USER_ID).updateChildren(updateData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            finish();
                        }else {
                            Toast.makeText(EditProfilMahasiswaActivity.this, R.string.gagal_memperbarui, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        }
    }


    private void saveData() {
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
                notify = true;
            }
        });
    }

    private void sendNotificationMahasiswa(String reciver, final String username, final String message ){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(reciver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(USER_ID, R.drawable.icon_dsn,  message, namaDosenLogin,
                            kodeDosen);

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNofirication(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1){
                                            Toast.makeText(EditProfilMahasiswaActivity.this,"Gagal", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
