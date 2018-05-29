package com.dental.anisandmahmmoud.dentalaandm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.onesignal.OneSignal;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth auth;
    CardView btn_add_patient , btn_list , btn_info ,btn_hours ;
    AlertDialog.Builder dlgAlert;
    Context _cxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        auth = FirebaseAuth.getInstance();
        _cxt = this;
        btn_add_patient = (CardView) findViewById(R.id.btn_add_patient);
        btn_list = (CardView) findViewById(R.id.btn_list);
        btn_info = (CardView) findViewById(R.id.btn_info);
        btn_hours = (CardView) findViewById(R.id.btn_hours);
//        btn_exit = (CardView) findViewById(R.id.btn_Exit);
        dlgAlert  = new AlertDialog.Builder(this);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        OneSignal.sendTag("Doc_ID",user.getEmail());
        btn_add_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(_cxt,AddPateintActivity.class);
                startActivity(intent);

            }
        });

        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(_cxt,ListPateintActivity.class);
                startActivity(intent);

            }
        });


        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(_cxt,Profile_Doc_Activity.class);
                startActivity(intent);

            }
        });

        btn_hours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(HomeActivity.this, "date collection ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(_cxt,DatesActivity.class);
                startActivity(intent);
            }
        });

//        btn_exit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
//                    builder = new AlertDialog.Builder(_cxt, android.R.style.Theme_Material_Dialog_Alert);
//                } else {
//                    builder = new AlertDialog.Builder(_cxt);
//                }
//                builder.setTitle("تسجيل الخروج")
//                        .setMessage("هل انت متأكد من تسجيل الخروج")
//                        .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                auth = FirebaseAuth.getInstance();
//                                auth.signOut();
//                                finish();
//                            }
//                        })
//                        .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // do nothing
//                                dialog.cancel();
//                                //Toast.makeText(_cxt, "no", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
//
//
//            }
//        });


    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            builder = new AlertDialog.Builder(_cxt, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(_cxt);
        }
        builder.setTitle("تسجيل الخروج")
                .setMessage("هل انت متأكد من الخروج من التطبيق ")
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       /* auth = FirebaseAuth.getInstance();
                        auth.signOut();
                        finish();*/
                        ActivityCompat.finishAffinity(HomeActivity.this);
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




