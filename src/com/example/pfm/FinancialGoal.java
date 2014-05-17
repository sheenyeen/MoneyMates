package com.example.pfm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FinancialGoal extends Activity{
	
	Button cancelButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.financial_goal);
		cancelButton = (Button) findViewById(R.id.cancelBtn);
		
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cancelIntent();
			}
		});
	}
	
	public void cancelIntent(){
		Intent cancelIntent = new Intent(this, Dashboard.class);
		startActivity(cancelIntent);
		finish();
	}

}
