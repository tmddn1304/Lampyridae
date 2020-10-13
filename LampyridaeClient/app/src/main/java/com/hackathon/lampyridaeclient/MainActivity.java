package com.hackathon.lampyridaeclient;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_title);

        if(checkPermission()) {
            ((Lampyridae)getApplication()).getMessageData();
        }

    }   //onCreate(Bundle savedInstanceState)

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.change_user_information, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_user_information:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 0) {
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "권한을 승인하지 않으면 위급시 문자 보내기 기능을 사용할 수 없습니다.", Toast.LENGTH_LONG).show();
                finish();
            }
            else {
                ((Lampyridae)getApplication()).getMessageData();
            }
            // 문자권한

            if(grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "권한을 승인하지 않으면 위치전송 기능을 사용할 수 없습니다.", Toast.LENGTH_LONG).show();
                finish();
            }
            // 위치권한(FINE)

            if(grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "권한을 승인하지 않으면 위치전송 기능을 사용할 수 없습니다.", Toast.LENGTH_LONG).show();
                finish();
            }
            // 위치권한(COARSE)
        }
    }

    private boolean checkPermission() {
        boolean result = false;

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }
        else {
            result = true;
        }

        return result;
    }

}
