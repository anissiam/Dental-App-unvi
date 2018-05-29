package com.dental.anisandmahmmoud.dentalaandm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
//import com.tuyenmonkey.mkloader.model.Line;

import java.util.concurrent.TimeUnit;

import utils.AppService;

public class LoginPatientActivity extends AppCompatActivity {
    private static final String TAG = "PhoneAuthActivity";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;
    private boolean mVerificationInProgress = false;

    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    private FirebaseAuth fbAuth;
    private FirebaseFirestore firestoreDB;
    private LinearLayout loadingProgress;
    private LinearLayout verifyLayout;
    private LinearLayout inputCodeLayout;

    private EditText phoneText;
    //private EditText codeText;
    private EditText e1,e2,e3,e4,e5,e6;
    private Button verifyButton;
    private Button sendButton;
    private TextView resendButton;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_patient);

        phoneText = (EditText) findViewById(R.id.et_phone);
        //codeText = (EditText) findViewById(R.id.e1);
        verifyButton = (Button) findViewById(R.id.btn_login);
        sendButton = (Button) findViewById(R.id.btn_sendCode);
        resendButton = (TextView) findViewById(R.id.btn_resendCode);
        e1 = (EditText) findViewById(R.id.e1);
        e2 = (EditText) findViewById(R.id.e2);
        e3 = (EditText) findViewById(R.id.e3);
        e4 = (EditText) findViewById(R.id.e4);
        e5 = (EditText) findViewById(R.id.e5);
        e6 = (EditText) findViewById(R.id.e6);


        // [START initialize_auth]
        fbAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        progressDialog = new ProgressDialog(this);

        fbAuth = FirebaseAuth.getInstance();

        if (fbAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), ProfilePatientPatientActivity.class));
        }


    }
    public void sendCode(View view) {
        String phoneNumberr = phoneText.getText().toString();
        String phoneNumber = "+97"+phoneNumberr;
        firestoreDB = FirebaseFirestore.getInstance();
        firestoreDB.collection("VisitList")
                .whereEqualTo("patientPhone", phoneNumber)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                String patientId = document.getString("patientId");
                                SharedPreferences.Editor editor = getSharedPreferences(AppService.appkey, MODE_PRIVATE).edit();
                                editor.putString("patientId", patientId);

                                Toast.makeText(LoginPatientActivity.this, "==>" + patientId, Toast.LENGTH_SHORT).show();
                                String doctorId = document.getString("doctorId");
                                editor.putString("doctorId", doctorId);
                                editor.apply();
                                Toast.makeText(LoginPatientActivity.this, doctorId, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d("copy id", "Error getting documents: ", task.getException());
                        }
                    }
                });

        setUpVerificatonCallbacks();
        progressDialog.setMessage("يتم ارسال رقم التحقق");
        progressDialog.show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                verificationCallbacks);
    }
    private void setUpVerificatonCallbacks() {

        verificationCallbacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(
                            PhoneAuthCredential credential) {

                        /*resendButton.setEnabled(false);
                        verifyButton.setEnabled(false);
                        codeText.setText("");*/
                        signInWithPhoneAuthCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                            Log.d(TAG, "Invalid credential: "
                                    + e.getLocalizedMessage());
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // SMS quota exceeded
                            Log.d(TAG, "SMS Quota exceeded.");
                        }
                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {

                        phoneVerificationId = verificationId;
                        resendToken = token;

                        /*verifyButton.setEnabled(true);
                        sendButton.setEnabled(false);
                        resendButton.setEnabled(true);*/
                    }
                };
    }

    public void verifyCode(View view) {
       String n1 =e1.getText().toString();
       String n2 =e2.getText().toString();
       String n3 =e3.getText().toString();
       String n4 =e4.getText().toString();
       String n5 =e5.getText().toString();
       String n6 =e6.getText().toString();
       String code = n1+n2+n3+n4+n5+n6;

        PhoneAuthCredential credential =
                PhoneAuthProvider.getCredential(phoneVerificationId, code);
        signInWithPhoneAuthCredential(credential);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        fbAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Intent intent = new Intent(LoginPatientActivity.this,ProfilePatientPatientActivity.class);
                            startActivity(intent);
                            FirebaseUser user = task.getResult().getUser();


                            Toast.makeText(LoginPatientActivity.this, user.getPhoneNumber(), Toast.LENGTH_SHORT).show();
                        } else {
                            if (task.getException() instanceof
                                    FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(LoginPatientActivity.this,
                                        "تأكد من رمز التحقق ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    public void resendCode(View view) {
        String phoneNumberr = phoneText.getText().toString();
        String phoneNumber = "+97"+phoneNumberr;

        setUpVerificatonCallbacks();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                verificationCallbacks,
                resendToken);
    }
}
