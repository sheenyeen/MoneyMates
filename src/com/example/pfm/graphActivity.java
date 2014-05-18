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

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.generate_report);

		LayoutToDisplayChart = (RelativeLayout) findViewById(R.id.relative);

		/*Button b_cancel = (Button) findViewById(R.id.cancel);
		b_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				graphActivity.this.finish();
			}

		});*/
		
		Bundle b = getIntent().getExtras();
		String[] labels = b.getStringArray("labelArray");
		double[] values = b.getDoubleArray("incomeExpenseArray");
		ArrayList<String> ar = new ArrayList<String>();
		ArrayList<Double> arr = new ArrayList<Double>();
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
		PieChartActivity achartIntent = new PieChartActivity();
		achartIntent.setResource(ar, arr);
		achartIntent.execute(graphActivity.this, LayoutToDisplayChart);

	}
}