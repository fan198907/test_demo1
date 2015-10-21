/**
 * 
 */
package com.firstsocket.changliao;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * @author Administrator
 *
 */
public class ContactFragment extends Fragment{
	private MyApplication app;
	private String[] strs = new String[]{"aaa","bbb"};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (MyApplication) getActivity().getApplication();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.contact, container,false);
		ListView listView = (ListView)v.findViewById(R.id.listContact);
		listView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1,strs));
		listView.invalidate();
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), TalkActivity.class);
				Bundle bundler = new Bundle();
				bundler.putString("userName", app.getUserName());
				bundler.putString("contactName", strs[position]);
				System.out.println(app.getUserName()+"========"+strs[position]);
				intent.putExtras(bundler);
				startActivity(intent);
			}
		});
		return v;
	}

}
