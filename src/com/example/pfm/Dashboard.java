package com.example.pfm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Dashboard extends Activity {
	
	ImageView insertTrans, financialGoal, reminders, settings;
	Button logoutButton;
	Bundle b = new Bundle();
	String userid;
	JSONArray categoryJArray, budgetJArray; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		insertTrans = (ImageView) findViewById(R.id.insertTrans);
		financialGoal = (ImageView) findViewById(R.id.financialGoal);
		reminders = (ImageView) findViewById(R.id.reminders);
		settings = (ImageView) findViewById(R.id.settings);
		logoutButton = (Button) findViewById(R.id.logoutBtn);
		
		//b = getIntent().getExtras();
		b.putString("userid", MyService.userid);
		userid = MyService.userid;
		Log.d("onCreateBundle", b.toString());
		Log.d("onCreateBundleID", b.getString("userid"));
		
		
		insertTrans.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				transIntent();			
			}			
		});
		
		financialGoal.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				financialGoalIntent();
			}
		});
		
		reminders.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("onClickBundle", b.toString());
				remindersIntent();
			}
		});
		
		settings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				settingsIntent();
			}
		});
		
		logoutButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				logoutDialog();
			}		
		});
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getTransaction connect = new getTransaction();
		connect.execute();
		
		getBudget connect2 = new getBudget();
		connect2.execute();
	}

	public void logoutDialog(){
		AlertDialog logoutAlert = new AlertDialog.Builder(this)
		.setTitle("Logout")
	    .setMessage("Are you sure you want to log out?")
	    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            loginIntent();
	        }
	     })
	    .setNegativeButton("No", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // do nothing
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	     .show();
	}
	
	public void loginIntent(){
		Intent loginIntent = new Intent(this, MainActivity.class);
		startActivity(loginIntent);
		finish();
	}
	
	public void transIntent(){
		Intent openTransaction = new Intent(this, Transaction.class);
		openTransaction.putExtras(b);
		Log.d("transBundle", b.toString());
		startActivity(openTransaction);
	}
	
	public void financialGoalIntent(){
		Intent financialGoalIntent = new Intent(this, ViewFinancialGoal.class);
		financialGoalIntent.putExtras(b);
		startActivity(financialGoalIntent);
	}
	
	public void remindersIntent(){
		Intent remindersIntent = new Intent(this, Reminders.class);
		remindersIntent.putExtras(b);
		Log.d("reminderBundle", b.toString());
		startActivity(remindersIntent);
	}
	
	public void settingsIntent(){
		Intent settingsIntent = new Intent(this, Settings.class);
		startActivity(settingsIntent);
	}
	
	class getTransaction extends AsyncTask<Void, Void, Void> {
		JSONObject jObject;
		LinearLayout llayout[];

		@Override
		protected Void doInBackground(Void... params) {
			JSONParser jsonparser = new JSONParser();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("userid", userid));
			//Log.d("userid", userid);
			//jObject = jsonparser.makeHttpRequest("http://10.0.2.2/login/viewTransaction.php", "GET", list);
			jObject = jsonparser.makeHttpRequest("http://moneymatespfms.net46.net/viewTransaction.php", "GET", list);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				Log.d("JSON", jObject.toString());
				if (jObject.getString("status").equals("success")) {
					MyService.transArray = jObject.getJSONArray("transaction");
					
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
	}
	
	class getBudget extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			JSONParser jsonparser = new JSONParser();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("userid", userid));
			Log.d("GET parameter", list.toString());
			Log.d("Calling to url",
					"http://moneymatespfms.net46.net/getBudget.php");
			JSONObject jObject = jsonparser.makeHttpRequest(
					"http://moneymatespfms.net46.net/getBudget.php", "GET",
					list);

			try {
				Log.d("JSON", jObject.toString());
				if (jObject.getString("status").equals("success")) {
					budgetJArray = jObject.getJSONArray("budget");
					categoryJArray = jObject.getJSONArray("category");
					MyService.budgetArray=budgetJArray;
					MyService.categoryArray=categoryJArray;
					Log.d("categoryJArray", categoryJArray.toString());

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
	}
}
	
