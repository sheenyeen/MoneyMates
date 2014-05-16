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
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class Transaction extends Activity {

	String userid = "";
	String currentMonth, currentYear;
	Double totalIncome = 0.00, totalExpense = 0.00, totalSaving = 0.00; 
	LinearLayout llayoutV1, llayoutV2, llayoutV3;
	TextView monthTV, totalIncomeTV, totalExpenseTV, totalSavingTV;
	Button lastMonthArrow, nextMonthArrow, addTransactionBtn, reportBtn;
	JSONArray jArray;
	Calendar currenttime;
	Bundle b;
	int counter;
	JSONObject trans;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_transaction);
		
		monthTV = (TextView) findViewById(R.id.displayMonth);
		lastMonthArrow = (Button) findViewById(R.id.lastMonthArrow);
		nextMonthArrow = (Button) findViewById(R.id.nextMonthArrow);
		addTransactionBtn = (Button) findViewById(R.id.addTransactionBtn);
		totalIncomeTV = (TextView) findViewById(R.id.totalIncomeTV);
		totalExpenseTV = (TextView) findViewById(R.id.totalExpenseTV);
		totalSavingTV = (TextView) findViewById(R.id.totalSavingTV);
		reportBtn = (Button) findViewById(R.id.reportBtn);

		currenttime = Calendar.getInstance();
		
		lastMonthArrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currenttime.add(Calendar.MONTH, -1);
				setTransaction();			
			}
		});

		nextMonthArrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currenttime.add(Calendar.MONTH, +1);
				setTransaction();
			}
		});
		
		addTransactionBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addTransactionIntent();
			}
		});
		
		reportBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				generateReportIntent();
			}
		});
		
		b = getIntent().getExtras();
		userid = b.getString("userid");
		//Log.d("bundlestring", b.toString());
		//Log.d("userID", userid);
		
		getTransaction connect = new getTransaction();
		connect.execute();
	}

	class getTransaction extends AsyncTask<Void, Void, Void> {
		JSONObject jObject;
		LinearLayout llayout[];

		@Override
		protected Void doInBackground(Void... params) {
			JSONParser jsonparser = new JSONParser();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("userid", userid));
			//Log.d("userid", userid);
			//jObject = jsonparser.makeHttpRequest("http://10.0.2.2/login/viewTransaction.php", "GET", list);
			jObject = jsonparser.makeHttpRequest("http://moneymatespfms.net46.net/viewTransaction.php", "GET", list);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			llayoutV1 = (LinearLayout) findViewById(R.id.llayoutV1);
			llayoutV2 = (LinearLayout) findViewById(R.id.llayoutV2);
			llayoutV3 = (LinearLayout) findViewById(R.id.llayoutV3);
			try {
				Log.d("JSON", jObject.toString());
				if (jObject.getString("status").equals("success")) {
					jArray = jObject.getJSONArray("transaction");
					setTransaction();

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
	
	public void addTransactionIntent(){
		Intent addTransactionIntent = new Intent(this, AddTransaction.class);
		b.putString("userid", userid);
		addTransactionIntent.putExtras(b);
		startActivity(addTransactionIntent);
		finish();
	}
	
	public void generateReportIntent(){
		Intent generateReportIntent = new Intent(this, GenerateReport.class);
		startActivity(generateReportIntent);
	}

	public void addLL(String x, String y, String transactionType) {
		TextView tv1 = new TextView(this);
		TextView tv2 = new TextView(this);
		final ImageButton editButton = new ImageButton(this);
		
		tv1.setText(x);
		
		Double parseDouble = Double.parseDouble(y);
		String aD2S = String.format("%.2f", parseDouble).toString();
		
		tv2.setText("RM " + aD2S);
		
		tv1.setTextSize(16);
		tv2.setTextSize(16);
		tv1.setPadding(0, 0, 0, 2);
		tv2.setPadding(0, 0, 0, 2);
		editButton.setImageResource(R.drawable.edit_button5);
		//editButton.setBackgroundColor(Color.TRANSPARENT);
		editButton.setTag(counter);
		editButton.setPadding(0, 0, 0, 7);
		counter++;
		
		if(transactionType.equals("1")){
			tv1.setTextColor(Color.parseColor("#29B32B"));
		}
		
		else if(transactionType.equals("2")){
			tv1.setTextColor(Color.parseColor("#E31B1B"));
		}

		// LinearLayout newll = new LinearLayout(this);
		// newll.setOrientation(LinearLayout.HORIZONTAL);

		LinearLayout.LayoutParams tv_layoutParams1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		tv_layoutParams1.gravity = Gravity.LEFT;

		LinearLayout.LayoutParams tv_layoutParams2 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		tv_layoutParams2.gravity = Gravity.RIGHT;
		
		LinearLayout.LayoutParams button_layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		button_layoutParams.width=35;
		button_layoutParams.height=35;
		button_layoutParams.gravity = Gravity.LEFT;
		
		//tv1.setBackgroundResource(R.drawable.textview_row);
		//tv2.setBackgroundResource(R.drawable.textview_row);
		//tv2.setGravity(Gravity.RIGHT);

		// newll.addView(tv1, tv_layoutParams);
		// newll.addView(tv2, tv_layoutParams);

		// LinearLayout.LayoutParams ll_layoutParams = new
		// LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

		llayoutV1.addView(tv1, tv_layoutParams1);
		llayoutV2.addView(tv2, tv_layoutParams2);
		llayoutV3.addView(editButton, button_layoutParams);
		
		editButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onClickModify(Integer.parseInt(editButton.getTag().toString()));
			}
		});

	}
	ArrayList<JSONObject> currList;
	public void setTransaction() {	
		currList = new ArrayList<JSONObject>();
		counter = 0;
		llayoutV1.removeAllViews();
		llayoutV2.removeAllViews();
		llayoutV3.removeAllViews();
		totalIncome = 0.0;
		totalExpense = 0.0;
		currentMonth = new SimpleDateFormat("MMM").format(currenttime.getTime());
		monthTV.setText(currentMonth + " " + currenttime.get(Calendar.YEAR));
		
		for (int i = 0; i < jArray.length(); i++) {
			try {
				trans = jArray.getJSONObject(i);

				String datetime = trans.getString("TransactionDateTime");
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
				try {
					Date date = simpleDateFormat.parse(datetime);
					Log.d("date", date.toString());
					Calendar c = Calendar.getInstance();
					c.setTime(date);

					if ((c.get(Calendar.MONTH) == currenttime.get(Calendar.MONTH)) && (c.get(Calendar.YEAR) == currenttime.get(Calendar.YEAR))) {
						currList.add(trans);
						String column1 = trans.getString("TransactionCategoryName");
						String column2 = trans.getString("Amount");
						String transactionType = trans.getString("TransactionTypeID");
						//Log.d("transactionType", transactionType);
						
						if(transactionType.equals("1")){
							Double parseDouble = Double.parseDouble(column2);
							totalIncome = totalIncome + parseDouble;
							//Log.d("totalincome", totalIncome.toString());
						}else if(transactionType.equals("2")){
							Double parseDouble = Double.parseDouble(column2);
							totalExpense = totalExpense + parseDouble;
							//Log.d("totalExpense", totalExpense.toString());
						}else{
							Log.d("Fail to add", "Fail to add income / expenses");
						}
							
						addLL(column1, column2, transactionType);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		totalSaving = totalIncome - totalExpense;		
		
		String d2sIncomeDP = String.format("%.2f", totalIncome);
		String d2sIncome = d2sIncomeDP.toString();
		totalIncomeTV.setText("RM " + d2sIncome);
		
		String d2sExpenseDP = String.format("%.2f", totalExpense);
		String d2sExpense = d2sExpenseDP.toString();
		totalExpenseTV.setText("RM " + d2sExpense);
		
		String d2sSavingDP = String.format("%.2f", totalSaving);
		String d2sSaving = d2sSavingDP.toString();
		totalSavingTV.setText("Net Balance: RM " + d2sSaving);
		
		if(totalSaving > 0){
			totalSavingTV.setTextColor(Color.parseColor("#6ABF65"));
		}else if(totalSaving < 0){
			totalSavingTV.setTextColor(Color.parseColor("#DE0909"));
		}else{
			totalSavingTV.setTextColor(Color.parseColor("#000000"));
		}
		
		//totalIncomeTV.setText("RM " + d2sIncome);
		//totalExpenseTV.setText("RM " + d2sExpense);
		//totalSavingTV.setText("RM " + d2sSaving);
	}
	
	public void onClickModify(int x){
		try {
			JSONObject job = currList.get(x);
			b.putString("transid", job.getString("TransactionID"));
			b.putString("transtypeid", job.getString("TransactionTypeID"));
			b.putString("transcategoryid", job.getString("TransactionCategoryID"));
			b.putString("resourceid", job.getString("ResourceID"));
			b.putString("amount", job.getString("Amount"));
			b.putString("remark", job.getString("Remark"));
			b.putString("date", job.getString("TransactionDateTime"));
			b.putString("statusid", job.getString("StatusID"));
			b.putString("createdate", job.getString("CreatedDateTime"));
			b.putString("editdate", job.getString("EditedDateTime"));
			Intent modifyTransaction = new Intent(this, ModifyTransaction.class);
			modifyTransaction.putExtras(b);
			startActivity(modifyTransaction);
			finish();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
