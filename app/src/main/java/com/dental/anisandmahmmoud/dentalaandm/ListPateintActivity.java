package com.dental.anisandmahmmoud.dentalaandm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import adapter.PaetintAdapter;
import model.Patient;
import utils.AppService;

public class ListPateintActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    RecyclerView rv_paetint;
    Context _cxt;
    FirebaseAuth mAuth;
    ArrayList<Patient> patient;
    PaetintAdapter Adapter;
    private FirebaseFirestore db;
    Toolbar toolbar;
    private ProgressBar progressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pateint);
        _cxt = this;
        patient = new ArrayList<Patient>();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("قائمة المرضى");
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        progressbar = (ProgressBar)findViewById(R.id.progressBarr);
        //db = AppService.getDb();
        db = FirebaseFirestore.getInstance();
        rv_paetint = (RecyclerView) findViewById(R.id.rv_paetint);
        rv_paetint.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ListPateintActivity.this);
        /*mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);*/
        rv_paetint.setLayoutManager(mLayoutManager);

        Adapter = new PaetintAdapter(_cxt, patient);
        Adapter.notifyDataSetChanged();
        rv_paetint.setAdapter(Adapter);
        SharedPreferences prefs = _cxt.getSharedPreferences(AppService.appkey, MODE_PRIVATE);
        String token = prefs.getString("token", "");
        Toast.makeText(this, token, Toast.LENGTH_SHORT).show();
        //Collections.reverse(patient);
        // rv_paetint.smoothScrollToPosition(0);

        GetPateints();
        /*Patient patient1 = new Patient("aa","ss","sa",true,"ddd",false);
        patient.add(patient1);
        */
    }

//    public void material_design_ball_clip_rotate_multiple_loader() {
//        findViewById(R.id.material_design_ball_clip_rotate_multiple_loader)
//                .setVisibility(View.VISIBLE);
//
//    }

    public void GetPateints()
    {

        progressbar.setVisibility(View.VISIBLE);
        //--read from sharepreferences
        SharedPreferences prefs = _cxt.getSharedPreferences(AppService.appkey, MODE_PRIVATE);
        String token = prefs.getString("token", "");
        mAuth = FirebaseAuth.getInstance();
        db.collection("Users").document(token).collection("Patient")
                .orderBy("patientDate", Query.Direction.DESCENDING)

                //.whereEqualTo("doctorId", mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    public String TAG = "Patient";
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            progressbar.setVisibility(View.GONE);
                            for (DocumentSnapshot document : task.getResult()) {

                                /*Intent intent = new Intent(_cxt,VisitActivity.class);
                                intent.putExtra("id11",document.getId());
                                startActivity(intent);*/
                                Patient p = new Patient();
                                p.setDocumentid(document.getId());
                                p.setPatientName(document.getString("patientName")+"");
                                patient.add(p);
                                Adapter.notifyDataSetChanged();
                                //    Toast.makeText(_cxt, , Toast.LENGTH_SHORT).show();
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items,menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return true;
        //super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(ListPateintActivity.this,HomeActivity.class));
    }

    //-- declaration  search
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<Patient> newList = new ArrayList<>();
        for(Patient patient :patient){
            String name = patient.getPatientName().toLowerCase();
            if (name.contains(newText)){
                newList.add(patient);
            }
        }
        Adapter.setFilter(newList);
        return true;
    }

}
