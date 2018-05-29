package com.dental.anisandmahmmoud.dentalaandm;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import model.Patient;
import model.vistPatient;
import utils.AppService;

public class VisitActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private EditText et_Id_visit, et_Name_Patient, et_complaint, et_Diagnosis, et_Notes;
    private Calendar currentDay;
    private ImageButton btn_Save;
    private Context _cxt;
    private FirebaseFirestore firestoreDB;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private String patientName;
    private String doctorId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit);
         firestoreDB = FirebaseFirestore.getInstance();
        _cxt = this;
        progressDialog = new ProgressDialog(this);
//        et_Id_visit = (EditText) findViewById(R.id.et_Id_visit);
        et_complaint = (EditText) findViewById(R.id.et_complaint);
        et_Diagnosis = (EditText) findViewById(R.id.et_Diagnosis);
        et_Notes = (EditText) findViewById(R.id.et_Notes);
        btn_Save = (ImageButton) findViewById(R.id.btn_Save);
       /* Intent i= getIntent();
        String id1= i.getStringExtra("id1");
       */
       /* Bundle bundle = getIntent().getExtras();
        if(bundle.getString("id1")!= null) {
            Toast.makeText(VisitActivity.this, bundle.getString("id1"), Toast.LENGTH_SHORT).show();
        }*/

        /*Patient p = new Patient();
        String  patientName = p.getPatientName();
        Toast.makeText(this, patientName, Toast.LENGTH_SHORT).show();*/
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMdd:HH.mm.ss.SS");
        Date myDate = new Date();
        String filename = timeStampFormat.format(myDate);
        ///  Toast.makeText(this, myDate.toString(), Toast.LENGTH_SHORT).show();

        Intent i= getIntent();
        String id1= i.getStringExtra("id1");


        SharedPreferences prefs = getSharedPreferences(AppService.appkey, MODE_PRIVATE);
        String token = prefs.getString("token", "");

        //   Toast.makeText(_cxt, id1, Toast.LENGTH_SHORT).show();
        /*SharedPreferences prefs1 = getSharedPreferences(PalcoreStoreService.appkey, MODE_PRIVATE);
        String token1 = prefs1.getString("token1", "");
        Toast.makeText(this, token1, Toast.LENGTH_SHORT).show();*/
       /* Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();*/

        firestoreDB.collection("Users").document(token).collection("Patient").document(id1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            String TAG = "Patient";
                            if (document != null && document.exists()) {
                                patientName = document.get("patientName").toString();
                                doctorId= document.get("doctorId").toString();
                                Toast.makeText(VisitActivity.this, patientName, Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            String TAG = "Patient";
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                visitPatientt();
                finish();

                Intent visit = new Intent(_cxt, HomeActivity.class);
                startActivity(visit);
            }
        });
    }



    public void visitPatientt() {



        //-- init visit

        SharedPreferences prefs = getSharedPreferences(AppService.appkey, MODE_PRIVATE);
        final String token = prefs.getString("token", "");


        /*Intent intent = getIntent();
        String id = intent.getStringExtra("id");*/

        //adding visit to Patient to  db

         /*String value ;
         Bundle bundle = getIntent().getExtras();
         value = bundle.getString("id");
        Toast.makeText(this, value, Toast.LENGTH_SHORT).show();*/
       /* firestoreDB.collection("Users").document(token).collection("Patient").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                         String TAG = "Patient";*/

        firestoreDB = FirebaseFirestore.getInstance();
        //  mAuth = FirebaseAuth.getInstance();
        //Patient patient = new Patient();

        String vistID;
        final String patientProplem;
        final String patientDiagnosis;
        final String patientNote;
//                        vistID = et_Id_visit.getText().toString().trim();
//                        if (TextUtils.isEmpty(vistID)){
//                            Toast.makeText(this, "الرجاء ادخال رقم الزيارة !", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
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
                               patientName = document.get("patientName").toString();

                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            String TAG = "Patient";
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
        patientProplem = et_complaint.getText().toString().trim();
        patientDiagnosis = et_Diagnosis.getText().toString().trim();
        patientNote = et_Notes.getText().toString().trim();

        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
        // SimpleDateFormat timeStampFormat = new SimpleDateFormat("DD-MM-YYYY");
        final Date myDate = new Date();
        String filename = timeStampFormat.format(myDate);





        //myDate.setTime(i.getLongExtra("Date",-1));


        //   Toast.makeText(this, myDate.toString(), Toast.LENGTH_SHORT).show();

        //patientName = et_Name_Patient.getText().toString().trim();


        final vistPatient vistPatient = new vistPatient(patientName,patientProplem,patientDiagnosis,patientNote,myDate);


        //   Toast.makeText(_cxt, id1, Toast.LENGTH_SHORT).show();
        progressDialog.setMessage("يتم إضافة الزيارة الاولى ....  " );
        progressDialog.show();

        firestoreDB.collection("Users").document(token).collection("Patient").document(id1)
                .collection("visitPatient")
                .add(vistPatient)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progressDialog.dismiss();
                        String visitRef = documentReference.getId();
                        SharedPreferences.Editor editor = getSharedPreferences(AppService.appkey, MODE_PRIVATE).edit();
                        editor.putString("tVisitRef",visitRef );
                        editor.apply();

                        final vistPatient vistPatient1 = new vistPatient(visitRef,doctorId,patientName,patientProplem,patientDiagnosis,patientNote,myDate);

                        firestoreDB.collection("VisitList").document()
                                .set(vistPatient1)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(VisitActivity.this, "تم اضافة الزيارة ", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(VisitActivity.this, "خطأ اعد المحاولة ...", Toast.LENGTH_SHORT).show();
            }
        });





                        /*firestoreDB.collection("Users").document(token).collection("Patient").document(id1)
                                .collection("visitPatient").document().set(vistPatient)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        Toast.makeText(VisitActivity.this, "تم اضافة الزيارة ", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(VisitActivity.this, "خطأ اعد المحاولة ...", Toast.LENGTH_SHORT).show();
                            }
                        });*/
    }

    @Override
    public void onBackPressed() {
        Intent visit = new Intent(_cxt, HomeActivity.class);
        startActivity(visit);
    }

    public void addNextVisit(View view) {

        currentDay = Calendar.getInstance();
        int year = currentDay.get(Calendar.YEAR);
        int month = currentDay.get(Calendar.MONTH);
        int day = currentDay.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog= new DatePickerDialog(this,this,year,month,day);
        datePickerDialog.show();


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //   Toast.makeText(this, year+"/"+(month+1)+"/"+dayOfMonth, Toast.LENGTH_SHORT).show();

        currentDay.set(year,month,dayOfMonth);

    }
}








