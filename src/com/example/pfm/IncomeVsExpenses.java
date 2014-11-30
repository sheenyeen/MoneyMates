package com.example.pfm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IncomeVsExpenses extends Activity{
	
	TextView monthTV;
	Button previousMonthBtn, nextMonthBtn;
	
	JSONArray categoryJArray, budgetJArray; 
	public static ArrayList<JSONObject> currList;
	Double totalIncome = 0.00, totalExpense = 0.00, totalSaving = 0.00; 
	int counter;
	JSONObject trans;
	String transactionType;
	JSONArray jArray;
	public static Calendar currenttime;
	Calendar c;
	String currentMonth, currentYear;
	String userid;
	Bundle b = new Bundle();
	
	double[] valueArray = new double[2];
	String labelArray[] = {"Income", "Expenses"};
	
	ArrayList<String> categoryLabelArrayList = new ArrayList<String>();
	ArrayList<Double> categoryArrayList = new ArrayList<Double>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.income_vs_expenses);
		//b = getIntent().getExtras();
		userid = MyService.userid;
		
		monthTV = (TextView) findViewById(R.id.monthTV);
		previousMonthBtn = (Button) findViewById(R.id.previousMonthBtn);
		nextMonthBtn = (Button) findViewById(R.id.nextMonthBtn);
		
		currenttime = Calendar.getInstance();
		currentMonth = new SimpleDateFormat("MMM").format(currenttime.getTime());
		monthTV.setText(currentMonth + " " + currenttime.get(Calendar.YEAR));
		
		previousMonthBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				currenttime.add(Calendar.MONTH, -1);
				setTransaction();	
			}
		});
		
		nextMonthBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				currenttime.add(Calendar.MONTH, +1);
				setTransaction();	
			}
		});	
		setTransaction();
	}
	
	public void setTransaction() {	
		categoryLabelArrayList.clear();
		categoryArrayList.clear();
		currList = new ArrayList<JSONObject>();
		counter = 0;
		totalIncome = 0.0;
		totalExpense = 0.0;
		currentMonth = new SimpleDateFormat("MMM").format(currenttime.getTime());
		monthTV.setText(currentMonth + " " + currenttime.get(Calendar.YEAR));
		
		for (int i = 0; i < MyService.transArray.length(); i++) {
			try {
				trans = MyService.transArray.getJSONObject(i);

				String datetime = trans.getString("TransactionDateTime");
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
				try {
					Date date = simpleDateFormat.parse(datetime);
					Log.d("date", date.toString());
					c = Calendar.getInstance();
					c.setTime(date);

					if ((c.get(Calendar.MONTH) == currenttime.get(Calendar.MONTH)) && (c.get(Calendar.YEAR) == currenttime.get(Calendar.YEAR))) {
						currList.add(trans);
						String column1 = trans.getString("TransactionCategoryName");
						String column2 = trans.getString("Amount");
						transactionType = trans.getString("TransactionTypeID");
						//Log.d("transactionType", transactionType);

						if(transactionType.equals("1")){
							Double parseDouble = Double.parseDouble(column2);
							totalIncome = totalIncome + parseDouble;
							//Log.d("totalincome", totalIncome.toString());
						}else if(transactionType.equals("2")){
							if(!categoryLabelArrayList.contains(column1)){
								categoryLabelArrayList.add(column1);
								categoryArrayList.add(Double.parseDouble(column2));
							}else{
								categoryArrayList.set(categoryLabelArrayList.indexOf(column1), categoryArrayList.get(categoryLabelArrayList.indexOf(column1))+Double.parseDouble(column2));
							}
							Double parseDouble = Double.parseDouble(column2);
							totalExpense = totalExpense + parseDouble;
							//Log.d("totalExpense", totalExpense.toString());
						}else{
							Log.d("Fail to add", "Fail to add income / expenses");
						}
							
						//addLL(column1, column2, transactionType);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		totalSaving = totalIncome - totalExpense;		
		
		Log.d("savingsArray", MyService.savingsArray.toString());
		
		valueArray[0] = totalIncome;
		valueArray[1] = totalExpense;
		
		String d2sIncomeDP = String.format("%.2f", totalIncome);
		String d2sIncome = d2sIncomeDP.toString();
		//totalIncomeTV.setText("RM " + d2sIncome);
		
		String d2sExpenseDP = String.format("%.2f", totalExpense);
		String d2sExpense = d2sExpenseDP.toString();
		//totalExpenseTV.setText("RM " + d2sExpense);
		
		String d2sSavingDP = String.format("%.2f", totalSaving);
		String d2sSaving = d2sSavingDP.toString();
		
		generateReportIntent();
		
		//totalSavingTV.setText("Net Balance: RM " + d2sSaving);
		
		/*if(totalSaving > 0){
			totalSavingTV.setTextColor(Color.parseColor("#01B18B"));
		}else if(totalSaving < 0){
			totalSavingTV.setTextColor(Color.parseColor("#F16C5B"));
		}else{
			totalSavingTV.setTextColor(Color.parseColor("#8C8C8C"));
		}*/
		
		//totalIncomeTV.setText("RM " + d2sIncome);
		//totalExpenseTV.setText("RM " + d2sExpense);
		//totalSavingTV.setText("RM " + d2sSaving);
	}
	
	public void generateReportIntent(){
		//b.putString("totalIncome", d2sIncomeDP);
		//b.putString("totalExpense", d2sExpenseDP);
		
		double[] categoryArrayDouble = new double[categoryArrayList.size()];
		String[] categoryLabelArray = new String[categoryLabelArrayList.size()];
		
		for(int i = 0; i < categoryLabelArrayList.size(); i++){
			categoryLabelArray[i] = categoryLabelArrayList.get(i);
		}
		for(int i = 0; i < categoryArrayList.size(); i++)
		{
			categoryArrayDouble[i] = categoryArrayList.get(i);
		}
		
		b.putStringArray("labelArray", labelArray);
		b.putDoubleArray("valueArray", valueArray);
		
		b.putStringArray("categoryLabelArray", categoryLabelArray);
		b.putDoubleArray("categoryValueArray", categoryArrayDouble);
		
		Log.d("categoryLabelArray", categoryLabelArray.toString());
		

		labels = b.getStringArray("labelArray");
		values = b.getDoubleArray("valueArray");	
		drawGraph();
	}
	
	


	RelativeLayout LayoutToDisplayChart;
	Button incomeExpenseBtn, categoryBtn, comparisonBtn;
	String[] labels;
	double[] values;
	ArrayList<String> ar = new ArrayList<String>();
	ArrayList<Double> arr = new ArrayList<Double>();
	PieChartActivity achartIntent = new PieChartActivity();
	public void drawGraph(){
		LayoutToDisplayChart = (RelativeLayout) findViewById(R.id.relative);
		
		ar.clear();
		arr.clear();
		for(int i = 0; i < labels.length; i++)
		{
			if(!ar.contains(labels[i]))
			{
				ar.add(labels[i]);
				Double a = new Double(values[i]);
				arr.add(a);
			}
			else
			{
				int index = ar.indexOf(labels[i]);
				Double curr = arr.get(index);
				curr += values[i];
				arr.set(index, curr);
			}
		}
		Log.d("string array length",""+ar.size());
		Log.d("string array detail",""+ar);
		Log.d("Int array length",""+arr.size());
		Log.d("string array detail",""+arr);
		achartIntent.setResource(ar, arr);
		achartIntent.execute(this, LayoutToDisplayChart);
	}

}
