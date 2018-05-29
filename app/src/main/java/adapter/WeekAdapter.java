package adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dental.anisandmahmmoud.dentalaandm.Profile_Patient_Activity;
import com.dental.anisandmahmmoud.dentalaandm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import model.vistPatient;
import utils.AppService;

import static android.content.Context.MODE_PRIVATE;

public class WeekAdapter  extends RecyclerView.Adapter<WeekAdapter.WeekHolder> {


    FirebaseAuth mAuth;
    Context _cxt;
    private ArrayList<vistPatient> List_vist;
    private FirebaseFirestore firestoreDB;
    public WeekAdapter(Context _cxt, ArrayList<vistPatient> List_vist) {
        this._cxt = _cxt;
        this.List_vist = List_vist;
    }

    @NonNull
    @Override
    public WeekHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_today, parent, false);
        WeekAdapter.WeekHolder holder = new WeekAdapter.WeekHolder(row);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final WeekHolder holder, int position) {
        // mAuth = FirebaseAuth.getInstance();
        // FirebaseUser user = mAuth.getCurrentUser();
        //  Patient patient = list_patient.get(position);
        SharedPreferences prefs = _cxt.getSharedPreferences(AppService.appkey, MODE_PRIVATE);
        final String token = prefs.getString("token", "");

        final vistPatient item = List_vist.get(position);

        Date date = item.getVisitDate();

        // SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("d-MM-yyyy");
        String filename = timeStampFormat.format(date);

        Date tday= new Date();
        SimpleDateFormat timeStampFormat1 = new SimpleDateFormat("d-MM-yyyy");
        String filename1 = timeStampFormat.format(date);


        //  Toast.makeText(_cxt, filename, Toast.LENGTH_SHORT).show();




        //Toast.makeText(_cxt, item.getDocumentid(), Toast.LENGTH_SHORT).show();
        holder.tv_name.setText(item.getPatentName());
        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_cxt, Profile_Patient_Activity.class);
                intent.putExtra("id", item.getDocumentid());
                _cxt.startActivity(intent);
            }
        });





        firestoreDB = FirebaseFirestore.getInstance();
        holder.tv_name.setText(item.getPatentName());
        firestoreDB.collection("VisitList").document()
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    String TAG = "Patient";
                    if (document != null && document.exists()) {
                        String patientName = document.get("patientName").toString();


                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    String TAG = "Patient";
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });





        /*firestoreDB.collection("Users").document(token).collection("Patient").document(patient.getDocumentid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    String TAG = "Patient";
                    if (document != null && document.exists()) {
                        String patientٍSex_ed = document.get("patientٍSex").toString();

                        if (patientٍSex_ed.equals("ذكر")) {
                            holder.img_profile1.setImageDrawable(_cxt.getResources().getDrawable(R.drawable.male));

                        } else {
                            holder.img_profile1.setImageDrawable(_cxt.getResources().getDrawable(R.drawable.female));

                        }

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    String TAG = "Patient";
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });*/

        holder.vm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(_cxt, Profile_Patient_Activity.class);
                intent.putExtra("id", item.getDocumentid());
                _cxt.startActivity(intent);


            }
        });


//        holder.btn_row_visit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                /*SharedPreferences.Editor editor = getSharedPreferences(PalcoreStoreService.appkey, MODE_PRIVATE).edit();
//                editor.putString("token1", item.getDocumentid());
//                editor.apply();*/
//
//                Intent intent = new Intent(_cxt,ListVisitActivity.class);
//                intent.putExtra("id",item.getDocumentid());
//               /* Intent intent1 = new Intent(_cxt,VisitActivity.class);
//                intent.putExtra("id1",item.getDocumentid());*/
//             //  Toast.makeText(_cxt, item.getDocumentid(), Toast.LENGTH_SHORT).show();
//                _cxt.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return List_vist.size();
    }

    public class WeekHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        Button btn_row_visit;
        private RelativeLayout relative_data;
        private CircleImageView img_profile1;
        private View vm;
        public WeekHolder(View view){
            super(view);
            vm = view;
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            img_profile1 = (CircleImageView) view.findViewById(R.id.img_profile1);

//            btn_row_visit = (Button) view.findViewById(R.id.btn_row_visit);
            relative_data = (RelativeLayout) view.findViewById(R.id.relative_data);
        }
    }
    public void setFilter(ArrayList<vistPatient> newlist_visit) {
        List_vist = new ArrayList<>();
        List_vist.addAll(newlist_visit);
        notifyDataSetChanged();
    }
}

