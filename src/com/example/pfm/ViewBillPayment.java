package com.example.pfm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.pfm.ModifyFinancialGoal.deleteGoal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class ViewBillPayment extends Activity {
	
	JSONArray jArray;
	ArrayList<String> eventlist = new ArrayList<String>();
	ArrayList<Bill> bills = new ArrayList<Bill>();
	ArrayList<Bill> filteredbills = new ArrayList<Bill>();
	 
	public GregorianCalendar month, itemmonth;// calendar instances.

	public CalendarAdapter adapter;// adapter instance
	public ArrayList<String> items;
	public ArrayList<Bill> selectedDateBill = new ArrayList<Bill>();
	public ArrayList<String> selectedDateBillString = new ArrayList<String>();
	public ArrayList<String> selectedDateBillID = new ArrayList<String>(); 
    public HashMap<String, List<String>> list_items;
    
    String[] days = {"S", "M", "T", "W", "T", "F", "S"};
    String currentMonth, userid, selectedGridDate, selectedBillId;
    Calendar currenttime;
    Bundle b = new Bundle();
    Button previousMonth, nextMonth, addBillBtn;
    TextView monthTV, dateTV;
    ListView billListView;
    BillAdapter listViewAdapter;

	@Override
	protected void onResume() {

		final getBill connect = new getBill();
		connect.execute();
		filteredbills.clear(); 
		listViewAdapter = new BillAdapter(ViewBillPayment.this, filteredbills);
		//BillAdapter adapter = new BillAdapter(BillPayment.this, selectedDateBill);
		billListView.setAdapter(listViewAdapter);
		super.onResume();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_bill_payment);
		previousMonth = (Button) findViewById(R.id.lastMonthArrow);
		nextMonth = (Button) findViewById(R.id.nextMonthArrow);
		monthTV = (TextView) findViewById(R.id.displayMonth);
		billListView = (ListView) findViewById(R.id.billListView);
		dateTV = (TextView) findViewById(R.id.dateTV);
		addBillBtn = (Button) findViewById(R.id.addBillBtn);
		
		b = new Bundle();
		userid = MyService.userid;
	
		month = (GregorianCalendar) GregorianCalendar.getInstance();
		itemmonth = (GregorianCalendar) month.clone();

		items = new ArrayList<String>();
        list_items = new HashMap<String, List<String>>();

		adapter = new CalendarAdapter(this, month);

		GridView dayGridView = (GridView) findViewById(R.id.dayGridView);
		final GridView gridview = (GridView) findViewById(R.id.gridView);
		
		currenttime = Calendar.getInstance();
		
		currentMonth = new SimpleDateFormat("MMM").format(currenttime.getTime());
		monthTV.setText(currentMonth + " " + currenttime.get(Calendar.YEAR));

		dateTV.setText(currenttime.get(Calendar.YEAR) + "-" + (currenttime.get(Calendar.MONTH)+1) + "-" + currenttime.get(Calendar.DATE));
		selectedGridDate = currenttime.get(Calendar.YEAR) + "-" + (currenttime.get(Calendar.MONTH)+1) + "-" + currenttime.get(Calendar.DATE);
		
		b.putString("selectedDate", selectedGridDate);
		Log.d("selectedDate", selectedGridDate);
		Log.d("selectedDateBundle", b.getString("selectedDate").toString());
		
		previousMonth.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				month.add(Calendar.MONTH, -1);
				currenttime.add(Calendar.MONTH, -1);
				currentMonth = new SimpleDateFormat("MMM").format(currenttime.getTime());
				monthTV.setText(currentMonth + " " + currenttime.get(Calendar.YEAR));
				adapter = new CalendarAdapter(ViewBillPayment.this, month);
				gridview.setAdapter(adapter);
				adapter.setListItems(list_items);
				adapter.notifyDataSetChanged();
			}
		});
		
		nextMonth.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				month.add(Calendar.MONTH, +1);
				currenttime.add(Calendar.MONTH, +1);
				currentMonth = new SimpleDateFormat("MMM").format(currenttime.getTime());
				monthTV.setText(currentMonth + " " + currenttime.get(Calendar.YEAR));
				adapter = new CalendarAdapter(ViewBillPayment.this, month);
				gridview.setAdapter(adapter);
				adapter.setListItems(list_items);
				adapter.notifyDataSetChanged();
			}
		});
		
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, days);
		dayGridView.setAdapter(arrayAdapter);
		gridview.setAdapter(adapter);
		
		gridview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long arg3) {
				
				selectedDateBillString.clear();
				selectedDateBillID.clear();
				((CalendarAdapter) parent.getAdapter()).setSelected(v);
				selectedGridDate = CalendarAdapter.dayString.get(position);
				dateTV.setText(selectedGridDate);
				b.putString("selectedDate", selectedGridDate);
				Log.d("selectedDate", selectedGridDate);
				Log.d("selectedDateBundle", b.getString("selectedDate").toString());
				
				if (list_items.containsKey(selectedGridDate))
                {
					selectedDateBillString.add(list_items.get(selectedGridDate).get(0)+" ( RM"+list_items.get(selectedGridDate).get(2)+" )");
					
					
					selectedDateBillID.add(list_items.get(selectedGridDate).get(1));
                }
				//ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(BillPayment.this, android.R.layout.simple_list_item_1, selectedDateBillString);
				//billListView.setAdapter(arrayAdapter);
				filteredbills.clear();
		        for(Bill haiz : bills){
					Log.d("loop bills,", ""+haiz);
		        	if(haiz.billDate.equals(selectedGridDate)){
		        		filteredbills.add(haiz);

						//Log.d("kena", "yes!");
		        	}
		        	else
						Log.d("bill date", haiz.billDate);
		        }
		        
		        
				listViewAdapter = new BillAdapter(ViewBillPayment.this, filteredbills);
				//BillAdapter adapter = new BillAdapter(BillPayment.this, selectedDateBill);
				billListView.setAdapter(listViewAdapter);

			}
		});
		
		billListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long arg3) {

				b.putString("bill", ""+filteredbills.get(position));
				Log.d("bill", ""+filteredbills.get(position));
				modifyBillIntent();
				
				//modifyBillIntent();
			}
		});		
		
		addBillBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addBillIntent();
			}
		});
	
	}
	
	public void addBillIntent(){
		Intent addBillIntent = new Intent(this, AddBill.class);
		addBillIntent.putExtras(b);
		Log.d("addBillBundle", b.toString());
		startActivity(addBillIntent);
	}
	
	public void modifyBillIntent(){
		Intent modifyBillIntent = new Intent(this, ModifyBill.class);
		modifyBillIntent.putExtras(b);
		startActivity(modifyBillIntent);
	}
	
	
	class getBill extends AsyncTask<Void, Void, Void>{
		JSONObject jObject;
		@Override
		protected Void doInBackground(Void... arg0) {
			JSONParser jsonparser = new JSONParser();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("userid", MyService.userid));
			Log.d("GET parameter", list.toString());
			Log.d("Calling to url", MyService.URL+"getBill.php");
			//JSONObject jObject = jsonparser.makeHttpRequest("http://10.0.2.2/login/getBill.php", "GET", list);
			jObject = jsonparser.makeHttpRequest(MyService.URL+"getBill.php", "GET", list);

			Log.d("JSON", jObject.toString());

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			try {
				if (jObject.getString("status").equals("success")) {
					JSONArray jarr = jObject.getJSONArray("bill");
					 for(int i = 0; i < jarr.length(); i++){
						 JSONObject job = jarr.getJSONObject(i);
						 
						 ArrayList<String> billItem = new ArrayList<String>();
						 billItem.add(job.getString("BillName"));
						 billItem.add(job.getString("BillID"));
						 billItem.add(job.getString("Amount")); 
						 
						 Bill aBill = new Bill(job.getString("BillID"),job.getString("BillName"),job.getString("Amount"),job.getString("BillDate"),
								 job.getString("TransactionCategoryID"),job.getString("Remark"),""+job.getString("IsPaid").equals("1"));
						 
						 bills.add(aBill);
						 
						 list_items.put(job.getString("BillDate"), billItem);
	
					 }

					adapter.setListItems(list_items);
					adapter.notifyDataSetChanged();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			list_items.clear();
			bills.clear();
			super.onPreExecute();
		}
		
	}
	

	
	public void paybtnclick(final View v){
		
		AlertDialog payBillAlert = new AlertDialog.Builder(this)
		.setTitle("Pay Bill")
	    .setMessage("Pay this bill?")
	    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	Log.d("BTN tag", v.getTag().toString());
	    		payBill p = new payBill();
	    		for(Bill haiz: bills)
	    			if(haiz.billId.equals(v.getTag().toString())){
	    				p.bill = haiz;
	    				Log.d("matched tag", v.getTag().toString());
	    			}
	    		p.v = (TextView) v;
	    		p.execute();
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
	
	
	class payBill extends AsyncTask<Void, Void, Void>{
		Bill bill;
		TextView v = null;
		JSONObject jObject;
		@Override
		protected Void doInBackground(Void... arg0) {
			JSONParser jsonparser = new JSONParser();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("userid", MyService.userid));
			list.add(new BasicNameValuePair("BillID", bill.billId));
			list.add(new BasicNameValuePair("BillAmount", bill.billAmount));
			list.add(new BasicNameValuePair("billCategoryId", bill.billCategoryId));
			list.add(new BasicNameValuePair("BillRemark", bill.billRemark));
			Log.d("GET parameter", list.toString());
			Log.d("Calling to url", MyService.URL+"payBill.php");
			//JSONObject jObject = jsonparser.makeHttpRequest("http://10.0.2.2/login/getBill.php", "GET", list);
			jObject = jsonparser.makeHttpRequest(MyService.URL+"payBill.php", "GET", list);

			Log.d("JSON", jObject.toString());
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			try {
				if (jObject.getString("status").equals("success")) {
					v.setEnabled(false);
					v.setText("Paid");
					v.setBackgroundResource(R.drawable.button_red);
					Toast toast = Toast.makeText(getApplicationContext(), "Bill paid", Toast.LENGTH_SHORT);
					toast.show();
				}
				else{
					Toast toast = Toast.makeText(getApplicationContext(), "Failed to pay bill", Toast.LENGTH_SHORT);
					toast.show();
				}
			} catch (JSONException e) {
				Toast toast = Toast.makeText(getApplicationContext(), "Failed to pay bill", Toast.LENGTH_SHORT);
				toast.show();
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
	}	

/*	
	CalendarView calendarView;
	TextView billTV;
	long date;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bill_payment);
		calendarView = (CalendarView) findViewById(R.id.calendarView1);
		billTV = (TextView) findViewById(R.id.billTV);
		
		calendarView.setShowWeekNumber(false);
		calendarView.setFirstDayOfWeek(2); //monday
		//calendarView.setSelectedWeekBackgroundColor("#4499CC00"));
		
		date = calendarView.getDate();

		
		calendarView.setOnDateChangeListener(new OnDateChangeListener(){

			@Override
			public void onSelectedDayChange(CalendarView view, int year,
					int month, int day) {
				if(calendarView.getDate() != date){
	                date = calendarView.getDate();
	                month = month+1;
	                Toast.makeText(getApplicationContext(), day + "/" + month + "/" + year, Toast.LENGTH_SHORT).show();
	                MyService.calendarYear = year;
	                MyService.calendarMonth = month; 
	                MyService.calendarDate = day;
	                
	                getBill connect = new getBill();
	                connect.execute();
	                //addBillIntent(year, month, day);
				}
			}			
		});
	}
	
	public void addBillIntent(int year, int month, int day){
		Intent addBillIntent = new Intent(this, AddBill.class);
		startActivity(addBillIntent);
		finish();
	}
	
	*/
}
