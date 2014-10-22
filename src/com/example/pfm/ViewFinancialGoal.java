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

import com.example.pfm.ViewBudget.getBudget;
import com.example.pfm.ViewBudget.saveBudget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewFinancialGoal extends Activity{
	
	ListView goalListView;
	
	Calendar currenttime;
	String currentMonth, currentYear;
	String amountInput, tag, startdate;
	String userid;
	Bundle b;
	JSONArray categoryJArray, budgetJArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_financial_goal);
		goalListView = (ListView) findViewById(R.id.goalListView);
		currenttime = Calendar.getInstance();
		Log.d("currenttime", currenttime.toString());
		
		b = getIntent().getExtras();
		userid = b.getString("userid");
		
		

		ArrayList<HashMap<String, String>> goals = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("goalname", "Car Saving");
		hm.put("duration", "Yearly" );
		hm.put("amount", "500.00");
		goals.add(hm);
		hm = new HashMap<String, String>();
		hm.put("goalname", "House leasing");
		hm.put("duration", "Yearly" );
		hm.put("amount", "700.00");
		goals.add(hm);
		hm = new HashMap<String, String>();
		hm.put("goalname", "Saving");
		hm.put("duration", "Monthly" );
		hm.put("amount", "50.00");
		goals.add(hm);
		hm = new HashMap<String, String>();
		hm.put("goalname", "Test");
		hm.put("duration", "Monthly" );
		hm.put("amount", "1000.00");
		goals.add(hm);
		GoalLazyAdapter adapter = new GoalLazyAdapter(this, goals);
		goalListView.setAdapter(adapter);
		
		goalListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), AddFinancialGoal.class);
				startActivity(intent);
			}
			
		});
		
	}
	
	/*@Override
	protected void onResume() {
		getBudget connect = new getBudget();
		connect.execute();
		super.onResume();
	}

	ArrayList<HashMap<String, String>> budget;
	public void setBudget(){
		budget = new ArrayList<HashMap<String, String>>();
		try {

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
			//Log.d("categories", categoryJArray.toString());
			//Log.d("budgets", budgetJArray.toString());
			for(int i = 0; i < categoryJArray.length(); i++){
				JSONObject categoryObj = categoryJArray.getJSONObject(i);
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("category", categoryObj.getString("TransactionCategoryName"));
				hm.put("categoryID", categoryObj.getString("TransactionCategoryID"));
				for(int j = 0; j < budgetJArray.length(); j++){
					JSONObject budgetObj = budgetJArray.getJSONObject(j);
					
					String datetime = budgetObj.getString("BudgetMonth");
					hm.put("amount", "0.00");
					Date date = simpleDateFormat.parse(datetime);
					Calendar c = Calendar.getInstance();
					c.setTime(date);
					
					if((c.get(Calendar.MONTH) == currenttime.get(Calendar.MONTH)) && (c.get(Calendar.YEAR) == currenttime.get(Calendar.YEAR)))
						if(budgetObj.getString("TransactionCategoryID").equals(categoryObj.getString("TransactionCategoryID"))){
							hm.put("budgetObjIndex", ""+j);

							hm.put("amount",  budgetObj.getString("Amount"));
							break;
						}

				}
				
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
	
	String categoryid;
	public void Click(View v){
		tag = (String) v.getTag();

		Log.d("indexTag", tag);
		
		AlertDialog.Builder setBudgetDialog = new AlertDialog.Builder(this);
		setBudgetDialog.setTitle("Set Budget");
		
		startdate = currenttime.get(Calendar.YEAR) + "-" + (currenttime.get(Calendar.MONTH)+1) + "-" + currenttime.get(Calendar.DATE);
		
		// Set up the input
		//final TextView amountTV = new TextView(this);
		final EditText input = new EditText(this);
		// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
		//amountTV.setText("Amount:");
		setBudgetDialog.setMessage("Amount: ");
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		setBudgetDialog.setView(input);
		
	    
	    setBudgetDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	           // Log.d("amountOK", "amount ok");
	        	amountInput = input.getText().toString();
	        	//Log.d("amountInput", amountInput);
	        	saveBudget connect = new saveBudget();
	        	connect.execute();
	        	
	        }
	     });
	    setBudgetDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	dialog.cancel();
	        }
	     });
	    setBudgetDialog.setIcon(android.R.drawable.ic_dialog_alert);
	    setBudgetDialog.show();
	}
	
	class saveBudget extends AsyncTask<Void, Void, Void>{
		JSONObject jObject = null;
		@Override
		protected Void doInBackground(Void... params) {
			Log.d("async task", "saveBudget");
			JSONObject budgetObj;
			try {
				budgetObj = budgetJArray.getJSONObject(Integer.parseInt(tag));
				budgetObj.put("Amount", amountInput);
				budgetJArray.put(Integer.parseInt(tag), budgetObj);
				JSONParser jsonparser = new JSONParser();
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("userid", userid));
				list.add(new BasicNameValuePair("amount", amountInput));
				list.add(new BasicNameValuePair("startdate",startdate));
				list.add(new BasicNameValuePair("categoryid",budgetObj.getString("TransactionCategoryID")));
				//JSONObject jObject = jsonparser.makeHttpRequest("http://10.0.2.2/login/setBudget.php", "GET", list);
				jObject = jsonparser.makeHttpRequest("http://moneymatespfms.net46.net/setBudget.php", "GET", list);
				Log.d("URL parameter", list.toString());
				Log.d("BudgetJSON", jObject.toString());
			} catch (NumberFormatException e) {
				Log.d("async task error", "NumberFormatException");
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
				Log.d("async task error", "JSONException");
			}
			
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(jObject != null)
			{
				setBudget();
				Toast toast = Toast.makeText(getApplicationContext(), "Budget saved.", Toast.LENGTH_SHORT);
				toast.show();
			}
			else
			{
				Toast toast = Toast.makeText(getApplicationContext(), "Budget not saved.", Toast.LENGTH_SHORT);
				toast.show();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
	}
	
	public void budgetSaved(){
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("category", budget.get(Integer.parseInt(tag)).get("category"));
		hm.put("categoryID", categoryid);
		hm.put("amount", amountInput);
		budget.set(Integer.parseInt(tag), hm);
		LazyAdapter adapter = new LazyAdapter(this, budget);
		budgetListView.setAdapter(adapter);
	}*/
}
