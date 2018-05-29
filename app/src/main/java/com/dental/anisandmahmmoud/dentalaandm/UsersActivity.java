package com.dental.anisandmahmmoud.dentalaandm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class UsersActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        if (mAuth.getCurrentUser() != null &&   user.getPhoneNumber().isEmpty() ) {
            finish();
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }else if (mAuth.getCurrentUser() != null && user.getEmail().isEmpty() ){
            finish();
            startActivity(new Intent(getApplicationContext(), ProfilePatientPatientActivity.class));
        }
    }

    public void doctor(View view) {

        Intent intent=new Intent(UsersActivity.this,LoginActivity.class);
        startActivity(intent);

    }

    public void patient(View view) {

        Intent intent=new Intent(UsersActivity.this,LoginPatientActivity.class);
        startActivity(intent);
    }
}
