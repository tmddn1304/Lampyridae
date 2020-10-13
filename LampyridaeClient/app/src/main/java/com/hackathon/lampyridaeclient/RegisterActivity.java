package com.hackathon.lampyridaeclient;

import android.app.Activity;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hackathon.lampyridaeclient.databinding.ActivityRegisterBinding;

public class RegisterActivity extends Activity {
    ActivityRegisterBinding activityRegisterBinding;

    boolean isFemale;
    String userName;
    String userAge;
    String userPhone;

    String sendTimer;
    String soundTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRegisterBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        UserData userData = ((Lampyridae)getApplication()).getSavedData();
        userName = userData.getUserName();
        userAge = userData.getUserAge();
        userPhone = userData.getUserPhone();
        isFemale = userData.isFemale();

        sendTimer = String.valueOf(((Lampyridae)getApplication()).getSendTimer());
        soundTimer = String.valueOf(((Lampyridae)getApplication()).getSoundTimer());

        init();
    }

    private void init() {
        activityRegisterBinding.etName.setText(userName);
        activityRegisterBinding.etAge.setText(userAge);
        activityRegisterBinding.etPhone.setText(userPhone);
        changeSelectedButton();
        activityRegisterBinding.etSendTimer.setText(sendTimer);
        activityRegisterBinding.etSoundTimer.setText(soundTimer);
    }

    private void changeSelectedButton() {
        activityRegisterBinding.btnMale.setBackgroundResource(isFemale ? R.drawable.white_button : R.drawable.clicked_button);
        activityRegisterBinding.btnFemale.setBackgroundResource(isFemale ? R.drawable.clicked_button : R.drawable.white_button);
    }

    public void setGender(View view) {
        switch (view.getId()) {
            case R.id.btn_male:
                isFemale = false;
                changeSelectedButton();
                break;

            case R.id.btn_female:
                isFemale = true;
                changeSelectedButton();
                break;
        }
    }

    public void resetInputData(View view) {
        activityRegisterBinding.etName.setText("");

        isFemale = false;
        changeSelectedButton();

        activityRegisterBinding.etAge.setText("");
        activityRegisterBinding.etPhone.setText("");
        activityRegisterBinding.etSendTimer.setText(getResources().getString(R.string.send_timer));
        activityRegisterBinding.etSoundTimer.setText(getResources().getString(R.string.sound_timer));
    }

    public void saveUserData(View view) {
        SharedPreferences preferences = getSharedPreferences("userInformation", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getResources().getString(R.string.save_data_key_user_name), activityRegisterBinding.etName.getText().toString());
        editor.putString(getResources().getString(R.string.save_data_key_user_age), isFemale ? getResources().getString(R.string.saved_data_female) : getResources().getString(R.string.saved_data_male));
        editor.putString(getResources().getString(R.string.save_data_key_user_age), activityRegisterBinding.etAge.getText().toString());
        editor.putString(getResources().getString(R.string.save_data_key_user_phone), activityRegisterBinding.etPhone.getText().toString());
        editor.putLong(getResources().getString(R.string.save_data_key_sound_timer), Long.valueOf(activityRegisterBinding.etSoundTimer.getText().toString()));
        editor.putLong(getResources().getString(R.string.save_data_key_send_timer), Long.valueOf(activityRegisterBinding.etSendTimer.getText().toString()));
        editor.apply();

        finish();
    }

}
