package com.hackathon.lampyridaeAdmin;

import android.app.PendingIntent;
import android.content.Context;
import android.nfc.NfcAdapter;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class LightAdapter extends ArrayAdapter<lightInfo> {

    private ArrayList<lightInfo> lightItems;
    private Context context;
    TextView tvLightNum,tvAddress,tvManage, tvManageNumber;
    boolean mWriteMode = false;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mNfcPendingIntent;

    public LightAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<lightInfo> objects) {
        super(context, resource, objects);
        this.context = context;
        lightItems =objects;
    }

    @Override
    public int getCount() {
        return lightItems.size();
    }

    @Override
    public lightInfo getItem(int position) {
        return lightItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final lightInfo lightTemp = lightItems.get(position);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview, parent, false);
        }

        tvLightNum = (TextView)convertView.findViewById(R.id.tvLightNum);
        tvAddress = (TextView)convertView.findViewById(R.id.tvAddress);
        tvManage = (TextView)convertView.findViewById(R.id.tvManage);
        tvManageNumber = (TextView)convertView.findViewById(R.id.tvManageNumber);

        lightInfo lightInfo1 = getItem(position);

        tvLightNum.setText(lightInfo1.getLightnum());
        tvAddress.setText(lightInfo1.getAddress());
        tvManage.setText(lightInfo1.getManage());
        tvManageNumber.setText(lightInfo1.getManageNumber());

        return convertView;
    }
    public void addItem(String lightNum,  String address, String manage, String manageNumber){
        lightInfo lightInfo1 = new lightInfo();

        lightInfo1.setLightnum(lightNum);
        lightInfo1.setAddress(address);
        lightInfo1.setManage(manage);
        lightInfo1.setManageNumber(manageNumber);

        lightItems.add(lightInfo1);
    }
}