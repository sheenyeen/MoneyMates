package com.example.pfm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Reminders extends Activity{
	
	Button backButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reminders);
		
		backButton = (Button) findViewById(R.id.backBtn);
		
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				backIntent();
			}
		});
	}

	public void backIntent(){
		Intent backIntent = new Intent(this, Dashboard.class);
		startActivity(backIntent);
		finish();
	}
}
