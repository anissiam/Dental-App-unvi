package com.dental.anisandmahmmoud.dentalaandm;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText et_Email;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        et_Email = (EditText)findViewById(R.id.et_Emaill);
        auth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);

    }
    public void reset(){
        String Email = et_Email.getText().toString().trim();
        if (TextUtils.isEmpty(Email)) {
            Toast.makeText(this, "الرجاء ادخال البريد الالكتروني !!", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("يتم ارسال الرسالة الرجاء التحقق من بريدك الالكتروني ...");
        progressDialog.show();
        auth.sendPasswordResetEmail(Email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            //Toast.makeText(ResetPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ResetPasswordActivity.this, "خطأ الرجاء التأكد من بريدك الالكتروني  ...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });





    }
    public void resetPass(View view) {
        reset();
    }
}

