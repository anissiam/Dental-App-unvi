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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

import model.Problem;
import model.VisitPatient;
import utils.AppService;

public class ProplemActivity extends AppCompatActivity {
    private EditText et_complaint,et_Place,et_Notes,et_Payment;
    private FirebaseFirestore firestoreDB;
    private ProgressDialog progressDialog;
    private ImageButton btn_Save;
    private Context _cxt;
    private String patientName;
    private String doctorId;
    private String patientId;
    private String patientPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proplem);
        et_complaint = (EditText)findViewById(R.id.et_complaint);
        et_Place = (EditText)findViewById(R.id.et_Place);
        et_Notes = (EditText)findViewById(R.id.et_Notes);
        et_Payment = (EditText)findViewById(R.id.et_Payment);

        firestoreDB = FirebaseFirestore.getInstance();
       // progressDialog = new ProgressDialog(this);

        //-- get Doc id
        SharedPreferences prefs = getSharedPreferences(AppService.appkey, MODE_PRIVATE);
        final String token = prefs.getString("token", "");

        //-- getPatient ID
        Intent i= getIntent();
        String id1= i.getStringExtra("id1");


        firestoreDB.collection("Users").document(token).collection("Patient").document(id1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            String TAG = "Patient";
                            if (document != null && document.exists()) {
                                patientId = document.getId();
                                patientPhone = document.get("p_phone").toString();
                                patientName = document.get("patientName").toString();
                                doctorId= document.get("doctorId").toString();
                                Toast.makeText(ProplemActivity.this, patientName, Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            String TAG = "Patient";
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });


                //finish();

               /* Intent visit = new Intent(_cxt, HomeActivity.class);
                startActivity(visit);*/


    }

    public void AddPrblem(){
        String ProProblem, ProPayment,ProPlace ,ProNote;
        ProProblem = et_complaint.getText().toString().trim();
        ProPayment = et_Payment.getText().toString().trim();
        ProPlace = et_Place.getText().toString().trim();
        ProNote = et_Notes.getText().toString().trim();


        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
        // SimpleDateFormat timeStampFormat = new SimpleDateFormat("DD-MM-YYYY");
        final Date myDate = new Date();
        String filename = timeStampFormat.format(myDate);

        //-- get Doc id
        final SharedPreferences prefs = getSharedPreferences(AppService.appkey, MODE_PRIVATE);
        final String token = prefs.getString("token", "");

        //--init firebase batabase
        firestoreDB = FirebaseFirestore.getInstance();

        //-- getPatient ID
        Intent i= getIntent();
        final String id1= i.getStringExtra("id1");
        Toast.makeText(this, id1, Toast.LENGTH_SHORT).show();
        //-- inti Problem
         final Problem problem = new Problem(ProProblem ,ProPayment,ProPlace,ProNote,myDate,patientName);
         //--Init progressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("يتم إضافة الزيارة الاولى  والشكوى ....  " );
        progressDialog.show();
        firestoreDB.collection("Users").document(token).collection("Patient").document(id1)
                .collection("Problem")
                .add(problem)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                   @Override
                   public void onSuccess(DocumentReference documentReference) {
                      // progressDialog.dismiss();
                       final String problemRef = documentReference.getId();
                       final String PaymentDone = "000";
                       final String VisitNote = " ";
                       VisitPatient visitPatient = new VisitPatient(doctorId,patientName,problemRef,
                               PaymentDone,myDate,VisitNote);
                       firestoreDB.collection("Users").document(token).collection("Patient").document(id1)
                               .collection("Problem").document(problemRef)
                               .collection("visitPatient").add(visitPatient)
                               .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                   @Override
                                   public void onSuccess(DocumentReference documentReference) {
                                       final String visitRref = documentReference.getId();
                                       VisitPatient visitPatient = new VisitPatient(visitRref,doctorId,patientName,problemRef,
                                               PaymentDone,VisitNote,patientId,patientPhone,myDate);
                                       firestoreDB.collection("VisitList")
                                                .add(visitPatient)
                                               .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                   @Override
                                                   public void onSuccess(DocumentReference documentReference) {
                                                       final String visitRref_copy = documentReference.getId();
                                                       firestoreDB.collection("VisitList").document(visitRref_copy)
                                                               .update("visit_id_copy",visitRref_copy)
                                                               .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                   @Override
                                                                   public void onSuccess(Void aVoid) {
                                                                       Toast.makeText(ProplemActivity.this, "تم إضافة الشكوى و الزيارة الاولى ",
                                                                               Toast.LENGTH_SHORT).show();
                                                                   }
                                                               });
                                                   }
                                               });
                                   }
                               });

                   }
               }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProplemActivity.this, "هنالك خطأ حاول مرة اخرى .!!!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void addProblem(View view) {
        AddPrblem();
        finish();
        Intent visit = new Intent(ProplemActivity.this, HomeActivity.class);
        startActivity(visit);
    }

    @Override
    public void onBackPressed() {
        Intent visit = new Intent(ProplemActivity.this, HomeActivity.class);
        startActivity(visit);
    }
}
