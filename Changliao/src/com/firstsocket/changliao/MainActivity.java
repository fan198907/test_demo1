package com.firstsocket.changliao;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private EditText etUserName,etPassword;
	private Button btnLogin,btnRegister;
	private String userName,password;
	private MyApplication myApp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		myApp = (MyApplication) getApplication();
		etUserName = (EditText)findViewById(R.id.etUserName);
		etPassword = (EditText)findViewById(R.id.etPassword);
		btnLogin = (Button)findViewById(R.id.btnLogin);
		btnRegister = (Button)findViewById(R.id.btnRegister);
		btnLogin.setOnClickListener(new ButtonOnClickListener());
		btnRegister.setOnClickListener(new ButtonOnClickListener());
	}
	
	class ButtonOnClickListener implements View.OnClickListener{

		/* (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
				case R.id.btnLogin:
					userName = etUserName.getText().toString();
					password = etPassword.getText().toString();
					if(userName.equals("") || password.equals("")){
						Toast toast = Toast.makeText(MainActivity.this, "用户名密码不能为空", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}else{
						if(isLogin(userName,password)){
							Intent intent = new Intent();
							myApp.setUserName(userName);
							intent.setClass(MainActivity.this, FriendActivity.class);
							startActivity(intent);
							finish();
						}
					}
					break;
				case R.id.btnRegister:
					break;
			}
		}
		
	}
	
	
	public boolean isLogin(String userName,String password){
		return true;
	}
	
	
}
