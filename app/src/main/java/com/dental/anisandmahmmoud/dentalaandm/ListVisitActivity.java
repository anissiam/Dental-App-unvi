package com.dental.anisandmahmmoud.dentalaandm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import adapter.VisitAdepter;
import model.VisitPatient;
import model.vistPatient;
import utils.AppService;

public class ListVisitActivity extends AppCompatActivity {
    RecyclerView recyclerView ;
    VisitAdepter adepter;
    ArrayList<VisitPatient> List_vist;
    private FirebaseFirestore firestoreDB;
    Context _cxt;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_list);
        List_vist = new ArrayList<>();
        _cxt = this;
        recyclerView = (RecyclerView) findViewById(R.id.rv_visit);
        recyclerView.setHasFixedSize(true);
        firestoreDB = FirebaseFirestore.getInstance();
        recyclerView.setLayoutManager(new LinearLayoutManager(_cxt));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("قائمة الزيارات ");
        setSupportActionBar(toolbar);

        adepter = new VisitAdepter(_cxt,List_vist);
        recyclerView.setAdapter(adepter);
        /*Intent i= getIntent();
        String id= i.getStringExtra("id");*/
        //Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
        getVisit();
    }

    private void getVisit() {
        SharedPreferences prefs = _cxt.getSharedPreferences(AppService.appkey, MODE_PRIVATE);
        String token = prefs.getString("token", "");
        ////token1 is a Patient id
        final String token1 = prefs.getString("token1", "");
        // mAuth = FirebaseAuth.getInstance();

        //-- init firestore DB
        firestoreDB = FirebaseFirestore.getInstance();

        Intent i= getIntent();
        String id= i.getStringExtra("id");

        Intent ii= getIntent();
        String ProblemID = ii.getStringExtra("ProblemID");

        SharedPreferences.Editor editor = getSharedPreferences(AppService.appkey, MODE_PRIVATE).edit();
        editor.putString("ProblemID",ProblemID );
        editor.apply();


        firestoreDB.collection("Users").document(token).collection("Patient").document(token1)
                .collection("Problem").document(ProblemID)
                .collection("visitPatient")
                .orderBy("visitDate", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot document : task.getResult()) {

                                /*Intent intent = new Intent(_cxt,VisitActivity.class);
                                intent.putExtra("id1",document.getId());*/

                    VisitPatient v= new VisitPatient();
                    //  v.setVistID(document.getString("vistID"));
                    v.setDocumentid(document.getId());
                    // v.setDocumentid(document.getId());
                    v.setVisitDate(document.getDate("visitDate"));
                    v.setPaymentDone(document.getString("paymentDone"));
                    List_vist.add(v);
                    adepter.notifyDataSetChanged();

                    //    Toast.makeText(_cxt, , Toast.LENGTH_SHORT).show();
                    Log.d("hjh", document.getId() + " => " + document.getData());

                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    public void add_Visit(View view) {
        Intent i= getIntent();
        String id= i.getStringExtra("id");


        Intent intent = new Intent(_cxt,AddNextVisitActivity.class);
        intent.putExtra("id1", id);

        SharedPreferences.Editor editor = getSharedPreferences(AppService.appkey, MODE_PRIVATE).edit();
        editor.putString("token11",id );
        editor.apply();
        startActivity(intent);
    }
}
