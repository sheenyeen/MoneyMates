package com.example.pfm;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddBill extends Activity{
	
	EditText billNameET, amountET, dateET, remarkET;
	Spinner categorySpinner;
	Button addBillBtn, payBillBtn, cancelBtn;
	
	Bundle b;
	String selectedDate, userid, remarkFieldString;
	List<String> spinnerList;
	JSONArray categoryjArray;
	int isPaid = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_bill);
		billNameET = (EditText) findViewById(R.id.billNameET);
		amountET = (EditText) findViewById(R.id.amountET);
		dateET = (EditText) findViewById(R.id.dateET);
		remarkET = (EditText) findViewById(R.id.remarkET);
		categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
		addBillBtn = (Button) findViewById(R.id.addBillBtn);
		payBillBtn = (Button) findViewById(R.id.payBillBtn);
		cancelBtn = (Button) findViewById(R.id.cancelBtn);
		
		b = getIntent().getExtras();
		selectedDate = b.getString("selectedDate");
		userid = MyService.userid;
		Log.d("selectedDate", selectedDate);
		dateET.setText(selectedDate);
		
		addBillBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addBillPayment connect = new addBillPayment();
				connect.execute();
			}
		});
		
		payBillBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
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
					categoryjArray = jObject.getJSONArray("category");
					spinnerList = new ArrayList<String>();
					for(int i = 0; i<categoryjArray.length(); i++){
						JSONObject jObject2 = categoryjArray.getJSONObject(i);
						spinnerList.add(jObject2.getString("TransactionCategoryName"));
					}					
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			spinnerAdapter();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}	
	}
	
	class addBillPayment extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			JSONParser jsonparser = new JSONParser();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("userid", userid));
			list.add(new BasicNameValuePair("billname", billNameET.getText().toString()));
			list.add(new BasicNameValuePair("billcategory", categorySpinner.getSelectedItem().toString()));
			list.add(new BasicNameValuePair("billamount",amountET.getText().toString()));
			list.add(new BasicNameValuePair("billdate",dateET.getText().toString()));
			
			if(remarkET.getText().toString().equals("")){
				remarkFieldString = "-"; 
				list.add(new BasicNameValuePair("billremark", remarkFieldString));
			}else{
				list.add(new BasicNameValuePair("billremark",remarkET.getText().toString()));
			}
			
			//JSONObject jObject = jsonparser.makeHttpRequest("http://10.0.2.2/login/addBill.php", "GET", list);
			JSONObject jObject = jsonparser.makeHttpRequest("http://moneymatespfms.net46.net/addBill.php", "GET", list);
			
			try {
				Log.d("JSON", jObject.toString());
				if(jObject.getString("status").equals("success")){
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
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}		
	}
	
	public void spinnerAdapter(){
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerList);
		categorySpinner.setAdapter(spinnerAdapter);
		//Log.d("spinner adapt", "blabla");
	}
}
