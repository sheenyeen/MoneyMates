package com.example.pfm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class AddBill extends Activity{
	
	EditText billNameET, amountET, dateET, remarkET;
	Spinner categorySpinner;
	Button addBillBtn, cancelBtn;
	Switch reminderSwitch;
	
	Bundle b;
	String selectedDate, userid, remarkFieldString;
	List<String> spinnerList;
	JSONArray categoryjArray;
	int isPaid = 0;
	int mhour, mminute;
	PendingIntent pi2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_bill);
		billNameET = (EditText) findViewById(R.id.billNameET);
		amountET = (EditText) findViewById(R.id.amountET);
		dateET = (EditText) findViewById(R.id.dateET);
		remarkET = (EditText) findViewById(R.id.remarkET);
		categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
		addBillBtn = (Button) findViewById(R.id.addBillBtn);
		cancelBtn = (Button) findViewById(R.id.cancelBtn);
		reminderSwitch = (Switch) findViewById(R.id.switch1);
		
		b = getIntent().getExtras();
		selectedDate = b.getString("selectedDate");
		userid = MyService.userid;
		Log.d("selectedDate", selectedDate);
		dateET.setText(selectedDate);
		
		getCategory connect = new getCategory();
		connect.execute();
		
		addBillBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addBillPayment connect2 = new addBillPayment();
				connect2.execute();
			}
		});
		
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});	
		

		reminderSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				Log.d("Clicked", "Switch");
				if(reminderSwitch.isChecked()){
					Log.d("Clicked", "Switch on");
					showDialog(1);
				}else{
					Log.d("Switch", "Switch off");
					//cancel alarm
					AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
					alarmManager.cancel(pi2);
					Toast toast = Toast.makeText(getApplicationContext(), "Alarm off.", Toast.LENGTH_SHORT);
					toast.show();
				}
			}
		});
	}
	
	class getCategory extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			JSONParser jsonparser = new JSONParser();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("transactionType", "2"));
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
	
	class addBillPayment extends AsyncTask<Void, Void, Void>{
		JSONObject jObject ;
		@Override
		protected Void doInBackground(Void... arg0) {
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
			jObject = jsonparser.makeHttpRequest(MyService.URL+"addBill.php", "GET", list);
			//jObject = jsonparser.makeHttpRequest("http://54.169.79.91/MoneyMatesPHP/addBill.php", "GET", list);
			
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			try {
				Log.d("JSON", jObject.toString());
				if(jObject.getString("status").equals("success")){
					Calendar calendar = Calendar.getInstance();
					AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
					//mPickTime.setText(new StringBuilder().append(pad(mhour)).append(":").append(pad(mminute)));
					//calendar.set(calendar.get(Calendar.YEAR), eventtime.get(Calendar.MONTH), eventtime.get(Calendar.DATE), eventtime.get(Calendar.HOUR_OF_DAY), eventtime.get(Calendar.MINUTE), eventtime.get(Calendar.SECOND));
					
					//if alarm hour b4 currtime
					if(calendar.get(Calendar.HOUR_OF_DAY) > mhour || (calendar.get(Calendar.HOUR_OF_DAY) == mhour && calendar.get(Calendar.MINUTE) >= mminute)){
						calendar.add(Calendar.DATE, 1);		
					}	
					
					calendar.set(Calendar.HOUR_OF_DAY, mhour);
					calendar.set(Calendar.MINUTE, mminute);
					calendar.set(Calendar.SECOND, 0);
					
					long alarmTime = calendar.getTimeInMillis();
					
					Intent alarmIntent = new Intent(getApplicationContext(), BillAlarm.class);
					alarmIntent.putExtra("alarmTime", alarmTime);
					
					pi2 = PendingIntent.getBroadcast(getApplicationContext(), 1, alarmIntent, pi2.FLAG_ONE_SHOT);
					am.set(AlarmManager.RTC_WAKEUP, alarmTime, pi2); 
					if(reminderSwitch.isChecked()){
						Toast toast = Toast.makeText(getApplicationContext(), "Alarm set.", Toast.LENGTH_SHORT);
						toast.show();	
					}
					finish();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}		
	}
	
	public void spinnerAdapter(){
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerList);
		categorySpinner.setAdapter(spinnerAdapter);
		//Log.d("spinner adapt", "blabla");
	}
	
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mhour = hourOfDay;
			mminute = minute;
			
		}
	};
	
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0:
			//return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);

		case 1:
			return new TimePickerDialog(this, mTimeSetListener, mhour, mminute, false);
		}
		return null;
	}
	
	
	
}
