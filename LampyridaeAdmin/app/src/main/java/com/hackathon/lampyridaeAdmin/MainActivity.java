package com.hackathon.lampyridaeAdmin;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    EditText etNumber;
    ImageView btnSearch;
    ListView lvList;

    Toolbar toolbar;

    LightAdapter lightAdapter;

    ArrayList<lightInfo> searchedArr;

    boolean mWriteMode = false;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mNfcPendingIntent;
    LinearLayout linearList, linearImage;
    ImageView ivGrape;
    lightInfo tempInfo;

    String data;

    InputMethodManager imm;

    private long pressedTime;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 101) {
                searchedArr = (ArrayList) msg.obj;

                LightAdapter lightAdapter = new LightAdapter(getApplicationContext(), R.layout.listview, searchedArr);
                lvList.setAdapter(lightAdapter);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        linearList = (LinearLayout) findViewById(R.id.linearList);
        linearImage = (LinearLayout) findViewById(R.id.linearImage);

        lvList = (ListView) findViewById(R.id.lvList);
        lvList.setOnItemClickListener(this);
        btnSearch = (ImageView) findViewById(R.id.btnSearch);
        etNumber = (EditText) findViewById(R.id.etNumber);
        btnSearch.setOnClickListener(this);
        ivGrape = (ImageView) findViewById(R.id.ivGraph);
        ivGrape.setOnClickListener(this);

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSearch:

                String lightNum = etNumber.getText().toString().trim();

                ConnectThread1 connectThread1 = new ConnectThread1(handler, lightNum);
                Log.e("TAG", lightNum + ", connectThread1 호출됨");
                connectThread1.start();

                break;
            case R.id.ivGraph:
                Intent intent = new Intent(this, GraphActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        linearImage.setVisibility(View.VISIBLE);
        linearList.setVisibility(View.INVISIBLE);

        imm.hideSoftInputFromWindow(etNumber.getWindowToken(), 0);

        tempInfo = searchedArr.get(position);
        Log.w("TAGG", tempInfo.toString());
        mNfcAdapter = NfcAdapter.getDefaultAdapter(MainActivity.this);
        mNfcPendingIntent = PendingIntent.getActivity(MainActivity.this, 0,
                new Intent(MainActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        enableTagWriteMode();

        data = tempInfo.toString();
    }

    private void enableTagWriteMode() {
        mWriteMode = true;
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter[] mWriteTagFilters = new IntentFilter[]{tagDetected};
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mWriteTagFilters, null);
    }

    private void disableTagWriteMode() {
        mWriteMode = false;
        mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // Tag writing mode
        if (mWriteMode && NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NdefRecord record = NdefRecord.createMime("Application/com.hackathon.lampyridaeClient", (tempInfo.getLightnum() + "," + tempInfo.getAddress()).getBytes());

            NdefMessage message = new NdefMessage(new NdefRecord[]{record});
            if (writeTag(message, detectedTag)) {
                Toast.makeText(this, "Success: Wrote placeid to nfc tag", Toast.LENGTH_LONG)
                        .show();
            }
        }
        linearList.setVisibility(View.VISIBLE);
        linearImage.setVisibility(View.INVISIBLE);
    }

    public boolean writeTag(NdefMessage message, Tag tag) {
        int size = message.toByteArray().length;
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    Toast.makeText(getApplicationContext(),
                            "Error: tag not writable",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (ndef.getMaxSize() < size) {
                    Toast.makeText(getApplicationContext(),
                            "Error: tag too small",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                ndef.writeNdefMessage(message);
                return true;
            } else {
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        return true;
                    } catch (IOException e) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onBackPressed() {

        if(linearImage.getVisibility() == View.INVISIBLE) {
            if (pressedTime == 0) {
                Toast.makeText(MainActivity.this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
                pressedTime = System.currentTimeMillis();
            } else {
                int seconds = (int) (System.currentTimeMillis() - pressedTime);

                if (seconds > 2000) {
                    pressedTime = 0;
                } else {
                    finish();
                }
            }
        }else {
            linearImage.setVisibility(View.INVISIBLE);
            linearList.setVisibility(View.VISIBLE);
        }
    }
}

