package com.lifeshortcuts.tenminutes2;

import android.Manifest;
import android.content.Context;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Context mContext;
    boolean offhook = false;
    boolean idle = false;

    long start = 0;
    long end = 0;

    TextView userCallTime;
    SharedPreference mSharedPreference;
    Button deleteTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        mSharedPreference = new SharedPreference(mContext);
        userCallTime = (TextView) findViewById(R.id.id_get_current_call_time);
        userCallTime.setText(String.valueOf(mSharedPreference.getValue("duration", 0l)/60)+getString(R.string.str_unit_minutes));

        deleteTime = (Button) findViewById(R.id.button);

        deleteTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSharedPreference.remove("duration");
                userCallTime.setText("0"+getString(R.string.str_unit_minutes));
            }
        });



        findViewById(R.id.id_bt_do_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:131"));
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                    return;
                }
                startActivity(intent);
            }
        });

        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        manager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private PhoneStateListener phoneStateListener = new PhoneStateListener() {
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d("call state", "idle");
                    if(offhook) {
                        idle = true;
                        offhook = false;

                        end = System.currentTimeMillis();
                        Log.d("call", "call"+(end - start)/1000);
                        long duration = ((end - start)/1000);
                        if((duration + mSharedPreference.getValue("duration", 0l)) <= (12 * 60)) {
                            long min = (duration + mSharedPreference.getValue("duration", 0l));


                            if ((mSharedPreference.getValue("duration", 0l)/60) <= (12 * 60)) {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:131"));
                                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.

                                    return;
                                }
                                startActivity(intent);
                            }
                        }
                        userCallTime.setText(String.valueOf((duration + mSharedPreference.getValue("duration", 0l))/60) + getString(R.string.str_unit_minutes));
                        mSharedPreference.put("duration", duration + mSharedPreference.getValue("duration", 0l));
                    } else {
                        idle = false;
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d("call state", "offhook");
                    offhook = !offhook;
                    start = System.currentTimeMillis();
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d("call state", "ringing");
                    break;

            }

        }
    };
}
