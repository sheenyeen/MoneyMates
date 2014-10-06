package com.example.pfm;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class Reminders extends Activity{
	
	Button backButton;
	Switch dailyReminderSwitch;
	TextView setBudget;
	int mhour, mminute;
	PendingIntent pi;
	String userid="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reminders);
		
		backButton = (Button) findViewById(R.id.backBtn);
		dailyReminderSwitch = (Switch) findViewById(R.id.dailyReminderSwitch);
		setBudget = (TextView) findViewById(R.id.budgetTV);
		
		
		dailyReminderSwitch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(dailyReminderSwitch.isChecked()){
					Log.d("Clicked", "Switch on");
					showDialog(1);
				}else{
					Log.d("Switch", "Switch off");
					//cancel alarm
					AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
					alarmManager.cancel(pi);
					Toast toast = Toast.makeText(getApplicationContext(), "Alarm off.", Toast.LENGTH_SHORT);
					toast.show();
				}
			}
		});
		
		setBudget.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				budgetIntent();
			}
		});
		
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				//backIntent();
			}
		});
	}
	
	public void budgetIntent(){
		Bundle b = getIntent().getExtras();
		Intent budgetIntent = new Intent(this, ViewBudget.class);
		budgetIntent.putExtras(b);
		startActivity(budgetIntent);
		Log.d("bundle", b.toString());
	}

	public void backIntent(){
		Intent backIntent = new Intent(this, Dashboard.class);
		startActivity(backIntent);
		finish();
	}
	
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mhour = hourOfDay;
			mminute = minute;
			Calendar calendar = Calendar.getInstance();
			AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
			//mPickTime.setText(new StringBuilder().append(pad(mhour)).append(":").append(pad(mminute)));
			//calendar.set(calendar.get(Calendar.YEAR), eventtime.get(Calendar.MONTH), eventtime.get(Calendar.DATE), eventtime.get(Calendar.HOUR_OF_DAY), eventtime.get(Calendar.MINUTE), eventtime.get(Calendar.SECOND));
			
			//if alarm hour b4 currtime
			if(calendar.get(Calendar.HOUR_OF_DAY) > mhour || (calendar.get(Calendar.HOUR_OF_DAY) == mhour && calendar.get(Calendar.MINUTE) >= mminute)){
				calendar.add(Calendar.DATE, 1);		
			}	
			
			calendar.set(Calendar.HOUR_OF_DAY, mhour);
			calendar.set(Calendar.MINUTE, mminute);
			
			long alarmTime = calendar.getTimeInMillis();
			
			Intent alarmIntent = new Intent(getApplicationContext(), Alarm.class);
			alarmIntent.putExtra("alarmTime", alarmTime);
			
			pi = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, 0);
			am.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, 1000 * 60 * 60 * 24 , pi); // Millisec * Second * Minute * repeat interval
			Toast toast = Toast.makeText(getApplicationContext(), "Alarm set.", Toast.LENGTH_SHORT);
			toast.show();	
		}
	};
	
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0:
			//return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);

		case 1:
			return new TimePickerDialog(this, mTimeSetListener, mhour, mminute, false);
		}
		return null;
	}
}
