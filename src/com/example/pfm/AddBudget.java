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

public class AddBudget extends Activity {
	
	Button saveBtn, cancelBtn;
	EditText budgetAmount;
	Switch budgetSwitch;
	
	String userid = "", startdate = "", categoryid = "";
	Bundle b;
	JSONArray jArray;
	List<String> categoryList;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_budget);
		
		saveBtn = (Button) findViewById(R.id.saveBtn);
		cancelBtn = (Button) findViewById(R.id.cancelBtn);
		budgetAmount = (EditText) findViewById(R.id.budgetAmountET);
		//startDate = (EditText) findViewById(R.id.startDate);
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
		//Log.d("bundlestring", b.toString());
		userid = b.getString("userid");
		startdate = b.getString("startdate");
		categoryid = b.getString("categoryid");
		
		//Log.d("userID", userid);
		//getCategory connect = new getCategory();
		//connect.execute();
		
	}
	
	class saveBudget extends AsyncTask<Void, Void, Void>{
		JSONObject jObject;
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONParser jsonparser = new JSONParser();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("userid", userid));
			list.add(new BasicNameValuePair("amount", budgetAmount.getText().toString()));
			list.add(new BasicNameValuePair("startdate",startdate));
			list.add(new BasicNameValuePair("categoryid",categoryid));
			//JSONObject jObject = jsonparser.makeHttpRequest("http://10.0.2.2/login/setBudget.php", "GET", list);
			jObject = jsonparser.makeHttpRequest(MyService.URL+"setBudget.php", "GET", list);
			//jObject = jsonparser.makeHttpRequest("http://54.169.79.91/MoneyMatesPHP/setBudget.php", "GET", list);
			Log.d("BudgetJSON", jObject.toString());
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			budgetSaved();
			/*try {
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
			}*/
		
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
	}
	
	public void budgetSaved(){
		Toast toast = Toast.makeText(getApplicationContext(), "Budget saved.", Toast.LENGTH_SHORT);
		toast.show();
		finish();
	}
	
	
	/*class getCategory extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			JSONParser jsonparser = new JSONParser();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("transactionType", "2"));
			//JSONObject jObject = jsonparser.makeHttpRequest("http://10.0.2.2/login/getCategory.php", "GET", list);
			JSONObject jObject = jsonparser.makeHttpRequest(MyService.URL+"getCategory.php", "GET", list);
			
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
	}*/
}


