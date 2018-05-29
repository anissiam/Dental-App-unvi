package com.dental.anisandmahmmoud.dentalaandm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.support.annotation.DrawableRes;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import adapter.ProblemAdapter;
import adapter.VisitAdepter;
import de.hdodenhof.circleimageview.CircleImageView;
import model.Patient;
import model.Problem;
import model.vistPatient;
import utils.AppService;
import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.solver.widgets.ConstraintWidget.VISIBLE;


public class Profile_Patient_Activity extends AppCompatActivity {
    private CircleImageView img_patient,btn_more;
    private TextView tv_Name_Patient;
    private EditText et_telephone,et_Add_Age_Patient;
    private CheckBox checkbox_sugar , checkbox_pressure , checkbox_Psychological ;
    private ImageButton btn_save ;

    private RadioGroup radio_group_smoking,radio_group_sex;
    private RadioButton radioButton_smoking,radioButton_SEX;
    private ProgressDialog dialog;
    private Date myDate1String;
    FirebaseAuth mAuth;

    private RelativeLayout relative2 , relative4;
    FirebaseFirestore firestoreDB;
    private RecyclerView rv_paetint;
    ProblemAdapter adepter;
    ArrayList<Problem> List_Problem;
    Context _cxt;
    private ProgressBar progressBarr;
    private AlertDialog.Builder dlgAlert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__patient_);
        firestoreDB = FirebaseFirestore.getInstance();
        progressBarr = (ProgressBar)findViewById(R.id.progressBarr);
        img_patient = (CircleImageView) findViewById(R.id.img_patient);
        tv_Name_Patient = (TextView)findViewById(R.id.tv_Name_Patient);
        et_telephone = (EditText)findViewById(R.id.et_telephone) ;
        et_Add_Age_Patient = (EditText)findViewById(R.id.et_Add_Age_Patient) ;

        checkbox_sugar = (CheckBox) findViewById(R.id.checkbox_sugar);
        checkbox_pressure = (CheckBox) findViewById(R.id.checkbox_pressure);
        checkbox_Psychological = (CheckBox) findViewById(R.id.checkbox_Psychological);


        // btn_save = (ImageButton) findViewById(R.id.btn_save);

        radio_group_smoking = (RadioGroup) findViewById(R.id.radio_group_smoking);
        img_patient = (CircleImageView) findViewById(R.id.img_patient);
        tv_Name_Patient = (TextView)findViewById(R.id.tv_Name_Patient);
        btn_more = (CircleImageView) findViewById(R.id.btn_more);
//        btn_save = (ImageButton) findViewById(R.id.btn_save);

        relative2 = (RelativeLayout) findViewById(R.id.relative2);
        relative4 = (RelativeLayout) findViewById(R.id.relative4);
        dlgAlert  = new AlertDialog.Builder(this);
        List_Problem = new ArrayList<>();
        _cxt = this;
        rv_paetint = (RecyclerView) findViewById(R.id.rv_paetint);
        rv_paetint.setHasFixedSize(true);

        rv_paetint.setLayoutManager(new LinearLayoutManager(_cxt));
        adepter = new ProblemAdapter(_cxt,List_Problem);
        rv_paetint.setAdapter(adepter);
        fetchPateintD();
        getProblemData();


        final Animation rotate = AnimationUtils.loadAnimation(_cxt,R.anim.rotatebutton);
        final Animation rotate2 = AnimationUtils.loadAnimation(_cxt,R.anim.rotatebutton2);



        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(relative4.getVisibility() == VISIBLE){
                    relative4.setVisibility(View.GONE);
                    relative2.setVisibility(VISIBLE);
                    btn_more.startAnimation(rotate2);

                }else if(relative2.getVisibility() == VISIBLE){
                    relative2.setVisibility(View.GONE);
                    relative4.setVisibility(View.VISIBLE);
                    btn_more.startAnimation(rotate);
                }
            }
        });

    }

