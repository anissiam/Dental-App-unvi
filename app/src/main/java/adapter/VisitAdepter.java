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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dental.anisandmahmmoud.dentalaandm.EditVisitActivity;
import com.dental.anisandmahmmoud.dentalaandm.R;
import com.dental.anisandmahmmoud.dentalaandm.VisitActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import model.Patient;
import model.VisitPatient;
import model.vistPatient;
import utils.AppService;

import static android.content.Context.MODE_PRIVATE;
import java.util.ArrayList;

import model.vistPatient;
import utils.AppService;

import static android.content.Context.MODE_PRIVATE;

public class VisitAdepter extends RecyclerView.Adapter<VisitAdepter.visitHolder>{

    Context context;
    ArrayList<VisitPatient> List_vist;
    FirebaseFirestore firestore ;
    private AlertDialog.Builder dlgAlert;
    // Intent i = new Intent(); ;
    public VisitAdepter(Context context, ArrayList<VisitPatient> list_vist) {
        this.context = context;
        List_vist = list_vist;
    }

    @NonNull
    @Override
    public visitHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_visit,parent,false);
        visitHolder holder = new visitHolder (row);
        return  holder ;
    }

    @Override
    public void onBindViewHolder(@NonNull final visitHolder holder, final int position) {
        dlgAlert  = new AlertDialog.Builder(context);
        /////token is a user id (mAth.getid)
        SharedPreferences prefs = context.getSharedPreferences(AppService.appkey, MODE_PRIVATE);
        final String token = prefs.getString("token", "");
        ////token1 is a Patient id
        final String token1 = prefs.getString("token1", "");
        //Toast.makeText(context, token1, Toast.LENGTH_SHORT).show();
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
            firestore.collection("Users").document(token).collection("Patient").document(token1)
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


       /* holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("Users").document(token).collection("Patient").document(token1)
                        .collection("Problem").document(ProblemID)
                        .collection("visitPatient").document(item.getDocumentid())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "تم حذف الزيارة ", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });*/

        holder.btn_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,EditVisitActivity.class);
                intent.putExtra("id",item.getDocumentid());
                //Intent intent1 = new Intent(_cxt,VisitActivity.class);
                //intent.putExtra("id1",item.getDocumentid());
                //Toast.makeText(_cxt, item.getDocumentid(), Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle("إنتبه")
                .setMessage("هل انت متأكد من حذف الشكوى ")
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        firestore.collection("Users").document(token).collection("Patient").document(token1)
                                .collection("Problem").document(ProblemID)
                                .collection("visitPatient").document(item.getDocumentid())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, "تم حذف الزيارة ", Toast.LENGTH_SHORT).show();
                                        // refresh list after delete
                                        List_vist.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, List_vist.size());
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "خطأ حاول مرة اخرى ... ", Toast.LENGTH_SHORT).show();
                            }
                        });
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
});



        // final String visitRef = prefs.getString("tVisitRef", "");

        //


  /*      // Toast.makeText(context, item.getDocumentid(), Toast.LENGTH_SHORT).show();

        ///-- fireStore new instance

        firestore = FirebaseFirestore.getInstance();


        *//*Intent intent1 = ((VisitActivity) context).getIntent();
        String id1= intent1.getStringExtra("id1");
        Toast.makeText(context, id1, Toast.LENGTH_SHORT).show();*//*

        ////get Visit date from firestore and store it in row_visit
        firestore.collection("Users").document(token).collection("Patient").document(token1)
                .collection("visitPatient").document(item.getDocumentid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            String TAG = "Patient";
                            if (document != null && document.exists()) {
                                Date dateB =  (Date)document.get("visitDate");
                                Locale locale = new Locale("ar");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE : MMMM / d / yyyy",locale);
                                // DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.DAY_OF_WEEK_FIELD , locale);
                                //DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat. , locale);
                                String date = dateFormat.format(dateB);

                     *//*   Date date = null;
                        try {
                            date = dateFormat.parse(dateB);
                            holder.tv_Date_visit.setText(date.toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
*//*
                                holder.tv_Date_visit.setText(date);
                                //    holder.tv_Date_visit.setText(document.get("visitDate").toString());
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            String TAG = "Patient";
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });



        *//*firestore.collection("Users").document(token).collection("Patient").document(token1)
                .collection("visitPatient").whereEqualTo("documentid",item.getDocumentid())
                .get()
                .addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {

                                    holder.tv_Date_visit.setText(document.get("visitDate").toString());

                                }
                            } else {
                                Log.d("Error", "Error getting documents: ", task.getException());
                            }
                        }
                    }
                });*//*


        *//*.addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {


                                  *//**//*  String date1= document.get("visitDate").toString();

                                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                    try {
                                        Date date = dateFormat.parse(date1);
                                        holder.tv_Date_visit.setText(date.toString());

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }*//**//*

         *//**//* SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                    Date date = dateFormat*//**//*
                                    holder.tv_Date_visit.setText(document.get("visitDate").toString());
                                    *//**//*Locale locale = new Locale("ar");
                                    String date =  document.get("visitDate").toString();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",locale);
                                    Date date1 = new Date();
                                    try {
                                      //  //SimpleDateFormat finalDateFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy HH:mm:ss a",locale);
                                         date1 = sdf.parse(date);
                                        holder.tv_Date_visit.setText(date1.toString());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }*//**//*
                                  //  String  finalDate = finalDateFormat.format(date1);

                              //      holder.tv_Date_visit.setText(finalDate);
                                    *//**//*SimpleDateFormat finalDateFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy HH:mm:ss a",locale);
                                     String  finalDate = finalDateFormat.format(date1);*//**//*
         *//**//* Locale locale = new Locale("ar");
                                    SimpleDateFormat sdf = new SimpleDateFormat(("yyyy-MM-d"));
                                    Date date3 = null;
                                    try {
                                        date3 = sdf.parse(date);
                                    } catch (Exception e) {

                                    }
                                    sdf = new SimpleDateFormat("EEEE, MMM dd, yyyy HH:mm:ss a",locale);
                                    String format = sdf.format(date3);*//**//*
                                 //   System.out.print("Result: " + format);
                                 //   Log.wtf("result",format);
                                    //holder.tv_Date_visit.setText(finalDate);
                                    //holder.tv_Date_visit.setText(document.get("visitDate").toString());

                                }
                            } else {
                                Log.d("Error", "Error getting documents: ", task.getException());
                            }

                        *//**//*QuerySnapshot document = task.getResult();
                        holder.tv_Date_visit.setText(document.toString());*//**//*
                        }
                    }
                }
        );*//*

        //  holder.tv_Date_visit.setText(item.getVisitDate().toString());

        ////// delete button listener


        //Toast.makeText(context, item.getDocumentid(), Toast.LENGTH_SHORT).show();
        holder.btn_visit_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(context);
                }
                builder.setTitle("إنتبه")
                        .setMessage("هل انت متأكد من حذف الزيارة ")
                        .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                firestore.collection("Users").document(token).collection("Patient").document(token1)
                                        .collection("visitPatient").document(item.getDocumentid()).delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(context, "Done... ", Toast.LENGTH_SHORT).show();
                                                // refresh list after delete
                                                List_vist.remove(position);
                                                notifyItemRemoved(position);
                                                notifyItemRangeChanged(position, List_vist.size());
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Failure... ", Toast.LENGTH_SHORT).show();
                                    }
                                });
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
        });
*/

       /* holder.tv_Diagnosis_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,EditVisitActivity.class);
                intent.putExtra("id",item.getVistID());
                //Intent intent1 = new Intent(_cxt,VisitActivity.class);
                //intent.putExtra("id1",item.getDocumentid());
                //Toast.makeText(_cxt, item.getDocumentid(), Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });



        holder.vm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,EditVisitActivity.class);
                intent.putExtra("id",item.getDocumentid());
                //Intent intent1 = new Intent(_cxt,VisitActivity.class);
                //intent.putExtra("id1",item.getDocumentid());
                //Toast.makeText(_cxt, item.getDocumentid(), Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });*/

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
            btn_Edit = (ImageView)itemView.findViewById(R.id.btn_Edit);
            btn_delete = (ImageView)itemView.findViewById(R.id.btn_delete);
        }
    }

}
