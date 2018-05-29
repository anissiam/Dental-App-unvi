package adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dental.anisandmahmmoud.dentalaandm.EditProblemActivity;
import com.dental.anisandmahmmoud.dentalaandm.ListVisitActivity;
import com.dental.anisandmahmmoud.dentalaandm.ListVisitPatientActivity;
import com.dental.anisandmahmmoud.dentalaandm.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import model.Problem;
import utils.AppService;

import static android.content.Context.MODE_PRIVATE;


public class ProblemAdapterPatient extends RecyclerView.Adapter<ProblemAdapterPatient.ProblemHolder>{
    Context context;
    ArrayList<Problem> List_Problem;
    private FirebaseFirestore firestore ;
    private AlertDialog.Builder dlgAlert;
    public ProblemAdapterPatient(Context context, ArrayList<Problem> list_Problem) {
        this.context = context;
        List_Problem = list_Problem;
    }

    @NonNull
    @Override
    public ProblemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_problem_patient,parent,false);
        ProblemAdapterPatient.ProblemHolder holder = new ProblemAdapterPatient.ProblemHolder(row);
        return  holder ;
    }

    @Override
    public void onBindViewHolder(@NonNull ProblemHolder holder, final int position) {
        final Problem item = List_Problem.get(position);
        firestore= FirebaseFirestore.getInstance();
        //--set Date to row ////////////////
        holder.tv_Problem.setText(item.getProProblem());
        //--convert the Date to arabic and cat it to EEEE : MMMM / d / yyyy
        Date dateB = item.getProDate();
        Locale locale = new Locale("ar");
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE : MMMM / d / yyyy",locale);
        // DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.DAY_OF_WEEK_FIELD , locale);
        //DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat. , locale);
        String date = dateFormat.format(dateB);
        holder.tv_Date_Problem.setText(date);
        holder.tv_place.setText(item.getProPlace());
        ///////////////////////////////////

        dlgAlert  = new AlertDialog.Builder(context);
        /////token is a user id (mAth.getid)
        SharedPreferences prefs = context.getSharedPreferences(AppService.appkey, MODE_PRIVATE);
        final String token = prefs.getString("token", "");
        ////token1 is a Patient id
        final String token1 = prefs.getString("token1", "");
        //Toast.makeText(context, token1, Toast.LENGTH_SHORT).show();



        holder.vm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ListVisitPatientActivity.class);
                intent.putExtra("ProblemID",item.getDocumentid());
                //Intent intent1 = new Intent(_cxt,VisitActivity.class);
                //intent.putExtra("id1",item.getDocumentid());*//*
                //Toast.makeText(_cxt, item.getDocumentid(), Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return List_Problem.size();
    }

    public class ProblemHolder extends RecyclerView.ViewHolder {
        private TextView tv_Problem,tv_Date_Problem,tv_place;
        private Button btn_Problem_delete,btn_Problem_edit;
        private View vm;
        public ProblemHolder(View itemView) {
            super(itemView);
            vm=itemView;
            tv_place = (TextView)itemView.findViewById(R.id.tv_place);
            tv_Problem= (TextView) itemView.findViewById(R.id.tv_Problem);
            tv_Date_Problem= (TextView) itemView.findViewById(R.id.tv_Date_Problem);
           /* btn_Problem_delete= (Button) itemView.findViewById(R.id.btn_Problem_delete);
            btn_Problem_edit = (Button) itemView.findViewById(R.id.btn_Problem_edit);*/
        }
    }
}
