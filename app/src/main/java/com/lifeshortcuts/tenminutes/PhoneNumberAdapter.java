package com.lifeshortcuts.tenminutes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by coriel on 2016. 3. 17..
 */
public class PhoneNumberAdapter extends BaseAdapter {

    private ArrayList<String> data;
    private Context mContext;

    public PhoneNumberAdapter(Context mContext, ArrayList<String> data) {
        this.data = data;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

          if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
              convertView = vi.inflate(R.layout.item_lv_phone_number, null);
        }

        TextView lvPhoneNumber = (TextView) convertView.findViewById(R.id.id_tv_phone_number);
        lvPhoneNumber.setText(data.get(position));

       return convertView;
    }
}
