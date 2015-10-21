/**
 * 
 */
package com.firstsocket.changliao;

import android.app.Application;

/**
 * @author Administrator
 *
 */
public class MyApplication extends Application{
	private String userName = "";
	@Override
	public void onCreate() {
		super.onCreate();
		setUserName(userName);
	}
	
	public void setUserName(String userName){
		this.userName = userName;
	}
	
	public String getUserName(){
		return userName;
	}

}
