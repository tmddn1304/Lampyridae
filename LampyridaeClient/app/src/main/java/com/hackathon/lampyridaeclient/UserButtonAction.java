package com.hackathon.lampyridaeclient;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.IOException;
import java.util.List;

public class UserButtonAction extends BroadcastReceiver {
    Context context;

    int currentCount;
    long currentSeconds;

    String latitude;
    String longitude;

    LocationManager manager;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        String PACKAGE_NAME = context.getResources().getString(R.string.save_data_key_button_action);
        String KEY_COUNT = context.getResources().getString(R.string.save_data_key_button_action_count);
        String KEY_SECONDS = context.getResources().getString(R.string.save_data_key_button_action_timer);

        SharedPreferences values = context.getSharedPreferences(PACKAGE_NAME, Activity.MODE_PRIVATE);

        currentCount = values.getInt(KEY_COUNT, 0);
        currentSeconds = values.getLong(KEY_SECONDS, 0);
        Log.w("TAG", "버튼 눌렸음!");

        SharedPreferences.Editor editor = values.edit();

        if (currentSeconds == 0) {
            editor.putLong(KEY_SECONDS, System.currentTimeMillis());
            Log.w("TAG", "currentSeconds 초기값");
        }

        if (System.currentTimeMillis() - currentSeconds < (Integer.parseInt(context.getResources().getString(R.string.button_action_timer)) * 1000) && currentCount > Integer.parseInt(context.getResources().getString(R.string.button_action_count))) {
            editor.putLong(KEY_SECONDS, 0);
            editor.putInt(KEY_COUNT, 0);

            startLocationService(context);

            Log.w("TAG", "3초 이하 5번 이상" + currentSeconds + "," + currentCount);
        } else if (System.currentTimeMillis() - currentSeconds > (Integer.parseInt(context.getResources().getString(R.string.button_action_timer)) * 1000) && currentCount > Integer.parseInt(context.getResources().getString(R.string.button_action_count))) {
            editor.putLong(KEY_SECONDS, 0);
            editor.putInt(KEY_COUNT, 0);

            Log.w("TAG", "3초 이상 5번 이상");
        } else {
            editor.putInt(KEY_COUNT, (++currentCount));
        }

        editor.apply();
        String intentAction = intent.getAction();
        Log.w("TAG", "인텐트 값 : " + intentAction + "," + currentCount);

    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());

            if(((Lampyridae)context.getApplicationContext()).getMessageData()) {
                ((Lampyridae)context.getApplicationContext()).sendSMS(context.getResources().getString(R.string.call_number), ((Lampyridae)context.getApplicationContext()).getMessageText().concat(" ").concat(addressCoding(latitude, longitude)));
            }

            Log.w("TAG", latitude+","+longitude);

            stopLocationService();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) { }

        @Override
        public void onProviderEnabled(String s) { }

        @Override
        public void onProviderDisabled(String s) { }
    };

    private void startLocationService(Context context) {
        manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        long minTime = 1000;
        float minDistance = 1;

        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, mLocationListener);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, mLocationListener);
    }

    private void stopLocationService() {
        manager.removeUpdates(mLocationListener);
    }

    private  String addressCoding(String latitude, String longitude){
        startLocationService(context);
        Double latitudeAddress, longitudeAddress;
        final Geocoder geocorder = new Geocoder(context);

        List<Address> list;
        String address = null;
        try{
            latitudeAddress = Double.parseDouble(latitude);
            longitudeAddress = Double.parseDouble(longitude);

            list = geocorder.getFromLocation( latitudeAddress, longitudeAddress, 1);

            if(list!=null){
                if(list.size()==0){
                    Log.w("TAG", "해당되는 주소가 없다");
                }
                else{
                    for(int i=0; i<list.size(); i++){
                        Address outAddress = list.get(i);
                        int addrCount = outAddress.getMaxAddressLineIndex()+1;
                        StringBuilder outAddrStr = new StringBuilder();
                        for(int k=0; k<addrCount; k++){
                            outAddrStr.append(outAddress.getAddressLine(k));
                        }
                        Log.w("TAG", outAddrStr.toString());

                        address = outAddrStr.toString();
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        if(address != null){
            Log.w("TAG", "확인 : "+address+" ");
            return address;
        }else{
            return "";
        }
    }
}