///////

    public void fetchPateintD(){

        SharedPreferences prefs = getSharedPreferences(AppService.appkey, MODE_PRIVATE);
        final String token = prefs.getString("token", "");

        ////get Patient id
        Intent i= getIntent();
        final String id= i.getStringExtra("id");
        firestoreDB.collection("Users").document(token).collection("Patient").document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            String TAG = "Patient";
                            if (document != null && document.exists()) {

                                String doctorId=  document.get("doctorId").toString();

                                String patientName = document.get("patientName").toString();
                                tv_Name_Patient.setText(patientName);

                                String patientAge = document.get("patientAge").toString();
                                et_Add_Age_Patient.setText(patientAge);

                                String phone =document.get("p_phone").toString();
                                et_telephone.setText(phone);

                                String patientٍSex_ed = document.get("patientٍSex").toString();



                                if (patientٍSex_ed.equals("ذكر")){
                                    img_patient.setImageDrawable(getResources().getDrawable(R.drawable.male));

                                }else {
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
                                if(patientSmoke.equals("نعم")){
                                    radio_group_smoking.check(R.id.radio_True_smoking);
                                }else {
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
    /*public void fetchPateintD(){

        SharedPreferences prefs = getSharedPreferences(PalcoreStoreService.appkey, MODE_PRIVATE);
        final String token = prefs.getString("token", "");

        ////get Patient id
        Intent i= getIntent();
        final String id= i.getStringExtra("id");
        firestoreDB.collection("Users").document(token).collection("Patient").document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            String TAG = "Patient";
                            if (document != null && document.exists()) {

                                String doctorId=  document.get("doctorId").toString();

                                String patientName = document.get("patientName").toString();
                                tv_Name_Patient.setText(patientName);

                                String patientٍSex_ed = document.get("patientٍSex").toString();


                                if (patientٍSex_ed.equals("ذكر")){
                                    img_patient.setImageDrawable(getResources().getDrawable(R.drawable.male));
                                }else {
                                    img_patient.setImageDrawable(getResources().getDrawable(R.drawable.female));
                                }

                                String phone =document.get("p_phone").toString();
                                et_telephone.setText(phone);

                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            String TAG = "Patient";
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }*/
    private void getProblemData() {
        progressBarr.setVisibility(View.VISIBLE);
        SharedPreferences prefs = _cxt.getSharedPreferences(AppService.appkey, MODE_PRIVATE);
        String token = prefs.getString("token", "");
        // mAuth = FirebaseAuth.getInstance();
        Intent i= getIntent();
        String id= i.getStringExtra("id");


        SharedPreferences.Editor editor = getSharedPreferences(AppService.appkey, MODE_PRIVATE).edit();
        editor.putString("token1",id );
        editor.apply();


        firestoreDB.collection("Users").document(token).collection("Patient").document(id).collection("Problem")
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

    /*public void add_Visit(View view) {
        Intent i= getIntent();
        String id= i.getStringExtra("id");


        Intent intent = new Intent(_cxt,VisitActivity.class);
        intent.putExtra("id1", id);

        SharedPreferences.Editor editor = getSharedPreferences(PalcoreStoreService.appkey, MODE_PRIVATE).edit();
        editor.putString("token11",id );
        editor.apply();
        startActivity(intent);
    }
    //-- refresh profile after add visit

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
*///
    public void UpdateP(View view) {

        dialog = new ProgressDialog(this);
        firestoreDB = FirebaseFirestore.getInstance();
        SharedPreferences prefs = getSharedPreferences(AppService.appkey, MODE_PRIVATE);
        final String token = prefs.getString("token", "");
        Intent i= getIntent();
        final String id= i.getStringExtra("id");

        mAuth = FirebaseAuth.getInstance();
        String doctorId = mAuth.getUid();

        String patientID, patientName, patientAge, NDC,phone;
        String patientٍSex = null, patientSmoke;
        patientName = tv_Name_Patient.getText().toString();

        patientAge = et_Add_Age_Patient.getText().toString().trim();
        phone = et_telephone.getText().toString().trim();
        int selectedId_smoking = radio_group_smoking.getCheckedRadioButtonId();
        radioButton_smoking = (RadioButton) findViewById(selectedId_smoking);
        patientSmoke = radioButton_smoking.getText().toString();

        //ImageView imageView= (ImageView)findViewById(R.id.img_patient);

/*
        if (patientٍSex.equals("ذكر")){
            patientٍSex = "ذكر";
        }else{
            patientٍSex = "أنثى";
        }*/

        /*int selectedId_SEX = radio_group_sex.getCheckedRadioButtonId();
        radioButton_SEX = (RadioButton) findViewById(selectedId_SEX);
        patientٍSex = radioButton_SEX.getText().toString();*/

        boolean sugar,Psychological,pressure;
        sugar = checkbox_sugar.isChecked();
        Psychological = checkbox_Psychological.isChecked();
        pressure = checkbox_pressure.isChecked();


        dialog.setTitle("تحديث  ");
        dialog.setMessage("يتم تعديل البيانات  ...");
        dialog.show();
        final Patient patient = new Patient(doctorId,patientName,patientAge,patientٍSex,patientSmoke,
                myDate1String,pressure,Psychological,sugar,phone);

        firestoreDB.collection("Users").document(token).collection("Patient").document(id)
                .update("patientAge",patientAge
                        ,"p_phone",phone
                        ,"patientSmoke",patientSmoke
                        ,"is_htn",pressure
                        ,"is_dm",sugar
                        ,"is_psychic",Psychological)

                //   .set(patient)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.dismiss();
                        finish();
                        startActivity(getIntent());
                        //   startActivity(new Intent(Profile_Patient_Activity.this,ListPateintActivity.class));
                        // Toast.makeText(EditPateintActivity.this, "Done...", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profile_Patient_Activity.this, "Failure...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void add_Problem(View view) {

        Intent intent = new Intent(_cxt,ProblemOtherActivity.class);
        startActivity(intent);
    }
    //-- refresh profile after add visit

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    public void deletePa(View view) {
        dialog = new ProgressDialog(this);
        //get User document (token)
        SharedPreferences prefs = getSharedPreferences(AppService.appkey, MODE_PRIVATE);
        final String token = prefs.getString("token", "");
        //get Patient document (id)
        Intent i= getIntent();
        final String id= i.getStringExtra("id");
        /*dialog.setTitle("Delete ");
        dialog.setMessage("Deleting ...");
        dialog.show();*/

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            builder = new AlertDialog.Builder(_cxt, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(_cxt);
        }
        builder.setTitle("إنتبه")
                .setMessage("هل انت متأكد من حذف المريض")
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        firestoreDB.collection("Users").document(token).collection("Patient").document(id)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        finish();
                                        //startActivity(getIntent());
                                        startActivity(new Intent(Profile_Patient_Activity.this,ListPateintActivity.class));
                                        //Toast.makeText(EditPateintActivity.this, "Patient is deleted ...", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(Profile_Patient_Activity.this, "هنالك خطأ", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.cancel();
                        //Toast.makeText(_cxt, "no", Toast.LENGTH_SHORT).show();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();







    }
}
