package com.dental.anisandmahmmoud.dentalaandm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
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

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import model.Problem;
import model.VisitPatient;
import utils.AppService;

public class ProblemOtherActivity extends AppCompatActivity {
    private EditText et_complaint,et_Place,et_Notes,et_Payment;
    private FirebaseFirestore firestoreDB;
    private ProgressDialog progressDialog;
    private ImageButton btn_Save;
    private Context _cxt;
    private String patientName;
    private String doctorId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_other);
        et_complaint = (EditText)findViewById(R.id.et_complaint);
        et_Place = (EditText)findViewById(R.id.et_Place);
        et_Notes = (EditText)findViewById(R.id.et_Notes);
        et_Payment = (EditText)findViewById(R.id.et_Payment);

        firestoreDB = FirebaseFirestore.getInstance();
        // progressDialog = new ProgressDialog(this);

        //-- get Doc id
        SharedPreferences prefs = getSharedPreferences(AppService.appkey, MODE_PRIVATE);
        final String token = prefs.getString("token", "");
        ////--token1 is a Patient id
        final String token1 = prefs.getString("token1", "");


        firestoreDB.collection("Users").document(token).collection("Patient").document(token1)
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
                                Toast.makeText(ProblemOtherActivity.this, patientName, Toast.LENGTH_SHORT).show();
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
        ////--token1 is a Patient id
        final String token1 = prefs.getString("token1", "");

        //--init firebase batabase
        firestoreDB = FirebaseFirestore.getInstance();

        //-- getPatient ID
       /* Intent i= getIntent();
        final String id1= i.getStringExtra("id1");
        Toast.makeText(this, id1, Toast.LENGTH_SHORT).show();*/
        //-- inti Problem
        final Problem problem = new Problem(ProProblem ,ProPayment,ProPlace,ProNote,myDate,patientName);
        //--Init progressDialog
        progressDialog = new ProgressDialog(this);
        /*progressDialog.setMessage("يتم إضافة الزيارة الاولى  والشكوى ....  " );
        progressDialog.show();*/
        firestoreDB.collection("Users").document(token).collection("Patient").document(token1)
                .collection("Problem")
                .add(problem)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        final String problemRef = documentReference.getId();
                        final String PaymentDone = "000";
                        final String VisitNote = " ";
                        VisitPatient visitPatient = new VisitPatient(doctorId,patientName,problemRef,
                                PaymentDone,myDate,VisitNote);
                        firestoreDB.collection("Users").document(token).collection("Patient").document(token1)
                                .collection("Problem").document(problemRef)
                                .collection("visitPatient").add(visitPatient)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        sendNotification();
                                        final String visitRref = documentReference.getId();
                                        VisitPatient visitPatient = new VisitPatient(visitRref,doctorId,patientName,problemRef,
                                                PaymentDone,VisitNote,myDate);
                                        firestoreDB.collection("VisitList").document()
                                                .set(visitPatient)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        Toast.makeText(ProblemOtherActivity.this, "تم إضافة الشكوى و الزيارة الاولى ",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProblemOtherActivity.this, "هنالك خطأ حاول مرة اخرى .!!!", Toast.LENGTH_SHORT).show();
            }
        });

//


    }

    private void sendNotification() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String send_email;
                    String Doc_id = "anis.siam.cs@gmail.com";
                    //This is a Simple Logic to Send Notification different Device Programmatically....
                    if (Doc_id.equals("user1@gmail.com")) {
                        send_email = "anis.siam@hotmail.com";
                    } else {
                        send_email = "anis.siam.cs@gmail.com";
                    }

                    try {
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic MzZiOTY5NGMtMDU1Ny00NTFhLTllN2MtN2YzMTA4MGFjOTc4");
                        con.setRequestMethod("POST");

                        String strJsonBody = "{"
                                + "\"app_id\": \"ce26d617-0d2a-461d-8be3-9d1d46a6e4af\","

                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"Doc_ID\", \"relation\": \"=\", \"value\": \"" + send_email + "\"}],"

                                + "\"data\": {\"foo\": \"bar\"},"
                                + "\"contents\": {\"en\": \"اضافة شكوى جديدة\"}"
                                + "}";


                        System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void addProblem(View view) {
        AddPrblem();
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent visit = new Intent(ProblemOtherActivity.this, HomeActivity.class);
        startActivity(visit);
    }
}
