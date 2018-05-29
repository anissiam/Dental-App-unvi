package com.dental.anisandmahmmoud.dentalaandm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Doctor;
import utils.AppService;

public class Activity_View_Doc extends AppCompatActivity {
    private ImageButton btn_delete , btn_edit ;
    private CircleImageView img_dc_data_view ;
    private EditText et_Clinic_Name , et_nameDoc , et_FullAdderss , et_Tele , et_Email ;
    private FirebaseFirestore firebaseFirestore;
    private ProgressDialog dialog;
    Context _cxt;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__view__doc);
        firebaseFirestore = FirebaseFirestore.getInstance();
        _cxt = this;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        et_Clinic_Name = (EditText) findViewById(R.id.et_Clinic_Name);
        et_nameDoc = (EditText) findViewById(R.id.et_nameDoc);
        et_FullAdderss = (EditText) findViewById(R.id.et_FullAdderss);
        et_Tele = (EditText) findViewById(R.id.et_Tele);
        et_Email = (EditText) findViewById(R.id.et_Email);
        btn_edit =(ImageButton) findViewById(R.id.btn_edit);
        //btn_delete=(ImageButton) findViewById(R.id.btn_delete);
        img_dc_data_view=(CircleImageView) findViewById(R.id.img_dc_data_view);

        if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()!=null) {
            String url = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();
            Glide.with(Activity_View_Doc.this).load(url).into(img_dc_data_view);
        }
        /*String url= FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();
        Glide.with(Activity_View_Doc.this).load(url).into(img_dc_data_view);*/
        fetchDocData();
    }

    private void fetchDocData() {
        et_Clinic_Name = (EditText) findViewById(R.id.et_Clinic_Name);
        et_nameDoc = (EditText) findViewById(R.id.et_nameDoc);
        et_FullAdderss = (EditText) findViewById(R.id.et_FullAdderss);
        et_Tele = (EditText) findViewById(R.id.et_Tele);
        et_Email = (EditText) findViewById(R.id.et_Email);
        btn_edit =(ImageButton) findViewById(R.id.btn_edit);
        //btn_delete=(ImageButton) findViewById(R.id.btn_delete);
        img_dc_data_view=(CircleImageView) findViewById(R.id.img_dc_data_view);
        dialog = new ProgressDialog(this);
        //--Get Firebase Authentication Instance
        firebaseFirestore  = FirebaseFirestore.getInstance();

        SharedPreferences prefs = _cxt.getSharedPreferences(AppService.appkey, MODE_PRIVATE);
        String token = prefs.getString("token", "");

        //--GET COLLECTION data and store it into docView
        firebaseFirestore.collection("Users").document(token)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            String TAG = "docView";
                            if (document != null && document.exists()) {

                                String DocName=  document.get("doctorName").toString();
                                et_nameDoc.setText(DocName);

                                String  doctorEmail = document.get( "doctorEmail").toString();
                                et_Email.setText(doctorEmail );

                                String  clinicPhone = document.get( "clinicPhone").toString();
                                et_Tele.setText(clinicPhone );

                                String  clinicName = document.get( "clinicName").toString();
                                et_Clinic_Name.setText(clinicName );

                                String  clinicAddress = document.get( "clinicAddress").toString();
                                et_FullAdderss.setText(clinicAddress );

                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            String TAG = "docView";
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });



    }

    public void editDocData(View view) {

        String clinicName, doctorName, clinicAddress, clinicPhone, doctorEmail;
        clinicName = et_Clinic_Name.getText().toString().trim();
        doctorName = et_nameDoc.getText().toString().trim();
        clinicAddress = et_FullAdderss.getText().toString().trim();
        clinicPhone = et_Tele.getText().toString().trim();
        doctorEmail = et_Email.getText().toString().trim();
        dialog = new ProgressDialog(this);
        //--Get Firebase Authentication Instance
        firebaseFirestore  = FirebaseFirestore.getInstance();

        SharedPreferences prefs = _cxt.getSharedPreferences(AppService.appkey, MODE_PRIVATE);
        String token = prefs.getString("token", "");
        Doctor doctor = new Doctor(clinicName, doctorName, clinicAddress, clinicPhone, doctorEmail);

        dialog.setMessage("يتم تعديل البيانات  ...");
        dialog.show();
        firebaseFirestore.collection("Users").document(token).set(doctor).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialog.dismiss();
                finish();
                startActivity(new Intent(Activity_View_Doc.this,Profile_Doc_Activity.class));
                //  Toast.makeText(Activity_View_Doc.this, "Update Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
//
                Toast.makeText(Activity_View_Doc.this, "خطأ بالتعديل حاول مرة اخرى ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

