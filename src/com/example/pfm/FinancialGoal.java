package com.example.pfm;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FinancialGoal extends Activity{
	
	Button cancelButton, saveButton;
	EditText goalAmount, startDate, endDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.financial_goal);
		cancelButton = (Button) findViewById(R.id.cancelBtn);
		saveButton = (Button) findViewById(R.id.saveBtn);
		goalAmount = (EditText) findViewById(R.id.goalAmount);
		startDate = (EditText) findViewById(R.id.startDateET);
		endDate = (EditText) findViewById(R.id.endDateET);
		
		
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
		Intent cancelIntent = new Intent(this, Dashboard.class);
		startActivity(cancelIntent);
		finish();
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
