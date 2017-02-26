package com.example.gdmap0fflinemap.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gdmap0fflinemap.R;

import java.io.IOException;

import static com.example.gdmap0fflinemap.activity.ClientThread.strPichRate;
import static com.example.gdmap0fflinemap.activity.ClientThread.strPitch;
import static com.example.gdmap0fflinemap.activity.ClientThread.strRoll;
import static com.example.gdmap0fflinemap.activity.ClientThread.strRollRate;
import static com.example.gdmap0fflinemap.activity.ClientThread.strYaw;
import static com.example.gdmap0fflinemap.activity.ClientThread.strYawRate;

/**
 * Created by Administrator on 2017/1/29.
 */

public class LinkIPActivity extends Activity {
    Button IPLink;
    Button Send;
    EditText IPText;
    EditText SendText;
    TextView textView;

    static String sIP;
    static int port;
    static boolean isConnecting = false;
    private static final String TAG = "LinkIPActivity";
    Handler handler;
    ClientThread clientThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.link_activity);
        findViewById();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                if (msg.what == 1) {
                    String blank = "\n";
                    textView.append(blank +"IMU数据为:" + blank+
                            "滚转角为：" + strRoll + blank
                            + "俯仰角为：" + strPitch + blank
                            + "航向角为："+ strYaw + blank
                            + "陀螺仪Rollrate为："+ strRollRate + blank
                            + "陀螺仪Pitchrate为：" + strPichRate+ blank
                            + "陀螺仪Yawrate为：" + strYawRate + blank
                            + blank);
                }
            }
        };

    }
    private void findViewById() {
        Send = (Button) findViewById(R.id.send);
        Send.setOnClickListener(new SendButtonListener());
        IPLink = (Button) findViewById(R.id.IPlink);
        IPLink.setOnClickListener(new LinkIpButtonListener());
        IPText = (EditText) findViewById(R.id.IPText);
        SendText = (EditText) findViewById(R.id.sendText);
        textView=(TextView)findViewById(R.id.content);
    }

    private class LinkIpButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (isConnecting) {
                isConnecting = false;
                try {
                    if (clientThread.socket != null) {
                        clientThread.socket.close();
                        clientThread.socket = null;
                        clientThread.br.close();
                        clientThread.br = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                new Thread(clientThread).interrupt();
                IPLink.setText("重新连接");
                IPText.setEnabled(true);
                handler.removeCallbacks(clientThread);
            } else if (IsIPVaild()){
                isConnecting = true;
                IPLink.setText("断开连接");
                IPText.setEnabled(false);
                new Thread(new ClientThread(handler)).start();
            }

        }
    }


    private class SendButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            try {
                Message msg = new Message();
                msg.what = 0x345;
                msg.obj = SendText.getText().toString();
                Log.d(TAG, "onClick: "+msg.obj);
                clientThread.revHandle.sendMessage(msg);
                SendText.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean IsIPVaild() {
        String IPStr = IPText.getText().toString();
        int start = IPStr.indexOf(":");
        if (IPStr.trim().equals("")) {
            Toast.makeText(this, "请填写IP地址及端口", Toast.LENGTH_SHORT).show();
            return false;
        } else if ((start == -1) || (start + 1 >= IPStr.length())) {
            Toast.makeText(this, "请将IP地址及端口填写完整", Toast.LENGTH_SHORT).show();
            return false;
        }
        sIP = IPStr.substring(0, start);
        String sPort = IPStr.substring(start + 1);
        port = Integer.parseInt(sPort);
        return true;
    }


}
