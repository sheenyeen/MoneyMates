package com.example.pfm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
	JSONArray categoryjArray, categoryidjArray;
	List<String> spinnerList;
	boolean addTransFlag = false;
	Calendar currenttime;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		
		currenttime = Calendar.getInstance();
		
		dateField.setText(currenttime.get(Calendar.YEAR) + "-" + (currenttime.get(Calendar.MONTH)+1) + "-" + currenttime.get(Calendar.DATE));

		getCategory connect = new getCategory();
		connect.execute();
		expenseButton.setBackgroundResource(R.drawable.button_grey);
		expenseButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				transactionType = "2";
				incomeButton.setBackgroundResource(R.drawable.button_blue_square);
				expenseButton.setBackgroundResource(R.drawable.button_grey);
				getCategory connect = new getCategory();
				connect.execute();
			}
		});
		
		incomeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				transactionType = "1";
				incomeButton.setBackgroundResource(R.drawable.button_grey);
				expenseButton.setBackgroundResource(R.drawable.button_blue_square);
				getCategory connect = new getCategory();
				connect.execute();
			}
		});	
		
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AddTransaction.this.finish();
			}
		});
		
		addTransactionButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(amountField.getText().toString().equals("")){
					Toast toast = Toast.makeText(getApplicationContext(), "Please insert amount.", Toast.LENGTH_SHORT);
					toast.show();
				}else if(dateField.getText().toString().equals("")){
					Toast toast = Toast.makeText(getApplicationContext(), "Please insert date.", Toast.LENGTH_SHORT);
					toast.show();
				}else{
					//Log.d("Check", "Enter else loop");
					double categorytotalexpenses = 0;
					String selectedCategoryId = "0";
					
					for(int i = 0; i < categoryjArray.length(); i++){
						try {
							JSONObject jObject = categoryjArray.getJSONObject(i);
							if((jObject.getString("TransactionCategoryName").equals(categoryList.getSelectedItem().toString()))){
								selectedCategoryId = jObject.getString("TransactionCategoryID");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					Log.d("Selected category id", selectedCategoryId);
					Log.d("Currlist", Transaction.currList.toString());
					for(int i = 0; i < Transaction.currList.size(); i++)
					{
						JSONObject job = Transaction.currList.get(i);
						try{										
							if(job.getString("TransactionCategoryID").equals(selectedCategoryId))
							{
								categorytotalexpenses += Double.parseDouble(job.getString("Amount"));
							} 
						}catch(JSONException e){
							e.printStackTrace();
						}
					}
					Log.d("total category expenses", String.valueOf(categorytotalexpenses));
					
					Log.d("budgetArray", String.valueOf(MyService.budgetArray.length()));
					
					for(int i = 0; i < MyService.budgetArray.length(); i++)
					{
						try {
							
							JSONObject job = MyService.budgetArray.getJSONObject(i);
							SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
							if(job.getString("TransactionCategoryID").equals(selectedCategoryId)){
								Date date = simpleDateFormat.parse(job.getString("BudgetMonth"));
								Log.d("compare date", date.toString() + Transaction.currenttime.get(Calendar.MONTH) + Transaction.currenttime.get(Calendar.YEAR));
								Calendar c = Calendar.getInstance();
								c.setTime(date);

								if ((c.get(Calendar.MONTH) == Transaction.currenttime.get(Calendar.MONTH)) && (c.get(Calendar.YEAR) == Transaction.currenttime.get(Calendar.YEAR))) {
									if((Double.parseDouble(amountField.getText().toString()) + categorytotalexpenses) >= Double.parseDouble(job.getString("Amount"))){
										Log.d("amount entered", amountField.getText().toString());
										Log.d("budget amount", job.getString("Amount"));
										Toast toast = Toast.makeText(getApplicationContext(),
												"Budget for " + categoryList.getSelectedItem().toString() + " category has reached.", Toast.LENGTH_LONG);
										toast.show();
										break;
									}
									else{
										Log.d("Check", "Trans > budget");
									}
									
								}
								else{
									Log.d("Check", "Month & year");									
								}
							}
							else{
								Log.d("Check", "Not = selected Category id");
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
						
					addTransaction connect2 = new addTransaction();
					connect2.execute();
				}
			}
		});		
	}
	
	class getCategory extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			JSONParser jsonparser = new JSONParser();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("transactionType", transactionType));
			//JSONObject jObject = jsonparser.makeHttpRequest("http://10.0.2.2/login/getCategory.php", "GET", list);
			JSONObject jObject = jsonparser.makeHttpRequest(MyService.URL+"getCategory.php", "GET", list);
			//JSONObject jObject = jsonparser.makeHttpRequest("http://54.169.79.91/MoneyMatesPHP/getCategory.php", "GET", list);
			
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
			spinnerAdapter();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}		
	}
	
	class addTransaction extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
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
			JSONObject jObject = jsonparser.makeHttpRequest(MyService.URL+"addTransaction.php", "GET", list);
			//JSONObject jObject = jsonparser.makeHttpRequest("http://54.169.79.91/MoneyMatesPHP/addTransaction.php", "GET", list);
			
			try {
				Log.d("JSON", jObject.toString());
				if(jObject.getString("status").equals("success")){
					addTransFlag = true;					
				}
				else{
					Log.d("Fail to add transaction", "Fail to add transaction");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(addTransFlag==true){
				transactionIntent();
			}
		}

		@Override
		protected void onPreExecute() {
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
