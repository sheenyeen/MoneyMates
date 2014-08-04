package com.example.pfm;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class GenerateReport extends Activity {

	Button incomeExpenseBtn, categoryBtn, comparisonBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generate_report);	
		
		incomeExpenseBtn = (Button) findViewById(R.id.incomeExpenseBtn);
		categoryBtn = (Button) findViewById(R.id.categoryBtn);
		comparisonBtn = (Button) findViewById(R.id.comparisonBtn);
	}
}
