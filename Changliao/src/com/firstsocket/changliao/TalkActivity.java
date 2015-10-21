/**
 * 
 */
package com.firstsocket.changliao;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class TalkActivity extends Activity{
	private EditText etMessage;
	private ListView lvShowMessage;
	private TextView tvUserName;
	private Button btnSend;
	private static String SERVER_HOST_IP = "192.168.1.199";
	private static int SERVER_HOST_PORT = 8888;
	private Socket socket;
//	private PrintWriter os;
//	private BufferedReader is;
	private DataOutputStream os;
	private DataInputStream is;
	private String userName = "";
	private String contactName = "";
	private String message = "";
	private Handler handler;
	private ArrayList<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
	private Map<String, Object> myMap = new HashMap<String, Object>();
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_talk);
		if(this.getIntent().getExtras().getString("userName") != null && !this.getIntent().getExtras().getString("userName").equals("")){
			userName = this.getIntent().getExtras().getString("userName").toString();
			System.out.println("user name = "+ userName);
		}
		if(this.getIntent().getExtras().getString("contactName") != null && !this.getIntent().getExtras().getString("contactName").equals("")){
			contactName = this.getIntent().getExtras().getString("contactName").toString();
			System.out.println("contactName="+contactName);
		}
		getWindow().setTitle(contactName);
		lvShowMessage = (ListView)findViewById(R.id.lvShowMessage);
		
		lvShowMessage.setDividerHeight(0);
		
		tvUserName = (TextView)findViewById(R.id.tvUserName);
		tvUserName.setText(userName);
		
		etMessage = (EditText)findViewById(R.id.etMessage);
		btnSend = (Button)findViewById(R.id.btnSend);
		btnSend.setOnClickListener(new ButtonOnClickListener());
		handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				switch(msg.what){
					case 1:
						MyListViewAdaper adapter = new MyListViewAdaper(TalkActivity.this, list);
						lvShowMessage.setAdapter(adapter);
						lvShowMessage.invalidate();
						break;
					default:
						break;
				}
			}
			
		};
		
		new Thread(){

			@Override
			public void run() {
				try {
					socket = new Socket(SERVER_HOST_IP, SERVER_HOST_PORT);
					os = new DataOutputStream(socket.getOutputStream());
					is = new DataInputStream(socket.getInputStream());
					while(!isFinishing()){
						message = is.readUTF();
						System.out.println("read " + message);
						if(!message.equals("close")){
							if(message.startsWith("上线")){
								
							}else if(message.startsWith("传输pic")){
								System.out.println("client 接受pic");
								int size = is.readInt();    
			                    byte[] data = new byte[size];    
			                    int len = 0;    
			                    while (len < size) {    
			                        len += is.read(data, len, size - len);    
			                    } 
			                    ByteArrayOutputStream outPut = new ByteArrayOutputStream();    
			                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);    
			                    bmp.compress(CompressFormat.PNG, 100, outPut);   
			                    myMap.put("pic", bmp);
			                    System.out.println("client 接受pic 成功");
			                    list.add(myMap);
			                    System.out.println(myMap);
			                    System.out.println(list+"前");
			                    myMap = new HashMap<String, Object>();
			                    System.out.println(list+"后");
								Message msg = new Message();
								msg.what = 1;
								handler.sendMessage(msg);
							}else if(message.startsWith("传输msg")){
								System.out.println("client 接受msg");
								String strMsg = is.readUTF();
								myMap.put("name", contactName);
								myMap.put("who", "other");
								myMap.put("what", strMsg);
//								myMap.put("what", "");
								System.out.println("client 接受msg 成功");
								if(list.size()>50){
									list.remove(0);
								}
								
							}
						}
					}
				} catch (UnknownHostException e) {
					System.out.println(e+" 服务异常");
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println(e+" io异常");
					e.printStackTrace();
				}
			}
			
		}.start();
		
	}
	
	@Override
	protected void onDestroy() {
		closeSocket();
		super.onDestroy();
	}

	class ButtonOnClickListener implements View.OnClickListener{

		/* (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {
			
			switch(v.getId()){
				case R.id.btnSend:
					new Thread(){

						@Override
						public void run() {
							Map<String, Object> myMap = new HashMap<String, Object>();
							myMap.put("name", userName);
							myMap.put("who", "me");
							myMap.put("what", etMessage.getText().toString());
							
							try {
//								File file = new File("/storage/emulated/legacy/pig.png");
								File file = new File(Environment.getExternalStorageDirectory()+"/pig.png");
								FileInputStream in = new FileInputStream(file);
								Bitmap map = BitmapFactory.decodeStream(in);
								myMap.put("pic", map);
							} catch (IOException e) {
								System.out.println(""+e);
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(list.size()>50){
								list.remove(0);
							}
							list.add(myMap);
							Message msg = new Message();
							msg.what = 1;
							handler.sendMessage(msg);
							sendMessage(etMessage.getText().toString());
						}
						
					}.start();
					break;
				default:
					break;
			}
			
		}
		
	}
	
	private void sendMessage(String msg)  
	  {
		JSONObject json = new JSONObject();
		try {
			json.put("userName", userName);
			json.put("contactName", contactName);
			json.put("message", msg);
		} catch (JSONException e) {
			System.out.println(e);
			e.printStackTrace();
		}
	    try {
	    	os.writeUTF("传输json");
			os.writeUTF(json.toString());
			os.writeUTF("传输pic");
			File file = new File(android.os.Environment .getExternalStorageDirectory()+"/"+"pig.png");
			try {
				FileInputStream fis = new FileInputStream(file);
				int size = fis.available();
				byte[] data = new byte[size];
				fis.read(data);
				System.out.println("client 发送 pic");
				os.writeInt(size);
				os.write(data);
				os.flush();
				System.out.println("client 发送 pic 成功");
			} catch (FileNotFoundException e) {
				System.out.println(""+e);
				e.printStackTrace();
			}catch(IOException e1){
				System.out.println(""+e1);
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	  } 
	
	 public void closeSocket()  
	  {  
	    try  
	    {  
	    	os.writeUTF("close");
			os.close();
			is.close();
			socket.close();  
	    }  
	    catch (IOException e)
	    {
	    	System.out.println("关闭socket失败"+e);
	    }
	  }

}
