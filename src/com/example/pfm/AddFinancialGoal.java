package com.example.pfm;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

public class AddFinancialGoal extends Activity{
	
	Button cancelButton, saveButton;
	EditText goalAmount, goalName, startDate;
	Spinner durationSpinner;
	RatingBar ratingBar;
	
	List<String> durationList;
	String userid;
	float ratingBarValue;
	boolean addGoalFlag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_financial_goal);
		cancelButton = (Button) findViewById(R.id.cancelBtn);
		saveButton = (Button) findViewById(R.id.saveBtn);
		
		goalAmount = (EditText) findViewById(R.id.goalAmount);
		goalName = (EditText) findViewById(R.id.goalNameET);
		startDate = (EditText) findViewById(R.id.startDateET);
		durationSpinner = (Spinner) findViewById(R.id.durationSpinner);
		ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		
		durationList = new ArrayList <String>();
		durationList.add("Weekly");
		durationList.add("Monthly");
		durationList.add("Yearly");
		spinnerAdapter();
		
		userid = MyService.userid;
		
		ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				// TODO Auto-generated method stub
				ratingBarValue = ratingBar.getRating();
				Log.d("Rating value", String.valueOf(ratingBarValue));
				
			}
		});		
		
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cancelIntent();
			}
		});
		
		saveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveGoal connect = new saveGoal();
				connect.execute();
			}
		});
		
		
	}
	
	public void cancelIntent(){
		finish();
	}
	
	public void spinnerAdapter(){
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, durationList);
		durationSpinner.setAdapter(spinnerAdapter);
	}
	
	class saveGoal extends AsyncTask<Void, Void, Void>{
		JSONObject jObject;
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONParser jsonparser = new JSONParser();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("userid", userid));
			list.add(new BasicNameValuePair("goalname", goalName.getText().toString() ));
			list.add(new BasicNameValuePair("amount", goalAmount.getText().toString()));
			list.add(new BasicNameValuePair("startdate",startDate.getText().toString()));
			list.add(new BasicNameValuePair("duration", durationSpinner.getSelectedItem().toString()));
			list.add(new BasicNameValuePair("priority", String.valueOf(ratingBarValue)));
			
			//JSONObject jObject = jsonparser.makeHttpRequest("http://10.0.2.2/login/addFinancialGoal.php", "GET", list);
			jObject = jsonparser.makeHttpRequest("http://moneymatespfms.net46.net/addFinancialGoal.php", "GET", list);
			Log.d("BudgetJSON", jObject.toString());
			try {
				if(jObject.getString("status").equals("success")){
					addGoalFlag = true;	
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(addGoalFlag==true){
				viewGoalIntent();
			}
							
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
	}
	
	public void viewGoalIntent(){
		Intent viewGoalIntent = new Intent(this, ViewFinancialGoal.class);
		startActivity(viewGoalIntent);
		finish();
	}
}
