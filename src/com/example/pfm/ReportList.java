package com.example.pfm;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ReportList extends Activity{
	
	String reports[] = {"Income VS Expenses", "Expense Categories", "Expense Trend"};
	
	ListView reportListview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_list);
		
		reportListview = (ListView) findViewById(R.id.reportListview);
		
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.addAll(Arrays.asList(reports));
		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, reports);
		reportListview.setAdapter(listAdapter);
		
		reportListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				if(position==0){
					incomeVsExpenses();
				}
				else if(position==1){
					expenseCategories();
				}	
				else if(position==2){
					lineChart();
				}
			}
		});
	}
	
	public void incomeVsExpenses(){
		Intent incomeVsExpensesIntent = new Intent(this, IncomeVsExpenses.class);
		startActivity(incomeVsExpensesIntent);
	}
	
	public void expenseCategories(){
		Intent expenseCategoriesIntent = new Intent(this, ExpenseCategories.class);
		startActivity(expenseCategoriesIntent);
	}
	
	public void lineChart(){
		Intent lineChartIntent = new Intent(this, LineChart.class);
		startActivity(lineChartIntent);         
	}
	
}
