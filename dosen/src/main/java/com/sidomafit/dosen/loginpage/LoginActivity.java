package com.sidomafit.dosen.loginpage;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.sidomafit.dosen.MainActivity;
import com.sidomafit.dosen.R;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    private Button btnGoogle;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private EditText etEmail, etPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnGoogle = findViewById(R.id.btn_google);
        Button btnLogin = findViewById(R.id.btn_login);
        etEmail = findViewById(R.id.et_email_login);
        etPassword = findViewById(R.id.et_password_login);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        loginUsingGoogle();


        btnLogin.setOnClickListener(v -> loginUsingEmail());


    }



    private void loginUsingGoogle() {
        btnGoogle.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Gagal Masuk Dengan Google", e);
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:Berhasil");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(LoginActivity.this, user.getEmail(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                       // updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:Gagal", task.getException());
                        Toast.makeText(LoginActivity.this, R.string.gagal_login, Toast.LENGTH_SHORT).show();
                        //updateUI(null);
                    }

                    // ...
                }).addOnFailureListener(e -> Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void loginUsingEmail() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.wait));
        dialog.show();

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(this, R.string.tidak_valid, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                    Toast.makeText(LoginActivity.this, R.string.berhasil_login, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }else {
                    Toast.makeText(LoginActivity.this, R.string.gagal_login, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user;
        user = mAuth.getCurrentUser();

        if (user !=null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }else {
            Toast.makeText(this, R.string.silahkan_login, Toast.LENGTH_SHORT).show();
        }
    }
}
