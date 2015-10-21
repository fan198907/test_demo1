/**
 * 
 */
package com.firstsocket.changliao;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class MyListViewAdaper extends BaseAdapter{
	private List<Map<String, Object>> list;
	private Activity activity;
	public MyListViewAdaper(Activity activity,List<Map<String,Object>> list){
		this.list = list;
		this.activity = activity;
	}
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Holder myHolder = new Holder();
		final Holder otherHolder = new Holder();
		if(convertView == null){
			System.out.println(list);
			if(list.get(position).get("who").toString().equals("me")){
				Bitmap bmp = null;
				convertView = LayoutInflater.from(activity).inflate(R.layout.my_list, null);
				myHolder.name = (TextView)convertView.findViewById(R.id.myName);
				myHolder.show = (TextView)convertView.findViewById(R.id.myShow);
				myHolder.show.setBackgroundResource(R.drawable.left);
				myHolder.name.setText(list.get(position).get("name").toString() + ": ");
				myHolder.ivMyPic = (ImageView)convertView.findViewById(R.id.myIvMyPic);
				ByteArrayOutputStream outPut = new ByteArrayOutputStream();    
//                bmp = BitmapFactory.decodeByteArray((byte[])list.get(position).get("pic"), 0, ((byte[])list.get(position).get("pic")).length);    
//                bmp.compress(CompressFormat.PNG, 100, outPut);    
				myHolder.ivMyPic.setImageBitmap((Bitmap)list.get(position).get("pic")); 
				myHolder.show.setText(list.get(position).get("what").toString());
			}else if(list.get(position).get("who").toString().equals("other")){
				Bitmap bmp = null;
				convertView = LayoutInflater.from(activity).inflate(R.layout.other_list, null);
				otherHolder.name = (TextView)convertView.findViewById(R.id.name);
				otherHolder.show = (TextView)convertView.findViewById(R.id.show);
				otherHolder.show.setBackgroundResource(R.drawable.right);
				otherHolder.name.setText(" :"+list.get(position).get("name").toString());
				otherHolder.ivMyPic = (ImageView)convertView.findViewById(R.id.ivMyPic);
				ByteArrayOutputStream outPut = new ByteArrayOutputStream();
//                bmp = BitmapFactory.decodeByteArray((byte[])list.get(position).get("pic"), 0, ((byte[])list.get(position).get("pic")).length);    
//                bmp.compress(CompressFormat.PNG, 100, outPut);    
				otherHolder.ivMyPic.setImageBitmap((Bitmap)list.get(position).get("pic")); 
				otherHolder.show.setText(list.get(position).get("what").toString());
			}
			
		}
		
		return convertView;
	}
	class Holder {
		TextView name,show;
		ImageView ivMyPic;
	}
}
