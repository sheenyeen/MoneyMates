package com.example.pfm;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Settings extends Activity{
	
	String settings[] = {"Currency", "Passcode", "Account settings", "Backup"};
	ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		list = (ListView) findViewById(R.id.list);
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.addAll(Arrays.asList(settings));
		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, settings);
		list.setAdapter(listAdapter);
		
		
	}
}
