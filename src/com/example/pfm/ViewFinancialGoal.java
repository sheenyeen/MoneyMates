package com.example.pfm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class ViewFinancialGoal extends Activity {

	TextView monthTV;
	Button addGoalBtn;
	ListView goalListView;
	Spinner durationSpinner;

	Calendar currenttime;
	String currentMonth, currentYear;
	String amountInput, tag, startdate;
	String userid;
	Bundle b;
	JSONArray goalJArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_financial_goal);
		
		addGoalBtn = (Button) findViewById(R.id.addGoalBtn);
		goalListView = (ListView) findViewById(R.id.goalListView);

		b = getIntent().getExtras();
		//userid = b.getString("userid");
		userid = MyService.userid;

		ArrayList<HashMap<String, String>> goals = new ArrayList<HashMap<String, String>>();
		GoalLazyAdapter adapter = new GoalLazyAdapter(this, goals);
		goalListView.setAdapter(adapter);
		

		goalListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				/*Intent intent = new Intent(getApplicationContext(), ModifyFinancialGoal.class);
				startActivity(intent);*/
				modifyFinancialGoal();
				
			}
			
		});
		
		addGoalBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent addGoalIntent = new Intent(getApplicationContext(), AddFinancialGoal.class);
				startActivity(addGoalIntent);
			}
		});
	}

	@Override
	protected void onResume() {
		GetGoal connect = new GetGoal();
		connect.execute();
		super.onResume();
	}

	ArrayList<HashMap<String, String>> goal;

	public void setGoal() {
		goal = new ArrayList<HashMap<String, String>>();
		try {
			Log.d("goals", goalJArray.toString());
			for (int i = 0; i < goalJArray.length(); i++) {
				JSONObject goalObj = goalJArray.getJSONObject(i);
				HashMap<String, String> hm = new HashMap<String, String>();

				hm.put("GoalName", goalObj.getString("GoalName"));
				hm.put("PeriodName", goalObj.getString("PeriodName"));
				hm.put("Amount", goalObj.getString("Amount"));
				goal.add(hm);
			}

		} catch (NullPointerException e) {
			Log.d("exeption", e.toString());
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.d("budget arraylist", goal.toString());
		GoalLazyAdapter adapter = new GoalLazyAdapter(this, goal);
		goalListView.setAdapter(adapter);
	}

	class GetGoal extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			JSONParser jsonparser = new JSONParser();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("userid", userid));
			Log.d("GET parameter", list.toString());
			Log.d("Calling to url", "http://moneymatespfms.net46.net/getGoal.php");
			//JSONObject jObject = jsonparser.makeHttpRequest("http://10.0.2.2/login/getGoal.php", "GET", list);
			JSONObject jObject = jsonparser.makeHttpRequest("http://moneymatespfms.net46.net/getGoal.php", "GET", list);

			try {
				Log.d("JSON", jObject.toString());
				if (jObject.getString("status").equals("success")) {
					goalJArray = jObject.getJSONArray("goal");

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			setGoal();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
	}
	
	class modifyGoal extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
	}

	public void modifyFinancialGoal(){
		modifyGoal connect2 = new modifyGoal();
		connect2.execute();
	}

}
