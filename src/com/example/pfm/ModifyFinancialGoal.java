package com.example.pfm;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.pfm.ModifyTransaction.deleteTransaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

public class ModifyFinancialGoal extends Activity{

	String userid, goalid, goalname, amount, startdate, priority, period, durationString, monthlyamount;
	Bundle b;
	List<String> durationList;
	int duration;
	int periodid;
	boolean modifyFlag = false, deleteFlag = false;
	int monthlyAmt;
	
	EditText goalName, goalAmount, startDate, monthlyAmount;
	Spinner durationSpinner;
	RatingBar priorityBar;
	Button saveBtn, deleteBtn, cancelBtn;
	LinearLayout monthlyAmountLL;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_goal);
		
		//userid = MyService.userid;
		
		b = getIntent().getExtras();
		Log.d("modify bundle", b.toString());
		userid = MyService.userid;
		goalid = b.getString("goalid");
		goalname = b.getString("goalname");
		amount = b.getString("amount");
		startdate = b.getString("startdate");
		priority = b.getString("priority");
		period = b.getString("period");
		monthlyamount = b.getString("monthlyamount");

		goalName = (EditText) findViewById(R.id.goalNameET);
		goalAmount = (EditText) findViewById(R.id.goalAmount);
		startDate = (EditText) findViewById(R.id.startDateET);
		durationSpinner = (Spinner) findViewById(R.id.durationSpinner);
		priorityBar = (RatingBar) findViewById(R.id.ratingBar);
		monthlyAmount = (EditText) findViewById(R.id.monthlyAmountET);
		saveBtn = (Button) findViewById(R.id.saveBtn);
		deleteBtn = (Button) findViewById(R.id.deleteBtn);
		cancelBtn = (Button) findViewById(R.id.cancelBtn);
		monthlyAmountLL = (LinearLayout) findViewById(R.id.monthlyAmountLL);
		
		
		
		goalName.setText(goalname);
		goalAmount.setText(amount);
		startDate.setText(startdate.substring(0,10));
		priorityBar.setRating(Float.parseFloat(priority));
		
		Log.d("modifygoal bundle", b.toString());
		
		if(period.equals("3")){
			duration=0;
		}
		else if(period.equals("4")){
			duration=1;
		}
		else if(period.equals("5")){
			duration=2;
		}
			
		durationList = new ArrayList <String>();
		durationList.add("Weekly");
		durationList.add("Monthly");
		durationList.add("Yearly");
		spinnerAdapter();
		
		if(durationSpinner.getSelectedItem().toString().equals("Yearly")){
			monthlyAmount.setVisibility(View.VISIBLE);
			if(!goalAmount.getText().toString().equals("")){
				monthlyAmountLL.setVisibility(View.VISIBLE);
				monthlyAmount.setText(monthlyamount);
			}
			else{
				monthlyAmt=0;
				monthlyAmount.setText("");
			}
		}
		
		else{
			monthlyAmount.setVisibility(View.INVISIBLE);
		}
		
		
		saveBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				durationString = durationSpinner.getSelectedItem().toString();
				if(durationString.equals("Weekly")){
					periodid = 3;
				}
				else if(durationString.equals("Monthly")){
					periodid = 4;
				}
				else if(durationString.equals("Yearly")){
					periodid = 5;
				}
				
				Log.d("periodid", String.valueOf(periodid));
				
				modifyGoal connect = new modifyGoal();
				connect.execute();
				
			}
		});
		
		deleteBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteAlert();
				
			}
		});
		
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				
			}
		});
	}
	
	public void spinnerAdapter(){
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, durationList);
		durationSpinner.setAdapter(spinnerAdapter);
		durationSpinner.setSelection(duration);
	}
	
	public void viewFinancialGoalIntent(){
		Intent viewFinancialGoalIntent = new Intent(this, ViewFinancialGoal.class);
		b.putString("userid", MyService.userid);
		viewFinancialGoalIntent.putExtras(b);
		startActivity(viewFinancialGoalIntent);
		finish();
	}
	
	public void deleteAlert(){
		AlertDialog deleteAlert = new AlertDialog.Builder(this)
		.setTitle("Delete transaction")
	    .setMessage("Delete goal?")
	    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	deleteGoal connect3 = new deleteGoal();
				connect3.execute();
	        }
	     })
	    .setNegativeButton("No", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // do nothing
	        }
	     })
	    .setIcon(android.R.drawable.ic_menu_delete)
	     .show();
		
	}
	
	class modifyGoal extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			JSONParser jsonparser = new JSONParser();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("userid", userid));
			list.add(new BasicNameValuePair("goalid", goalid));
			list.add(new BasicNameValuePair("goalname", goalName.getText().toString()));
			list.add(new BasicNameValuePair("amount", goalAmount.getText().toString()));
			list.add(new BasicNameValuePair("startdate", startDate.getText().toString()));
			list.add(new BasicNameValuePair("priority", String.valueOf(priorityBar.getRating())));
			list.add(new BasicNameValuePair("period", String.valueOf(periodid)));

			Log.d("JSON", list.toString());
			//JSONObject jObject = jsonparser.makeHttpRequest("http://10.0.2.2/login/modifyTransaction.php", "GET", list);
			JSONObject jObject =jsonparser.makeHttpRequest(MyService.URL+"modifyFinancialGoal.php", "GET", list);

			try {
				Log.d("JSON", jObject.toString());
				if (jObject.getString("status").equals("success")) {
					Log.d("Modification", "Successfully modified.");
					modifyFlag = true;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(modifyFlag==true){
				viewFinancialGoalIntent();
				Toast toast = Toast.makeText(getApplicationContext(), "Sucessfully modified goal!", Toast.LENGTH_SHORT);
	    		toast.show();
				
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
	}
	
	class deleteGoal extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONParser jsonparser = new JSONParser();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("userid", userid));
			list.add(new BasicNameValuePair("goalid", goalid));

			//JSONObject jObject = jsonparser.makeHttpRequest("http://10.0.2.2/login/deleteTransaction.php", "GET", list);
			JSONObject jObject = jsonparser.makeHttpRequest(MyService.URL+"deleteFinancialGoal.php","GET", list);

			try {
				Log.d("JSON", jObject.toString());
				if (jObject.getString("status").equals("success")) {
					deleteFlag = true;
					Log.d("Deletion", "Successfully deleted financial goal.");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			if (deleteFlag == true) {
				viewFinancialGoalIntent();
	            Toast toast = Toast.makeText(getApplicationContext(), "Sucessfully deleted goal!", Toast.LENGTH_SHORT);
	    		toast.show();
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
	}
}
