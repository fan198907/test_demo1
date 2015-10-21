/**
 * 
 */
package com.test.mytalksevice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * @author Administrator
 *
 */
public class MainActivity extends Activity{
	private Button btnStartService;
	public class BootCompletedReceiver extends BroadcastReceiver{ 
		 public void onReceive(Context context, Intent intent) {
		  if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
		   Intent newIntent = new Intent(context,MyService.class);
		   context.startService(newIntent);
		  } 
		 }
		}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnStartService = (Button)findViewById(R.id.btnStartService);
		btnStartService.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,MyService.class);
			    startService(intent);
			    Toast.makeText(MainActivity.this, "服务启动成功", Toast.LENGTH_LONG).show();

			}
		});
	}

}
