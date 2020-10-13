package com.hackathon.lampyridaeclient;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;

public class Lampyridae extends Application {
    private String userName;
    private String userGender;
    private String userAge;
    private String userPhone;

    private long sendTimer;
    private long soundTimer;

    StringBuilder stringBuilder;

    SharedPreferences preferences;

    public boolean getMessageData() {
        boolean result = false;

        preferences = getSharedPreferences(getResources().getString(R.string.save_data_key_user_information), MODE_PRIVATE);
        userName = preferences.getString(getResources().getString(R.string.save_data_key_user_name), "");
        if(userName.equals("")) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
        else {
            readSavedData();
            result = true;
        }

        stringBuilder = new StringBuilder();
        stringBuilder.append(userName).append(" ").append(userGender).append(" ").append(userAge);

        return result;
    }   //getMessageData()

    private void readSavedData() {
        userName = preferences.getString(getResources().getString(R.string.save_data_key_user_name), "");
        userGender = preferences.getString(getResources().getString(R.string.save_data_key_user_gender), getResources().getString(R.string.saved_data_male));
        userAge = preferences.getString(getResources().getString(R.string.save_data_key_user_age), "");
        userPhone = preferences.getString(getResources().getString(R.string.save_data_key_user_phone), "");

        sendTimer = preferences.getLong(getResources().getString(R.string.save_data_key_send_timer), Long.valueOf(getResources().getString(R.string.send_timer)));
        soundTimer = preferences.getLong(getResources().getString(R.string.save_data_key_sound_timer), Long.valueOf(getResources().getString(R.string.sound_timer)));
    }

    public String getMessageText() {
        String result = "";

        if(stringBuilder != null) {
            result = stringBuilder.toString();
        }

        return result;
    }

    public void sendSMS(String targetPhoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(targetPhoneNumber, null, message, null, null);
    }   //sendSMS(String targetPhoneNumber, String message)

    public boolean isAvailablePhone() {
        boolean result;

        result = userPhone.equals("");

        return !result;
    }

    public long getSendTimer() {
        readSavedData();

        return sendTimer;
    }

    public long getSoundTimer() {
        readSavedData();

        return soundTimer;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public UserData getSavedData() {
        UserData userData = new UserData(getResources().getString(R.string.saved_data_female));

        readSavedData();

        userData.setUserName(userName);
        userData.setUserGender(userGender);
        userData.setUserAge(userAge);
        userData.setUserPhone(userPhone);

        return userData;
    }

}
