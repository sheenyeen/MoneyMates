package com.example.pfm;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BudgetProgress extends Activity {

	TextView monthTV;
	Button previousMonth, nextMonth;
	Calendar currenttime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.budget_progress);
		monthTV = (TextView) findViewById(R.id.displayMonth);
		previousMonth = (Button) findViewById(R.id.lastMonthArrow);
		nextMonth = (Button) findViewById(R.id.nextMonthArrow);
		currenttime = Calendar.getInstance();
		
		previousMonth.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				currenttime.add(Calendar.MONTH, -1);
				//call method
			}
		});
		
		nextMonth.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				currenttime.add(Calendar.MONTH, -1);
				//call method
			}
		});
		
		
		
	}
}
