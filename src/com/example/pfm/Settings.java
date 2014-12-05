package com.example.pfm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Settings extends Activity{
	
	String settings[] = {"Daily Reminder", "Logout"};
	ListView settingListview;

	ArrayList<HashMap<String,String>> settingList = new ArrayList<HashMap<String,String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		settingListview = (ListView) findViewById(R.id.list);
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("name","Daily Reminder");
		hm.put("imagename","alarm");
		settingList.add(hm);
		
		hm = new HashMap<String,String>();
		hm.put("name","Log out");
		hm.put("imagename","power");
		settingList.add(hm);
		
		//ArrayList<String> arrayList = new ArrayList<String>();
		//arrayList.addAll(Arrays.asList(settings));
		//ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, settings);
		//settingListview.setAdapter(listAdapter);
		
		SettingAdapter adapter = new SettingAdapter(this, settingList);
		settingListview.setAdapter(adapter);
		
		
		settingListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				if(position==0){
					dailyReminderIntent();
				}
				
				else if(position==1){
					logoutIntent();
				}
			}
		});
	}

	public void dailyReminderIntent(){
		Intent dailyReminderIntent = new Intent(this, Reminders.class);
		startActivity(dailyReminderIntent);
	}
	
	public void logoutIntent(){
		Intent logoutIntent = new Intent(this, MainActivity.class);
		MyService.userid="";
		startActivity(logoutIntent);
		finish();
	}
}
