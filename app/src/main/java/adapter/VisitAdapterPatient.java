package adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dental.anisandmahmmoud.dentalaandm.EditVisitActivity;
import com.dental.anisandmahmmoud.dentalaandm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import model.VisitPatient;
import utils.AppService;

import static android.content.Context.MODE_PRIVATE;



public class VisitAdapterPatient extends RecyclerView.Adapter<VisitAdapterPatient.visitHolder>{

    Context context;
    ArrayList<VisitPatient> List_vist;
    FirebaseFirestore firestore ;
    private AlertDialog.Builder dlgAlert;
    // Intent i = new Intent(); ;
    public VisitAdapterPatient(Context context, ArrayList<VisitPatient> list_vist) {
        this.context = context;
        List_vist = list_vist;
    }

    @NonNull
    @Override
    public visitHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_visit_patient,parent,false);
        visitHolder holder = new visitHolder (row);
        return  holder ;
    }

    @Override
    public void onBindViewHolder(@NonNull final visitHolder holder, final int position) {
        dlgAlert  = new AlertDialog.Builder(context);
        /////token is a user id (mAth.getid)
        SharedPreferences prefs = context.getSharedPreferences(AppService.appkey, MODE_PRIVATE);
        final String doctorId = prefs.getString("doctorId", "");
        final String patientId = prefs.getString("patientId", "");
        final String ProblemID = prefs.getString("ProblemID", "");

        final VisitPatient item = List_vist.get(position);

        firestore= FirebaseFirestore.getInstance();

///////==>0

        //holder.tv_number_visit.setText((CharSequence) item);


        Date dateB = item.getVisitDate();
        if (dateB!=null){
            Locale locale = new Locale("ar");
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE : MMMM / d / yyyy",locale);
            String date = dateFormat.format(dateB);
            holder.tv_date.setText(date);


            SimpleDateFormat dateFormat1 = new SimpleDateFormat("h:mm a",locale);
            String time = dateFormat1.format(dateB);
            holder.tv_hour.setText(time);
        }else {
            firestore.collection("Users").document(doctorId).collection("Patient").document(patientId)
                    .collection("Problem").document(ProblemID)
                    .collection("visitPatient").document(item.getDocumentid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                String TAG = "Patient";
                                if (document != null && document.exists()) {
                                    Date dateB = document.getDate("visitDate");
                                    Locale locale = new Locale("ar");
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE : MMMM / d / yyyy",locale);
                                    String date = dateFormat.format(dateB);
                                    holder.tv_date.setText(date);

                                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("h:mm a",locale);
                                    String time = dateFormat1.format(dateB);
                                    holder.tv_hour.setText(time);
                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                String TAG = "Problem";
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
        }
        holder.tv_total.setText(item.getPaymentDone());




    }

    @Override
    public int getItemCount() {
        return List_vist.size();
    }
    class visitHolder extends RecyclerView.ViewHolder {
        TextView tv_hour, tv_date,tv_total;
        ImageView btn_Edit,btn_delete;
        private View vm;

        public visitHolder(View itemView) {
            super(itemView);
            vm=itemView;
            tv_hour= (TextView) itemView.findViewById(R.id.tv_hour);
            tv_date = (TextView)itemView.findViewById(R.id.tv_date);
            tv_total= (TextView)itemView.findViewById(R.id.tv_total);

        }
    }

}
