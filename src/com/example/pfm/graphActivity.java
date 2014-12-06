package com.example.pfm;

import java.util.ArrayList;

import android.os.Bundle;

import android.app.Activity;

import android.content.Intent;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class graphActivity extends Activity {

	RelativeLayout LayoutToDisplayChart;
	Button incomeExpenseBtn, categoryBtn, comparisonBtn;
	String[] labels;
	double[] values;
	ArrayList<String> ar = new ArrayList<String>();
	ArrayList<Double> arr = new ArrayList<Double>();

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.income_vs_expenses);

		LayoutToDisplayChart = (RelativeLayout) findViewById(R.id.relative);
		incomeExpenseBtn = (Button) findViewById(R.id.incomeExpenseBtn);
		categoryBtn = (Button) findViewById(R.id.categoryBtn);
		comparisonBtn = (Button) findViewById(R.id.comparisonBtn);

		/*Button b_cancel = (Button) findViewById(R.id.cancel);
		b_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				graphActivity.this.finish();
			}

		});*/
		
		final Bundle b = getIntent().getExtras();
		labels = b.getStringArray("labelArray");
		values = b.getDoubleArray("valueArray");	
		
		//String[] categoryLabel = b.getStringArray("categoryLabelArray");
		//double[] categoryValue = b.getDoubleArray("categoryValueArray");
		
		/*categoryBtn.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				labels = b.getStringArray("categoryLabelArray");
				values = b.getDoubleArray("categoryValueArray");
				drawGraph();
			}
		});
		
		incomeExpenseBtn.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				labels = b.getStringArray("labelArray");
				values = b.getDoubleArray("valueArray");
				drawGraph();
			}
		});
		
		comparisonBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lineGraph();
			}
		});*/
		
		drawGraph();
	}
	
	public void lineGraph(){
		Intent intent = new Intent(this, ExpenseTrend.class);
		startActivity(intent);
	}

	PieChartActivity achartIntent = new PieChartActivity();
	public void drawGraph(){
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
		achartIntent.execute(graphActivity.this, LayoutToDisplayChart);
	}
}