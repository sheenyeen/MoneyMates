package com.example.pfm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class Dashboard extends Activity {
	
	ImageView insertTrans, financialGoal, reminders, settings;
	Button logoutButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		insertTrans = (ImageView) findViewById(R.id.insertTrans);
		financialGoal = (ImageView) findViewById(R.id.financialGoal);
		reminders = (ImageView) findViewById(R.id.reminders);
		settings = (ImageView) findViewById(R.id.settings);
		logoutButton = (Button) findViewById(R.id.logoutBtn);
		
		insertTrans.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				transIntent();			
			}			
		});
		
		financialGoal.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				financialGoalIntent();
			}
		});
		
		reminders.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				remindersIntent();
			}
		});
		
		settings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				settingsIntent();
			}
		});
		
		logoutButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				logoutDialog();
			}		
		});
	}
	
	public void logoutDialog(){
		AlertDialog logoutAlert = new AlertDialog.Builder(this)
		.setTitle("Logout")
	    .setMessage("Are you sure you want to log out?")
	    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            loginIntent();
	        }
	     })
	    .setNegativeButton("No", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // do nothing
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	     .show();
	}
	
	public void loginIntent(){
		Intent loginIntent = new Intent(this, MainActivity.class);
		startActivity(loginIntent);
		finish();
	}
	
	public void transIntent(){
		Bundle b = getIntent().getExtras();
		Intent openTransaction = new Intent(this, Transaction.class);
		openTransaction.putExtras(b);
		startActivity(openTransaction);
	}
	
	public void financialGoalIntent(){
		Intent financialGoalIntent = new Intent(this, FinancialGoal.class);
		startActivity(financialGoalIntent);
	}
	
	public void remindersIntent(){
		Intent remindersIntent = new Intent(this, Reminders.class);
		startActivity(remindersIntent);
	}
	
	public void settingsIntent(){
		Intent settingsIntent = new Intent(this, Settings.class);
		startActivity(settingsIntent);
	}
}
