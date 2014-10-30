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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class ViewBudget extends Activity {

	TextView monthTV;
	Button previousMonth, nextMonth;
	Button amountBtn, progressBtn;
	ListView budgetListView, progressListView;

	Calendar currenttime;
	String currentMonth, currentYear;
	String amountInput, tag, startdate;
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
		progressListView = (ListView) findViewById(R.id.progressListView);
		amountBtn = (Button) findViewById(R.id.amountBtn);
		progressBtn = (Button) findViewById(R.id.progressBtn);
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
		
		amountBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				progressListView.setVisibility(View.INVISIBLE);
				budgetListView.setVisibility(View.VISIBLE);
				
			}
		});
		
		progressBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				budgetListView.setVisibility(View.INVISIBLE);
				progressListView.setVisibility(View.VISIBLE);
			}
		});

		b = getIntent().getExtras();
		userid = b.getString("userid");

		budgetListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View v,
					int position, long arg3) {
				Log.d("listview onclick", "" + position);
			}
		});
	}

	@Override
	protected void onResume() {
		getBudget connect = new getBudget();
		connect.execute();
		super.onResume();
	}

	ArrayList<HashMap<String, String>> budget;

	public void setBudget() {
		currentMonth = new SimpleDateFormat("MMM").format(currenttime.getTime());
		monthTV.setText(currentMonth + " " + currenttime.get(Calendar.YEAR));
		budget = new ArrayList<HashMap<String, String>>();
		try {

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Log.d("categories", categoryJArray.toString());
			Log.d("budgets", budgetJArray.toString());
			for (int i = 0; i < categoryJArray.length(); i++) {
				JSONObject categoryObj = categoryJArray.getJSONObject(i);
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("category",categoryObj.getString("TransactionCategoryName"));
				hm.put("categoryID",categoryObj.getString("TransactionCategoryID"));
				hm.put("budgetObjIndex", "-"+i);
				for (int j = 0; j < budgetJArray.length(); j++) {
					JSONObject budgetObj = budgetJArray.getJSONObject(j);
					String datetime = budgetObj.getString("BudgetMonth");
					hm.put("amount", "0.00");
					Date date = simpleDateFormat.parse(datetime);
					Calendar c = Calendar.getInstance();
					c.setTime(date);
					if ((c.get(Calendar.MONTH) == currenttime.get(Calendar.MONTH)) && (c.get(Calendar.YEAR) == currenttime.get(Calendar.YEAR)))
						if (budgetObj.getString("TransactionCategoryID").equals(categoryObj.getString("TransactionCategoryID"))) {
							hm.put("budgetObjIndex", "" + j);
							hm.put("amount", budgetObj.getString("Amount"));
							break;
						}

				}

				budget.add(hm);
			}

		} catch (NullPointerException e) {
			Log.d("exeption", e.toString());
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.d("budget arraylist", budget.toString());
		LazyAdapter adapter = new LazyAdapter(this, budget);
		budgetListView.setAdapter(adapter);
	}

	class getBudget extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			JSONParser jsonparser = new JSONParser();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("userid", userid));
			Log.d("GET parameter", list.toString());
			Log.d("Calling to url",
					"http://moneymatespfms.net46.net/getBudget.php");
			JSONObject jObject = jsonparser.makeHttpRequest(
					"http://moneymatespfms.net46.net/getBudget.php", "GET",
					list);

			try {
				Log.d("JSON", jObject.toString());
				if (jObject.getString("status").equals("success")) {
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

	public void Click(View v) {
		tag = (String) v.getTag();

		Log.d("indexTag", tag);

		AlertDialog.Builder setBudgetDialog = new AlertDialog.Builder(this);
		setBudgetDialog.setTitle("Set Budget");

		startdate = currenttime.get(Calendar.YEAR) + "-"
				+ (currenttime.get(Calendar.MONTH) + 1) + "-"
				+ currenttime.get(Calendar.DATE);

		JSONObject budgetObj = new JSONObject();
		String tempAmount = "0.00";
		try {
				budgetObj = budgetJArray.getJSONObject(Integer.parseInt(tag));
				tempAmount = budgetObj.getString("Amount");
			
		} catch (NumberFormatException e) {
			Log.d("async task error", "NumberFormatException");
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
			Log.d("async task error", "JSONException");
		} finally {
			final EditText input = new EditText(this);
			input.setText(tempAmount);
			// Specify the type of input expected; this, for example, sets the input
			// as a password, and will mask the text
			// amountTV.setText("Amount:");
			setBudgetDialog.setMessage("Amount: ");
			input.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_NUMBER_FLAG_DECIMAL);
			setBudgetDialog.setView(input);

			setBudgetDialog.setPositiveButton("Save",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Log.d("amountOK", "amount ok");
							amountInput = input.getText().toString();
							// Log.d("amountInput", amountInput);
							saveBudget connect = new saveBudget();
							connect.execute();

						}
					});
			setBudgetDialog.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			setBudgetDialog.setIcon(android.R.drawable.ic_dialog_alert);
			setBudgetDialog.show();
		}
		
	}

	class saveBudget extends AsyncTask<Void, Void, Void> {
		JSONObject jObject = null;

		@Override
		protected Void doInBackground(Void... params) {
			Log.d("async task", "saveBudget");
			JSONObject budgetObj = new JSONObject();
			try {
				if(tag.charAt(0) != '-')
					budgetObj = budgetJArray.getJSONObject(Integer.parseInt(tag));
				else{
					budgetObj.put("TransactionCategoryID", categoryJArray.getJSONObject(Integer.parseInt(tag.substring(1,tag.length()))).getString("TransactionCategoryID"));
					budgetObj.put("BudgetMonth", currenttime.get(Calendar.YEAR)+"-"+(currenttime.get(Calendar.MONTH)+1)+"-"+currenttime.get(Calendar.DATE));
				}
				budgetObj.put("Amount", amountInput);
				budgetJArray.put(budgetJArray.length(), budgetObj);
				JSONParser jsonparser = new JSONParser();
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("userid", userid));
				list.add(new BasicNameValuePair("amount", amountInput));
				list.add(new BasicNameValuePair("startdate", startdate));
				list.add(new BasicNameValuePair("categoryid", budgetObj.getString("TransactionCategoryID")));
				// JSONObject jObject =
				// jsonparser.makeHttpRequest("http://10.0.2.2/login/setBudget.php",
				// "GET", list);
				jObject = jsonparser.makeHttpRequest(
						"http://moneymatespfms.net46.net/setBudget.php", "GET",
						list);
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
			if (jObject != null) {
				setBudget();
				Toast toast = Toast.makeText(getApplicationContext(),
						"Budget saved.", Toast.LENGTH_SHORT);
				toast.show();
			} else {
				Toast toast = Toast.makeText(getApplicationContext(),
						"Budget not saved.", Toast.LENGTH_SHORT);
				toast.show();
			}
			/*
			 * try { if(jObject.getString("status").equals("success")){ String
			 * isBudgetOver = jObject.getString("overbudget");
			 * if(isBudgetOver.equals("false")){ Toast toast =
			 * Toast.makeText(getApplicationContext(),
			 * "Budget set successfully. u still can spend more money",
			 * Toast.LENGTH_SHORT); toast.show(); } else{ Toast toast =
			 * Toast.makeText(getApplicationContext(),
			 * "Budget set successfully. u r currently overbudget for " +
			 * jObject.getString("amount"), Toast.LENGTH_SHORT); toast.show(); }
			 * 
			 * } else{ Toast toast = Toast.makeText(getApplicationContext(),
			 * "Fail to save.", Toast.LENGTH_SHORT); toast.show(); } } catch
			 * (JSONException e) { e.printStackTrace(); }
			 */

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
	}

	public void budgetSaved() {
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("category", budget.get(Integer.parseInt(tag)).get("category"));
		hm.put("categoryID", categoryid);
		hm.put("amount", amountInput);
		budget.set(Integer.parseInt(tag), hm);
		LazyAdapter adapter = new LazyAdapter(this, budget);
		budgetListView.setAdapter(adapter);
	}

}
