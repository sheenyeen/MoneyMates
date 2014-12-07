package com.example.pfm;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.pfm.AddBill.addBillPayment;
import com.example.pfm.AddBill.getCategory;
import com.example.pfm.ModifyTransaction.deleteTransaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ModifyBill extends Activity {
	
	Button saveBtn, deleteBtn, cancelBtn;
	EditText billNameET, billAmountET, billDateET, billRemarkET;
	Spinner categorySpinner;
	Bundle b;
	String userid;
	List<String> spinnerList, spinnerListId;
	JSONArray categoryjArray;
	Bill bill;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_bill);
		
		b = getIntent().getExtras();
		
		bill = Bill.createFromString(b.getString("bill"));
		
		saveBtn = (Button) findViewById(R.id.saveBtn);
		deleteBtn = (Button) findViewById(R.id.deleteBtn);
		cancelBtn = (Button) findViewById(R.id.cancelBtn);
		billNameET = (EditText) findViewById(R.id.billNameET);
		billAmountET = (EditText) findViewById(R.id.amountET);
		billDateET = (EditText) findViewById(R.id.dateET);
		billRemarkET = (EditText) findViewById(R.id.remarkET);
		categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
		
		userid = MyService.userid;
		
		billNameET.setText(bill.billName);
		billAmountET.setText(bill.billAmount);
		billDateET.setText(bill.billDate);
		billRemarkET.setText(bill.billRemark);
		getCategory connect = new getCategory();
		connect.execute();
		
		saveBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UpdateBill connect2 = new UpdateBill();
				connect2.execute();
			}
		});
		
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		deleteBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				deleteAlert();
			}
		});
		
	}
	
	public void deleteAlert(){
		AlertDialog logoutAlert = new AlertDialog.Builder(this)
		.setTitle("Delete transaction")
	    .setMessage("Delete this bill?")
	    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	DeleteBill connect3 = new DeleteBill();
				connect3.execute();
	        }
	     })
	    .setNegativeButton("No", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // do nothing
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	     .show();
		
	}
	
	class getCategory extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			JSONParser jsonparser = new JSONParser();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("transactionType", "2"));
			//JSONObject jObject = jsonparser.makeHttpRequest("http://10.0.2.2/login/getCategory.php", "GET", list);
			JSONObject jObject = jsonparser.makeHttpRequest(MyService.URL+"getCategory.php", "GET", list);
			
			try {
				Log.d("JSON", jObject.toString());
				if(jObject.getString("status").equals("success")){
					categoryjArray = jObject.getJSONArray("category");
					spinnerList = new ArrayList<String>();
					spinnerListId = new ArrayList<String>();
					for(int i = 0; i<categoryjArray.length(); i++){
						JSONObject jObject2 = categoryjArray.getJSONObject(i);
						spinnerList.add(jObject2.getString("TransactionCategoryName"));
						spinnerListId.add(jObject2.getString("TransactionCategoryID"));
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


	class UpdateBill extends AsyncTask<Void, Void, Void>{
		JSONObject jObject;
		@Override
		protected Void doInBackground(Void... arg0) {
			JSONParser jsonparser = new JSONParser();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("BillID", bill.billId));
			list.add(new BasicNameValuePair("BillName", billNameET.getText().toString()));
			list.add(new BasicNameValuePair("BillAmount", billAmountET.getText().toString()));
			list.add(new BasicNameValuePair("BillDate", billDateET.getText().toString()));
			list.add(new BasicNameValuePair("BillRemark", billRemarkET.getText().toString()));
			list.add(new BasicNameValuePair("billCategoryId", spinnerListId.get(spinnerList.indexOf(categorySpinner.getSelectedItem()))));
			//JSONObject jObject = jsonparser.makeHttpRequest("http://10.0.2.2/login/getCategory.php", "GET", list);
			Log.d("GET parameter", list.toString());
			Log.d("Calling to url", MyService.URL+"editBill.php");
			jObject = jsonparser.makeHttpRequest(MyService.URL+"editBill.php", "GET", list);

			Log.d("JSON", jObject.toString());
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			try {
				Log.d("JSON", jObject.toString());
				if(jObject.getString("status").equals("success")){
					Toast.makeText(getApplicationContext(), "Update successfully", Toast.LENGTH_SHORT).show();
					finish();
				}
				else

					Toast.makeText(getApplicationContext(), "Fail to update", Toast.LENGTH_SHORT).show();
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}	
	}


	class DeleteBill extends AsyncTask<Void, Void, Void>{
		JSONObject jObject;
		@Override
		protected Void doInBackground(Void... arg0) {
			JSONParser jsonparser = new JSONParser();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("BillID", bill.billId));//JSONObject jObject = jsonparser.makeHttpRequest("http://10.0.2.2/login/getCategory.php", "GET", list);
			Log.d("GET parameter", list.toString());
			Log.d("Calling to url", MyService.URL+"deleteBill.php");
			jObject = jsonparser.makeHttpRequest(MyService.URL+"deleteBill.php", "GET", list);

			Log.d("JSON", jObject.toString());
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			try {
				Log.d("JSON", jObject.toString());
				if(jObject.getString("status").equals("success")){
					Toast.makeText(getApplicationContext(), "Delete successfully", Toast.LENGTH_SHORT).show();
					finish();
				}
				else

					Toast.makeText(getApplicationContext(), "Fail to delete", Toast.LENGTH_SHORT).show();
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}	
	}
	
	public void spinnerAdapter(){
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerList);
		categorySpinner.setAdapter(spinnerAdapter);
		categorySpinner.setSelection(spinnerListId.indexOf(""+bill.billCategoryId));
		Log.d("spinner adapt", "blabla "+bill.billCategoryId);
		Log.d("spinner adapt", "blabla "+spinnerListId);
	}
}
