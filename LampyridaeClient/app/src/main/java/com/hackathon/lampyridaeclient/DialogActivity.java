package com.hackathon.lampyridaeclient;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.hackathon.lampyridaeclient.databinding.ActivityDialogBinding;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class DialogActivity extends Activity {
    ActivityDialogBinding activityDialogBinding;

    private CountDownTimer countDownTimer;

    private String streetLampId;

    Vibrator vibrator;

    long[] pattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        activityDialogBinding = DataBindingUtil.setContentView(this, R.layout.activity_dialog);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //getActionBar()

        streetLampId = "";

        Tag tag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Ndef ndef = Ndef.get(tag);

        NdefRecord[] records = null;
        if (ndef != null) {
            NdefMessage ndefMessage = ndef.getCachedNdefMessage();
            records = ndefMessage.getRecords();
        }

        if (records != null) {
            byte[] payload = records[0].getPayload();
            try {
                streetLampId = new String(payload, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        final int[] timer = new int[1];
        if(((Lampyridae)getApplication()).getMessageData()) {
            timer[0] = (int)((Lampyridae)getApplication()).getSendTimer();
        }
        else {
            timer[0] = Integer.parseInt(getResources().getString(R.string.send_timer));
        }

        activityDialogBinding.tvDialogTitle.setText(getResources().getText(R.string.dialog_title));
        activityDialogBinding.tvDialogMessage.setTextColor(Color.WHITE);

        activityDialogBinding.btnDialogAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendStreetLampSMS(streetLampId);
                dialogFinish();
            }
        });
        activityDialogBinding.btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFinish();
            }
    });

        setColorTextMessage(timer[0], activityDialogBinding.tvDialogMessage);

        countDownTimer = new CountDownTimer(timer[0] * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timer[0] = timer[0]-1;

                setColorTextMessage(timer[0], activityDialogBinding.tvDialogMessage);
            }

            @Override
            public void onFinish() {
                sendStreetLampSMS(streetLampId);
                dialogFinish();
            }
        }.start();

        pattern = new long[((int)((Lampyridae)getApplication()).getSendTimer()) * 2];

        for(int i = 0; i < pattern.length; i++) {
            if (i % 2 == 0) {
                pattern[i] = 100;
            } else if (i % 2 == 1) {
                pattern[i] = 900;
            }
        }

        // 진동 코드 시작
        vibrator = ((Vibrator)getSystemService(VIBRATOR_SERVICE));
        vibrator.vibrate(pattern, -1);
        // 소리 코드 끝

    }   //onCreate(Bundle savedInstanceState)

    private void eventToServer() {
        Thread thread = new Thread(new ServerEvent());
        thread.setDaemon(true);
        thread.start();
    }   //eventToServer(String streetLampId)

    private void setColorTextMessage(int timer, TextView textView) {
        String timerString = String.valueOf(timer);
        textView.setText("");

        SpannableStringBuilder builder = new SpannableStringBuilder(timerString.concat(getResources().getString(R.string.dialog_message)));
        builder.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, timerString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.append(builder);
    }   //setColorTextMessage(int timer, TextView textView)

    private void sendStreetLampSMS(String lampId) {
        Lampyridae lampyridae = (Lampyridae)getApplication();

        if(lampyridae.getMessageData()) {
            lampyridae.sendSMS(getResources().getString(R.string.call_number), lampId.concat("\n").concat(lampyridae.getMessageText()).concat("\n").concat("도와주세요"));
        }

        if(lampyridae.isAvailablePhone()) {
            lampyridae.sendSMS(lampyridae.getUserPhone(), lampId.concat("\n").concat(getResources().getString(R.string.help_message)));
        }

        // 경고음 시작
        Intent intent = new Intent(DialogActivity.this, PlaySound.class);
        startService(intent);

        eventToServer();

    }   //sendStreetLampSMS(String lampId)

    private void cancelSMS() {
        countDownTimer.cancel();
    }   //cancelSMS()

    private void dialogFinish() {
        cancelSMS();
        vibrator.cancel();
        finish();
    }   //dialogFinish()

    private class ServerEvent implements Runnable {

        @Override
        public void run() {
            Socket socket = null;
            PrintWriter printWriter = null;

            try {
                socket = new Socket(getResources().getString(R.string.server_address), Integer.valueOf(getResources().getString(R.string.server_port)));

                if(socket.isConnected()) {
                    printWriter = new PrintWriter(socket.getOutputStream(), true);
                    printWriter.println(getResources().getString(R.string.server_message_identifier).concat(",").concat(streetLampId));
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(printWriter != null) {
                    printWriter.close();
                }

                try {
                    if(socket != null) {
                        socket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }   //run()

    }   //ServerEvent

}
