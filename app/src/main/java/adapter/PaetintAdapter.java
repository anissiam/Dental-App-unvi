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

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Patient;
import utils.AppService;

import static android.content.Context.MODE_PRIVATE;

public class PaetintAdapter extends RecyclerView.Adapter<PaetintAdapter.PaetintHolder> {
    FirebaseAuth mAuth;
    Context _cxt;
    ArrayList<Patient> list_patient;


    public PaetintAdapter(Context context, ArrayList<Patient> patient) {

        list_patient = patient;
        _cxt = context;
    }

    @NonNull
    @Override
    public PaetintHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.paetint_row, parent, false);
        PaetintHolder holder = new PaetintHolder(row);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PaetintHolder holder, int position) {
        // mAuth = FirebaseAuth.getInstance();
        // FirebaseUser user = mAuth.getCurrentUser();
        //  Patient patient = list_patient.get(position);
        SharedPreferences prefs = _cxt.getSharedPreferences(AppService.appkey, MODE_PRIVATE);
        final String token = prefs.getString("token", "");
        FirebaseFirestore firestoreDB;
        final Patient item = list_patient.get(position);
        //Toast.makeText(_cxt, item.getDocumentid(), Toast.LENGTH_SHORT).show();
        holder.tv_name.setText(item.getPatientName());
        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_cxt, Profile_Patient_Activity.class);
                intent.putExtra("id", item.getDocumentid());
                _cxt.startActivity(intent);
            }
        });

        firestoreDB = FirebaseFirestore.getInstance();
        firestoreDB.collection("Users").document(token).collection("Patient").document(item.getDocumentid())
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
        });

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
        return list_patient.size();
    }

     class PaetintHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        Button btn_row_visit;
        RelativeLayout relative_data;
        CircleImageView img_profile1;
        private View vm;

        public PaetintHolder(View view) {
            super(view);
            vm = view;
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            img_profile1 = (CircleImageView) view.findViewById(R.id.img_profile1);

//            btn_row_visit = (Button) view.findViewById(R.id.btn_row_visit);
            relative_data = (RelativeLayout) view.findViewById(R.id.relative_data);

        }

    }

    public void setFilter(ArrayList<Patient> newlist_patient) {
        list_patient = new ArrayList<>();
        list_patient.addAll(newlist_patient);
        notifyDataSetChanged();
    }
}