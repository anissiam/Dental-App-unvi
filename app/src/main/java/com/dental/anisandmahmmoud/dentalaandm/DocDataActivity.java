package com.dental.anisandmahmmoud.dentalaandm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import model.Doctor;
import utils.AppService;

public class DocDataActivity extends AppCompatActivity {
    ImageButton btn_Save;
    EditText et_Clinic_Name, et_nameDoc, et_FullAdderss, et_Tele, et_Email;
    Context _cxt;
    FirebaseFirestore firestoreDB;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_data);
        _cxt = this;
        et_Clinic_Name = (EditText) findViewById(R.id.et_Clinic_Name);
        et_nameDoc = (EditText) findViewById(R.id.et_nameDoc);
        et_FullAdderss = (EditText) findViewById(R.id.et_FullAdderss);
        et_Tele = (EditText) findViewById(R.id.et_Tele);
        et_Email = (EditText) findViewById(R.id.et_Email);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        firestoreDB = FirebaseFirestore.getInstance();

//        assert user != null;
        et_Email.setText(user.getEmail());
        et_Tele.setText(user.getPhoneNumber() );
        et_nameDoc.setText(user.getDisplayName());
        btn_Save = (ImageButton) findViewById(R.id.btn_Save);
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddClinicInfo();
                Intent reg = new Intent(_cxt, HomeActivity.class);
                startActivity(reg);
            }
        });
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void AddClinicInfo() {
        String clinicName, doctorName, clinicAddress, clinicPhone, doctorEmail;
        clinicName = et_Clinic_Name.getText().toString().trim();
        doctorName = et_nameDoc.getText().toString().trim();
        clinicAddress = et_FullAdderss.getText().toString().trim();
        clinicPhone = et_Tele.getText().toString().trim();
        doctorEmail = et_Email.getText().toString().trim();

        //--Get Firebase Authentication Instance
        mAuth = FirebaseAuth.getInstance();
        firestoreDB = FirebaseFirestore.getInstance();

        //--initialize Doctor
        Doctor doctor = new Doctor(clinicName, doctorName, clinicAddress, clinicPhone, doctorEmail);

        SharedPreferences.Editor editor = getSharedPreferences(AppService.appkey, MODE_PRIVATE).edit();
        editor.putString("token", mAuth.getUid());
        editor.apply();

        //--Adding user to DB
        firestoreDB.collection("Users").document(mAuth.getUid()).set(doctor).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(_cxt, "تم الإضافة...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
