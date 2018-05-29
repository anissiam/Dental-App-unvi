package com.dental.anisandmahmmoud.dentalaandm;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.alamkanak.weekview.WeekView;
import com.google.firebase.auth.FirebaseAuth;

public class DatesActivity extends AppCompatActivity {
    CardView btn_today_view,btn_month,btn_week,btn_year;
    Context _cxt;
    AlertDialog.Builder dlgAlert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dates);
        _cxt = this;
        btn_today_view = (CardView) findViewById(R.id.btn_today_view);
        btn_month = (CardView) findViewById(R.id.btn_month);
        btn_week = (CardView) findViewById(R.id.btn_week);
        btn_year = (CardView) findViewById(R.id.btn_year);
//        btn_exit = (CardView) findViewById(R.id.btn_Exit);
        dlgAlert  = new AlertDialog.Builder(this);

        btn_today_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(_cxt,TodayActivity.class);
                startActivity(intent);

            }
        });

        btn_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_cxt,WeekActivity.class);
                startActivity(intent);
            }
        });

        btn_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_cxt,MonthActivity.class);
                startActivity(intent);
            }
        });

        btn_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_cxt,YearActivity.class);
                startActivity(intent);
            }
        });
    }
}
