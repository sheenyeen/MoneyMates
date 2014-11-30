package com.example.pfm;

import java.util.ArrayList;
import java.util.Arrays;

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
	ListView settingList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		settingList = (ListView) findViewById(R.id.list);
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.addAll(Arrays.asList(settings));
		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, settings);
		settingList.setAdapter(listAdapter);
		
		settingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				if(position==0){
					dailyReminderIntent();
				}
			}
		});
	}

	public void dailyReminderIntent(){
		Intent dailyReminderIntent = new Intent(this, Reminders.class);
		startActivity(dailyReminderIntent);
	}
}
