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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import model.Problem;
import utils.AppService;

public class EditProblemActivity extends AppCompatActivity {
    private EditText et_complaint,et_Place,et_Notes,et_Payment;
    private FirebaseFirestore firestoreDB;
    private ProgressDialog progressDialog;
    private ImageButton btn_Save;
    private Context _cxt;
    private String patientName;
    private String doctorId;
    private Date problemDate;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_problem);

        et_complaint = (EditText)findViewById(R.id.et_complaint);
        et_Place = (EditText)findViewById(R.id.et_Place);
        et_Notes = (EditText)findViewById(R.id.et_Notes);
        et_Payment = (EditText)findViewById(R.id.et_Payment);
        firestoreDB = FirebaseFirestore.getInstance();

        SharedPreferences prefs = getSharedPreferences(AppService.appkey, MODE_PRIVATE);
        String token = prefs.getString("token", "");
        ////--token1 is a Patient id
        final String token1 = prefs.getString("token1", "");

        //-- problem id  ID
        Intent i= getIntent();
        String ProblemID= i.getStringExtra("ProblemID");

        firestoreDB.collection("Users").document(token).collection("Patient").document(token1)
                .collection("Problem").document(ProblemID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            String TAG = "Problem";
                            if (document != null && document.exists()) {
                                 patientName = document.getString("patentName");
                                 doctorId= document.getString("doctorId");
                                  problemDate = document.getDate("proDate");
                                 String ProblemNote = document.getString("proNote");
                                 et_Notes.setText(ProblemNote);
                                 String Payment = document.getString("proPayment");
                                 et_Payment.setText(Payment);
                                 String place = document.getString("proPlace");
                                 et_Place.setText(place);
                                 String problem = document.getString("proProblem");
                                 et_complaint.setText(problem);
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            String TAG = "Problem";
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

        /*firestoreDB.collection("Users").document(token).collection("Patient").document(token1)
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
                               // Toast.makeText(EditProblemActivity.this, patientName, Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            String TAG = "Patient";
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });*/

    }
    public void Update(){
        dialog = new ProgressDialog(this);
        //-- get Doc id
        final SharedPreferences prefs = getSharedPreferences(AppService.appkey, MODE_PRIVATE);
        final String token = prefs.getString("token", "");
        ////--token1 is a Patient id
        final String token1 = prefs.getString("token1", "");

        //--init firebase batabase
        firestoreDB = FirebaseFirestore.getInstance();

        String ProProblem, ProPayment,ProPlace ,ProNote;
        ProProblem = et_complaint.getText().toString().trim();
        ProPayment = et_Payment.getText().toString().trim();
        ProPlace = et_Place.getText().toString().trim();
        ProNote = et_Notes.getText().toString().trim();

        //-- problem id  ID
        Intent i= getIntent();
        String ProblemID= i.getStringExtra("ProblemID");
        dialog.setTitle("تحديث  ");
        dialog.setMessage("يتم تعديل البيانات  ...");
        dialog.show();
        firestoreDB.collection("Users").document(token).collection("Patient").document(token1)
                .collection("Problem").document(ProblemID)
                .update("proNote",ProNote
                ,"proProblem",ProProblem
                        ,"proPayment",ProPayment
                        ,"proPlace", ProPlace)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.dismiss();
                        finish();
                       // startActivity(getIntent());
                    }
                });
    }

    public void editProblem(View view) {
        Update();
    }
}
