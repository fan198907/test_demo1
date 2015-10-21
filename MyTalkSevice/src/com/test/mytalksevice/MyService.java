package com.test.mytalksevice;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Vector;
import com.alibaba.fastjson.JSONObject;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Handler;
import android.os.IBinder;

public class MyService extends Service{

	private static int SERVER_HOST_PORT = 8888;
	private static Vector<Object> vector1=new Vector<Object>();
	private static Vector<Object> vector2=new Vector<Object>();
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("服务已经启动");
				try{
					ServerSocket server = null;
					try{
						server = new ServerSocket(SERVER_HOST_PORT);
					}catch(Exception e){
						System.out.println(e+"1111111");
					}
					while(true){
						Socket socket = null;
						try{
							socket = server.accept();
			//				socket.getInetAddress();
			//				socket.getPort();
							new CreateNewSocketThread(socket);
						}catch(Exception e){
							System.out.println(e+"222222222");
						}
					}
				}catch(Exception e){
					System.out.println(e+"333333333");
				}
			}

		}.start();
		super.onCreate();
	}
	

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}



	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	public static class CreateNewSocketThread extends Thread{
		private String name;
		String contactName = "";
		String message = "";
		private Socket socket;
		private DataOutputStream out;
		private DataInputStream in;
		String  revStr=null;
		CreateNewSocketThread(Socket socket) throws IOException{
			this.socket = socket;
			this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
			start();
		}
		public void run() {
			while(true){
				try{
//						System.out.println("true");
						revStr = in.readUTF();
						System.out.println("revStr = " + revStr);
						
						Bitmap bmp = null;
						if(revStr.equals("传输json")){
							System.out.println("service 接受 json");
							String readStr = in.readUTF();
							JSONObject jsonObject = JSONObject.parseObject(readStr);
							System.out.println(jsonObject);
							name = jsonObject.getString("userName");
							contactName = jsonObject.getString("contactName");
							message = jsonObject.getString("message");
							System.out.println("service 接受 json success");
						}else if(revStr.equals("传输pic")){
							System.out.println("service 接受 pic");
							int size = in.readInt();    
							System.out.println("service 接受 size 成功");
		                    byte[] data = new byte[size];    
		                    int len = 0;    
		                    while (len < size) {    
		                        len += in.read(data, len, size - len);    
		                    }    
		                      
		                    bmp = BitmapFactory.decodeByteArray(data, 0, data.length); 
		                    System.out.println("service 接受 pic success"+bmp);
		                     
		                    
//		                    ByteArrayOutputStream outPut = new ByteArrayOutputStream();  
//							bmp.compress(CompressFormat.PNG, 100, outPut);
//		                    byte[] bytes = outPut.toByteArray(); 
//		                    out.writeInt(outPut.size());
//		                    out.write(bytes); 
//		                    out.flush();
		                     
						}else if(revStr.equals("close")){
							out.close();
							in.close();
							socket.close();
							vector1.remove(name);
							vector2.remove(this);
						}
						if(!vector1.contains(name)){
							System.out.println("注册"+name+"成功");
							vector1.add(name);
							vector2.add(this);
						}
						Enumeration enuc=vector2.elements();
						while(enuc.hasMoreElements()){
							CreateNewSocketThread th=(CreateNewSocketThread)enuc.nextElement();
							System.out.println(vector2.size()+"    "+th.name + "   "+contactName);
							if(th.name.equals(contactName)){
								System.out.println("发送给"+contactName+"成功");
								if(!message.equals("")){
									System.out.println("service 发送 message");
									th.out.writeUTF("传输msg");
									th.out.writeUTF(message);
								} 
								if(bmp!=null){
									System.out.println("service 发送 pic");
									th.out.writeUTF("传输pic");
									ByteArrayOutputStream outPut = new ByteArrayOutputStream();  
									bmp.compress(CompressFormat.PNG, 100, outPut);
				                    byte[] bytes = outPut.toByteArray(); 
				                    out.writeInt(outPut.size());
				                    out.write(bytes); 
				                    out.flush();
				                    System.out.println("service 发送 pic 成功");
								}
							}
						}
				}catch(EOFException e1){
					
				}catch(IOException e){
					System.out.println("失败"+e);
					try {
						out.close();
						in.close();
						socket.close();
						vector1.remove(name);
						vector2.remove(this);
					} catch (IOException e1) {
						System.out.println("io失败"+e1);
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
					
				}
			}
		}
		
	}
}
