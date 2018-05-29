package com.dental.anisandmahmmoud.dentalaandm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;
import utils.AppService;

public class Profile_Doc_Activity extends AppCompatActivity {
    private TextView tv_Name_Doc,tv_FullAdderss,tv_Tele,tv_Email,tv_Clinc;
    private CircleImageView img_doc ;
    private FirebaseFirestore firebaseFirestore;
    Context _cxt;
    private FirebaseAuth mAuth;
    private ProgressBar progressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__doc_);
        tv_Name_Doc = (TextView)findViewById(R.id.tv_Name_Doc);
        tv_FullAdderss = (TextView)findViewById(R.id.tv_FullAdderss);
        tv_Clinc= (TextView)findViewById(R.id.tv_Clinc);
        tv_Tele = (TextView)findViewById(R.id.tv_Tele);
        tv_Email = (TextView)findViewById(R.id.tv_Email);
        img_doc = (CircleImageView)findViewById(R.id.img_doc);
        progressbar = (ProgressBar)findViewById(R.id.progressBarr);
        firebaseFirestore = FirebaseFirestore.getInstance();
        _cxt = this;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        // img_doc=(CircleImageView) findViewById(R.id.img_doc);
        if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()!=null) {
            String url = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();
            Glide.with(Profile_Doc_Activity.this).load(url).into(img_doc);
        }
        fetchDocData();
    }

    public void fetchDocData(){
        progressbar.setVisibility(View.VISIBLE);
        tv_Name_Doc = (TextView)findViewById(R.id.tv_Name_Doc);
        tv_FullAdderss = (TextView)findViewById(R.id.tv_FullAdderss);
        tv_Clinc= (TextView)findViewById(R.id.tv_Clinc);
        tv_Tele = (TextView)findViewById(R.id.tv_Tele);
        tv_Email = (TextView)findViewById(R.id.tv_Email);
        img_doc = (CircleImageView)findViewById(R.id.img_doc);
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
                            progressbar.setVisibility(View.GONE);
                            DocumentSnapshot document = task.getResult();
                            String TAG = "docView";
                            if (document != null && document.exists()) {
                                String DocName=  document.get("doctorName").toString();
                                tv_Name_Doc.setText(DocName);

                                String  doctorEmail = document.get("doctorEmail").toString();
                                tv_Email.setText(doctorEmail );

                                String  clinicPhone = document.get("clinicPhone").toString();
                                tv_Tele.setText(clinicPhone );

                                String  clinicName = document.get("clinicName").toString();
                                tv_Clinc.setText(clinicName );

                                String  clinicAddress = document.get("clinicAddress").toString();
                                tv_FullAdderss.setText(clinicAddress );

                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            String TAG = "docView";
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profile_Doc_Activity.this, "خطأ", Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Profile_Doc_Activity.this,HomeActivity.class));

    }

    public void editDoc(View view) {

        startActivity(new Intent(Profile_Doc_Activity.this,Activity_View_Doc.class));

    }

    public void signOut(View view) {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            builder = new AlertDialog.Builder(_cxt, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(_cxt);
        }
        builder.setTitle("تسجيل الخروج")
                .setMessage("هل انت متأكد من تسجيل الخروج")
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth = FirebaseAuth.getInstance();
                        mAuth.signOut();
                        ActivityCompat.finishAffinity(Profile_Doc_Activity.this);
                        finish();
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

