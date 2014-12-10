package com.example.pfm;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.pfm.AddTransaction.getCategory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class ModifyTransaction extends Activity {

	Button expenseButton, incomeButton, saveButton, deleteButton, cancelButton;
	EditText amountField, dateField, remarkField;
	Spinner categoryList;
	TextView selectedSpinnerTV;
	String userid = "", transid = "", transtypeid = "", transcategoryid = "",
			resourceid = "", amount = "", remark = "", date = "",
			statusid = "", createdate = "", editdate = "";
	String transactionType = "1", remarkFieldString;
	Bundle b;
	JSONArray jArray;
	List<String> spinnerList;
	boolean deleteFlag = false, modifyFlag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_transaction);

		b = getIntent().getExtras();
		//userid = b.getString("userid");
		userid = MyService.userid;
		transid = b.getString("transid");
		transtypeid = b.getString("transtypeid");
		transcategoryid = b.getString("transcategoryid");
		resourceid = b.getString("resourceid");
		amount = b.getString("amount");
		remark = b.getString("remark");
		date = b.getString("date");
		statusid = b.getString("statusid");
		createdate = b.getString("createdate");
		editdate = b.getString("editdate");

		Log.d("bundlestring", b.toString());
		Log.d("userID", userid);

		expenseButton = (Button) findViewById(R.id.expenseBtn);
		incomeButton = (Button) findViewById(R.id.incomeBtn);
		saveButton = (Button) findViewById(R.id.saveBtn);
		deleteButton = (Button) findViewById(R.id.deleteBtn);
		cancelButton = (Button) findViewById(R.id.cancelBtn);
		amountField = (EditText) findViewById(R.id.amountField);
		dateField = (EditText) findViewById(R.id.dateField);
		remarkField = (EditText) findViewById(R.id.remarkField);
		categoryList = (Spinner) findViewById(R.id.categoryList);

		amountField.setText(amount);
		dateField.setText(date.substring(0,10));
		remarkField.setText(remark);

		getSpinnerCategory connect2 = new getSpinnerCategory();
		connect2.execute();
		
		expenseButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				incomeButton.setBackgroundResource(R.drawable.button_blue_square);
				expenseButton.setBackgroundResource(R.drawable.button_grey);
				transtypeid = "2";
				getSpinnerCategory connect = new getSpinnerCategory();
				connect.execute();
			}
		});
		
		incomeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				incomeButton.setBackgroundResource(R.drawable.button_grey);
				expenseButton.setBackgroundResource(R.drawable.button_blue_square);
				transtypeid = "1";
				getSpinnerCategory connect = new getSpinnerCategory();
				connect.execute();
			}
		});	

		cancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ModifyTransaction.this.finish();
			}
		});

		deleteButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteAlert();
			}
		});
		
		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//Log.d("CLICK save", "asd");
				modifyTransaction connect = new modifyTransaction();
				connect.execute();
			}
		});
	}

	class getSpinnerCategory extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONParser jsonparser = new JSONParser();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("transactionType", transtypeid));
			//JSONObject jObject = jsonparser.makeHttpRequest("http://10.0.2.2/login/getSpinnerCategory.php","GET", list);
			JSONObject jObject = jsonparser.makeHttpRequest(MyService.URL+"getSpinnerCategory.php","GET", list);

			try {
				Log.d("JSON", jObject.toString());
				if (jObject.getString("status").equals("success")) {
					jArray = jObject.getJSONArray("category");
					spinnerList = new ArrayList<String>();
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject categories = jArray.getJSONObject(i);
						spinnerList.add(categories.getString("TransactionCategoryName"));
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

	class modifyTransaction extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			JSONParser jsonparser = new JSONParser();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("userid", userid));
			list.add(new BasicNameValuePair("transid", transid));
			list.add(new BasicNameValuePair("transactiontype", transtypeid));
			list.add(new BasicNameValuePair("transactioncategory", categoryList.getSelectedItem().toString()));
			list.add(new BasicNameValuePair("amount", amountField.getText().toString()));
			list.add(new BasicNameValuePair("date", dateField.getText().toString()));

			if (remarkField.getText().toString().equals("")) {
				remarkFieldString = "-";
				list.add(new BasicNameValuePair("remark", remarkFieldString));
			} else {
				list.add(new BasicNameValuePair("remark", remarkField.getText().toString()));
			}

			//JSONObject jObject = jsonparser.makeHttpRequest("http://10.0.2.2/login/modifyTransaction.php", "GET", list);
			JSONObject jObject =jsonparser.makeHttpRequest(MyService.URL+"modifyTransaction.php", "GET", list);

			try {
				Log.d("JSON", jObject.toString());
				if (jObject.getString("status").equals("success")) {
					Log.d("Modification", "Successfully modified.");
					modifyFlag = true;
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
			if (modifyFlag == true) {
				Toast toast = Toast.makeText(getApplicationContext(), "Transaction updated", Toast.LENGTH_SHORT);
	    		toast.show();
				transactionIntent();
			}else{
				Log.d("Modify Fail", "Fail to modify transaction");
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Log.d("Execute modify", "Modify PreExecute");
		}

	}

	class deleteTransaction extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			JSONParser jsonparser = new JSONParser();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("userid", userid));
			list.add(new BasicNameValuePair("transid", transid));

			//JSONObject jObject = jsonparser.makeHttpRequest("http://10.0.2.2/login/deleteTransaction.php", "GET", list);
			JSONObject jObject = jsonparser.makeHttpRequest(MyService.URL+"deleteTransaction.php","GET", list);

			try {
				Log.d("JSON", jObject.toString());
				if (jObject.getString("status").equals("success")) {
					deleteFlag = true;
					Log.d("Deletion", "Transaction deleted.");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			if (deleteFlag == true) {
				transactionIntent();
	            Toast toast = Toast.makeText(getApplicationContext(), "Transaction deleted", Toast.LENGTH_SHORT);
	    		toast.show();
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

	}

	public void spinnerAdapter() {
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, spinnerList);
		categoryList.setAdapter(spinnerAdapter);

		try {
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject categories = jArray.getJSONObject(i);
				Log.d("Comparing", categories.getString("TransactionCategoryID")+", "+transcategoryid);
				if(categories.getString("TransactionCategoryID").equals(transcategoryid))
				{
					categoryList.setSelection(i);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void transactionIntent() {
		Intent transactionIntent = new Intent(this, Transaction.class);
		b.putString("userid", userid);
		transactionIntent.putExtras(b);
		startActivity(transactionIntent);
		finish();
	}
	
	public void deleteAlert(){
		AlertDialog logoutAlert = new AlertDialog.Builder(this)
		.setTitle("Delete transaction")
	    .setMessage("Delete this transaction?")
	    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	deleteTransaction connect3 = new deleteTransaction();
				connect3.execute();
	        }
	     })
	    .setNegativeButton("No", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // do nothing
	        }
	     })
	    .setIcon(android.R.drawable.ic_menu_delete)
	     .show();
		
	}

}
