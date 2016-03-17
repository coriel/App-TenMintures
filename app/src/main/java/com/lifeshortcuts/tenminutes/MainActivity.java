package com.lifeshortcuts.tenminutes;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lvPhoneNumberList;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        ArrayList<String> alPhoneNumber = new ArrayList<String>();
        alPhoneNumber.add("1");
        alPhoneNumber.add("2");
        alPhoneNumber.add("3");
        alPhoneNumber.add("4");
        alPhoneNumber.add("5");

        lvPhoneNumberList = (ListView) findViewById(R.id.id_lv_phone_number);
        PhoneNumberAdapter pnAdapter = new PhoneNumberAdapter(mContext, alPhoneNumber);
        lvPhoneNumberList.setAdapter(pnAdapter);
    }


}
