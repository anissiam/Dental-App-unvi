package com.dental.anisandmahmmoud.dentalaandm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.Date;

import adapter.ProblemAdapter;
import adapter.ProblemAdapterPatient;
import de.hdodenhof.circleimageview.CircleImageView;
import model.Patient;
import model.Problem;
import utils.AppService;

import static android.support.constraint.solver.widgets.ConstraintWidget.VISIBLE;

public class ProfilePatientPatientActivity extends AppCompatActivity {
    private CircleImageView img_patient, btn_more;
    private TextView tv_Name_Patient;
    private EditText et_telephone, et_Add_Age_Patient;
    private CheckBox checkbox_sugar, checkbox_pressure, checkbox_Psychological;
    private ImageButton btn_save;

    private RadioGroup radio_group_smoking, radio_group_sex;
    private RadioButton radioButton_smoking, radioButton_SEX;
    private ProgressDialog dialog;
    private Date myDate1String;
    FirebaseAuth mAuth;

    private RelativeLayout relative2, relative4;
    FirebaseFirestore firestoreDB;
    private RecyclerView rv_paetint;
    ProblemAdapterPatient adepter;
    ArrayList<Problem> List_Problem;
    Context _cxt;
    private ProgressBar progressBarr;
    private AlertDialog.Builder dlgAlert;

    private String doctorId;
    private String patientId;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_patient_pateint);
        firestoreDB = FirebaseFirestore.getInstance();
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        progressBarr = (ProgressBar) findViewById(R.id.progressBarr);
        img_patient = (CircleImageView) findViewById(R.id.img_patient);
        tv_Name_Patient = (TextView) findViewById(R.id.tv_Name_Patient);
        et_telephone = (EditText) findViewById(R.id.et_telephone);
        et_Add_Age_Patient = (EditText) findViewById(R.id.et_Add_Age_Patient);

        checkbox_sugar = (CheckBox) findViewById(R.id.checkbox_sugar);
        checkbox_pressure = (CheckBox) findViewById(R.id.checkbox_pressure);
        checkbox_Psychological = (CheckBox) findViewById(R.id.checkbox_Psychological);


        // btn_save = (ImageButton) findViewById(R.id.btn_save);

        radio_group_smoking = (RadioGroup) findViewById(R.id.radio_group_smoking);
        img_patient = (CircleImageView) findViewById(R.id.img_patient);
        tv_Name_Patient = (TextView) findViewById(R.id.tv_Name_Patient);
        btn_more = (CircleImageView) findViewById(R.id.btn_more);
//        btn_save = (ImageButton) findViewById(R.id.btn_save);

        relative2 = (RelativeLayout) findViewById(R.id.relative2);
        relative4 = (RelativeLayout) findViewById(R.id.relative4);
        dlgAlert = new AlertDialog.Builder(this);
        List_Problem = new ArrayList<>();
        _cxt = this;
        rv_paetint = (RecyclerView) findViewById(R.id.rv_paetint);
        rv_paetint.setHasFixedSize(true);

        rv_paetint.setLayoutManager(new LinearLayoutManager(_cxt));
        adepter = new ProblemAdapterPatient(_cxt, List_Problem);
        rv_paetint.setAdapter(adepter);




        final Animation rotate = AnimationUtils.loadAnimation(_cxt, R.anim.rotatebutton);
        final Animation rotate2 = AnimationUtils.loadAnimation(_cxt, R.anim.rotatebutton2);


        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (relative4.getVisibility() == VISIBLE) {
                    relative4.setVisibility(View.GONE);
                    relative2.setVisibility(VISIBLE);
                    btn_more.startAnimation(rotate2);

                } else if (relative2.getVisibility() == VISIBLE) {
                    relative2.setVisibility(View.GONE);
                    relative4.setVisibility(View.VISIBLE);
                    btn_more.startAnimation(rotate);
                }
            }
        });

        fetchPateintD();
        OneSignal.sendTag("Patient_Id",phone);
        getProblemData();
    }

///////

    public void fetchPateintD() {

        SharedPreferences prefs = getSharedPreferences(AppService.appkey, MODE_PRIVATE);
        final String doctorId = prefs.getString("doctorId", "");
        final String patientId = prefs.getString("patientId", "");
        firestoreDB.collection("Users").document(doctorId).collection("Patient").document(patientId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            String TAG = "Patient";
                            if (document != null && document.exists()) {

                                String doctorId = document.get("doctorId").toString();

                                String patientName = document.get("patientName").toString();
                                tv_Name_Patient.setText(patientName);

                                String patientAge = document.get("patientAge").toString();
                                et_Add_Age_Patient.setText(patientAge);

                                phone = document.get("p_phone").toString();
                                et_telephone.setText(phone);


                                String patientٍSex_ed = document.get("patientٍSex").toString();


                                if (patientٍSex_ed.equals("ذكر")) {
                                    img_patient.setImageDrawable(getResources().getDrawable(R.drawable.male));

                                } else {
                                    img_patient.setImageDrawable(getResources().getDrawable(R.drawable.female));

                                }


                                myDate1String = (Date) document.get("patientDate");

                                boolean pressure = (boolean) document.get("is_htn");
                                checkbox_pressure.setChecked(pressure);

                                boolean Psychological = (boolean) document.get("is_psychic");
                                checkbox_Psychological.setChecked(Psychological);

                                boolean sugar = (boolean) document.get("is_dm");
                                checkbox_sugar.setChecked(sugar);


                                String patientSmoke = document.get("patientSmoke").toString();
                                if (patientSmoke.equals("نعم")) {
                                    radio_group_smoking.check(R.id.radio_True_smoking);
                                } else {
                                    radio_group_smoking.check(R.id.radio_False_smoking);
                                }
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            String TAG = "Patient";
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    private void getProblemData() {
        progressBarr.setVisibility(View.VISIBLE);
        SharedPreferences prefs = getSharedPreferences(AppService.appkey, MODE_PRIVATE);
        final String doctorId = prefs.getString("doctorId", "");
        final String patientId = prefs.getString("patientId", "");
        // mAuth = FirebaseAuth.getInstance();
        Intent i= getIntent();
        String id= i.getStringExtra("id");


        SharedPreferences.Editor editor = getSharedPreferences(AppService.appkey, MODE_PRIVATE).edit();
        editor.putString("token1",id );
        editor.apply();


        firestoreDB.collection("Users").document(doctorId).collection("Patient").document(patientId).collection("Problem")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressBarr.setVisibility(View.GONE);
                        for (DocumentSnapshot document : task.getResult()) {

                                /*Intent intent = new Intent(_cxt,VisitActivity.class);
                                intent.putExtra("id1",document.getId());*/

                            Problem problem= new Problem();

                            problem.setDocumentid(document.getId());
                            //   v.setVistID(document.getString("vistID"));
                            problem.setProProblem(document.getString("proProblem"));
                            problem.setProDate(document.getDate("proDate"));
                            problem.setProPlace(document.getString("proPlace"));
                            List_Problem.add(problem);
                            adepter.notifyDataSetChanged();

                            //    Toast.makeText(_cxt, , Toast.LENGTH_SHORT).show();
                            //  Log.d(TAG, document.getId() + " => " + document.getData());

                        }
                    }
                });
    }

    //-- refresh profile after add visit

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

}

