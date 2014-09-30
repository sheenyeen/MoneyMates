package com.example.pfm;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
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
import android.widget.Switch;
import android.widget.Toast;

public class Budget extends Activity {
	
	Button saveBtn, cancelBtn;
	EditText budgetAmount, startDate, endDate, budgetName;
	Spinner budgetCategory;
	Switch budgetSwitch;
	
	String userid = "";
	Bundle b;
	JSONArray jArray;
	List<String> categoryList;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.budget);
		
		saveBtn = (Button) findViewById(R.id.saveBtn);
		cancelBtn = (Button) findViewById(R.id.cancelBtn);
		budgetName = (EditText) findViewById(R.id.budgetNameET);
		budgetAmount = (EditText) findViewById(R.id.budgetAmountET);
		startDate = (EditText) findViewById(R.id.startDate);
		endDate = (EditText) findViewById(R.id.endDate);
		budgetCategory = (Spinner) findViewById(R.id.budgetCategory);
		budgetSwitch = (Switch) findViewById(R.id.budgetSwitch);
		
		saveBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveBudget connect = new saveBudget();
				connect.execute();
			}
		});
		
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		b = getIntent().getExtras();
		/*Intent stupidIntent = new Intent(this, Dashboard.class);
		stupidIntent.putExtras(b);
		userid = b.getString("userid");*/
		Log.d("bundlestring", b.toString());
		userid = b.getString("userid");
		Log.d("userID", userid);
		getCategory connect = new getCategory();
		connect.execute();
		
	}
	
	class getCategory extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			JSONParser jsonparser = new JSONParser();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("transactionType", "2"));
			//JSONObject jObject = jsonparser.makeHttpRequest("http://10.0.2.2/login/getCategory.php", "GET", list);
			JSONObject jObject = jsonparser.makeHttpRequest("http://moneymatespfms.net46.net/getCategory.php", "GET", list);
			
			try {
				Log.d("JSON", jObject.toString());
				if(jObject.getString("status").equals("success")){
					jArray = jObject.getJSONArray("category");
					categoryList = new ArrayList<String>();
					categoryList.add("All");
					for(int i = 0; i<jArray.length(); i++){
						JSONObject jObject2 = jArray.getJSONObject(i);
						categoryList.add(jObject2.getString("TransactionCategoryName"));
					}					
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
			spinnerAdapter();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
	}
	
	class saveBudget extends AsyncTask<Void, Void, Void>{
		JSONObject jObject;
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONParser jsonparser = new JSONParser();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("userid", userid));
			list.add(new BasicNameValuePair("budgetname", budgetName.getText().toString()));
			list.add(new BasicNameValuePair("amount", budgetAmount.getText().toString()));
			list.add(new BasicNameValuePair("startdate",startDate.getText().toString()));
			list.add(new BasicNameValuePair("enddate",endDate.getText().toString()));
			list.add(new BasicNameValuePair("category", budgetCategory.getSelectedItem().toString()));
			//JSONObject jObject = jsonparser.makeHttpRequest("http://10.0.2.2/login/setBudget.php", "GET", list);
			jObject = jsonparser.makeHttpRequest("http://moneymatespfms.net46.net/setBudget.php", "GET", list);
			Log.d("BudgetJSON", jObject.toString());
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				if(jObject.getString("status").equals("success")){
					String isBudgetOver = jObject.getString("overbudget");
					if(isBudgetOver.equals("false")){
						Toast toast = Toast.makeText(getApplicationContext(), "Budget set successfully. u still can spend more money", Toast.LENGTH_SHORT);
						toast.show();	
					}
					else{
						Toast toast = Toast.makeText(getApplicationContext(), "Budget set successfully. u r currently overbudget for " + jObject.getString("amount"), Toast.LENGTH_SHORT);
						toast.show();	
					}
						
				}
				else{
					Toast toast = Toast.makeText(getApplicationContext(), "Fail to save.", Toast.LENGTH_SHORT);
					toast.show();	
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			budgetSaved();
			
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
	}
	
	public void spinnerAdapter(){
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryList);
		budgetCategory.setAdapter(spinnerAdapter);
		//Log.d("spinner adapt", "blabla");
	}
	
	public void budgetSaved(){
		Toast toast = Toast.makeText(getApplicationContext(), "Budget saved.", Toast.LENGTH_SHORT);
		toast.show();
		finish();
	}
}


