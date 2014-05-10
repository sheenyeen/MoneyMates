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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class Transaction extends Activity {

	String userid = "";
	String currentMonth, currentYear;
	LinearLayout llayoutV1, llayoutV2;
	TextView monthTV;
	Button lastMonthArrow, nextMonthArrow;
	JSONArray jArray;
	Calendar currenttime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transaction2);
		monthTV = (TextView) findViewById(R.id.displayMonth);
		lastMonthArrow = (Button) findViewById(R.id.lastMonthArrow);
		nextMonthArrow = (Button) findViewById(R.id.nextMonthArrow);

		currenttime = Calendar.getInstance();
		
		lastMonthArrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				currenttime.add(Calendar.MONTH, -1);
				setTransaction();			
			}
		});

		nextMonthArrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				currenttime.add(Calendar.MONTH, +1);
				setTransaction();
			}
		});
		
		Bundle b = getIntent().getExtras();
		userid = b.getString("userid");
		Log.d("bundlestring", b.toString());
		Log.d("userID", userid);
		getTransaction connect = new getTransaction();
		connect.execute();
	}

	class getTransaction extends AsyncTask<Void, Void, Void> {
		JSONObject jObject;
		LinearLayout llayout[];

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONParser jsonparser = new JSONParser();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("userid", userid));
			Log.d("userid", userid);
			jObject = jsonparser.makeHttpRequest(
					"http://10.0.2.2/login/viewTransaction.php", "GET", list);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			llayoutV1 = (LinearLayout) findViewById(R.id.llayoutV1);
			llayoutV2 = (LinearLayout) findViewById(R.id.llayoutV2);
			try {
				Log.d("JSON", jObject.toString());
				if (jObject.getString("status").equals("success")) {
					jArray = jObject.getJSONArray("transaction");
					setTransaction();

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
	}

	public void addLL(String a, String b) {
		TextView tv1 = new TextView(this);
		TextView tv2 = new TextView(this);
		tv1.setText(a);
		tv2.setText(b);

		// LinearLayout newll = new LinearLayout(this);
		// newll.setOrientation(LinearLayout.HORIZONTAL);

		LinearLayout.LayoutParams tv_layoutParams1 = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		tv_layoutParams1.gravity = Gravity.LEFT;

		LinearLayout.LayoutParams tv_layoutParams2 = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		tv_layoutParams2.gravity = Gravity.RIGHT;
		// tv2.setGravity(Gravity.RIGHT);

		// newll.addView(tv1, tv_layoutParams);
		// newll.addView(tv2, tv_layoutParams);

		// LinearLayout.LayoutParams ll_layoutParams = new
		// LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

		llayoutV1.addView(tv1, tv_layoutParams1);
		llayoutV2.addView(tv2, tv_layoutParams2);

	}

	public void setTransaction() {

		llayoutV1.removeAllViews();
		llayoutV2.removeAllViews();
		currentMonth = new SimpleDateFormat("MMM").format(currenttime.getTime());
		//currentYear = new SimpleDateFormat("YYYY").format(currenttime.getTime());
		monthTV.setText(currentMonth + " " + currenttime.get(Calendar.YEAR));
		
		for (int i = 0; i < jArray.length(); i++) {
			JSONObject trans;
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
						String column1 = trans.getString("TransactionCategoryName");
						String column2 = trans.getString("Amount");
						addLL(column1, column2);
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
	}
}
