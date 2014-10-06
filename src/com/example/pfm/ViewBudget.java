package com.example.pfm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class ViewBudget extends Activity{
	
	TextView monthTV;
	Button previousMonth, nextMonth;
	ListView budgetListView;
	
	Calendar currenttime;
	String currentMonth, currentYear;
	String userid;
	Bundle b;
	JSONArray categoryJArray, budgetJArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_budget);
		
		monthTV = (TextView) findViewById(R.id.displayMonth);
		previousMonth = (Button) findViewById(R.id.lastMonthArrow);
		nextMonth = (Button) findViewById(R.id.nextMonthArrow);
		budgetListView = (ListView) findViewById(R.id.budgetListView);
		currenttime = Calendar.getInstance();
		Log.d("currenttime", currenttime.toString());
		
		previousMonth.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currenttime.add(Calendar.MONTH, -1);
				setBudget();
			}
		});
		
		nextMonth.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currenttime.add(Calendar.MONTH, +1);
				setBudget();
			}
		});	
		
		b = getIntent().getExtras();
		userid = b.getString("userid");
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		getBudget connect = new getBudget();
		connect.execute();
		super.onResume();
	}

	public void setBudget(){
		currentMonth = new SimpleDateFormat("MMM").format(currenttime.getTime());
		monthTV.setText(currentMonth + " " + currenttime.get(Calendar.YEAR));
		displayBudget();
	}
	
	class getBudget extends AsyncTask<Void, Void, Void>{
		@Override
		protected Void doInBackground(Void... params) {
			JSONParser jsonparser = new JSONParser();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("userid", userid));
			Log.d("GET parameter", list.toString());
			Log.d("Calling to url", "http://moneymatespfms.net46.net/getBudget.php");
			JSONObject jObject = jsonparser.makeHttpRequest("http://moneymatespfms.net46.net/getBudget.php", "GET", list);
			
			try {
				Log.d("JSON", jObject.toString());
				if(jObject.getString("status").equals("success")){
					budgetJArray = jObject.getJSONArray("budget");
					categoryJArray = jObject.getJSONArray("category");
					

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			setBudget();
		}


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}		
	}
	
	public void displayBudget()
	{
		ArrayList<HashMap<String, String>> budget;
		
		budget = new ArrayList<HashMap<String, String>>();
		try {

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); 

			Log.d("categories", categoryJArray.toString());
			Log.d("budgets", budgetJArray.toString());
			for(int i = 0; i < categoryJArray.length(); i++){
				JSONObject categoryObj = categoryJArray.getJSONObject(i);
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("category", categoryObj.getString("TransactionCategoryName"));
				hm.put("categoryID", categoryObj.getString("TransactionCategoryID"));
				String amount =  "0";
				for(int j = 0; j < budgetJArray.length(); j++){
					JSONObject budgetObj = budgetJArray.getJSONObject(j);
					
					// TODO chg column name
					String datetime = budgetObj.getString("BudgetMonth");
					
					Date date = simpleDateFormat.parse(datetime);
					Log.d("date", date.toString());
					Calendar c = Calendar.getInstance();
					c.setTime(date);
					
					if((c.get(Calendar.MONTH) == currenttime.get(Calendar.MONTH)) && (c.get(Calendar.YEAR) == currenttime.get(Calendar.YEAR)))
						if(budgetObj.getString("TransactionCategoryID").equals(categoryObj.getString("TransactionCategoryID")))
							amount = budgetObj.getString("Amount");
				}
				hm.put("amount", amount);
				budget.add(hm);
			}			
				
		} catch (NullPointerException e) {
			Log.d("exeption", e.toString());
			e.printStackTrace();
		}
		 catch (Exception e) {
				e.printStackTrace();
			}

		Log.d("budget arraylist", budget.toString());
		LazyAdapter adapter = new LazyAdapter(this, budget);
		budgetListView.setAdapter(adapter);
	} 
	
	public void Click(View v){
		Intent addBudgetIntent = new Intent(this, AddBudget.class);
		Bundle b = new Bundle();
		String tag = (String) v.getTag();
		
		Log.d("categoryTag", tag);

		b.putString("userid", userid);
		b.putString("startdate", currenttime.get(Calendar.YEAR) + "-" + (currenttime.get(Calendar.MONTH) +1) + "-" + (currenttime.get(Calendar.DATE)+1));
		b.putString("categoryid", tag);
		
		Log.d("currenttime", currenttime.toString());
		Log.d("startdate", b.getString("startdate"));
		addBudgetIntent.putExtras(b);
		Log.d("Response", b.toString());
		startActivity(addBudgetIntent);
	}
}
