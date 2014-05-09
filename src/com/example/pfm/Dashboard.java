package com.example.pfm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class Dashboard extends Activity {
	
	ImageView insertTrans;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		insertTrans = (ImageView) findViewById(R.id.insertTrans);
		insertTrans.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				transIntent();			
			}			
		});
	}
	public void transIntent(){
		Bundle b = getIntent().getExtras();
		Intent openTransaction = new Intent(this, Transaction.class);
		openTransaction.putExtras(b);
		startActivity(openTransaction);
	}
}
