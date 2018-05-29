package com.dental.anisandmahmmoud.dentalaandm;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;

import model.VisitPatient;
import model.vistPatient;
import utils.AppService;

public class EditVisitActivity extends AppCompatActivity {
    private EditText et_after,et_before,et_PaymentDone,et_all,et_Notes;
    private Calendar currentDay;
    private ImageButton btn_Save;
    private Context _cxt;
    FirebaseFirestore firestoreDB;
    private FirebaseAuth mAuth;
    private TextView tv_visitDatee,tv_visitTimee;
    private ProgressDialog progressDialog;
    private String patientName;
    private String doctorId;
    private String problemId;
    private String paymentTotal;
    private Date date,visitDate;
    private String visit_id_copy;
    Calendar date1;
    double p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_visit);
        _cxt = this;
        _cxt = this;
        et_PaymentDone = (EditText) findViewById(R.id.et_now);
        et_all = (EditText) findViewById(R.id.et_all);
        et_after = (EditText) findViewById(R.id.et_after);
        et_before = (EditText) findViewById(R.id.et_before);
        et_Notes = (EditText) findViewById(R.id.et_Notes);

        btn_Save = (ImageButton) findViewById(R.id.btn_Save);
        firestoreDB = FirebaseFirestore.getInstance();
        tv_visitDatee = (TextView)findViewById(R.id.tv_visitDatee);

        progressDialog = new  ProgressDialog(this);
        fetchVisit();
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editVisit();

                   /*Intent visit = new Intent(_cxt, HomeActivity.class);
                startActivity(visit);*/
            }
        });
    }
    //-- get visit data from database
    public void fetchVisit(){
        SharedPreferences prefs = getSharedPreferences(AppService.appkey, MODE_PRIVATE);
        ////// user id
        final String token = prefs.getString("token", "");
        //Toast.makeText(this, "token:"+token, Toast.LENGTH_SHORT).show();
        ///// patient id
        final String token1 = prefs.getString("token1", "");
        // Toast.makeText(this, "token1:"+token1, Toast.LENGTH_SHORT).show();
        //// visit id
        Intent intent = getIntent();
        String id= intent.getStringExtra("id");
        //-- Problem ID
        final String ProblemID = prefs.getString("ProblemID", "");
        //Toast.makeText(this, "id:"+id, Toast.LENGTH_SHORT).show();
        /*FirebaseUser user = mAuth.getCurrentUser();
        mAuth = FirebaseAuth.getInstance();*/
        firestoreDB = FirebaseFirestore.getInstance();
        //-- fireStore query to get data


        firestoreDB.collection("Users").document(token).collection("Patient").document(token1)
                .collection("Problem").document(ProblemID)
                .collection("visitPatient").document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            String TAG = "visit";
                            if (document != null && document.exists()) {
                                patientName = document.getString("patentName");

                                doctorId= document.getString("doctorId");

                                problemId = document.getString("problemId");

                                visitDate = document.getDate("visitDate");

                                tv_visitDatee.setText(visitDate.toString());

                                String paymentDone = document.getString("paymentDone");
                                et_PaymentDone.setText(paymentDone);

                                String visitNote = document.getString("visitNote");
                                et_Notes.setText(visitNote);

                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            String TAG = "visit";
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
        firestoreDB.collection("Users").document(token).collection("Patient").document(token1)
                .collection("Problem")
                .document(ProblemID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            String TAG = "Patient";
                            if (document != null && document.exists()) {
                                paymentTotal = document.getString("proPayment");
                                et_all.setText(paymentTotal);
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            String TAG = "Patient";
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
        // Toast.makeText(this, "=>>"+id, Toast.LENGTH_SHORT).show();
        firestoreDB.collection("VisitList")
                .whereEqualTo("visitId_Primary",id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                 visit_id_copy = document.getId();
                                //Toast.makeText(EditVisitActivity.this, visit_id_copy, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d("copy id", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
    //-- Edit visit Data Button
    public void editVisit() {

        if (date!=null){
        SharedPreferences prefs = getSharedPreferences(AppService.appkey, MODE_PRIVATE);
        ////// user id
        final String token = prefs.getString("token", "");
        //   Toast.makeText(this, "token:"+token, Toast.LENGTH_SHORT).show();
        ///// patient id
        final String token1 = prefs.getString("token1", "");
        //  Toast.makeText(this, "token1:"+token1, Toast.LENGTH_SHORT).show();
        final String ProblemID = prefs.getString("ProblemID", "");
        //// visit id
        Intent intent = getIntent();
        final String id= intent.getStringExtra("id");
        // Toast.makeText(this, "id:"+id, Toast.LENGTH_SHORT).show();

        firestoreDB = FirebaseFirestore.getInstance();
        progressDialog.setMessage("تعديل البيانات  ...");
        progressDialog.show();
        String vistID;
        String patientProplem;
        String patientDiagnosis;
        String patientNote;
//        vistID = et_Id_visit_ed.getText().toString().trim();
        final String patienttNote, paymentDone;
        patienttNote = et_Notes.getText().toString().trim();
        paymentDone = et_PaymentDone.getText().toString().trim();

        if (TextUtils.isEmpty(paymentDone)) {
            Toast.makeText(this, "الرجاء ادخال المبلغ المدفوع  !", Toast.LENGTH_SHORT).show();
            return;
        }
        //-- firestore query to update date
        firestoreDB.collection("Users").document(token).collection("Patient").document(token1)
                .collection("Problem").document(ProblemID)
                .collection("visitPatient").document(id)
                .update("visitNote",patienttNote
                ,"visitDate",date
                        ,"paymentDone",paymentDone)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // startActivity(new Intent(EditVisitActivity.this,ListPateintActivity.class));
                progressDialog.dismiss();


// Create a query against the collection.

                firestoreDB.collection("VisitList").document(visit_id_copy)
                        .update("visitNote",patienttNote
                                ,"visitDate",date
                                ,"paymentDone",paymentDone)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditVisitActivity.this, "تم التعديل ...", Toast.LENGTH_SHORT).show();
                            }
                        });

                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditVisitActivity.this, "خطأ....", Toast.LENGTH_SHORT).show();
            }
        });
    }else{
            SharedPreferences prefs = getSharedPreferences(AppService.appkey, MODE_PRIVATE);
            ////// user id
            final String token = prefs.getString("token", "");
            //   Toast.makeText(this, "token:"+token, Toast.LENGTH_SHORT).show();
            ///// patient id
            final String token1 = prefs.getString("token1", "");
            //  Toast.makeText(this, "token1:"+token1, Toast.LENGTH_SHORT).show();
            final String ProblemID = prefs.getString("ProblemID", "");
            //// visit id
            Intent intent = getIntent();
            final String id= intent.getStringExtra("id");
            // Toast.makeText(this, "id:"+id, Toast.LENGTH_SHORT).show();

            firestoreDB = FirebaseFirestore.getInstance();
            progressDialog.setMessage("تعديل البيانات  ...");
            progressDialog.show();
            final String vistID;
            String patientProplem;
            String patientDiagnosis;
            String patientNote;
//        vistID = et_Id_visit_ed.getText().toString().trim();
            final String patienttNote, paymentDone;
            patienttNote = et_Notes.getText().toString().trim();
            paymentDone = et_PaymentDone.getText().toString().trim();

            if (TextUtils.isEmpty(paymentDone)) {
                Toast.makeText(this, "الرجاء ادخال المبلغ المدفوع  !", Toast.LENGTH_SHORT).show();
                return;
            }
            //-- firestore query to update date
            firestoreDB.collection("Users").document(token).collection("Patient").document(token1)
                    .collection("Problem").document(ProblemID)
                    .collection("visitPatient").document(id)
                    .update("visitNote",patienttNote
                            ,"paymentDone",paymentDone)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // startActivity(new Intent(EditVisitActivity.this,ListPateintActivity.class));
                            progressDialog.dismiss();
                            // Create a query against the collection.
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditVisitActivity.this, "خطأ....", Toast.LENGTH_SHORT).show();
                }
            });
            firestoreDB.collection("VisitList").document(visit_id_copy)
                    .update("visitNote",patienttNote
                            ,"paymentDone",paymentDone)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EditVisitActivity.this, "تم التعديل ...", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date1 = Calendar.getInstance();
        new DatePickerDialog(_cxt, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date1.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(_cxt, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date1.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date1.set(Calendar.MINUTE, minute);
                        Log.v("time", "The choosen one " + date1.getTime());
                        tv_visitDatee.setText(date1.getTime().toString());
                        date = date1.getTime();
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }
    public void addNextVisitD(View view) {
        showDateTimePicker();

    }
}

