package com.dental.anisandmahmmoud.dentalaandm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.UnicodeSetSpanner;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.onesignal.OneSignal;

import utils.AppService;

public class SplashMainActivity extends AppCompatActivity {
    TextView tv_welcome ;
    ImageView img_welcome ;
    Context _cxt;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_main);
        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        _cxt = this;
        tv_welcome = (TextView) findViewById(R.id.tv_welcome);
        img_welcome = (ImageView) findViewById(R.id.img_welcome);

        Animation animation = AnimationUtils.loadAnimation(_cxt,R.anim.mytransition);
        tv_welcome.startAnimation(animation);
        img_welcome.startAnimation(animation);

        final Intent intent=new Intent(_cxt,UsersActivity.class);



        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        /*final String email= user.getEmail();
        Toast.makeText(this, email, Toast.LENGTH_SHORT).show();
        final String phone =user.getPhoneNumber();*/


        //--Home intent
        final Intent Homeintent=new Intent(_cxt,UsersActivity.class);

        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(3000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {

                    /*if (mAuth.getCurrentUser() != null &&  user.getEmail() != null ) {
                        finish();
                        startActivity(new Intent(getApplicationContext(), ProfilePatientPatientActivity.class));
                    }else if (mAuth.getCurrentUser() != null && user.getPhoneNumber() != null ){
                        finish();
                        startActivity(new Intent(getApplicationContext(), ProfilePatientPatientActivity.class));
                    }else {*/
                        finish();
                        startActivity(new Intent(getApplicationContext(), UsersActivity.class));

                    /*SharedPreferences prefs = _cxt.getSharedPreferences(AppService.appkey, MODE_PRIVATE);
                    String token = prefs.getString("token", "");
                    if(!token.isEmpty()){
                        //Toast.makeText(SplashMainActivity.this, "a", Toast.LENGTH_SHORT).show();

                        startActivity(intent);
                        finish();
                    }else{
                      //  Toast.makeText(SplashMainActivity.this, "bbbbbbbb", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    }*/



                }
            }
        };

        timer.start();
    }
}
