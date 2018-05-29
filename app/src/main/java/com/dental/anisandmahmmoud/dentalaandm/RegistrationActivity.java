package com.dental.anisandmahmmoud.dentalaandm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.AppService;

public class RegistrationActivity extends AppCompatActivity {
    private EditText et_email,et_password;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        et_email = (EditText)findViewById(R.id.et_email);
        et_password = (EditText)findViewById(R.id.et_password);
    }

    public Boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-])*@[A-Za-z0-9]+(\\.[A-Za-z0-9])*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }



    public void registerUser(){
        //  firebaseAuth = FirebaseAuth.getInstance();
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

        firebaseAuth.createUserWithEmailAndPassword(Email,Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            //  Toast.makeText(MainActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = getSharedPreferences(AppService.appkey, MODE_PRIVATE).edit();
                            editor.putString("token", firebaseAuth.getUid());
                            editor.apply();
                            finish();
                            startActivity(new Intent(getApplicationContext(),DocDataActivity.class));

                        }else {
                            Toast.makeText(RegistrationActivity.this, "لا يمكن اتمام العملية الرجاء المحاولة مرة اخرى ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
    public void regDoctor(View view) {



        registerUser();


    }
}
