package com.example.pfm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;


public class Alarm extends BroadcastReceiver{
	   
	 Vibrator vibrator;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		long alarmTime = intent.getLongExtra("alarmTime", 0);
		Toast toast = Toast.makeText(context, "Alarm received.", Toast.LENGTH_SHORT);
		toast.show();
		
		/*NotificationManager notificationMgr = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
	    Notification notification = new Notification(R.drawable.pfm_logo, "Alarm", alarmTime);
	    vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	    
	    // launch dashboard when user clicks on notification
	    PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, Dashboard.class), 0);
	    notification.setLatestEventInfo(context, "MoneyMates Reminder", "Remember to key in your expenses for today!", contentIntent);
	    notificationMgr.notify("Alarm", 0, notification);
	    vibrator.vibrate(1000);*/
	    
	   NotificationCompat.Builder mBuilder =
	    	    new NotificationCompat.Builder(context)
	   			.setSmallIcon(R.drawable.pfm_logo_small)
	   			.setContentTitle("Daily Reminder")
	   			.setContentText("Remember to insert daily transactions!");
	   
	   mBuilder.setVibrate(new long[] {1000,1000,1000,1000,1000});
	   PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(context, Dashboard.class), 0);
	   mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
	   mBuilder.setContentIntent(pi);
	   NotificationManager notificationMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	   notificationMgr.notify(0, mBuilder.build());
	}	
}


/*
public class Alarm extends Service {

	
	
	@Override

	public void onCreate() {

	// TODO Auto-generated method stub

	Toast.makeText(this, "MyAlarmService.onCreate()", Toast.LENGTH_LONG).show();

	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "MyAlarmService.onBind()", Toast.LENGTH_LONG).show();
		return null;
	}
	
	@Override

	public void onDestroy() {

	// TODO Auto-generated method stub

	super.onDestroy();

	Toast.makeText(this, "MyAlarmService.onDestroy()", Toast.LENGTH_LONG).show();

	}



	@Override

	public void onStart(Intent intent, int startId) {

	// TODO Auto-generated method stub

	super.onStart(intent, startId);

	Toast.makeText(this, "MyAlarmService.onStart()", Toast.LENGTH_LONG).show();

	}


	@Override

	public boolean onUnbind(Intent intent) {

	// TODO Auto-generated method stub

	Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG).show();

	return super.onUnbind(intent);

	}
}*/
