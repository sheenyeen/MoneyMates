package com.example.pfm;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service{
	
	static String userid = "";
	static final String URL = "http://54.169.79.91/MoneyMatesPHP/";
	static JSONArray transArray = new JSONArray();
	static JSONArray categoryArray = new JSONArray();
	static JSONArray budgetArray = new JSONArray();
	static ArrayList<HashMap<String, String>> savingsArray = new ArrayList<HashMap<String, String>>();
	static HashMap<String, String> hashMap = new HashMap<String, String>();
	static int calendarMonth = 0; 
	static int calendarYear = 0; 
	static int calendarDate = 0;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//Toast.makeText(this, "Service created.", Toast.LENGTH_SHORT).show();
		Log.d("Service created", "service created");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		//Toast.makeText(this, "Service started.", Toast.LENGTH_SHORT).show();
		Log.d("Service start", "service start");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//Toast.makeText(this, "Service stopped.", Toast.LENGTH_SHORT).show();
	}

}
