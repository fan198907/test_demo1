/**
 * 
 */
package com.firstsocket.changliao;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Button;

/**
 * @author Administrator
 *
 */
public class FriendActivity extends Activity{
	private Button btnContact;
	private FragmentTransaction transaction;
	private ContactFragment contactFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend);
		
		transaction = getFragmentManager().beginTransaction();
		if(null == contactFragment){
			contactFragment = new ContactFragment();
		}
		transaction.add(R.id.frameLayout, contactFragment);
		transaction.commit();
	}

}
