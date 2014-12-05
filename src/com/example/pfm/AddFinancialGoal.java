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
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

public class AddFinancialGoal extends Activity{
	
	Button cancelButton, saveButton;
	EditText goalAmount, goalName, startDate;
	Spinner durationSpinner;
	RatingBar ratingBar;
	LinearLayout monthlyAmountLL;
	EditText monthlyAmountET;
	
	List<String> durationList;
	String userid;
	float ratingBarValue;
	boolean addGoalFlag = false;
	int monthlyAmt = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_financial_goal);
		cancelButton = (Button) findViewById(R.id.cancelBtn);
		saveButton = (Button) findViewById(R.id.saveBtn);
		
		goalAmount = (EditText) findViewById(R.id.goalAmount);
		goalName = (EditText) findViewById(R.id.goalNameET);
		startDate = (EditText) findViewById(R.id.startDateET);
		durationSpinner = (Spinner) findViewById(R.id.durationSpinner);
		ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		monthlyAmountLL = (LinearLayout) findViewById(R.id.monthlyAmountLL);
		monthlyAmountET = (EditText) findViewById(R.id.monthlyAmountET);
		
		durationList = new ArrayList <String>();
		durationList.add("Weekly");
		durationList.add("Monthly");
		durationList.add("Yearly");
		spinnerAdapter();
		
		userid = MyService.userid;
		
		goalAmount.addTextChangedListener(new TextWatcher(){


			@Override
			public void afterTextChanged(Editable arg0) {
				if(durationSpinner.getSelectedItem().toString().equals("Yearly")){
					if(!goalAmount.getText().toString().equals("")){
						monthlyAmt = Integer.parseInt(goalAmount.getText().toString()) / 12;
						monthlyAmountET.setText(""+monthlyAmt);
					}
					else{
						monthlyAmountET.setText("");
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				
			}
		});
		
		durationSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(durationSpinner.getSelectedItem().toString().equals("Yearly")){
					monthlyAmountLL.setVisibility(View.VISIBLE);
					if(!goalAmount.getText().toString().equals("")){
						monthlyAmt = Integer.parseInt(goalAmount.getText().toString()) / 12;
						Log.d("monthlyAmount", String.valueOf(monthlyAmt));
						monthlyAmountET.setText(String.valueOf(monthlyAmt));
					}
					else{
						monthlyAmt = 0;
						monthlyAmountET.setText("");
					}
				}
				
				else{
					monthlyAmountLL.setVisibility(View.INVISIBLE);
					monthlyAmt = 0;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
			
		});
		
		
		
		ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				ratingBarValue = ratingBar.getRating();
				Log.d("Rating value", String.valueOf(ratingBarValue));
				
			}
		});		
		
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancelIntent();
			}
		});
		
		saveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(goalAmount.getText().toString().equals("") || goalName.getText().toString().equals("") || startDate.getText().toString().equals("") || ratingBar.getNumStars() == 0){
					Toast toast = Toast.makeText(getApplicationContext(), "Please fill in all required fields.", Toast.LENGTH_SHORT);
					toast.show();
				}else{
					Log.d("Click save goal", "MING MING JIU CLICK D");
					saveGoal connect = new saveGoal();
					connect.execute();
				}
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
			JSONParser jsonparser = new JSONParser();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("userid", userid));
			list.add(new BasicNameValuePair("goalname", goalName.getText().toString() ));
			list.add(new BasicNameValuePair("amount", goalAmount.getText().toString()));
			list.add(new BasicNameValuePair("startdate",startDate.getText().toString()));
			list.add(new BasicNameValuePair("duration", durationSpinner.getSelectedItem().toString()));
			list.add(new BasicNameValuePair("priority", String.valueOf(ratingBarValue)));
			list.add(new BasicNameValuePair("monthlyamount", monthlyAmountET.getText().toString()));
			
			//JSONObject jObject = jsonparser.makeHttpRequest("http://10.0.2.2/login/addFinancialGoal.php", "GET", list);
			jObject = jsonparser.makeHttpRequest("http://moneymatespfms.net46.net/addFinancialGoal.php", "GET", list);
			Log.d("GoalJSON", jObject.toString());
			try {
				if(jObject.getString("status").equals("success")){
					addGoalFlag = true;	
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}	
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Log.d("post execute", "MING MING JIU CLICK D "+addGoalFlag);
			if(addGoalFlag==true){
				viewGoalIntent();
			}else{
				Log.d("Add goal failed", "Fail to add goal");
			}
							
		}

		@Override
		protected void onPreExecute() {
			Log.d("Click save goal22222", "MING MING JIU CLICK D");
			super.onPreExecute();
		}
		
	}
	
	public void viewGoalIntent(){
		Intent viewGoalIntent = new Intent(this, ViewFinancialGoal.class);
		startActivity(viewGoalIntent);
		finish();
	}
}
