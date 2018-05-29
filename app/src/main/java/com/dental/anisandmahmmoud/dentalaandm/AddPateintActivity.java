package com.dental.anisandmahmmoud.dentalaandm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.onesignal.OneSignal;

import java.text.SimpleDateFormat;
import java.util.Date;

import model.Patient;
import utils.AppService;

public class AddPateintActivity extends AppCompatActivity {
    ImageButton btn_Save;

    EditText et_Add_Id_Patient, et_Add_Name_Patient, et_Add_Age_Patient,et_Tele_p;
    RadioGroup radio_group_smoking, radio_group_SEX;
    RadioButton radioButton_smoking, radioButton_SEX;

    //RadioButton radio_male , radio_female , radio_True_smoking , radio_False_smoking;
    CheckBox checkbox_sugar, checkbox_pressure, checkbox_Psychological;
    FirebaseFirestore firestoreDB;
    FirebaseAuth mAuth;
    Date myDate1String;
    private ProgressDialog progressDialog;
    Context _cxt;
    private String TAG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pateint);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        firestoreDB = FirebaseFirestore.getInstance();
        _cxt = this;
        progressDialog = new ProgressDialog(this);
        btn_Save = (ImageButton) findViewById(R.id.btn_Save);
//        et_Add_Id_Patient = (EditText)findViewById(R.id.et_Add_Id_Patient);
        et_Add_Name_Patient = (EditText) findViewById(R.id.et_Add_Name_Patient);
        et_Add_Age_Patient = (EditText) findViewById(R.id.et_Add_Age_Patient);
        et_Tele_p = (EditText)findViewById(R.id.et_Tele_p);
//        String doctorId = mAuth.getUid();
//        et_Add_Id_Patient.setText(doctorId);
     /* radio_male = (RadioButton) findViewById(R.id.radio_male);
        radio_female = (RadioButton) findViewById(R.id.radio_female);
        radio_True_smoking = (RadioButton) findViewById(R.id.radio_True_smoking);
        radio_False_smoking = (RadioButton) findViewById(R.id.radio_False_smoking);*/
        SharedPreferences prefs = getSharedPreferences(AppService.appkey, MODE_PRIVATE);
        String token = prefs.getString("token", "");
        Toast.makeText(this, token, Toast.LENGTH_SHORT).show();
        radio_group_smoking = (RadioGroup) findViewById(R.id.radio_group_smoking);
        radio_group_SEX = (RadioGroup) findViewById(R.id.radio_group_sex);

        checkbox_sugar = (CheckBox) findViewById(R.id.checkbox_sugar);
        checkbox_pressure = (CheckBox) findViewById(R.id.checkbox_pressure);
        checkbox_Psychological = (CheckBox) findViewById(R.id.checkbox_Psychological);

        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPatient();

            }
        });
    }


    public void addPatient() {


        FirebaseUser user = mAuth.getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        String doctorId = mAuth.getUid();
        //--read from sharepreferences
        SharedPreferences prefs = getSharedPreferences(AppService.appkey, MODE_PRIVATE);
        String token = prefs.getString("token", "");

        firestoreDB = FirebaseFirestore.getInstance();
        final String patientID, patientName, patientAge, NDC,phone;
        String patientٍSex, patientSmoke;

        patientName = et_Add_Name_Patient.getText().toString().trim();
       /* Patient pp = new Patient();
        pp.setPatientName(patientName);*/
        if (TextUtils.isEmpty(patientName)){
            Toast.makeText(this, "الرجاء ادخال اسم المريض !", Toast.LENGTH_SHORT).show();
            return;
        }
        patientAge = et_Add_Age_Patient.getText().toString().trim();

        if (TextUtils.isEmpty(patientAge)){
            Toast.makeText(this, "الرجاء ادخال عمر المريض !", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedId_smoking = radio_group_smoking.getCheckedRadioButtonId();
        radioButton_smoking = (RadioButton) findViewById(selectedId_smoking);
        patientSmoke = radioButton_smoking.getText().toString();

        int selectedId_SEX = radio_group_SEX.getCheckedRadioButtonId();
        radioButton_SEX = (RadioButton) findViewById(selectedId_SEX);
        patientٍSex = radioButton_SEX.getText().toString();


       /* StringBuffer result = new StringBuffer();
        result.append(checkbox_sugar.isChecked());
        result.append(checkbox_pressure.isChecked());
        result.append(checkbox_Psychological.isChecked());
        NDC = result.toString().trim();*/
        boolean sugar, Psychological, pressure;
        sugar = checkbox_sugar.isChecked();
        Psychological = checkbox_Psychological.isChecked();
        pressure = checkbox_pressure.isChecked();
        phone = "+97"+et_Tele_p.getText().toString().trim();
        ////get Patient id
        Intent i = getIntent();
        String id = i.getStringExtra("id");


        //--adding to SharedPreferences
        /* SharedPreferences.Editor editor = getSharedPreferences(PalcoreStoreService.appkey, MODE_PRIVATE).edit();
        editor.putString("patientName", patientName);
        editor.apply();
        */
//


        SimpleDateFormat timeStampFormat1 = new SimpleDateFormat("EEEE, MMM dd, yyyy HH:mm:ss a");
        Date myDate1 = new Date();
        String filename = timeStampFormat1.format(myDate1);

        //--init pateint
        Patient patient = new Patient(doctorId, patientName, patientAge, patientٍSex, patientSmoke, myDate1,
                pressure, Psychological, sugar,phone);
        progressDialog.setMessage("يتم اضافة المريض " +patientName );
        progressDialog.show();
        //adding pateint to db
        firestoreDB.collection("Users").document(token).collection("Patient").add(patient)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progressDialog.dismiss();
                        String id1 = documentReference.getId();

                        // String pName=  pnamez.get("patientName").toString();
                        /* SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage("+972595658043", null, "Message", null, null);*/
                        Intent intent = new Intent(AddPateintActivity.this, ProplemActivity.class);
                        intent.putExtra("id1", id1);
                        startActivity(intent);
                        // Toast.makeText(_cxt, id1, Toast.LENGTH_SHORT).show();
                        Toast.makeText(AddPateintActivity.this, "تم اضافة المريض ...", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(AddPateintActivity.this, "خطأ حاول مرة اخرى ", Toast.LENGTH_SHORT).show();
            }
        });
               /* .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddPateintActivity.this, "Success to add patient", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddPateintActivity.this, "Failed to add patient ", Toast.LENGTH_SHORT).show();
                    }
        });*/

    }
}

