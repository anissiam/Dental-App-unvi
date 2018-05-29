package com.dental.anisandmahmmoud.dentalaandm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.onesignal.OneSignal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.AppService;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 1001;

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText et_email , et_password;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore firestoreDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

    et_email = (EditText)findViewById(R.id.et_email) ;
    et_password = (EditText)findViewById(R.id.et_password) ;
    progressDialog=new ProgressDialog(this);
//        FacebookSdk.sdkInitialize(getApplicationContext());


    TextView tv_SignUp = (TextView)findViewById(R.id.tv_SignUp);

        tv_SignUp.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(getApplicationContext(),RegistrationActivity.class);
            startActivity(intent);

        }
    });

        /*FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton mFacebookSignInButton = (LoginButton) findViewById(R.id.connectWithFbButton);*/
    // Facebook LoginActivity
    firestoreDB = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        if (mAuth.getCurrentUser() != null) {
        finish();
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }


    //LoginButton  mFacebookSignInButton = (LoginButton) findViewById(R.id.connectWithFbButton);
//        LoginButton mFacebookSignInButton =findViewById(R.id.connectWithFbButton);
//        mFacebookSignInButton.setReadPermissions("email", "public_profile", "user_birthday");
    // mFacebookSignInButton.setReadPermissions("email", "public_profile", "user_birthday", "user_friends");
//        mCallbackManager = CallbackManager.Factory.create();
//        mFacebookSignInButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Log.d(TAG, "facebook:onSuccess:" + loginResult);
//                firebaseAuthWithFacebook(loginResult.getAccessToken());
//            }
//
//            @Override
//            public void onCancel() {
//                Log.d(TAG, "facebook:onCancel");
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Log.d(TAG, "facebook:onError", error);
//            }
//        });

    // Google Sign-In
    // Assign fields
    RelativeLayout mGoogleSignInButton = (RelativeLayout) findViewById(R.id.relative_google);

    // Set click listeners


        mGoogleSignInButton.setOnClickListener(this);

    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
            .enableAutoManage(this  ,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    // Initialize FirebaseAuth
    // mAuth = FirebaseAuth.getInstance();

    mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                // User is signed out
                Log.d(TAG, "onAuthStateChanged:signed_out");
            }
        }
    };


}


    public Boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-])*@[A-Za-z0-9]+(\\.[A-Za-z0-9])*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    ///firebase Auth with Email
    private void UserLogin() {
        mAuth = FirebaseAuth.getInstance();
        String Email = et_email.getText().toString().trim();
        String Password= et_password.getText().toString().trim();
        if (TextUtils.isEmpty(Email)){
            et_email.setError("أدخل عنوان البريد الإلكتروني");
        }else if (!emailValidator(et_email.getText().toString())) {
            et_email.setError("أدخل عنوان البريد الإلكتروني بشكل صحيح");
        }
        if (TextUtils.isEmpty(Password)){
            et_password.setError("أدخل كلمة السر");
            return;
        }
        progressDialog.setMessage("Registration User ...");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(Email,Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            SharedPreferences.Editor editor = getSharedPreferences(AppService.appkey, MODE_PRIVATE).edit();
                            editor.putString("token", mAuth.getUid());
                            editor.apply();
                            finish();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        }
                    }
                });
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGooogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            SharedPreferences.Editor editor = getSharedPreferences(AppService.appkey, MODE_PRIVATE).edit();
                            editor.putString("token", mAuth.getUid());
                            editor.apply();
                            firestoreDB.collection("Users").document(mAuth.getUid())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document != null && document.exists()) {
                                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                                    finish();
                                                } else {
                                                    startActivity(new Intent(LoginActivity.this, DocDataActivity.class));
                                                    finish();
                                                    Log.d(TAG, "No such document");
                                                }
                                            } else {
                                                String TAG = "docView";
                                                Log.d(TAG, "get failed with ", task.getException());
                                            }
                                        }
                                    });


                            /*startActivity(new Intent(Login_new_Activity.this, HomeActivity.class));
                            finish();*/
                        }
                    }
                });
                             /* firestoreDB.collection("Users").document(mAuth.getUid())
                                      .get()
                                      .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                          @Override
                                          public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                              if (task.isSuccessful()) {
                                                  DocumentSnapshot document = task.getResult();
                                                  String TAG = "docView";
                                                  if (document != null && document.exists()) {
                                                     String name= document.get("doctorName").toString();
                                                     String doctorEmail = document.get("doctorEmail").toString();
                                                     String clinicPhone = document.get("clinicPhone").toString();
                                                     String clinicName = document.get("clinicName").toString();
                                                     String clinicAddress = document.get("clinicAddress").toString();
                                                      Doctor doctor = new Doctor(clinicName, name, clinicAddress, clinicPhone, doctorEmail);
                                                   //   document.get("doctorName").toString();
                                                     // startActivity(new Intent(Login_new_Activity.this, HomeActivity.class));
                                                      //--adding to SharedPreferences

                                                        firestoreDB.collection("Users").document(mAuth.getUid())
                                                               .set(doctor)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                    //    Toast.makeText(Login_new_Activity.this, "success", Toast.LENGTH_SHORT).show();

                                                                        startActivity(new Intent(Login_new_Activity.this, HomeActivity.class));

                                                                    }
                                                                });

                                                  } else {
                                                      startActivity(new Intent(Login_new_Activity.this, DocDataActivity.class));
                                                      finish();
                                                      Log.d(TAG, "No such document");
                                                  }
                                              } else {
                                                  String TAG = "docView";
                                                  Log.d(TAG, "get failed with ", task.getException());
                                              }
                                          }
                                      });
                        }
                    }
                });*/ }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_google:
                signIn();
                break;
            default:
                return;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //  mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed
                Log.e(TAG, "Google Sign In failed.");
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    public void newAccount(View view) {


    }

    public void loginWithEmail(View view) {
        UserLogin();
    }

    public void resetPasss(View view) {
        startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}








