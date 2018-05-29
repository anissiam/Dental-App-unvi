package com.dental.anisandmahmmoud.dentalaandm;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import adapter.TodayAdepter;
import adapter.WeekAdapter;
import model.vistPatient;
import utils.AppService;

public class WeekActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private RecyclerView rv_paetint;
    private Context _cxt;
    private TextView dateview ;
    private FirebaseAuth mAuth;
    private ArrayList<vistPatient> List_vist;
    private WeekAdapter Adapter;
    private FirebaseFirestore db;
    private Toolbar toolbar1;
    private ProgressBar progressbar;
    private Date date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);
        dateview = (TextView)findViewById(R.id.dateview);
        _cxt = this;
        List_vist = new ArrayList<vistPatient>();
        toolbar1 = (Toolbar) findViewById(R.id.toolbar);
        toolbar1.setTitle("قائمة المرضى");
        setSupportActionBar(toolbar1);
        mAuth = FirebaseAuth.getInstance();
        progressbar = (ProgressBar)findViewById(R.id.progressBarr);
        //db = AppService.getDb();
        db = FirebaseFirestore.getInstance();
        rv_paetint = (RecyclerView) findViewById(R.id.rv_paetint);
        rv_paetint.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(WeekActivity.this);
        /*mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);*/
        rv_paetint.setLayoutManager(mLayoutManager);

        Adapter = new WeekAdapter (_cxt, List_vist);
        Adapter.notifyDataSetChanged();
        rv_paetint.setAdapter(Adapter);
        SharedPreferences prefs = _cxt.getSharedPreferences(AppService.appkey, MODE_PRIVATE);
        String token = prefs.getString("token", "");
        Toast.makeText(this, token, Toast.LENGTH_SHORT).show();
        //Collections.reverse(patient);
        // rv_paetint.smoothScrollToPosition(0);
         date = new Date();
        //  Date dateB =  (Date)document.get("visitDate");
        Locale locale = new Locale("ar");
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE : MMMM / d ",locale);
        // DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.DAY_OF_WEEK_FIELD , locale);
        //DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat. , locale);
        String date1 = dateFormat.format(date);
        dateview.setText(date1);
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
        // String da = "5/9/2018";
      /*  final Date date = new Date();
        Date tomorrow = new Date(date.getTime() + (1000 * 60 * 60 * 24));

        Date yesterday =  new Date(date.getTime() - (1000 * 60 * 60 * 24));


        Calendar currentCalendar = Calendar.getInstance();
        int week = currentCalendar.get(Calendar.DAY_OF_WEEK);
        //  int year = currentCalendar.get(Calendar.YEAR);
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(date);
        int targetWeek = targetCalendar.get(Calendar.DAY_OF_WEEK);

        //   return week == targetWeek ;
        Date week_n = new Date(date.getTime() + (1000 * 60 * 60 * 24 *6));

        Date Week_l =  new Date(date.getTime() - (1000 * 60 * 60 * 24 *6));
        // Toast.makeText(this, yesterday.toString(), Toast.LENGTH_SHORT).show();
      *//*  Locale locale = new Locale("ar");
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE : MMMM / d ",locale);
        String date1 = dateFormat.format(date);*/




        progressbar.setVisibility(View.VISIBLE);
        //--read from sharepreferences
        SharedPreferences prefs = _cxt.getSharedPreferences(AppService.appkey, MODE_PRIVATE);
        String token = prefs.getString("token", "");
        mAuth = FirebaseAuth.getInstance();



        db.collection("VisitList")
                //  .orderBy("patientDate", Query.Direction.DESCENDING)
                //.whereEqualTo("visitDate",date)
                // .whereGreaterThan("visitDate",Week_l)
                // .whereLessThan("visitDate",week_n)
                //.whereEqualTo("doctorId", mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    public String TAG = "Patient";
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            progressbar.setVisibility(View.GONE);
                            for (DocumentSnapshot document : task.getResult()) {


                                SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat timeStampFormat_w = new SimpleDateFormat("EEEE");
                                String filename = timeStampFormat_w.format(date);



                                Date date1 = document.getDate("visitDate");
                                SimpleDateFormat timeStampFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat timeStampFormat_w1 = new SimpleDateFormat("EEEE");
                                String filename1 = timeStampFormat_w1.format(date1);
                                
                                Calendar calendar = Calendar.getInstance();
                                 int firstDayOfWeek=  calendar.getFirstDayOfWeek();


                                 if(  isDateInCurrentWeek(date1)){
                                     vistPatient v = new vistPatient();
                                     v.setDocumentid(document.getId());
                                     v.setVisitDate(document.getDate("visitDate"));
                                     v.setPatentName(document.getString("patentName"));
                                     List_vist.add(v);
                                 }

                               /* if (filename.equals(filename1)){
                                    vistPatient v = new vistPatient();
                                    v.setDocumentid(document.getId());
                                    v.setVisitDate(document.getDate("visitDate"));
                                    v.setPatentName(document.getString("patentName"));
                                    List_vist.add(v);
                                }*/





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
    public boolean isDateInCurrentWeek(Date date)
    {
        Date currentWeekStart, currentWeekEnd;

        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setFirstDayOfWeek(Calendar.SATURDAY);
        while(currentCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)
        {
            currentCalendar.add(Calendar.DATE,-1);//go one day before
        }
        currentWeekStart = currentCalendar.getTime();

        currentCalendar.add(Calendar.DATE, 7); //add 6 days after Monday
        currentWeekEnd = currentCalendar.getTime();

        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setFirstDayOfWeek(Calendar.SATURDAY);
        targetCalendar.setTime(date);


        Calendar tempCal = Calendar.getInstance();
        tempCal.setTime(currentWeekStart);

        boolean result = false;
        while(!(tempCal.getTime().after(currentWeekEnd)))
        {
            if(tempCal.get(Calendar.DAY_OF_YEAR)==targetCalendar.get(Calendar.DAY_OF_YEAR))
            {
                result=true;
                break;
            }
            tempCal.add(Calendar.DATE,1);//advance date by 1
        }

        return result;
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


    /*@Override
    public void onBackPressed() {
        startActivity(new Intent(TodayActivity.this,HomeActivity.class));
    }
*/
    //-- init search
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<vistPatient> newList = new ArrayList<>();
        for(vistPatient vi :List_vist){
            String name = vi.getPatentName().toLowerCase();
            if (name.contains(newText)){
                newList.add(vi);
            }
        }
        Adapter.setFilter(newList);
        return true;
    }

}

