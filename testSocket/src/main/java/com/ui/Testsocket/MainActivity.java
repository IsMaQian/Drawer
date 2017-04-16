package com.ui.Testsocket;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MainActivity extends Activity {

	public static String IP_Address="10.0.66.64";
	public static int Port=12345;

	EditText edit=null;
	Button send=null;
	TextView text=null;

	Handler handler=null;
	Socket socket=null;
	DataOutputStream dos=null;
	DataInputStream dis=null;
	String messageRecv=null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		edit=(EditText) findViewById(R.id.edit);
		send=(Button)findViewById(R.id.send);
		send.setOnClickListener(new ConnectionListener());
		text = (TextView)findViewById(R.id.text);

		handler = new Handler(){
			@Override
			public void handleMessage(Message msg){
				super.handleMessage(msg);
				Bundle b = msg.getData();  //��ȡ��Ϣ�е�Bundle����
				String str = b.getString("data");  //��ȡ��Ϊdata���ַ�����ֵ
				text.append(str);
			}
		};
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}


	class ConnectionListener implements OnClickListener{
		@Override
		public void onClick(View v){
			new ConnectionThread(edit.getText().toString()).start();
		}


	}


	class ConnectionThread extends Thread{
		String message = null;
		public ConnectionThread(String msg){
			message = msg;
		}
		@Override
		public void run(){
			if(socket == null){
				try {
					//Log.d("socket","new socket");
					socket = new Socket(IP_Address, Port);
					//��ȡsocket�����������
					dis = new DataInputStream(socket.getInputStream());
					dos = new DataOutputStream(socket.getOutputStream());
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try{
					dos.writeUTF(message);
					dos.flush();
					messageRecv = dis.readUTF();
					Message msg = new Message();
					Bundle b = new Bundle();
					b.putString("data", messageRecv);
					msg.setData(b);
					handler.sendMessage(msg);
			}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//���û���յ����ݣ�������
			}
		}
	}
