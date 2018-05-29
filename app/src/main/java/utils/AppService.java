package utils;

import android.app.Application;

import com.google.firebase.firestore.FirebaseFirestore;

public class AppService extends Application{
    public static String  appkey = "com.dental.anisandmahmmoud.dentalaandm";
    static FirebaseFirestore db ;

    @Override
    public void onCreate() {
        super.onCreate();
        db = FirebaseFirestore.getInstance();
    }

    public static FirebaseFirestore getDb() {
        return db;
    }

    public static String getAppkey() {
        return appkey;
    }
}
