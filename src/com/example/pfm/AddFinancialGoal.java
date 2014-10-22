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
import android.widget.Spinner;

public class AddFinancialGoal extends Activity{
	
	Button cancelButton, saveButton;
	EditText goalAmount, startDate, endDate;
	Spinner durationSpinner;
	
	List<String> durationList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_financial_goal);
		cancelButton = (Button) findViewById(R.id.cancelBtn);
		saveButton = (Button) findViewById(R.id.saveBtn);
		goalAmount = (EditText) findViewById(R.id.goalAmount);
		startDate = (EditText) findViewById(R.id.startDateET);
		durationSpinner = (Spinner) findViewById(R.id.durationSpinner);
		
		durationList = new ArrayList <String>();
		durationList.add("Weekly");
		durationList.add("Monthly");
		durationList.add("Yearly");
		spinnerAdapter();
		
		
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
		//Log.d("spinner adapt", "blabla");
	}
	
	class saveGoal extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
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
}
