package com.dental.anisandmahmmoud.dentalaandm;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import model.VisitPatient;
import utils.AppService;

public class AddNextVisitActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
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
    private String paymentTotal;
    private String patientId;
    private String patientPhone;
    private Date date;
    Calendar date1;
    double p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_next_visit);
        _cxt = this;
        et_PaymentDone = (EditText) findViewById(R.id.et_now);
        et_all = (EditText) findViewById(R.id.et_all);
        et_after = (EditText) findViewById(R.id.et_after);
        et_before = (EditText) findViewById(R.id.et_before);
        et_Notes = (EditText) findViewById(R.id.et_Notes);

        btn_Save = (ImageButton) findViewById(R.id.btn_Save);

        tv_visitDatee = (TextView)findViewById(R.id.tv_visitDatee);


        progressDialog = new  ProgressDialog(this);




       /* Intent i= getIntent();
        String id1= i.getStringExtra("id1");
       */
       /* Bundle bundle = getIntent().getExtras();
        if(bundle.getString("id1")!= null) {
            Toast.makeText(VisitActivity.this, bundle.getString("id1"), Toast.LENGTH_SHORT).show();
        }*/


        /*SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMdd:HH.mm.ss.SS");
        Date myDate = new Date();
        String filename = timeStampFormat.format(myDate);
        Toast.makeText(this, myDate.toString(), Toast.LENGTH_SHORT).show();*/





        /*Intent i= getIntent();
        String id1= i.getStringExtra("id1");*/
        firestoreDB = FirebaseFirestore.getInstance();

        SharedPreferences prefs = getSharedPreferences(AppService.appkey, MODE_PRIVATE);
        String token = prefs.getString("token", "");
        ////--token1 is a Patient id
        final String token1 = prefs.getString("token1", "");
        //-- Problem ID
        final String ProblemID = prefs.getString("ProblemID", "");

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
                                patientId = document.getId();
                                patientPhone = document.get("p_phone").toString();
                                //Toast.makeText(AddNextVisitActivity.this, patientName, Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            String TAG = "Patient";
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
        firestoreDB.collection("Users").document(token).collection("Patient").document(token1) .collection("Problem")
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
        double sum = 0.0;
        firestoreDB.collection("Users").document(token).collection("Patient").document(token1) .collection("Problem")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot document : task.getResult()) {

                             //p = Double.parseDouble(document.getString("paymentDone"));

                        }
                    }
                });
                  //  sum = sum+p;
        Toast.makeText(this, sum+"", Toast.LENGTH_SHORT).show();
                                //  Toast.makeText(_cxt, id1, Toast.LENGTH_SHORT).show();
        /*SharedPreferences prefs1 = getSharedPreferences(PalcoreStoreService.appkey, MODE_PRIVATE);
        String token1 = prefs1.getString("token1", "");
        Toast.makeText(this, token1, Toast.LENGTH_SHORT).show();*/
       /* Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();*/

        //  Toast.makeText(this, tv_visitDatee.getText().toString(), Toast.LENGTH_SHORT).show();
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visitPatientt();

                   /*Intent visit = new Intent(_cxt, HomeActivity.class);
                startActivity(visit);*/
            }
        });
    }



    public void visitPatientt() {


        SimpleDateFormat ss1 = new SimpleDateFormat("yyyy-MM-dd");
        progressDialog = new  ProgressDialog(this);
        firestoreDB = FirebaseFirestore.getInstance();
        //Date myDate = new Date();
        // String filename = ss.format(tv_visitDatee.getText().toString());
        if (date!=null) {
        /*try {
            date = ss1.parse(ndate);*/
            SharedPreferences prefs = getSharedPreferences(AppService.appkey, MODE_PRIVATE);
            String token = prefs.getString("token", "");
            //-- Patient ID
            final String token1 = prefs.getString("token1", "");
            //-- Problem ID
            final String ProblemID = prefs.getString("ProblemID", "");


            final String patientNote, paymentDone;
            patientNote = et_Notes.getText().toString().trim();
            paymentDone = et_PaymentDone.getText().toString().trim();
            if (TextUtils.isEmpty(paymentDone)) {
                Toast.makeText(this, "الرجاء ادخال المبلغ المدفوع  !", Toast.LENGTH_SHORT).show();
                return;
            }

            VisitPatient visitPatient = new VisitPatient(doctorId, patientName, ProblemID
                    , paymentDone, date, patientNote);

            //  Toast.makeText(_cxt, id1, Toast.LENGTH_SHORT).show();
            progressDialog.setMessage("يتم اضافة الزيارة  ");
            progressDialog.show();
            firestoreDB.collection("Users").document(token).collection("Patient").document(token1)
                    .collection("Problem").document(ProblemID)
                    .collection("visitPatient")
                    //.document()
                    //.set(vistPatient)
                    .add(visitPatient)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            progressDialog.dismiss();
                            String visitRef = documentReference.getId();
                            VisitPatient visitPatient1 = new VisitPatient(visitRef,doctorId,patientName,ProblemID,
                                    paymentDone,patientNote,patientId,patientPhone,date);
                            progressDialog.dismiss();
                            firestoreDB.collection("VisitList")
                                    .add(visitPatient1)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            final String visitRref_copy = documentReference.getId();
                                            firestoreDB.collection("VisitList").document(visitRref_copy)
                                                    .update("visit_id_copy",visitRref_copy)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(AddNextVisitActivity.this, "تم اضافة الزيارة ", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });



                                        }
                                    });
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddNextVisitActivity.this, "خطأ حاول مرة اخرى ...", Toast.LENGTH_SHORT).show();
                }
            });
            /*firestoreDB.collection("Users").document(token).collection("Patient").document(id1)
                    .collection("visitPatient")
                    //.document()
                    //.set(vistPatient)
                    .add(vistPatient)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            String visitRef = documentReference.getId();
                            final vistPatient vistPatient1 = new vistPatient(visitRef,doctorId,patientName,patientProplem,patientDiagnosis,patientNote,date);

                            firestoreDB.collection("VisitList").document()

                                    .set(vistPatient1)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AddNextVisitActivity.this, "تم اضافة الزيارة ", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddNextVisitActivity.this, "خطأ...", Toast.LENGTH_SHORT).show();
                }
            });*/


        }else {

        /*} catch (ParseException e) {
            e.printStackTrace();*/
            Toast.makeText(this, "الرجاء ادخال التاريخ اولا !! ", Toast.LENGTH_SHORT).show();

        }
    }

       /* String ndate = tv_visitDatee.getText().toString().trim();
        SimpleDateFormat ss1 = new SimpleDateFormat("yyyy-MM-dd");
        //Date myDate = new Date();
        // String filename = ss.format(tv_visitDatee.getText().toString());
          date = new Date();


        try {
            date = ss1.parse(ndate);
            //-- init visit

            SharedPreferences prefs = getSharedPreferences(AppService.appkey, MODE_PRIVATE);
            String token = prefs.getString("token", "");


        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

            //adding visit to Patient to  db

         String value ;
         Bundle bundle = getIntent().getExtras();
         value = bundle.getString("id");
        Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
          firestoreDB.collection("Users").document(token).collection("Patient").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                         String TAG = "Patient";

            //  mAuth = FirebaseAuth.getInstance();
            //Patient patient = new Patient();
            Intent i = getIntent();
            String id1 = i.getStringExtra("id1");
            String vistID;

            final String patientProplem;
            final String patientDiagnosis;
            final String patientNote;
//            vistID = et_Id_visit.getText().toString().trim();
//            if (TextUtils.isEmpty(vistID)){
//                Toast.makeText(this, "الرجاء ادخال رقم الزيارة !", Toast.LENGTH_SHORT).show();
//                return;
//            }
            firestoreDB = FirebaseFirestore.getInstance();

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
                                    Toast.makeText(AddNextVisitActivity.this, patientName, Toast.LENGTH_SHORT).show();
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


            //SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
            Profile_Patient_Activity profile_patient_activity = new Profile_Patient_Activity();


            //  Toast.makeText(_cxt, currentDay.toString(), Toast.LENGTH_SHORT).show();




            //myDate.setTime(i.getLongExtra("Date",-1));


            //Toast.makeText(this, myDate.toString(), Toast.LENGTH_SHORT).show();
           // String visitRef = documentReference.getId();
            //patientName = et_Name_Patient.getText().toString().trim();
            final vistPatient vistPatient = new vistPatient( patientName,patientProplem, patientDiagnosis, patientNote, date);


            //  Toast.makeText(_cxt, id1, Toast.LENGTH_SHORT).show();
            progressDialog.setMessage("يتم اضافة الزيارة  " );
            progressDialog.show();
            firestoreDB.collection("Users").document(token).collection("Patient").document(id1)
                    .collection("visitPatient")
                    //.document()
                    //.set(vistPatient)
                    .add(vistPatient)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            String visitRef = documentReference.getId();
                            final vistPatient vistPatient1 = new vistPatient(visitRef,doctorId,patientName,patientProplem,patientDiagnosis,patientNote,date);

                            firestoreDB.collection("VisitList").document()

                                    .set(vistPatient1)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AddNextVisitActivity.this, "تم اضافة الزيارة ", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddNextVisitActivity.this, "خطأ...", Toast.LENGTH_SHORT).show();
                }
            });


            firestoreDB.collection("Users").document(token).collection("Patient").document(id1)
                    .collection("visitPatient").document().set(vistPatient)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();

                            firestoreDB.collection("VisitList").document()
                                    .set(vistPatient)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AddNextVisitActivity.this, "تم اضافة الزيارة ", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            finish();
                        }
                    });

        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "الرجاء ادخال التاريخ اولا !! ", Toast.LENGTH_SHORT).show();
        }
    }*/


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
       /* SimpleDateFormat ss = new SimpleDateFormat("dd-MM-yyyy");
      //  Date date = new Date();
     //   String currentdate= ss.format(date);
        currentDay = Calendar.getInstance();
       // tv_visitDatee.setText(date.toString());

        CalendarView vieww = new CalendarView(this);
        setContentView(vieww);

        vieww.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @SuppressLint("WrongConstant")
            @Override
            public void onSelectedDayChange(CalendarView arg0, int year, int month,
                                            int date) {
                currentDay = new GregorianCalendar( year, month, date );
                tv_visitDatee.setText(date+ "/"+(month+1)+"/"+year);
                Toast.makeText(getApplicationContext(),date+ "/"+month+"/"+year,4000).show();
            }
        });

*/

       /* final View dialogView = View.inflate(getApplicationContext(), R.layout.date_time_picker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();

        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

                Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());

               Long time = calendar.getTimeInMillis();
               tv_visitDatee.setText(time.toString());
                alertDialog.dismiss();
            }});
        alertDialog.setView(dialogView);
        alertDialog.show();
*/
        /*currentDay = Calendar.getInstance();
        int year = currentDay.get(Calendar.YEAR);
        int month = currentDay.get(Calendar.MONTH);
        int day = currentDay.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog= new DatePickerDialog(this,this,year,month,day);
        datePickerDialog.show();*/

    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Toast.makeText(this, year+"/"+(month+1)+"/"+dayOfMonth, Toast.LENGTH_SHORT).show();
        //  Date date = new Date(year,month,dayOfMonth,30,30,30);

        //tv_visitDatee.setText(date.toString());
        // tv_visitDatee.setText(date.toString());

//        Date D =(Date) tv_visitDatee.getText();
        // String D = tv_visitDatee.getText().toString();
        //  D.getTime();
        // Toast.makeText(this, D, Toast.LENGTH_SHORT).show();

        /* Date Da =(Date) tv_visitDatee.getText() ;
        Toast.makeText(this, Da.toString(), Toast.LENGTH_SHORT).show();*/
        currentDay.set(year,month,dayOfMonth);
        SimpleDateFormat ss = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat ss1 = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(year-1900,month,dayOfMonth);
        String currentdate= ss1.format(date);
        //tv_visitDatee.setText(currentdate);

    }
        }