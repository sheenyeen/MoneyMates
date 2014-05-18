package com.example.pfm;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddTransaction extends Activity {
	
	Button expenseButton, incomeButton, addTransactionButton, cancelButton;
	EditText amountField, dateField, remarkField;
	Spinner categoryList;
	TextView selectedSpinnerTV;
	String userid = "", remarkFieldString;
	Bundle b;
	String transactionType = "2"; 
	JSONArray jArray;
	List<String> spinnerList;
	boolean addTransFlag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_transaction);
		
		expenseButton = (Button) findViewById(R.id.expenseBtn);
		incomeButton = (Button) findViewById(R.id.incomeBtn);
		amountField = (EditText) findViewById(R.id.amountField);
		dateField = (EditText) findViewById(R.id.dateField);
		remarkField = (EditText) findViewById(R.id.remarkField);
		categoryList = (Spinner) findViewById(R.id.categoryList);
		addTransactionButton = (Button) findViewById(R.id.addTransactionBtn);
		cancelButton = (Button) findViewById(R.id.cancelBtn);
		
		b = getIntent().getExtras();
		userid = b.getString("userid");
		Log.d("bundlestring", b.toString());
		Log.d("userID", userid);

		getCategory connect = new getCategory();
		connect.execute();
		expenseButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				transactionType = "2";
				getCategory connect = new getCategory();
				connect.execute();
			}
		});
		
		incomeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				transactionType = "1";
				getCategory connect = new getCategory();
				connect.execute();
			}
		});	
		
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AddTransaction.this.finish();
			}
		});
		
		addTransactionButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addTransaction connect2 = new addTransaction();
				connect2.execute();
			}
		});		
	}
	
	class getCategory extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONParser jsonparser = new JSONParser();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("transactionType", transactionType));
			//JSONObject jObject = jsonparser.makeHttpRequest("http://10.0.2.2/login/getCategory.php", "GET", list);
			JSONObject jObject = jsonparser.makeHttpRequest("http://moneymatespfms.net46.net/getCategory.php", "GET", list);
			
			try {
				Log.d("JSON", jObject.toString());
				if(jObject.getString("status").equals("success")){
					jArray = jObject.getJSONArray("category");
					spinnerList = new ArrayList<String>();
					for(int i = 0; i<jArray.length(); i++){
						JSONObject jObject2 = jArray.getJSONObject(i);
						spinnerList.add(jObject2.getString("TransactionCategoryName"));
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
			spinnerAdapter();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}		
	}
	
	class addTransaction extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Log.d("category", categoryList.getSelectedItem().toString());
			//selectedSpinnerTV = (TextView) categoryList.getSelectedView();
			JSONParser jsonparser = new JSONParser();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("userid", userid));
			list.add(new BasicNameValuePair("transactiontype", transactionType));
			list.add(new BasicNameValuePair("transactioncategory", categoryList.getSelectedItem().toString()));
			list.add(new BasicNameValuePair("amount",amountField.getText().toString()));
			list.add(new BasicNameValuePair("date",dateField.getText().toString()));
			
			if(remarkField.getText().toString().equals("")){
				remarkFieldString = "-"; 
				list.add(new BasicNameValuePair("remark", remarkFieldString));
			}else{
				list.add(new BasicNameValuePair("remark",remarkField.getText().toString()));
			}
			
			//JSONObject jObject = jsonparser.makeHttpRequest("http://10.0.2.2/login/addTransaction.php", "GET", list);
			JSONObject jObject = jsonparser.makeHttpRequest("http://moneymatespfms.net46.net/addTransaction.php", "GET", list);
			
			try {
				Log.d("JSON", jObject.toString());
				if(jObject.getString("status").equals("success")){
					addTransFlag = true;					
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
			if(addTransFlag==true){
				/*Context context = getApplicationContext();
				CharSequence text = "Maintenance reported successfully";
				int duration = Toast.LENGTH_SHORT;

				// TODO
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();*/
				transactionIntent();
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}	
		
	}
	
	public void spinnerAdapter(){
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerList);
		categoryList.setAdapter(spinnerAdapter);
		//Log.d("spinner adapt", "blabla");
	}
	
	public void transactionIntent(){
		Intent transactionIntent = new Intent(this, Transaction.class);
		b.putString("userid", userid);
		transactionIntent.putExtras(b);
		startActivity(transactionIntent);
		finish();
	}
}
