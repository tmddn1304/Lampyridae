package com.hackathon.lampyridaeAdmin;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ConnectThread2 extends Thread{
    String hostName = "61.99.142.131";        // IP 주소 확인하고 바꾸기

    String location;
    Handler handler;

    private BufferedWriter sockWriter;
    private BufferedReader sockReader;

    boolean isRun = true;

    ArrayList<lightInfo> arrayList;

    String s = null;

    ConnectThread2(Handler handler, String location){
        this.handler = handler;
        this.location = location;
        arrayList = new ArrayList<lightInfo>();
    }

    public void run(){
        int hostPort = 8181;
        Socket socket = null;

        try {
            Log.e("TAG", hostName + ":" + hostPort + " 접속 요청");

            // 서버에 연결요청
            socket = new Socket(hostName, hostPort);

            // 통신을 하기 위해 i/o stream얻기
            sockWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));

            // 서버에게 데이터 전송
            PrintWriter printWriter = new PrintWriter(sockWriter,true);

            printWriter.println("admin2,"+location);
            printWriter.flush();

            sockReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String data = "";
            String data1 = "";

            for(int i = 0; i<5; i++){
                lightInfo lightInfo = new lightInfo();
                data = sockReader.readLine();
                lightInfo.setLightnum(data);
                data1 = sockReader.readLine();
                lightInfo.setCount(Integer.parseInt(data1));
                arrayList.add(lightInfo);
            }

            Message msg = new Message();
            msg.what = 102;
            msg.obj = arrayList;
            handler.sendMessage(msg);
            Log.e("TAG" , arrayList.toString());

            socket.close();

        } catch (UnknownHostException e) {
            Log.e("TAG", "UnKnownHostException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("TAG", "IOException");
            e.printStackTrace();
        }finally {
            isRun = false;
        }
    }
}